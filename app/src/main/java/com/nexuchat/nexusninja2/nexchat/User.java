package com.nexuchat.nexusninja2.nexchat;

import android.net.Uri;

/**
 * Created by NexusNinja2 on 5/30/2017.
 */

public class User
{
    private String mEmail, mPassword, mFullName, mGender, mUserID, mPhotoUrl;

    public User()
    {

    }

    public User(String mEmail, String mPassword, String mFullName, String mGender, String mUserID) {
        this.mEmail = mEmail;
        this.mPassword = mPassword;
        this.mFullName = mFullName;
        this.mGender = mGender;
        this.mUserID = mUserID;
    }

    public User(String mEmail, String mFullName, String mPhotoUrl) {
        this.mEmail = mEmail;
        this.mFullName = mFullName;
        this.mPhotoUrl = mPhotoUrl;
    }

    public String getmEmail() {

        return mEmail;
    }

    public String getmPhotoUrl() {
        return mPhotoUrl;
    }

    public void setmPhotoUrl(String mPhotoUrl) {
        this.mPhotoUrl = mPhotoUrl;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getmFullName() {
        return mFullName;
    }

    public void setmFullName(String mFullName) {
        this.mFullName = mFullName;
    }

    public String getmGender() {
        return mGender;
    }

    public void setmGender(String mGender) {
        this.mGender = mGender;
    }

    public String getmUserID() {
        return mUserID;
    }

    public void setmUserID(String mUserID) {
        this.mUserID = mUserID;
    }
}
