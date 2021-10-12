package com.backendapi.hotelmanagement.constant;

public final class Constants {

    public static final String API_SECRET_KEY = "hotelmanagementkey";

    // token validity is 2 hours, after token will expire
    public static final long TOKEN_VALIDITY = 2 * 60 * 60 * 1000;

    private Constants(){

    }
}
