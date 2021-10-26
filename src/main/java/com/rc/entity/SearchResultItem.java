package com.rc.entity;

/**
 * 搜索结果条目
 * Created by song on 24/03/2017.
 */

public class SearchResultItem implements Comparable<SearchResultItem>
{
    private String id;
    private String name;
    private String type;
    private Object tag;

    public SearchResultItem()
    {
    }

    public SearchResultItem(String id, String name, String type)
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
        return "SearchResultItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public int compareTo(SearchResultItem o)
    {
        return 0;
    }

    public Object getTag()
    {
        return tag;
    }

    public void setTag(Object tag)
    {
        this.tag = tag;
    }
}
