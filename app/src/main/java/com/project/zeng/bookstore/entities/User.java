package com.project.zeng.bookstore.entities;

/**
 * Created by zeng on 2017/2/25.
 * User的实体
 */

public class User {

    /**
     * User的域
     */
    private String id;
    private String passwordOrToken;//密码或账号
    private String username;
    private String grade;
    private String sex;
    private String major;
    private String location;
    private String phone;
    private String more;

    public User() {
    }

    public User(String id, String password) {
        this.id = id;
        this.passwordOrToken = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPasswordOrToken() {
        return passwordOrToken;
    }

    public void setPasswordOrToken(String passwordOrToken) {
        this.passwordOrToken = passwordOrToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }
}
