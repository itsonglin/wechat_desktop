package com.rc.entity;

/**
 * Created by song on 20/06/2017.
 */
public class SelectUserData
{
    private String name;
    private boolean selected;

    public SelectUserData(String name, boolean selected)
    {
        this.name = name;
        this.selected = selected;
    }


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }
}
