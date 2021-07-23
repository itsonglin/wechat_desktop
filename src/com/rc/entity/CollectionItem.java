package com.rc.entity;

/**
 * @author song
 * @date 19-11-11 18:09
 * @description
 * @since
 */
public class CollectionItem
{
    public String name;
    public String description;
    public int priority;
    public String deadline;
    public String id;
    public boolean read;
    public boolean notified;

    public CollectionItem()
    {
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

    public String getDeadline()
    {
        return deadline;
    }

    public void setDeadline(String deadline)
    {
        this.deadline = deadline;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public boolean isRead()
    {
        return read;
    }

    public void setRead(boolean read)
    {
        this.read = read;
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
