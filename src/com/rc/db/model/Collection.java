package com.rc.db.model;

import java.sql.Date;

/**
 * Created by song on 08/11/2019.
 */

public class Collection extends BasicModel implements Comparable<Collection>
{
    private String id;
    private String name;
    private String description;
    private int priority;
    private Date deadline;
    private boolean read;
    private boolean notified;

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

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public int getPriority()
    {
        return priority;
    }

    public void setPriority(int priority)
    {
        this.priority = priority;
    }

    public Date getDeadline()
    {
        return deadline;
    }

    public void setDeadline(Date deadline)
    {
        this.deadline = deadline;
    }

    public boolean isRead()
    {
        return read;
    }

    public void setRead(boolean read)
    {
        this.read = read;
    }

    @Override
    public int compareTo(Collection o)
    {
        return this.id.compareTo(o.id);
    }


    public boolean isNotified()
    {
        return notified;
    }

    public void setNotified(boolean notified)
    {
        this.notified = notified;
    }
}


