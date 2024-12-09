package com.alicansadeler.todolist.paths;

public final class ApiUrls {
    private ApiUrls() {}

    public static final String BASE = "/api/v1";
    public static final String USERS = BASE + "/users";
    public static final String TASKS = BASE + "/tasks";


    public static final String ID = "/{id}";
    public static final String BY_USER_ID = "/user/{userId}";
    public static final String BY_STATUS = "/status/{status}";
}