package com.recycup.recycup_cafe;

public class User {
    String phoneNumber;
    String name;
    String password;
    int point;

    private static class SingletonHolder {
        private static User INSTANCE = new User();
    }

    public static User getInstance() {

        return SingletonHolder.INSTANCE;
    }

    public static void clear(){
        SingletonHolder.INSTANCE.name = null;
        SingletonHolder.INSTANCE.phoneNumber = null;
        SingletonHolder.INSTANCE.password = null;
        SingletonHolder.INSTANCE.point = 0;
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

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
