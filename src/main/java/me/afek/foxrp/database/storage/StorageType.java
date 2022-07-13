package me.afek.foxrp.database.storage;

public enum StorageType {

    JSON(),
    SQLITE(),
    MYSQL(),
    MONGODB();

    public static StorageType parse(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (Exception ignored) {
        }
        return JSON;
    }
}