package com.example.campuscart.constants;

import org.springframework.security.core.parameters.P;

public class Constants {
    public static final String SECERET_KEY = "fefevjnkijojoi3rfefnjenlkfldnoirjf0rof3rifropnmlknfdwfo9p0o9fn3relkmv0p3j";
    public static final int JWT_TOKEN_VALIDITY = 7*24*60*60;
    public static final int COOKIE_VALIDITY = 7*24*60*60;
    public static final String AUTH_COOKIE = "AUTH_COOKIE";
    public static final String LOGIN_SUCCESS = "Login is successful";
    public static final String LOGOUT_SUCCESS = "Logout is successful";
    public static final String ROLES = "roles";

    public static class QueryConstants {
        public final static String FETCH_ADDRESS_BY_ACCOUNT_ID = "SELECT add FROM Address add WHERE add.user.accountId=:accountId";
        public final static String FETCH_CARTITEM_BY_CART_AND_PRODUCT = "SELECT ct FROM CartItem ct WHERE ct.cart.id = :cartId AND ct.product.id = :productId";
        public final static String FETCH_CART_BY_ACCOUNT_ID = "SELECT c FROM Cart c WHERE c.user.accountId = :accountId";
        public final static String FETCH_ORDER_BY_ACCOUNT_ID = "SELECT o FROM Order o WHERE o.user.accountId = :accountId";
        public final static String FETCH_PRODUCTS_BY_SELLER_ID = "SELECT p FROM Product p WHERE p.seller.accountId = :accountId";
        public final static String FETCH_SERVICEABLE_PRODUCTS_BY_CATEGORY = "SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.isServiceable = true";
    }
}
