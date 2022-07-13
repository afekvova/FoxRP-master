package me.afek.foxrp.repositories;

import java.util.List;

public interface BaseRepository<K, V> {

    public void initial();

    public void close();

    public void addData(K key, V value);

    public boolean containData(K key);

    public V removeData(K key);

    public V getData(K key);

    public List<V> getData();
}
