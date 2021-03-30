package org.turings.investigationapplicqation.Entity;

import java.io.Serializable;

public class User implements Serializable {

    private int id;
    private String user_name;//用户名字
    private String head_protrait;//头像
    private String password;//密码
    private String phone;//唯一标识手机号
    public User() {
        super();
    }


    public User(int id, String user_name, String head_protrait, String password, String unique_id) {
        super();
        this.id = id;
        this.user_name = user_name;
        this.head_protrait = head_protrait;
        this.password = password;
        this.phone = unique_id;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUser_name() {
        return user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getHead_protrait() {
        return head_protrait;
    }


    public void setHead_protrait(String head_protrait) {
        this.head_protrait = head_protrait;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }
}
