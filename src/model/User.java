package model;

import java.util.concurrent.atomic.AtomicInteger;

public class User {
    private final int id;
    private String name;
    private String email;
    private static AtomicInteger counter = new AtomicInteger(1);


    public User(String name, String email) {
        this.id = counter.getAndIncrement();
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        int recNo = id;
        return recNo + "  ----  " + name + ", " + email;
    }}
