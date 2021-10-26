package com.rc.db.model;

/**
 * Created by song on 08/06/2017.
 */
public class CurrentUser extends BasicModel
{
    private String userId;

    private String username;

    private String authToken;

    private String password;

    private String rawPassword;

    private String expireDate;

    private String realName;

    private String bcrypt;

    private String avatarOrigin;

    public CurrentUser()
    {
    }

    public CurrentUser(String userId, String username, String authToken, String password, String rawPassword, String expireDate, String realName, String bcrypt, String avatarOrigin)
    {
        this.userId = userId;
        this.username = username;
        this.authToken = authToken;
        this.password = password;
        this.rawPassword = rawPassword;
        this.expireDate = expireDate;
        this.realName = realName;
        this.bcrypt = bcrypt;
        this.avatarOrigin = avatarOrigin;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getAuthToken()
    {
        return authToken;
    }

    public void setAuthToken(String authToken)
    {
        this.authToken = authToken;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getRawPassword()
    {
        return rawPassword;
    }

    public void setRawPassword(String rawPassword)
    {
        this.rawPassword = rawPassword;
    }

    public String getExpireDate()
    {
        return expireDate;
    }

    public void setExpireDate(String expireDate)
    {
        this.expireDate = expireDate;
    }

    public String getRealName()
    {
        return realName;
    }

    public void setRealName(String realName)
    {
        this.realName = realName;
    }

    public String getBcrypt()
    {
        return bcrypt;
    }

    public void setBcrypt(String bcrypt)
    {
        this.bcrypt = bcrypt;
    }

    public String getAvatarOrigin()
    {
        return avatarOrigin;
    }

    public void setAvatarOrigin(String avatarOrigin)
    {
        this.avatarOrigin = avatarOrigin;
    }

    @Override
    public String toString()
    {
        return "CurrentUser{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", authToken='" + authToken + '\'' +
                ", password='" + password + '\'' +
                ", rawPassword='" + rawPassword + '\'' +
                ", expireDate='" + expireDate + '\'' +
                ", realName='" + realName + '\'' +
                ", bcrypt='" + bcrypt + '\'' +
                ", avatarOrigin='" + avatarOrigin + '\'' +
                '}';
    }
}
