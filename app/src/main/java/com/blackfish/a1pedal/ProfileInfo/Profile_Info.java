package com.blackfish.a1pedal.ProfileInfo;

public class Profile_Info {
    private static Profile_Info dataObject = null;

    private Profile_Info() {
        // left blank intentionally
    }

    public static Profile_Info getInstance() {
        if (dataObject == null)
            dataObject = new Profile_Info();
        return dataObject;
    }


    private String token;
    User UserObject;


    // Getter Methods

    public String getToken() {
        return token;
    }

    public User getUser() {
        return UserObject;
    }

    // Setter Methods

    public void setToken(String token) {
        this.token = token;
    }

    public void setUser(User userObject) {
        this.UserObject = userObject;
    }
}
