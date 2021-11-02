package com.rc.adapter.message;

import com.rc.adapter.BaseAdapter;
import com.rc.adapter.ViewHolder;
import com.rc.app.Launcher;
import com.rc.components.SizeAutoAdjustTextArea;
import com.rc.components.message.MessageImageLabel;
import com.rc.components.RCListView;
import com.rc.components.message.MessagePopupMenu;
import com.rc.components.message.RCMessageBubble;
import com.rc.db.model.Message;
import com.rc.db.model.Room;
import com.rc.db.service.MessageService;
import com.rc.entity.FileAttachmentItem;
import com.rc.entity.MessageItem;
import com.rc.frames.ImageViewerFrame;
import com.rc.panels.ChatPanel;
import com.rc.frames.MainFrame;
import com.rc.components.UserInfoPopup;
import com.rc.helper.AttachmentIconHelper;
import com.rc.helper.MessageViewHolderCacheHelper;
import com.rc.panels.RoomsPanel;
import com.rc.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rc.utils.ImageUtil.isGif;

/**
 * Created by song on 17-6-2.
 */
public class MessageAdapter extends BaseAdapter<BaseMessageViewHolder>
{
    private final RCListView listView;
    private List<MessageItem> messageItems;
    private AttachmentIconHelper attachmentIconHelper = new AttachmentIconHelper();
    private MessageService messageService = Launcher.messageService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private MessagePopupMenu popupMenu = new MessagePopupMenu();


    MessageViewHolderCacheHelper messageViewHolderCacheHelper;

    public MessageAdapter(List<MessageItem> messageItems, RCListView listView, MessageViewHolderCacheHelper messageViewHolderCacheHelper)
    {
        this.messageItems = messageItems;
        this.listView = listView;

        //currentUser = currentUserService.findAll().get(0);
        this.messageViewHolderCacheHelper = messageViewHolderCacheHelper;
    }

    @Override
    public int getItemViewType(int position)
    {
        return messageItems.get(position).getMessageType();
    }

    @Override
    public BaseMessageViewHolder onCreateViewHolder(int viewType, int position)
    {
        switch (viewType)
        {
            case MessageItem.SYSTEM_MESSAGE:
            {
                MessageSystemMessageViewHolder holder = messageViewHolderCacheHelper.tryGetSystemMessageViewHolder();


                if (holder == null)
                {
                    holder = new MessageSystemMessageViewHolder();
                }

                return holder;
            }
            case MessageItem.RIGHT_TEXT:
            {
                MessageRightTextViewHolder holder = messageViewHolderCacheHelper.tryGetRightTextViewHolder();


                if (holder == null)
                {
                    holder = new MessageRightTextViewHolder();
                }

                return holder;
            }
            case MessageItem.LEFT_TEXT:
            {
                MessageLeftTextViewHolder holder = messageViewHolderCacheHelper.tryGetLeftTextViewHolder();


                if (holder == null)
                {
                    holder = new MessageLeftTextViewHolder();
                }

                return holder;
            }
            case MessageItem.RIGHT_IMAGE:
            {
                MessageRightImageViewHolder holder = messageViewHolderCacheHelper.tryGetRightImageViewHolder();


                if (holder == null)
                {
                    holder = new MessageRightImageViewHolder();
                }

                return holder;
            }
            case MessageItem.LEFT_IMAGE:
            {
                MessageLeftImageViewHolder holder = messageViewHolderCacheHelper.tryGetLeftImageViewHolder();


                if (holder == null)
                {
                    holder = new MessageLeftImageViewHolder();
                }

                return holder;
            }
            case MessageItem.RIGHT_ATTACHMENT:
            {
                MessageRightAttachmentViewHolder holder = messageViewHolderCacheHelper.tryGetRightAttachmentViewHolder();


                if (holder == null)
                {
                    holder = new MessageRightAttachmentViewHolder();
                }

                return holder;
            }
            case MessageItem.LEFT_ATTACHMENT:
            {
                MessageLeftAttachmentViewHolder holder = messageViewHolderCacheHelper.tryGetLeftAttachmentViewHolder();


                if (holder == null)
                {
                    holder = new MessageLeftAttachmentViewHolder();
                }

                return holder;
            }
        }

        return null;
    }

