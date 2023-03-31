package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.ReactiveRedisConnection;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.serializer.RedisElementReader;
import org.springframework.data.redis.serializer.RedisElementWriter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);


    //webflux
    @Cacheable("books")
    public Mono<String> getBookNameByIsbn(String isbn) {
        return findBookInSlowSource(isbn);
    }

    private Mono<String> findBookInSlowSource(String isbn) {
        // some long processing
        try {
            logger.info("Loading data from DB {}", isbn);
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Mono.just("JPoint Redis key " + isbn);
    }

    //reactive api of redis
    @Autowired
    ReactiveRedisConnectionFactory reactiveRedisConnectionFactory;

    @Autowired
    RedisCacheConfiguration redisConfiguration;

    @Autowired
    ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    public Mono<String> getProductByIdReactiveRedisApi(String id) {


        RedisElementWriter<String> writer = redisConfiguration.getKeySerializationPair().getWriter();
        RedisElementReader<String> reader = redisConfiguration.getKeySerializationPair().getReader();


        return Mono.using(reactiveRedisConnectionFactory::getReactiveConnection,

                    it-> it.stringCommands().get(writer.write(id)).map(reader::read),

                    ReactiveRedisConnection::closeLater);
    }
}