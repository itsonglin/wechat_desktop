package com.rc.db.model;

/**
 * Created by song on 09/06/2017.
 */

/**
 * Created by song on 10/03/2017.
 */

public class Room extends BasicModel implements Comparable<Room>
{
    private String roomId;
    private String type;
    private String name;
    private String topic;
    private String muted;
    private String member;
    private boolean sysMes;
    private int msgSum;
    private long lastChatAt;
    private String creatorName;
    private String creatorId;
    private String jitsiTimeout;
    private boolean readOnly;
    private boolean archived;
    private boolean defaultRoom;
    private String createdAt;
    private String updatedAt;
    private int unreadCount;
    private int totalReadCount;
    private String lastMessage;


    public int getUnreadCount()
    {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount)
    {
        this.unreadCount = unreadCount;
    }
    public String getRoomId()
    {
        return roomId;
    }

    public void setRoomId(String roomId)
    {
        this.roomId = roomId;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getTopic()
    {
        return topic;
    }

    public void setTopic(String topic)
    {
        this.topic = topic;
    }

    public String getMuted()
    {
        return muted;
    }

    public void setMuted(String muted)
    {
        this.muted = muted;
    }

    public String getMember()
    {
        return member;
    }

    public void setMember(String member)
    {
        this.member = member;
    }

    public boolean isSysMes()
    {
        return sysMes;
    }

    public void setSysMes(boolean sysMes)
    {
        this.sysMes = sysMes;
    }

    public int getMsgSum()
    {
        return msgSum;
    }

    public void setMsgSum(int msgSum)
    {
        this.msgSum = msgSum;
    }

    public long getLastChatAt()
    {
        return lastChatAt;
    }

    public void setLastChatAt(long lastChatAt)
    {
        this.lastChatAt = lastChatAt;
    }

    public String getCreatorName()
    {
        return creatorName;
    }

    public void setCreatorName(String creatorName)
    {
        this.creatorName = creatorName;
    }

    public String getCreatorId()
    {
        return creatorId;
    }

    public void setCreatorId(String creatorId)
    {
        this.creatorId = creatorId;
    }

    public String getJitsiTimeout()
    {
        return jitsiTimeout;
    }

    public void setJitsiTimeout(String jitsiTimeout)
    {
        this.jitsiTimeout = jitsiTimeout;
    }

    public boolean isReadOnly()
    {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly)
    {
        this.readOnly = readOnly;
    }

    public boolean isArchived()
    {
        return archived;
    }

    public void setArchived(boolean archived)
    {
        this.archived = archived;
    }

    public boolean isDefaultRoom()
    {
        return defaultRoom;
    }

    public void setDefaultRoom(boolean defaultRoom)
    {
        this.defaultRoom = defaultRoom;
    }

    public String getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(String createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt()
    {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString()
    {
        return "Room{" +
                "roomId='" + roomId + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", topic='" + topic + '\'' +
                ", muted='" + muted + '\'' +
                ", member='" + member + '\'' +
                ", sysMes=" + sysMes +
                ", msgSum=" + msgSum +
                ", lastChatAt=" + lastChatAt +
                ", creatorName='" + creatorName + '\'' +
                ", creatorId='" + creatorId + '\'' +
                ", jitsiTimeout='" + jitsiTimeout + '\'' +
                ", readOnly=" + readOnly +
                ", archived=" + archived +
                ", defaultRoom=" + defaultRoom +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", unreadCount=" + unreadCount +
                ", totalReadCount=" + totalReadCount +
                ", lastMessage='" + lastMessage + '\'' +
                '}';
    }

    @Override
    public int compareTo(Room o)
    {
        if (this.getType().equals(o.getType()))
        {
            return (int)(this.getLastChatAt() - o.getLastChatAt());
        }
        else
        {
            return this.getType().compareTo(o.getType());
        }
    }

    public String getLastMessage()
    {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage)
    {
        this.lastMessage = lastMessage;
    }

    public int getTotalReadCount()
    {
        return totalReadCount;
    }

    public void setTotalReadCount(int totalReadCount)
    {
        this.totalReadCount = totalReadCount;
    }
}


