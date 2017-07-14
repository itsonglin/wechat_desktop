package com.rc.entity;

import com.rc.app.Launcher;
import com.rc.db.model.FileAttachment;
import com.rc.db.model.ImageAttachment;
import com.rc.db.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by song on 20/03/2017.
 */

public class MessageItem implements Comparable<MessageItem>
{
    public static final int SYSTEM_MESSAGE = 0;
    public static final int LEFT_TEXT = 1;
    public static final int LEFT_IMAGE = 2;
    public static final int LEFT_ATTACHMENT = 3;

    public static final int RIGHT_TEXT = -1;
    public static final int RIGHT_IMAGE = -2;
    public static final int RIGHT_ATTACHMENT = -3;


    private String id;
    private String roomId;
    private String messageContent;
    private boolean groupable;
    private long timestamp;
    private String senderUsername;
    private String senderId;
    private long updatedAt;
    private int unreadCount;
    private boolean needToResend;
    private int progress;
    private boolean deleted;
    private int messageType;

    /*List<FileAttachmentItem> fileAttachments = new ArrayList<>();
    List<ImageAttachmentItem> imageAttachments = new ArrayList<>();*/

    private FileAttachmentItem fileAttachment;
    private ImageAttachmentItem imageAttachment;

    public MessageItem()
    {
    }

    public MessageItem(Message message, String currentUserId)
    {
        this();
        this.setId(message.getId());
        this.setMessageContent(message.getMessageContent());
        this.setGroupable(message.isGroupable());
        this.setRoomId(message.getRoomId());
        this.setSenderId(message.getSenderId());
        this.setSenderUsername(message.getSenderUsername());
        this.setTimestamp(message.getTimestamp());
        this.setUpdatedAt(message.getUpdatedAt());
        this.setNeedToResend(message.isNeedToResend());
        this.setProgress(message.getProgress());
        this.setDeleted(message.isDeleted());

        boolean isFileAttachment = false;
        boolean isImageAttachment = false;

        if (message.getFileAttachmentId() != null)
        {
            isFileAttachment = true;

            FileAttachment fa = Launcher.fileAttachmentService.findById(message.getFileAttachmentId());
            this.fileAttachment = new FileAttachmentItem(fa);
        }
        if (message.getImageAttachmentId() != null)
        {
            isImageAttachment = true;

            ImageAttachment ia = Launcher.imageAttachmentService.findById(message.getImageAttachmentId());
            this.imageAttachment = new ImageAttachmentItem(ia);
        }

        /*for (FileAttachment fa : message.getFileAttachments())
        {
            this.fileAttachments.add(new FileAttachmentItem(fa));
        }

        for (ImageAttachment ia : message.getImageAttachments())
        {
            this.imageAttachments.add(new ImageAttachmentItem(ia));
        }*/

        if (message.isSystemMessage())
        {
            this.setMessageType(SYSTEM_MESSAGE);
        }
        else
        {
            // 自己发的消息
            if (message.getSenderId().equals(currentUserId))
            {
                // 文件附件
                if (isFileAttachment)
                {
                    this.setMessageType(RIGHT_ATTACHMENT);
                }
                // 图片消息
                else if (isImageAttachment)
                {
                    this.setMessageType(RIGHT_IMAGE);
                }
                // 普通文本消息
                else
                {
                    this.setMessageType(RIGHT_TEXT);
                }
            }
            else
            {
                // 文件附件
                if (isFileAttachment)
                {
                    this.setMessageType(LEFT_ATTACHMENT);
                }
                // 图片消息
                else if (isImageAttachment)
                {
                    this.setMessageType(LEFT_IMAGE);
                }
                // 普通文本消息
                else
                {
                    this.setMessageType(LEFT_TEXT);
                }
            }
        }
    }

    @Override
    public int compareTo( MessageItem o)
    {
        return (int) (this.getTimestamp() - o.getTimestamp());

    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getRoomId()
    {
        return roomId;
    }

    public void setRoomId(String roomId)
    {
        this.roomId = roomId;
    }

    public String getMessageContent()
    {
        return messageContent;
    }

    public void setMessageContent(String messageContent)
    {
        this.messageContent = messageContent;
    }

    public boolean isGroupable()
    {
        return groupable;
    }

    public void setGroupable(boolean groupable)
    {
        this.groupable = groupable;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getSenderUsername()
    {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername)
    {
        this.senderUsername = senderUsername;
    }

    public String getSenderId()
    {
        return senderId;
    }

    public void setSenderId(String senderId)
    {
        this.senderId = senderId;
    }

    public long getUpdatedAt()
    {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public int getUnreadCount()
    {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount)
    {
        this.unreadCount = unreadCount;
    }

    public boolean isNeedToResend()
    {
        return needToResend;
    }

    public void setNeedToResend(boolean needToResend)
    {
        this.needToResend = needToResend;
    }

    public int getProgress()
    {
        return progress;
    }

    public void setProgress(int progress)
    {
        this.progress = progress;
    }

    public boolean isDeleted()
    {
        return deleted;
    }

    public void setDeleted(boolean deleted)
    {
        this.deleted = deleted;
    }

    public int getMessageType()
    {
        return messageType;
    }

    public void setMessageType(int messageType)
    {
        this.messageType = messageType;
    }

    public FileAttachmentItem getFileAttachment()
    {
        return fileAttachment;
    }

    public void setFileAttachment(FileAttachmentItem fileAttachment)
    {
        this.fileAttachment = fileAttachment;
    }

    public ImageAttachmentItem getImageAttachment()
    {
        return imageAttachment;
    }

    public void setImageAttachment(ImageAttachmentItem imageAttachment)
    {
        this.imageAttachment = imageAttachment;
    }
}

