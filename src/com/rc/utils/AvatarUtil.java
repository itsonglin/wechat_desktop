package com.rc.utils;

import com.rc.app.Launcher;
import com.rc.components.Colors;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by song on 15/03/2017.
 */

public class AvatarUtil
{
    private static final Color[] colorArr;

    static
    {
        colorArr = new Color[]{
                new Color(244, 67, 54),
                new Color(233, 30, 99),
                new Color(156, 39, 176),
                new Color(103, 58, 183),
                new Color(63, 81, 181),
                new Color(33, 150, 243),
                new Color(3, 169, 244),
                new Color(0, 188, 212),
                new Color(0, 150, 136),
                new Color(76, 175, 80),
                new Color(139, 195, 74),
                new Color(205, 220, 57),
                new Color(255, 193, 7),
                new Color(255, 152, 0),
                new Color(255, 87, 34),
                new Color(121, 85, 72),
                new Color(158, 158, 158),
                new Color(96, 125, 139)
        };
    }

    private static final String AVATAR_CACHE_ROOT;
    private static final String CUSTOM_AVATAR_CACHE_ROOT;
    private static final int DEFAULT_AVATAR = 0;
    private static final int CUSTOM_AVATAR = 1;

    private static Map<String, Image> avatarCache = new HashMap<>();

    static
    {
        AVATAR_CACHE_ROOT = Launcher.appFilesBasePath + "/cache/avatar";

        File file = new File(AVATAR_CACHE_ROOT);
        if (!file.exists())
        {
            file.mkdirs();
            System.out.println("创建头像缓存目录：" + file.getAbsolutePath());
        }

        CUSTOM_AVATAR_CACHE_ROOT = AVATAR_CACHE_ROOT + "/custom";
        file = new File(CUSTOM_AVATAR_CACHE_ROOT);
        if (!file.exists())
        {
            file.mkdirs();
            System.out.println("创建用户自定义头像缓存目录：" + file.getAbsolutePath());
        }
    }


    public static Image createOrLoadGroupAvatar(String groupName, String[] members, String type)
    {
        Image avatar;
        avatar = avatarCache.get(groupName);

        // 如果在内存中的缓存
        if (avatar == null)
        {
            avatar = getCachedImageAvatar(groupName);

            // 如果在硬盘中有缓存的文件
            if (avatar == null)
            {
                // 如果尚未从服务器获取群成员，则获取默认群组头像
                if (members == null || members.length < 1)
                {
                    String sign = type.equals("p") ? "#" : "##";
                    avatar = getCachedImageAvatar(sign);

                    // 默认群组头像不存在，则生成
                    if (avatar == null)
                    {
                        System.out.println("创建群组默认头像 : " + groupName);
                        avatar = createAvatar(sign, groupName);
                    }
                }
                // 有群成员，根据群成员的头像合成群头像
                else
                {
                    System.out.println("创建群组个性头像 : " + groupName);
                    avatar = createGroupAvatar(groupName, members);
                }
            }

            avatarCache.put(groupName, avatar);
        }

        return avatar;
    }

    public static Image createOrLoadUserAvatar(String username)
    {
        Image avatar;

        avatar = avatarCache.get(username);
        if (avatar == null)
        {
            avatar = getCachedImageAvatar(username);
            if (avatar == null)
            {
                avatar = createAvatar(username, username);
            }

            avatarCache.put(username, avatar);
        }

        return avatar;
    }

    /**
     * 刷新用户头像缓存
     * @param username
     */
    public static void refreshUserAvatarCache(String username)
    {
        avatarCache.put(username, null);
    }


