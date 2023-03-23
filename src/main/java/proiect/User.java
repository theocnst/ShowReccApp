package proiect;

import enums.RoleEnum;

public class User {
    private String username;
    private String password;
    RoleEnum role;

    public User(String username, String password, RoleEnum role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User() {

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public RoleEnum getRole() {
        return role;
    }

}
