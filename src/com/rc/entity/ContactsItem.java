package com.rc.entity;

import com.rc.utils.CharacterParser;

/**
 * Created by song on 17-5-30.
 */
public class ContactsItem implements Comparable<ContactsItem>
{
    private String id;
    private String name;
    private String type;

    public ContactsItem()
    {
    }

    public ContactsItem(String id, String name, String type)
    {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "ContactsItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public int compareTo(ContactsItem o)
    {
        String tc = CharacterParser.getSelling(this.getName()).toUpperCase();
        String oc = CharacterParser.getSelling(o.getName()).toUpperCase();
        return tc.compareTo(oc);
    }
}
