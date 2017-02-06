package com.example.quyet.podomoro.settings;

/**
 * Created by quyet on 1/14/2017.
 */

public class LoginCredentials {

    private String username;
    private String password;
    private String token;
    public LoginCredentials(String password, String username, String token) {
        this.password = password;
        this.username = username;
        this.token  = token;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "LoginCredentials{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
