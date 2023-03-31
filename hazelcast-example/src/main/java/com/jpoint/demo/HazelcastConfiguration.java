package com.jpoint.demo;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfiguration {

    @Bean
    public IMap<String, String> cache(HazelcastInstance instance) {
        return instance.getMap("books");
    }
}
