package com.recycup.recycup;

public class User {
    String phoneNumber;
    String name;
    String password;

    private static class SingletonHolder {
        private static User INSTANCE = new User();
    }

    public static User getInstance() {

        return SingletonHolder.INSTANCE;
    }

    public User() {

    }
    public User(String phoneNumber, String name) {

    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