    private static Image createAvatar(String sign, String name)
    {
        String drawString;
        if (sign.length() > 1)
        {
            drawString = sign.substring(0, 1).toUpperCase() + sign.substring(1, 2).toLowerCase();
        }
        else
        {
            drawString = sign;
        }

        try
        {
            int width = 200;
            int height = 200;

            // 创建BufferedImage对象
            Font font = FontUtil.getDefaultFont(96, Font.PLAIN);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            // 获取Graphics2D
            Graphics2D g2d = image.createGraphics();

            // 抗锯齿
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // 画图
            g2d.setBackground(getColor(name));
            g2d.clearRect(0, 0, width, height);

            // 文字
            g2d.setFont(font);
            g2d.setPaint(new Color(255, 255, 255));
            FontMetrics fm = g2d.getFontMetrics(font);
            int strWidth = fm.stringWidth(drawString);
            int strHeight = fm.getHeight();
            int x = (width - strWidth) / 2;

            g2d.drawString(drawString, x, strHeight);

            BufferedImage roundImage = ImageUtil.setRadius(image, width, height, 35);

            g2d.dispose();
            File file = new File(AVATAR_CACHE_ROOT + "/" + sign + ".png");
            ImageIO.write(roundImage, "png", file);

            return roundImage;
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }


    private static Color getColor(String username)
    {
        int position = username.length() % colorArr.length;
        return colorArr[position];
    }

    public static void saveAvatar(BufferedImage image, String username)
    {
        saveAvatar(image, username, CUSTOM_AVATAR);
    }

    private static void saveAvatar(BufferedImage image, String username, int type)
    {
        String path = "";
        if (type == DEFAULT_AVATAR)
        {
            path = AVATAR_CACHE_ROOT + "/" + username + ".png";
        }
        else if (type == CUSTOM_AVATAR)
        {
            path = CUSTOM_AVATAR_CACHE_ROOT + "/" + username + ".png";
        }
        else
        {
            throw new RuntimeException("类型不存在");
        }

        File avatarPath = new File(path);

        try
        {
            if (image != null)
            {
                BufferedImage bufferedImage = ImageUtil.setRadius(image, image.getWidth(), image.getHeight(), 35);
                ImageIO.write(bufferedImage, "png", avatarPath);
            }
            else
            {
                throw new RuntimeException("头像保存失败，数据为空");
            }

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static Image getCachedImageAvatar(String username)
    {
        if (customAvatarExist(username))
        {
            String path = CUSTOM_AVATAR_CACHE_ROOT + "/" + username + ".png";

            return readImage(path);
        }
        else if (defaultAvatarExist(username))
        {
            String path = AVATAR_CACHE_ROOT + "/" + username + ".png";
            return readImage(path);
        }
        else
        {
            return null;
        }
    }

    private static BufferedImage readImage(String path)
    {
        try
        {
            return ImageIO.read(new File(path));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }


    public static boolean customAvatarExist(String username)
    {
        String path = CUSTOM_AVATAR_CACHE_ROOT + "/" + username + ".png";
        File file = new File(path);
        return file.exists();
    }

    public static boolean defaultAvatarExist(String username)
    {
        String path = AVATAR_CACHE_ROOT + "/" + username + ".png";
        File file = new File(path);
        return file.exists();
    }

    public static void deleteCustomAvatar(String username)
    {
        String path = CUSTOM_AVATAR_CACHE_ROOT + "/" + username + ".png";

        File file = new File(path);
        if (file.exists())
        {
            file.delete();
        }
    }

    public static void deleteGroupAvatar(String groupName)
    {
        String path = AVATAR_CACHE_ROOT + "/" + groupName + ".png";
        File file = new File(path);
        if (file.exists())
        {
            file.delete();
        }
    }


    public static Image createGroupAvatar(String groupName, String[] users)
    {

        try
        {
            int width = 200;
            int height = 200;

            // 创建BufferedImage对象
            // 选择TYPE_INT_ARGB目的在于可创建透明背景的图，否则圆角外的地方会变成黑色
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            // 获取Graphics2D
            Graphics2D g2d = image.createGraphics();

            // 绘制一个圆角的灰色背景
            g2d.setComposite(AlphaComposite.Src);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Colors.GROUP_AVATAR_BACKGROUND);
            g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, 35, 35));
            g2d.setComposite(AlphaComposite.SrcAtop);

            Rectangle[] rectangles = getSubAvatarPoints(users);
            int max = users.length > 9 ? 9 : users.length;
            for (int i = 0; i < max; i++)
            {
                Image avatar = AvatarUtil.createOrLoadUserAvatar(users[i]);
                g2d.drawImage(avatar, rectangles[i].x, rectangles[i].y, rectangles[i].width, rectangles[i].height, null);
            }

            g2d.dispose();

            // 缓存到磁盘
            File file = new File(AVATAR_CACHE_ROOT + "/" + groupName + ".png");
            ImageIO.write(image, "png", file);

            return image;
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }

    private static Rectangle[] getSubAvatarPoints(String[] users)
    {
        int gap = 8;
        int parentWidth = 200;

        Rectangle[] rectangles = new Rectangle[users.length];

        int x;
        int y;

        if (users.length == 1)
        {
            int childWidth = parentWidth / 2;
            x = (parentWidth - childWidth) / 2;
            rectangles[0] = new Rectangle(x, x, childWidth, childWidth);
        }
        else if (users.length == 2)
        {
            int childWidth = (parentWidth - gap * 3) / 2;

            // 第一个
            y = (parentWidth - childWidth) / 2;
            Rectangle r1 = new Rectangle(gap, y, childWidth, childWidth);

            // 第二个
            x = gap * 2 + childWidth;
            Rectangle r2 = new Rectangle(x, y, childWidth, childWidth);

            rectangles[0] = r1;
            rectangles[1] = r2;
        }
        else if (users.length == 3)
        {
            int childWidth = (parentWidth - gap * 3) / 2;


            // 第一个
            x = (parentWidth - childWidth) / 2;
            y = gap;
            Rectangle r1 = new Rectangle(x, y, childWidth, childWidth);

            // 第二个
            x = gap;
            y = childWidth + gap * 2;
            Rectangle r2 = new Rectangle(x, y, childWidth, childWidth);

            // 第三个
            x = childWidth + gap * 2;
            Rectangle r3 = new Rectangle(x, y, childWidth, childWidth);


            rectangles[0] = r1;
            rectangles[1] = r2;
            rectangles[2] = r3;
        }
        else if (users.length == 4)
        {
            int childWidth = (parentWidth - gap * 3) / 2;


            // 第一个
            Rectangle r1 = new Rectangle(gap, gap, childWidth, childWidth);

            // 第二个
            x = childWidth + gap * 2;
            Rectangle r2 = new Rectangle(x, gap, childWidth, childWidth);

            // 第三个
            x = gap;
            y = childWidth + gap * 2;
            Rectangle r3 = new Rectangle(x, y, childWidth, childWidth);

            // 第四个
            x = childWidth + gap * 2;
            Rectangle r4 = new Rectangle(x, y, childWidth, childWidth);


            rectangles[0] = r1;
            rectangles[1] = r2;
            rectangles[2] = r3;
            rectangles[3] = r4;
        }
        else if (users.length == 5)
        {
            int childWidth = (parentWidth - gap * 4) / 3;

            // 第一个
            x = (parentWidth - childWidth * 2 - gap) / 2;
            Rectangle r1 = new Rectangle(x, x, childWidth, childWidth);

            // 第二个
            y = x;
            x = x + gap + childWidth;
            Rectangle r2 = new Rectangle(x, y, childWidth, childWidth);

            // 第三个
            y = r1.y + gap + childWidth;
            Rectangle r3 = new Rectangle(gap, y, childWidth, childWidth);

            // 第四个
            x = gap * 2 + childWidth;
            Rectangle r4 = new Rectangle(x, y, childWidth, childWidth);

            // 第五个
            x = gap * 3 + childWidth * 2;
            Rectangle r5 = new Rectangle(x, y, childWidth, childWidth);

            rectangles[0] = r1;
            rectangles[1] = r2;
            rectangles[2] = r3;
            rectangles[3] = r4;
            rectangles[4] = r5;
        }
        else if (users.length == 6)
        {
            int childWidth = (parentWidth - gap * 4) / 3;

            // 第一个
            y = (parentWidth - childWidth * 2 - gap) / 2;
            Rectangle r1 = new Rectangle(gap, y, childWidth, childWidth);

            // 第二个
            x = gap * 2 + childWidth;
            Rectangle r2 = new Rectangle(x, y, childWidth, childWidth);

            // 第三个
            x = gap * 3 + childWidth * 2;
            Rectangle r3 = new Rectangle(x, y, childWidth, childWidth);


            // 第四个
            y = r1.y + gap + childWidth;
            Rectangle r4 = new Rectangle(gap, y, childWidth, childWidth);

            // 第五个
            x = gap * 2 + childWidth;
            Rectangle r5 = new Rectangle(x, y, childWidth, childWidth);

            // 第六个
            x = gap * 3 + childWidth * 2;
            Rectangle r6 = new Rectangle(x, y, childWidth, childWidth);

            rectangles[0] = r1;
            rectangles[1] = r2;
            rectangles[2] = r3;
            rectangles[3] = r4;
            rectangles[4] = r5;
            rectangles[5] = r6;
        }
        else if (users.length == 7)
        {
            int childWidth = (parentWidth - gap * 4) / 3;

            // 第一个
            x = (parentWidth - childWidth) / 2;
            Rectangle r1 = new Rectangle(x, gap, childWidth, childWidth);

            // 第二个
            y = gap * 2 + childWidth;
            Rectangle r2 = new Rectangle(gap, y, childWidth, childWidth);

            // 第三个
            x = gap * 2 + childWidth;
            Rectangle r3 = new Rectangle(x, y, childWidth, childWidth);

            // 第四个
            x = gap * 3 + childWidth * 2;
            Rectangle r4 = new Rectangle(x, y, childWidth, childWidth);

            // 第五个
            y = r2.y + childWidth + gap;
            Rectangle r5 = new Rectangle(gap, y, childWidth, childWidth);

            // 第六个
            x = gap * 2 + childWidth;
            Rectangle r6 = new Rectangle(x, y, childWidth, childWidth);

            // 第七个
            x = gap * 3 + childWidth * 2;
            Rectangle r7 = new Rectangle(x, y, childWidth, childWidth);

            rectangles[0] = r1;
            rectangles[1] = r2;
            rectangles[2] = r3;
            rectangles[3] = r4;
            rectangles[4] = r5;
            rectangles[5] = r6;
            rectangles[6] = r7;
        }
        else if (users.length == 8)
        {
            int childWidth = (parentWidth - gap * 4) / 3;

            // 第一个
            x = (parentWidth - childWidth * 2 - gap) / 2;
            Rectangle r1 = new Rectangle(x, gap, childWidth, childWidth);

            // 第二个
            x = x + gap + childWidth;
            Rectangle r2 = new Rectangle(x, gap, childWidth, childWidth);

            // 第三个
            y = gap * 2 + childWidth;
            Rectangle r3 = new Rectangle(gap, y, childWidth, childWidth);

            // 第四个
            x = gap * 2 + childWidth;
            Rectangle r4 = new Rectangle(x, y, childWidth, childWidth);

            // 第五个
            x = gap * 3 + childWidth * 2;
            Rectangle r5 = new Rectangle(x, y, childWidth, childWidth);

            // 第六个
            y = r3.y + childWidth + gap;
            Rectangle r6 = new Rectangle(gap, y, childWidth, childWidth);

            // 第七个
            x = gap * 2 + childWidth;
            Rectangle r7 = new Rectangle(x, y, childWidth, childWidth);

            // 第八个
            x = gap * 3 + childWidth * 2;
            Rectangle r8 = new Rectangle(x, y, childWidth, childWidth);


            rectangles[0] = r1;
            rectangles[1] = r2;
            rectangles[2] = r3;
            rectangles[3] = r4;
            rectangles[4] = r5;
            rectangles[5] = r6;
            rectangles[6] = r7;
            rectangles[7] = r8;
        }

        else if (users.length >= 9)
        {
            int childWidth = (parentWidth - gap * 4) / 3;

            int index = 0;
            for (int i = 1; i <= 3; i++)
            {
                y = gap * i + (i - 1) * childWidth;

                for (int j = 1; j <= 3; j++)
                {
                    x = gap * j + (j - 1) * childWidth;
                    Rectangle r = new Rectangle(x, y, childWidth, childWidth);

                    rectangles[index++] = r;
                }
            }
        }

        return rectangles;
    }
}
