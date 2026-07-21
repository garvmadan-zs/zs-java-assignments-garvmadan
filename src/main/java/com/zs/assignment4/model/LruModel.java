package com.zs.assignment4.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class LruModel<K, V> {
    private final int capacity;
    private final Map<K, V> entries;

    public LruModel(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero.");
        }
        this.capacity = capacity;
        this.entries = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > capacity;
            }
        };
    }

    public void put(K key, V value) {
        entries.put(key, value);
    }

    public V get(K key) {
        return entries.get(key);
    }

    public boolean contains(K key) {
        return entries.containsKey(key);
    }

    public void remove(K key) {
        entries.remove(key);
    }

    public int size() {
        return entries.size();
    }

    public void clear() {
        entries.clear();
    }

    public String display() {
        if (entries.isEmpty()) {
            return "Cache is empty.";
        }

        StringBuilder builder = new StringBuilder("LRU Cache contents:\n");
        for (Map.Entry<K, V> entry : entries.entrySet()) {
            builder.append("- ").append(entry.getKey()).append(" -> ").append(String.valueOf(entry.getValue())).append("\n");
        }
        return builder.toString();
    }
}
