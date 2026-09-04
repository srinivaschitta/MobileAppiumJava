package com.saucedemo.mobile.data;

public final class DemoUser {

    private final String username;
    private final String password;

    private DemoUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static DemoUser standardUser() {
        return new DemoUser("bob@example.com", "10203040");
    }

    public static DemoUser wrongPasswordUser() {
        return new DemoUser("bob@example.com", "wrong-password");
    }

    public static DemoUser lockedOutUser() {
        return new DemoUser("alice@example.com", "10203040");
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }
}
