package com.rc.entity;

import com.rc.utils.CharacterParser;

/**
 * Created by song on 17-5-30.
 */
public class SelectUserItem implements Comparable<SelectUserItem>
{
    private String id;
    private String name;
    private String type;

    public SelectUserItem()
    {
    }

    public SelectUserItem(String id, String name, String type)
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
        return "SelectUserItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public int compareTo(SelectUserItem o)
    {
        String tc = CharacterParser.getSelling(this.getName());
        String oc = CharacterParser.getSelling(o.getName());
        return tc.compareTo(oc);
    }
}
