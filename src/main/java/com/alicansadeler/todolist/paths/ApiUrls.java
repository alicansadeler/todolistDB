package com.alicansadeler.todolist.paths;

import com.alicansadeler.todolist.service.AuthService;

public final class ApiUrls {
    private ApiUrls() {}

    public static final String BASE = "/api/v1";
    public static final String USERS = BASE + "/users";
    public static final String TASKS = BASE + "/tasks";


    public static final String ID = "/{id}";
    public static final String BY_USER_ID = "/user/{userId}";
    public static final String BY_STATUS = "/status/{status}";

    public static final String AUTH = BASE + "/auth";
    public static final String LOGIN = AUTH + "/login";
    public static final String REGISTER = AUTH + "/register";

}