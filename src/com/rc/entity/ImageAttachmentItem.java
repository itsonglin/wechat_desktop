package com.rc.entity;

import com.rc.db.model.ImageAttachment;

/**
 * Created by song on 17/05/2017.
 */

public class ImageAttachmentItem
{
    private String id;
    private String title;
    private String description;
    private String imageUrl;
    private int width;
    private int height;
    private long imagesize;

    public ImageAttachmentItem()
    {
    }

    public ImageAttachmentItem(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public ImageAttachmentItem(ImageAttachment ia)
    {
        this.id = ia.getId();
        this.title = ia.getTitle();
        this.description = ia.getDescription();
        this.imageUrl = ia.getImageUrl();
        this.width = ia.getWidth();
        this.height = ia.getHeight();
        this.imagesize = ia.getImagesize();
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public long getImagesize()
    {
        return imagesize;
    }

    public void setImagesize(long imagesize)
    {
        this.imagesize = imagesize;
    }
}