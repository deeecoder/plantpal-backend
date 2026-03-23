package com.plantpal.dto;

public class AuthResponse {
    private String token;
    private String type;
    private Long userId;
    private String name;
    private String email;

    public AuthResponse() {}

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public static AuthResponseBuilder builder() { return new AuthResponseBuilder(); }

    public static class AuthResponseBuilder {
        private String token, type, name, email;
        private Long userId;

        public AuthResponseBuilder token(String t) { this.token = t; return this; }
        public AuthResponseBuilder type(String t) { this.type = t; return this; }
        public AuthResponseBuilder userId(Long id) { this.userId = id; return this; }
        public AuthResponseBuilder name(String n) { this.name = n; return this; }
        public AuthResponseBuilder email(String e) { this.email = e; return this; }

        public AuthResponse build() {
            AuthResponse r = new AuthResponse();
            r.token = this.token; r.type = this.type;
            r.userId = this.userId; r.name = this.name; r.email = this.email;
            return r;
        }
    }
}
