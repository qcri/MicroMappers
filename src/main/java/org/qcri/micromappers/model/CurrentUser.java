package org.qcri.micromappers.model;

/**
 * Created by jlucas on 11/25/16.
 */
public class CurrentUser {

    private String user_name;
    private boolean is_anonymous;

    public CurrentUser(String user_name) {
        this.user_name = user_name;
        this.is_anonymous = false;
    }

    public CurrentUser(){
        this.user_name = "Anonymous";
        this.is_anonymous = true;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public boolean is_anonymous() {
        return is_anonymous;
    }

    public void setIs_anonymous(boolean is_anonymous) {
        this.is_anonymous = is_anonymous;
    }
}
