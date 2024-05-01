package com.example.campuscart.constants;

public class Constants {
    public static final String SECERET_KEY = "fefevjnkijojoi3rfefnjenlkfldnoirjf0rof3rifropnmlknfdwfo9p0o9fn3relkmv0p3j";
    public static final int JWT_TOKEN_VALIDITY = 7*24*60*60;
    public static final int COOKIE_VALIDITY = 7*24*60*60;
    public static final String AUTH_COOKIE = "AUTH_COOKIE";
    public static final String LOGIN_SUCCESS = "Login is successful";
    public static final String LOGOUT_SUCCESS = "Logout is successful";
    public static final String ROLES = "roles";

    public class QueryConstants {
        public final static String FETCH_ADDRESS_BY_ACCOUNT_ID = "SELECT add FROM Address add WHERE add.user.accountId=:accountId";
    }
}