    @Override
    public void onBindViewHolder(BaseMessageViewHolder viewHolder, int position)
    {
        if (viewHolder == null)
        {
            return;
        }

        final MessageItem item = messageItems.get(position);
        MessageItem preItem = position == 0 ? null : messageItems.get(position - 1);

        processTimeAndAvatar(item, preItem, viewHolder);

        if (viewHolder instanceof MessageSystemMessageViewHolder)
        {
            processSystemMessage(viewHolder, item);
        } else if (viewHolder instanceof MessageRightTextViewHolder)
        {
            processRightTextMessage(viewHolder, item);
        } else if (viewHolder instanceof MessageLeftTextViewHolder)
        {
            processLeftTextMessage(viewHolder, item);
        } else if (viewHolder instanceof MessageRightImageViewHolder)
        {
            processRightImageMessage(viewHolder, item);
        } else if (viewHolder instanceof MessageLeftImageViewHolder)
        {
            processLeftImageMessage(viewHolder, item);
        } else if (viewHolder instanceof MessageRightAttachmentViewHolder)
        {
            processRightAttachmentMessage(viewHolder, item);
        } else if (viewHolder instanceof MessageLeftAttachmentViewHolder)
        {
            processLeftAttachmentMessage(viewHolder, item);
        }
    }

    private void processSystemMessage(ViewHolder viewHolder, MessageItem item)
    {
        MessageSystemMessageViewHolder holder = (MessageSystemMessageViewHolder) viewHolder;
        holder.text.setText(item.getMessageContent());
    }

    private void processLeftAttachmentMessage(ViewHolder viewHolder, MessageItem item)
    {
        MessageLeftAttachmentViewHolder holder = (MessageLeftAttachmentViewHolder) viewHolder;
        holder.attachmentTitle.setText(item.getMessageContent());

        Map map = new HashMap();
        map.put("attachmentId", item.getFileAttachment().getId());
        map.put("name", item.getFileAttachment().getTitle());
        map.put("messageId", item.getId());
        holder.attachmentPanel.setTag(map);

        ImageIcon attachmentTypeIcon = attachmentIconHelper.getImageIcon(item.getFileAttachment().getTitle());
        holder.attachmentIcon.setIcon(attachmentTypeIcon);
        holder.sender.setText(item.getSenderUsername());

        setAttachmentClickListener(holder, item);
        processAttachmentSize(holder, item);

        //listView.setScrollHiddenOnMouseLeave(holder.attachmentPanel);
        //listView.setScrollHiddenOnMouseLeave(holder.messageBubble);
        //listView.setScrollHiddenOnMouseLeave(holder.attachmentTitle);

        // 绑定右键菜单
        attachPopupMenu(viewHolder, MessageItem.LEFT_ATTACHMENT);
    }

