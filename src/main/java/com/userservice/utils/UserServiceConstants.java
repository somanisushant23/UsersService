package com.userservice.utils;

public class UserServiceConstants {

    //user ack messages
    public static final String USER_CREATED = "User profile created";
    public static final String USER_UPDATED = "User profile updated";

    //error messages
    public static final String USER_NOT_FOUND = "User profile not found";
    public static final String USER_NOT_AUTHORIZED = "Client forbidden!!";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    public static final String DATABASE_ERROR = "A database constraint violation occurred";
    public static final String DUPLICATE_EMAIL = "Email already exists, please use a different email address";
    public static final int ENCRYPTION_CYCLES = 16;
}
