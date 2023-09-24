package com.project.cinemarest.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.Cache;
import com.project.cinemarest.model.MovieResponse.Movie;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MoviesCache {

    private static MoviesCache instance;

    private final Cache<String, List<Movie>> cache;

    public MoviesCache() {
        this.cache = CacheBuilder.newBuilder()
            .maximumSize(10)
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build();
    }

    public static synchronized MoviesCache getInstance() {
        if (instance == null) {
            instance = new MoviesCache();
        }
        return instance;
    }

    public List<Movie> getFromCache(String key) {
        return cache.getIfPresent(key);
    }

    public void addToCache(String key, List<Movie> movies) {
        cache.put(key, movies);
    }
}
