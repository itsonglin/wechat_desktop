package com.rc.db.model;


/**
 * Created by song on 09/03/2017.
 */

public class ContactsUser extends BasicModel
{
    private String userId;

    private String username;

    private String name;

    public ContactsUser()
    {
    }

    public ContactsUser(String userId, String username, String name)
    {
        this.userId = userId;
        this.username = username;
        this.name = name;
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "ContactsUser{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