    private void processRightAttachmentMessage(ViewHolder viewHolder, MessageItem item)
    {
        MessageRightAttachmentViewHolder holder = (MessageRightAttachmentViewHolder) viewHolder;
        holder.attachmentTitle.setText(item.getMessageContent());

        Map map = new HashMap();
        map.put("attachmentId", item.getFileAttachment().getId());
        map.put("name", item.getFileAttachment().getTitle());
        map.put("messageId", item.getId());
        holder.attachmentPanel.setTag(map);

        ImageIcon attachmentTypeIcon = attachmentIconHelper.getImageIcon(item.getFileAttachment().getTitle());
        holder.attachmentIcon.setIcon(attachmentTypeIcon);

        if (item.getProgress() != 0 && item.getProgress() != 100)
        {
            Message msg = messageService.findById(item.getId());
            if (msg != null)
            {
                item.setProgress(msg.getProgress());

                holder.progressBar.setVisible(true);
                holder.progressBar.setValue(item.getProgress());

                if (item.getProgress() == 100)
                {
                    holder.progressBar.setVisible(false);
                } else
                {
                    if (!ChatPanel.getContext().uploadingOrDownloadingFiles.contains(item.getFileAttachment().getId()))
                    {
                        item.setNeedToResend(true);
                    }
                }
            }
        } else
        {
            holder.progressBar.setVisible(false);
        }


        // 判断是否显示重发按钮
        if (item.isNeedToResend())
        {
            holder.sizeLabel.setVisible(false);
            holder.progressBar.setVisible(false);
            holder.resend.setVisible(true);
        } else
        {
            holder.resend.setVisible(false);
        }

        holder.resend.addMouseListener(new MessageMouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (item.getUpdatedAt() > 0)
                {
                    holder.resend.setVisible(false);
                    System.out.println("这条消息其实已经发送出去了-file");
                    return;
                }

                // TODO: 向服务器重新发送消息
                Message message = messageService.findById(item.getId());
                message.setUpdatedAt(System.currentTimeMillis());
                message.setNeedToResend(false);
                messageService.update(message);
                holder.resend.setVisible(false);

                Room room = Launcher.getContext().roomService.findById(message.getRoomId());
                room.setLastMessage("[图片]");
                Launcher.getContext().roomService.update(room);
                RoomsPanel.getContext().notifyDataSetChanged(true);

                super.mouseClicked(e);
            }
        });

        setAttachmentClickListener(holder, item);

        if (item.getUpdatedAt() > 0)
        {
            processAttachmentSize(holder, item);
        } else
        {
            holder.sizeLabel.setText("等待上传...");
        }

        // 绑定右键菜单
        attachPopupMenu(viewHolder, MessageItem.RIGHT_ATTACHMENT);

        // listView.setScrollHiddenOnMouseLeave(holder.attachmentPanel);
        // listView.setScrollHiddenOnMouseLeave(holder.messageBubble);
        // listView.setScrollHiddenOnMouseLeave(holder.attachmentTitle);
    }

    /**
     * 设置附件点击监听
     *
     * @param viewHolder
     * @param item
     */
    private void setAttachmentClickListener(MessageAttachmentViewHolder viewHolder, MessageItem item)
    {
        MessageMouseListener listener = new MessageMouseListener()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    ChatPanel.getContext().downloadOrOpenFile(item.getId());
                }
            }
        };


        viewHolder.attachmentPanel.addMouseListener(listener);
        viewHolder.attachmentTitle.addMouseListener(listener);
    }

    private void processAttachmentSize(MessageAttachmentViewHolder viewHolder, MessageItem item)
    {
        FileAttachmentItem attachment = item.getFileAttachment();
        String path;
        // 远程服务器文件
        if (attachment.getLink().startsWith("/file-upload"))
        {
            path = FileCache.tryGetFileCache(item.getFileAttachment().getId(), item.getFileAttachment().getTitle());
        }
        // 我自己上传的文件
        else
        {
            path = attachment.getLink();
        }

        if (path != null)
        {
            viewHolder.sizeLabel.setVisible(true);
            viewHolder.sizeLabel.setText(FileCache.fileSizeString(path));
        }
    }

    /**
     * 对方发送的图片
     *
     * @param viewHolder
     * @param item
     */
    private void processLeftImageMessage(ViewHolder viewHolder, MessageItem item)
    {
        MessageLeftImageViewHolder holder = (MessageLeftImageViewHolder) viewHolder;
        holder.sender.setText(item.getSenderUsername());

        processImage(item, holder.image, holder);

        // listView.setScrollHiddenOnMouseLeave(holder.image);
        // listView.setScrollHiddenOnMouseLeave(holder.imageBubble);

        // 绑定右键菜单
        attachPopupMenu(viewHolder, MessageItem.LEFT_IMAGE);
    }

    /**
     * 我发送的图片
     *
     * @param viewHolder
     * @param item
     */
    private void processRightImageMessage(ViewHolder viewHolder, MessageItem item)
    {
        MessageRightImageViewHolder holder = (MessageRightImageViewHolder) viewHolder;

        processImage(item, holder.image, holder);

        if (item.getProgress() != 0 && item.getProgress() != 100)
        {
            Message msg = messageService.findById(item.getId());
            if (msg != null)
            {
                item.setProgress(msg.getProgress());

                if (item.getProgress() == 100)
                {
                    holder.sendingProgress.setVisible(false);
                } else
                {
                    if (!ChatPanel.getContext().uploadingOrDownloadingFiles.contains(item.getImageAttachment().getId()))
                    {
                        item.setNeedToResend(true);
                    }
                }
            }
        } else
        {
            if (item.getUpdatedAt() < 1)
            {
                holder.sendingProgress.setVisible(true);
            } else
            {
                holder.sendingProgress.setVisible(false);
            }
        }


        // 判断是否显示重发按钮
        if (item.isNeedToResend())
        {
            holder.resend.setVisible(true);
        } else
        {
            holder.resend.setVisible(false);
        }

        holder.resend.addMouseListener(new MessageMouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (item.getUpdatedAt() > 0)
                {
                    holder.resend.setVisible(false);
                    System.out.println("这条消息其实已经发送出去了-image");
                    return;
                }

                // TODO: 向服务器重新发送消息
                Message message = messageService.findById(item.getId());
                message.setUpdatedAt(System.currentTimeMillis());
                message.setNeedToResend(false);
                messageService.update(message);
                holder.resend.setVisible(false);
                Room room = Launcher.getContext().roomService.findById(message.getRoomId());
                room.setLastMessage("[文件]");
                Launcher.getContext().roomService.update(room);
                RoomsPanel.getContext().notifyDataSetChanged(true);
                super.mouseClicked(e);
            }
        });

        // 绑定右键菜单
        attachPopupMenu(viewHolder, MessageItem.RIGHT_IMAGE);

        // listView.setScrollHiddenOnMouseLeave(holder.image);
        // listView.setScrollHiddenOnMouseLeave(holder.imageBubble);
    }

    private void processImage(MessageItem item, MessageImageLabel imageLabel, ViewHolder holder)
    {
       /* String url;
        if (imageUrl.startsWith("/file-upload"))
        {
            //url = Launcher.HOSTNAME + imageUrl + ".jpg?rc_uid=" + currentUser.getUserId() + "&rc_token=" + currentUser.getAuthToken();
            url = Launcher.HOSTNAME + imageUrl + "?rc_uid=" + currentUser.getUserId() + "&rc_token=" + currentUser.getAuthToken();
        }
        else
        {
            url = "file://" + imageUrl;
        }*/

        loadImageThumb(holder, item, imageLabel);

        // 当点击图片时，使用默认程序打开图片
        imageLabel.addMouseListener(new MessageMouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    ImageCache.requestOriginalAsynchronously(item.getImageAttachment().getId(), item.getImageAttachment().getImageUrl(), new ImageCache.ImageCacheRequestListener()
                    {
                        @Override
                        public void onSuccess(ImageIcon icon, String path)
                        {
                            try
                            {
                                //Desktop.getDesktop().open(new File(path));
                                ImageViewerFrame frame = new ImageViewerFrame(path);
                                frame.setVisible(true);

                                // 如果图片获取成功，则重新加载缩略图
                                if (!isGif(path))
                                {
                                    ImageIcon thumbIcon = (ImageIcon) imageLabel.getIcon();
                                    if (thumbIcon.getDescription().endsWith("image_error.png"))
                                    {
                                        loadImageThumb(holder, item, imageLabel);
                                    }
                                }

                            } catch (Exception e1)
                            {
                                JOptionPane.showMessageDialog(null, "图像不存在", "图像不存在", JOptionPane.ERROR_MESSAGE);
                                e1.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(String why)
                        {
                            // 图片不存在，显示错误图片
                            ImageViewerFrame frame = new ImageViewerFrame(getClass().getResource("/image/image_error.png").getPath());
                            frame.setVisible(true);
                        }
                    });
                }
                super.mouseClicked(e);
            }
        });
    }

    private void loadImageThumb(ViewHolder holder, MessageItem item, MessageImageLabel imageLabel)
    {
        String imageUrl = item.getImageAttachment().getImageUrl();

        Map map = new HashMap();
        map.put("attachmentId", item.getImageAttachment().getId());
        map.put("url", imageUrl);
        map.put("messageId", item.getId());
        imageLabel.setTag(map);

        ImageIcon imageIcon = null;
        boolean isGif = isGif(imageUrl);
        if (!isGif)
        {
            imageIcon = ImageCache.tryGetThumbCache(item.getImageAttachment().getId());
        }

        if (imageIcon == null)
        {
            imageLabel.setIcon(IconUtil.getIcon(this, "/image/image_loading.gif", true));

            if (isGif)
            {
                // 显示GIF图, 不使用缩略图
                ImageCache.requestOriginalAsynchronously(item.getImageAttachment().getId(), imageUrl, new ImageCache.ImageCacheRequestListener()
                {
                    @Override
                    public void onSuccess(ImageIcon icon, String path)
                    {
                        ImageIcon resized = preferredImageSize(icon, path);
                        imageLabel.setIcon(resized);
                        holder.revalidate();
                        holder.repaint();
                    }

                    @Override
                    public void onFailed(String why)
                    {
                        imageLabel.setIcon(IconUtil.getIcon(this, "/image/image_error.png", 64, 64, true));
                        holder.revalidate();
                        holder.repaint();
                    }
                });
            } else
            {
                // 显示其他图像, 获取缩略图
                ImageCache.requestThumbAsynchronously(item.getImageAttachment().getId(), imageUrl, new ImageCache.ImageCacheRequestListener()
                {
                    @Override
                    public void onSuccess(ImageIcon icon, String path)
                    {
                        ImageIcon resized = preferredImageSize(icon, path);
                        preferredImageSize(resized, path);
                        imageLabel.setIcon(icon);
                        holder.revalidate();
                        holder.repaint();
                    }

                    @Override
                    public void onFailed(String why)
                    {
                        System.err.println(imageUrl + " -- " + why);
                        imageLabel.setIcon(IconUtil.getIcon(this, "/image/image_error.png", 64, 64, true));
                        holder.revalidate();
                        holder.repaint();
                    }
                });
            }

        } else
        {
            preferredImageSize(imageIcon, null);
            imageLabel.setIcon(imageIcon);
        }
    }

    /**
     * 根据图片尺寸大小调整图片显示的大小
     *
     * @param imageIcon
     * @return
     */
    public ImageIcon preferredImageSize(ImageIcon imageIcon, String path)
    {
        int width = imageIcon.getIconWidth();
        int height = imageIcon.getIconHeight();
        float scale = width * 1.0F / height;

        int tWidth = width;
        int tHeight = height;

        // 限制图片显示大小
        int maxImageWidth = (int) (MainFrame.getContext().currentWindowWidth * 0.2);
        if (width > maxImageWidth)
        {
            tWidth = maxImageWidth;
            tHeight = (int) (tWidth / scale);
        }

        boolean isGif = path != null && isGif(path);
        if (isGif)
        {
            return ImageUtil.scaleGif(path, tWidth * 1.0F / width, tHeight * 1.0F / height);
        } else
        {
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(tWidth, tHeight, Image.SCALE_SMOOTH));
        }

        return imageIcon;
    }


    /**
     * 处理 我发送的文本消息
     *
     * @param viewHolder
     * @param item
     */
    private void processRightTextMessage(ViewHolder viewHolder, final MessageItem item)
    {
        MessageRightTextViewHolder holder = (MessageRightTextViewHolder) viewHolder;

        handleTextAndExpression(holder.expressionLabel, holder.messageBubble, holder.text, item);


        // 判断是否显示重发按钮
        boolean needToUpdateResendStatus = !item.isNeedToResend() && item.getUpdatedAt() < 1 && System.currentTimeMillis() - item.getTimestamp() > 10 * 1000;

        if (item.isNeedToResend() || needToUpdateResendStatus)
        {
            if (needToUpdateResendStatus)
            {
                messageService.updateNeedToResend(item.getId(), true);
            }

            logger.debug("显示重发按钮");

            holder.sendingProgress.setVisible(false);
            holder.resend.setVisible(true);
        } else
        {
            holder.resend.setVisible(false);
            // 如果是刚发送的消息，显示正在发送进度条
            if (item.getUpdatedAt() < 1)
            {
                holder.sendingProgress.setVisible(true);
            } else
            {
                holder.sendingProgress.setVisible(false);
            }
        }


        holder.resend.addMouseListener(new MessageMouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (item.getUpdatedAt() > 0)
                {
                    holder.resend.setVisible(false);
                    System.out.println("这条消息其实已经发送出去了-text");
                    return;
                }

                // TODO: 向服务器重新发送消息
                Message message = messageService.findById(item.getId());
                message.setUpdatedAt(System.currentTimeMillis());
                message.setNeedToResend(false);
                messageService.update(message);
                holder.resend.setVisible(false);
                Room room = Launcher.getContext().roomService.findById(message.getRoomId());
                room.setLastMessage(message.getMessageContent());
                Launcher.getContext().roomService.update(room);
                RoomsPanel.getContext().notifyDataSetChanged(false);

                super.mouseClicked(e);
            }
        });

        // 绑定右键菜单
        attachPopupMenu(viewHolder, MessageItem.RIGHT_TEXT);
    }

    /**
     * 处理 对方 发送的文本消息
     *
     * @param viewHolder
     * @param item
     */
    private void processLeftTextMessage(ViewHolder viewHolder, final MessageItem item)
    {
        MessageLeftTextViewHolder holder = (MessageLeftTextViewHolder) viewHolder;

        /*holder.text.setText(item.getMessageContent());
        holder.text.setTag(item.getId());*/

        handleTextAndExpression(holder.expressionLabel, holder.messageBubble, holder.text, item);


        holder.sender.setText(item.getSenderUsername());

        // listView.setScrollHiddenOnMouseLeave(holder.messageBubble);
        // listView.setScrollHiddenOnMouseLeave(holder.text);
        attachPopupMenu(viewHolder, MessageItem.LEFT_TEXT);
    }

    /**
     * 处理文本与表情，如果消息只包含一个表情而没有其他内容，则尝试解析是否存在对应的表情。
     *
     * @param expressionLabel
     * @param messageBubble
     * @param text
     * @param item
     */
    private void handleTextAndExpression(JLabel expressionLabel, JComponent messageBubble, SizeAutoAdjustTextArea text, MessageItem item)
    {
        boolean isExpression = true;
        if (item.getMessageContent().matches(" :\\w+: "))
        {
            String filename = item.getMessageContent().substring(2, item.getMessageContent().length() - 2);

            URL url = getClass().getResource("/expression/meng2/" + filename + ".gif");
            if (url != null)
            {
                ImageIcon imageIcon = new ImageIcon(url);

                expressionLabel.setIcon(imageIcon);
                messageBubble.setVisible(false);
                expressionLabel.setVisible(true);
            } else
            {
                isExpression = false;
            }

        } else if (item.getMessageContent().matches(":\\w+:"))
        {
            // 自定义表情
            String code = item.getMessageContent();
            if (EmojiUtil.isCustomEmoji(code))
            {
                ImageIcon imageIcon = EmojiUtil.getCustomEmoji(code, 120, 120);
                expressionLabel.setIcon(imageIcon);
                messageBubble.setVisible(false);
                expressionLabel.setVisible(true);
            }
            else
            {
                isExpression = false;
            }

        } else
        {
            isExpression = false;
        }

        if (!isExpression)
        {
            expressionLabel.setVisible(false);
            messageBubble.setVisible(true);

            text.setText(item.getMessageContent());
            text.setTag(item.getId());
        }
    }

    /**
     * 处理消息发送时间 以及 消息发送者头像
     *
     * @param item
     * @param preItem
     * @param holder
     */
    private void processTimeAndAvatar(MessageItem item, MessageItem preItem, BaseMessageViewHolder holder)
    {
        // 如果当前消息的时间与上条消息时间相差大于1分钟，则显示当前消息的时间
        if (preItem != null)
        {
            if (TimeUtil.inTheSameMinute(item.getTimestamp(), preItem.getTimestamp()))
            {
                holder.time.setVisible(false);
            } else
            {
                holder.time.setVisible(true);
                holder.time.setText(TimeUtil.diff(item.getTimestamp(), true));
            }
        } else
        {
            holder.time.setVisible(true);
            holder.time.setText(TimeUtil.diff(item.getTimestamp(), true));
        }

        if (holder.avatar != null)
        {
            ImageIcon icon = new ImageIcon();
            Image image = AvatarUtil.createOrLoadUserAvatar(item.getSenderUsername()).getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            icon.setImage(image);
            holder.avatar.setIcon(icon);

            if (item.getMessageType() == MessageItem.LEFT_ATTACHMENT
                    || item.getMessageType() == MessageItem.LEFT_IMAGE
                    || item.getMessageType() == MessageItem.LEFT_TEXT)
            {
                bindAvatarAction(holder.avatar, item.getSenderUsername());
            }
        }


        /*
        {
            holder.avatar.setImageBitmap(AvatarUtil.createOrLoadUserAvatar(this.activity, item.getSenderUsername()));
        }*/
    }


    private void bindAvatarAction(JLabel avatarLabel, String username)
    {

        avatarLabel.addMouseListener(new MessageMouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                UserInfoPopup popup = new UserInfoPopup(username);
                popup.show(e.getComponent(), e.getX(), e.getY());

                super.mouseClicked(e);
            }
        });
    }

    @Override
    public int getCount()
    {
        return messageItems.size();
    }

    private void attachPopupMenu(ViewHolder viewHolder, int messageType)
    {
        JComponent contentComponent = null;
        RCMessageBubble messageBubble = null;

        switch (messageType)
        {
            case MessageItem.RIGHT_TEXT:
            {
                MessageRightTextViewHolder holder = (MessageRightTextViewHolder) viewHolder;
                contentComponent = holder.text;
                messageBubble = holder.messageBubble;

                break;
            }
            case MessageItem.LEFT_TEXT:
            {
                MessageLeftTextViewHolder holder = (MessageLeftTextViewHolder) viewHolder;
                contentComponent = holder.text;
                messageBubble = holder.messageBubble;
                break;
            }
            case MessageItem.RIGHT_IMAGE:
            {
                MessageRightImageViewHolder holder = (MessageRightImageViewHolder) viewHolder;
                contentComponent = holder.image;
                messageBubble = holder.imageBubble;
                break;
            }
            case MessageItem.LEFT_IMAGE:
            {
                MessageLeftImageViewHolder holder = (MessageLeftImageViewHolder) viewHolder;
                contentComponent = holder.image;
                messageBubble = holder.imageBubble;
                break;
            }
            case MessageItem.RIGHT_ATTACHMENT:
            {
                MessageRightAttachmentViewHolder holder = (MessageRightAttachmentViewHolder) viewHolder;
                contentComponent = holder.attachmentPanel;
                messageBubble = holder.messageBubble;

                holder.attachmentTitle.addMouseListener(new MessageMouseListener()
                {
                    @Override
                    public void mouseReleased(MouseEvent e)
                    {
                        if (e.getButton() == MouseEvent.BUTTON3)
                        {
                            // 通过holder.attachmentPane.getTag()可以获取文件附件信息
                            popupMenu.show(holder.attachmentPanel, e.getX(), e.getY(), MessageItem.RIGHT_ATTACHMENT);
                        }
                    }
                });
                break;
            }
            case MessageItem.LEFT_ATTACHMENT:
            {
                MessageLeftAttachmentViewHolder holder = (MessageLeftAttachmentViewHolder) viewHolder;
                contentComponent = holder.attachmentPanel;
                messageBubble = holder.messageBubble;

                holder.attachmentTitle.addMouseListener(new MessageMouseListener()
                {
                    @Override
                    public void mouseReleased(MouseEvent e)
                    {
                        if (e.getButton() == MouseEvent.BUTTON3)
                        {
                            popupMenu.show(holder.attachmentPanel, e.getX(), e.getY(), MessageItem.LEFT_ATTACHMENT);
                        }
                    }
                });
                break;
            }
        }

        JComponent finalContentComponent = contentComponent;
        RCMessageBubble finalMessageBubble = messageBubble;

        contentComponent.addMouseListener(new MessageMouseListener()
        {
            @Override
            public void mouseExited(MouseEvent e)
            {
                if (e.getX() > finalContentComponent.getWidth() || e.getY() > finalContentComponent.getHeight())
                {
                    finalMessageBubble.setBackgroundIcon(finalMessageBubble.getBackgroundNormalIcon());
                }
                super.mouseExited(e);
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                finalMessageBubble.setBackgroundIcon(finalMessageBubble.getBackgroundActiveIcon());
                super.mouseEntered(e);
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON3)
                {
                    popupMenu.show((Component) e.getSource(), e.getX(), e.getY(), messageType);
                }

                super.mouseReleased(e);
            }
        });

        messageBubble.addMouseListener(new MessageMouseListener()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON3)
                {
                    popupMenu.show(finalContentComponent, e.getX(), e.getY(), messageType);
                }
            }
        });
    }


}
