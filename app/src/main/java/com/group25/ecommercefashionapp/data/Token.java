package com.group25.ecommercefashionapp.data;

public class Token {
    public boolean isTokenValid;
    public String token;
    public Token(String token) {
        this.isTokenValid = true;
        this.token = token;
    }
}
