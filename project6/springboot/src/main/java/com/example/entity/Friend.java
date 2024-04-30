package com.example.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Table(name = "new_table")
public class Friend {

    @Id
    private int userid;

    @Id
    private int upid;

    public Friend() {
    }

    public Friend(int userid, int upid) {
        this.userid = userid;
        this.upid = upid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getUpid() {
        return upid;
    }

    public void setUpid(int upid) {
        this.upid = upid;
    }

}