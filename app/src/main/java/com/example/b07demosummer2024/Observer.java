package com.example.b07demosummer2024;

public abstract class Observer {
    DatabaseManager manager;
    public Observer(DatabaseManager manager) {
        this.manager = manager;
    }
    public abstract void update(int maxPages);
}
