package zs.assignment4.services;

import zs.assignment4.model.LruCache;

public class LruCacheService {
    private static final int DEFAULT_CAPACITY = 10;
    private final LruCache<String, String> cache;

    public LruCacheService() {
        this.cache = new LruCache<>(DEFAULT_CAPACITY);
    }

    public void put(String key, String value) {
        cache.put(key, value);
    }

    public String get(String key) {
        return cache.get(key);
    }

    public boolean contains(String key) {
        return cache.contains(key);
    }

    public String displayCache() {
        return cache.display();
    }

    public int size() {
        return cache.size();
    }
}
