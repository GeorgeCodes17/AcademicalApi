package com.AcademicalApi.objects;

public record Account(String username, String password, String firstName, String lastName) {
    public Account(String username, String password) {
        this(username, password, null, null);
    }
}
