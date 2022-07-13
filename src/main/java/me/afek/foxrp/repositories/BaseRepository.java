package me.afek.foxrp.repositories;

import java.util.List;

public interface BaseRepository<K, V> {

    void initial();

    void close();

    void addData(K key, V value);

    boolean containsData(K key);

    V removeData(K key);

    V getData(K key);

    List<V> getData();

}
