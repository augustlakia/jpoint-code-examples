package com.jpoint.demo;

import com.hazelcast.map.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.cache.CacheMono;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);


    // Plain method, just add @Cacheable no more actions
    @Cacheable("products")
    public Mono<String> getProductByIdReactor(String ID) {
        return findByIdMongoReactiveMethod(ID);
    }

    private Mono<String> findByIdMongoReactiveMethod(String ID) {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return Mono.just("JPoint Hazelcast key " + ID);
    }

    // reactor addons
    @Autowired
    IMap<String, String> cache;

    public Mono<String> getProductByIdCacheMono(String id) {

        return CacheMono
                .lookup( k -> Mono.fromCompletionStage(cache.getAsync(k)).map(Signal::next), id)

                .onCacheMissResume(findByIdMongoReactiveMethod(id))

                .andWriteWith((k, signal) -> Mono.fromCompletionStage(cache.putAsync(k, signal.get())).then());
    }

    @Autowired
    CacheManager cacheManager;

    // blokcing using reactive api
    public Mono<String> getProductByIdReactiveApi(String id) {
        Cache cache = cacheManager.getCache("products");
        Optional<String> value = Optional.ofNullable(cache.get(id, String.class));
        return value.map(Mono::just)
                .orElseGet(() ->  {
                    return findByIdMongoReactiveMethod(id).doOnSuccess(v -> cache.put(id, v));
                });
    }
}