package me.afek.foxrp.repositories.impl;

import me.afek.foxrp.repositories.BaseRepository;
import me.afek.foxrp.repositories.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class WarningRepository implements BaseRepository<String, Integer> {

    private final Map<String, Integer> warnings = new ConcurrentHashMap<>();

    @Override
    public void initial() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
        this.warnings.clear();
    }

    @Override
    public void addData(String key, Integer value) {
        key = key.toLowerCase();
        this.warnings.put(key, this.warnings.getOrDefault(key, 0) + value);
    }

    @Override
    public boolean containsData(String key) {
        return this.warnings.containsKey(key.toLowerCase());
    }

    @Override
    public Integer removeData(String key) {
        return this.warnings.remove(key.toLowerCase());
    }

    @Override
    public Integer getData(String key) {
        return this.warnings.getOrDefault(key.toLowerCase(), 0);
    }

    @Override
    public List<Integer> getData() {
        throw new UnsupportedOperationException();
    }
}
