package com.ls.voluntaryplatformapp.model;

import java.util.List;

public class LoginBean {
        /**
         * accountId : 2
         * authorities : [{"authority":"STUDENT"}]
         * accessToken : a935d0ba-16c5-4ac4-8e83-abbb79807c44
         * expireIn : 2591999
         * scopes : ["api"]
         * refreshToken : 26337fbf-794c-46aa-a341-0c2784dfe5f4
         */

        private int accountId;
        private String accessToken;
        private int expireIn;
        private String refreshToken;
        private List<AuthoritiesBean> authorities;
        private List<String> scopes;

        public int getAccountId() {
            return accountId;
        }

        public void setAccountId(int accountId) {
            this.accountId = accountId;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public int getExpireIn() {
            return expireIn;
        }

        public void setExpireIn(int expireIn) {
            this.expireIn = expireIn;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public List<AuthoritiesBean> getAuthorities() {
            return authorities;
        }

        public void setAuthorities(List<AuthoritiesBean> authorities) {
            this.authorities = authorities;
        }

        public List<String> getScopes() {
            return scopes;
        }

        public void setScopes(List<String> scopes) {
            this.scopes = scopes;
        }

        public static class AuthoritiesBean {
            /**
             * authority : STUDENT
             */

            private String authority;

            public String getAuthority() {
                return authority;
            }

            public void setAuthority(String authority) {
                this.authority = authority;
            }
        }
    }