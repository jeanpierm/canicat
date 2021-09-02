package com.jeanpier.canicat.config;

public class Routes {
    // Para ver tu ip local ejecuta el comando "ipconfig" en cmd/powershell. (IPv4)
//    private static final String DOMAIN = "192.168.100.195";
    private static final String DOMAIN = "canicat-backend.herokuapp.com";
//    puerto solo usado para desarrollo, NO en producción
    private static final String PORT = "3000";
    public static final String API_PATH = "/api";
    public static final String PETS_PATH = "/pets";
    public static final String USERS_PATH = "/users";
    public static final String VACCINE_PATH = "/vaccines";
    public static final String LOGIN_PATH = "/auth/login";
    public static final String BASE_URI = "https://" + DOMAIN;
}
