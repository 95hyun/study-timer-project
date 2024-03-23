package com.toyproject.studytimerproject.domain.user.entity;

public enum UserRole {

    MEMBER(Authority.MEMBER),
    MASTER(Authority.MASTER);

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String MEMBER = "ROLE_MEMBER";
        public static final String MASTER = "ROLE_MASTER";
    }
}
