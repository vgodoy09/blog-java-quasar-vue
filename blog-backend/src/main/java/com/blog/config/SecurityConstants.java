package com.blog.config;

public class SecurityConstants {
    public static final String SECRET = "$2a$10$tLV8fgldUVw44Un.FS.mKOHj5KxEtQDQrRn.HlgG2ZYYvRt19HY2a"; // WEylogP21R6I5LoyrLv1BC
    public static final long EXPIRATION_TIME = 86400000; // 1 day
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/api/auth/login";
    public static final String REFRESH_URL = "/api/auth/refresh";
    public static final String REGISTER_URL = "/api/auth/register";
    public static final String LOGOUT_URL = "/api/auth/logout";
    public static final String USER_URL = "/api/auth/user";
    public static final String PWS = "$2a$10$NInVIuDcX6ELFV9L4HbQEuTDfTokgU8NCU.9jB8SHVSRq0BIwOigm"; // WEylogP21R6I5LoyrLv1
    public static final String USR = "api.blog";
    
    private SecurityConstants() {}
}