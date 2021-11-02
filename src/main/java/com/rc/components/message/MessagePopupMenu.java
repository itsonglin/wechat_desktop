package com.rc.components.message;

import com.rc.app.Launcher;
import com.rc.res.Colors;
import com.rc.components.RCMenuItemUI;
import com.rc.components.SizeAutoAdjustTextArea;
import com.rc.db.model.FileAttachment;
import com.rc.db.service.FileAttachmentService;
import com.rc.entity.MessageItem;
import com.rc.panels.ChatPanel;
import com.rc.frames.MainFrame;
import com.rc.utils.ClipboardUtil;
import com.rc.utils.FileCache;
import com.rc.utils.ImageCache;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.Map;

/**
 * Created by song on 2017/6/5.
 */
public class MessagePopupMenu extends JPopupMenu
{
    private int messageType;
    private FileAttachmentService fileAttachmentService = Launcher.fileAttachmentService;

    private JMenuItem item1 = new JMenuItem("复制");
    private JMenuItem item2 = new JMenuItem("删除");
    private JMenuItem item3 = new JMenuItem("转发");
    private JMenuItem item4 = new JMenuItem("另存为");

    private JFileChooser jfc = new JFileChooser();


    public MessagePopupMenu()
    {
        initMenuItem();
    }

    private void initMenuItem()
    {

        item1.setUI(new RCMenuItemUI());
        item1.addActionListener(new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                switch (messageType)
                {
                    case MessageItem.RIGHT_TEXT:
                    case MessageItem.LEFT_TEXT:
                    {
                        SizeAutoAdjustTextArea textArea = (SizeAutoAdjustTextArea) getInvoker();
                        String text = textArea.getSelectedText() == null ? textArea.getText() : textArea.getSelectedText();
                        if (text != null)
                        {
                            ClipboardUtil.copyString(text);
                        }
                        break;
                    }
                    case (MessageItem.RIGHT_IMAGE):
                    case (MessageItem.LEFT_IMAGE):
                    {
                        MessageImageLabel imageLabel = (MessageImageLabel) getInvoker();
                        Object obj = imageLabel.getTag();
                        if (obj != null)
                        {
                            Map map = (Map) obj;
                            String id = (String) map.get("attachmentId");
                            String url = (String) map.get("url");
                            ImageCache.requestOriginalAsynchronously(id, url, new ImageCache.ImageCacheRequestListener()
                            {
                                @Override
                                public void onSuccess(ImageIcon icon, String path)
                                {
                                    if (path != null && !path.isEmpty())
                                    {
                                        ClipboardUtil.copyImage(path);
                                    }
                                    else
                                    {
                                        System.out.println("图片不存在，复制失败");
                                    }
                                }

                                @Override
                                public void onFailed(String why)
                                {
                                    System.out.println("图片不存在，复制失败");
                                }
                            });
                        }
                        break;
                    }

                    case (MessageItem.RIGHT_ATTACHMENT):
                    case (MessageItem.LEFT_ATTACHMENT):
                    {
                        AttachmentPanel attachmentPanel = (AttachmentPanel) getInvoker();
                        Object obj = attachmentPanel.getTag();
                        if (obj != null)
                        {
                            Map map = (Map) obj;
                            String id = (String) map.get("attachmentId");
                            String name = (String) map.get("name");

                            String path = FileCache.tryGetFileCache(id, name);
                            if (path != null && !path.isEmpty())
                            {
                                ClipboardUtil.copyFile(path);
                            }
                            else
                            {
                                FileAttachment attachment = fileAttachmentService.findById(id);
                                if (attachment == null)
                                {
                                    JOptionPane.showMessageDialog(MainFrame.getContext(), "文件不存在", "文件不存在", JOptionPane.WARNING_MESSAGE);
                                    return;
                                }

                                String link = attachment.getLink();
                                if (link.startsWith("/file-upload"))
                                {
                                    JOptionPane.showMessageDialog(MainFrame.getContext(), "请先下载文件", "请先下载文件", JOptionPane.WARNING_MESSAGE);
                                    return;
                                }
                                else
                                {
                                    File file = new File(link);
                                    if (!file.exists())
                                    {
                                        JOptionPane.showMessageDialog(MainFrame.getContext(), "文件不存在，可能已被删除", "文件不存在", JOptionPane.WARNING_MESSAGE);
                                        return;
                                    }
                                    ClipboardUtil.copyFile(link);
                                }
                            }
                        }
                        break;
                    }
                }

            }
        });


        item2.setUI(new RCMenuItemUI());
        item2.addActionListener(new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String messageId = null;
                switch (messageType)
                {
                    case MessageItem.RIGHT_TEXT:
                    case MessageItem.LEFT_TEXT:
                    {
                        SizeAutoAdjustTextArea textArea = (SizeAutoAdjustTextArea) getInvoker();
                        messageId = textArea.getTag().toString();
                        break;
                    }
                    case (MessageItem.RIGHT_IMAGE):
                    case (MessageItem.LEFT_IMAGE):
                    {
                        MessageImageLabel imageLabel = (MessageImageLabel) getInvoker();
                        Object obj = imageLabel.getTag();
                        if (obj != null)
                        {
                            Map map = (Map) obj;
                            messageId = (String) map.get("messageId");
                        }
                        break;
                    }
                    case (MessageItem.RIGHT_ATTACHMENT):
                    case (MessageItem.LEFT_ATTACHMENT):
                    {
                        AttachmentPanel attachmentPanel = (AttachmentPanel) getInvoker();
                        Object obj = attachmentPanel.getTag();
                        if (obj != null)
                        {
                            Map map = (Map) obj;
                            messageId = (String) map.get("messageId");
                        }
                        break;
                    }
                }

                if (messageId != null && !messageId.isEmpty())
                {
                    ChatPanel.getContext().deleteMessage(messageId);
                }
            }
        });

        item3.setUI(new RCMenuItemUI());
        item3.addActionListener(new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("转发");
            }
        });

        item4.setUI(new RCMenuItemUI());
        item4.addActionListener(new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                switch (messageType)
                {
                    case (MessageItem.RIGHT_IMAGE):
                    case (MessageItem.LEFT_IMAGE):
                    {
                        MessageImageLabel imageLabel = (MessageImageLabel) getInvoker();
                        Object obj = imageLabel.getTag();
                        if (obj != null)
                        {
                            Map map = (Map) obj;
                            String id = (String) map.get("attachmentId");
                            String url = (String) map.get("url");
                            ImageCache.requestOriginalAsynchronously(id, url, new ImageCache.ImageCacheRequestListener()
                            {
                                @Override
                                public void onSuccess(ImageIcon icon, String path)
                                {
                                    if (path != null && !path.isEmpty())
                                    {
                                        saveAsFile(path);
                                    }
                                    else
                                    {
                                        System.out.println("图片不存在，复制失败");
                                    }
                                }

                                @Override
                                public void onFailed(String why)
                                {
                                    System.out.println("图片不存在，复制失败");
                                }
                            });
                        }
                        break;
                    }

                    case (MessageItem.RIGHT_ATTACHMENT):
                    case (MessageItem.LEFT_ATTACHMENT):
                    {
                        AttachmentPanel attachmentPanel = (AttachmentPanel) getInvoker();
                        Object obj = attachmentPanel.getTag();
                        if (obj != null)
                        {
                            Map map = (Map) obj;
                            String id = (String) map.get("attachmentId");
                            String name = (String) map.get("name");

                            String path = FileCache.tryGetFileCache(id, name);
                            if (path != null && !path.isEmpty())
                            {
                                saveAsFile(path);
                            }
                            else
                            {
                                FileAttachment attachment = fileAttachmentService.findById(id);
                                if (attachment == null)
                                {
                                    JOptionPane.showMessageDialog(MainFrame.getContext(), "文件不存在", "文件不存在", JOptionPane.WARNING_MESSAGE);
                                    return;
                                }

                                String link = attachment.getLink();
                                if (link.startsWith("/file-upload"))
                                {
                                    JOptionPane.showMessageDialog(MainFrame.getContext(), "请先下载文件", "请先下载文件", JOptionPane.WARNING_MESSAGE);
                                    return;
                                }
                                else
                                {
                                    File file = new File(link);
                                    if (!file.exists())
                                    {
                                        JOptionPane.showMessageDialog(MainFrame.getContext(), "文件不存在，可能已被删除", "文件不存在", JOptionPane.WARNING_MESSAGE);
                                        return;
                                    }

                                    saveAsFile(link);
                                }
                            }
                        }
                        break;
                    }
                }

            }
        });

        this.add(item1);
        this.add(item2);
        this.add(item4);

        setBorder(new LineBorder(Colors.SCROLL_BAR_TRACK_LIGHT));
        setBackground(Colors.FONT_WHITE);
    }

    /**
     * 文件另存为
     *
     * @param path
     */
    private void saveAsFile(String path)
    {
        File srcFile = new File(path);
        if (!srcFile.exists())
        {
            JOptionPane.showMessageDialog(null, "文件不存在，可能已被删除或无访问权限", "另存失败", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String filename = srcFile.getName();
        //JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("保存");
        File filePath = FileSystemView.getFileSystemView().getHomeDirectory();
        File defaultFile = new File(filePath + File.separator + filename.substring(filename.indexOf("_") + 1));

        jfc.setSelectedFile(defaultFile);

        int flag = jfc.showSaveDialog(this);

        if (flag == JFileChooser.APPROVE_OPTION)
        {
            File destFile = jfc.getSelectedFile();
            if (destFile == null)
            {
                return;
            }

            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try(FileInputStream inputStream = new FileInputStream(srcFile);
                        FileOutputStream outputStream = new FileOutputStream(destFile))
                    {

                        byte[] buffer = new byte[2048];
                        int len;
                        while ((len = inputStream.read(buffer)) > -1)
                        {
                            outputStream.write(buffer, 0, len);
                        }

                        inputStream.close();
                        outputStream.close();
                    }
                    catch (Exception e)
                    {
                        JOptionPane.showMessageDialog(null, "文件保存失败，文件可能已被删除或无访问权限", "另存失败", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }


    @Override
    public void show(Component invoker, int x, int y)
    {
        throw new RuntimeException("此方法不会弹出菜单，请调用 show(Component invoker, int x, int y, int messageType) ");
        //super.show(invoker, x, y);
    }

    public void show(Component invoker, int x, int y, int messageType)
    {
        if (invoker instanceof MessageImageLabel || invoker instanceof AttachmentPanel)
        {
            item4.setVisible(true);
        }
        else
        {
            item4.setVisible(false);
        }

        this.messageType = messageType;
        super.show(invoker, x, y);
    }


}
