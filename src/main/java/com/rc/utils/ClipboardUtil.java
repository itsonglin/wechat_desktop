package com.rc.utils;

import com.rc.app.Launcher;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by song on 20/06/2017.
 */
public class ClipboardUtil
{
    private static Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    public static final String CLIPBOARD_TEMP_DIR;

    static
    {
        CLIPBOARD_TEMP_DIR = Launcher.appFilesBasePath + System.getProperty("file.separator") + "clipboard_temp";
        File file = new File(CLIPBOARD_TEMP_DIR);
        if (!file.exists())
        {
            System.out.println("创建剪切板临时文件缓存目录：" + file.getAbsolutePath());
            file.mkdirs();
        }
    }

    public static void copyString(String content)
    {
        if (content != null)
        {
            Transferable tText = new StringSelection(content);
            clipboard.setContents(tText, null);
        }
    }

    public static void copyImage(String path)
    {
        try
        {
            Image image = ImageIO.read(new File(path));
            clipboard.setContents(new ImageTransferable(image), null);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void copyImage(Image image)
    {
        clipboard.setContents(new ImageTransferable(image), null);
    }

    public static void copyFile(String path)
    {
        try
        {
            File file = new File(path);
            //clipboard.setContents(new FileTransferable(file), null);
            Transferable contents = new Transferable()
            {
                DataFlavor[] dataFlavors = new DataFlavor[]{DataFlavor.javaFileListFlavor};

                @Override
                public Object getTransferData(DataFlavor flavor)
                        throws UnsupportedFlavorException, IOException
                {
                    ArrayList<File> files = new ArrayList<>();
                    files.add(file);
                    return files;
                }

                @Override
                public DataFlavor[] getTransferDataFlavors()
                {
                    return dataFlavors;
                }

                @Override
                public boolean isDataFlavorSupported(DataFlavor flavor)
                {
                    for (int i = 0; i < dataFlavors.length; i++)
                    {
                        if (dataFlavors[i].equals(flavor))
                        {
                            return true;
                        }
                    }
                    return false;
                }
            };

            clipboard.setContents(contents, null);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static Object paste()
    {
        Transferable transferable = clipboard.getContents(null);
        if (transferable != null)
        {
            try
            {
                if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
                {
                    List<File> files = (java.util.List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                    List<Object> datas = new ArrayList<>();
                    for (File file : files)
                    {
                        // 复制的是图片
                        if (isImage(file))
                        {
                            datas.add(transferImageFileToImageIcon(file));
                        }
                        // 复制的是非图片文件
                        else
                        {
                            datas.add(file.getAbsolutePath());
                        }

                    }

                    return datas;
                }
                else if (transferable.isDataFlavorSupported(DataFlavor.imageFlavor))
                {
                    Object obj = transferable.getTransferData(DataFlavor.imageFlavor);

                    if (obj instanceof Image)
                    {
                        Image image = (Image) obj;
                        File destFile = new File(CLIPBOARD_TEMP_DIR + File.separator + "clipboard_image_" + UUID.randomUUID() + ".png");
                        BufferedImage outImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
                        outImage.getGraphics().drawImage(image, 0, 0, null);
                        ImageIO.write(outImage, "png", destFile);

                        ImageIcon icon = new ImageIcon(image);
                        icon.setDescription(destFile.getAbsolutePath());
                        return icon;
                    }
                }
                else if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor))
                {
                    return transferable.getTransferData(DataFlavor.stringFlavor);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * @param file
     * @return
     */
    private static boolean isImage(File file)
    {
        String suffix = file.getName().substring(file.getName().lastIndexOf(".") + 1).toLowerCase();
        return suffix.equals("jpg") || suffix.equals("jpeg") || suffix.equals("png") || suffix.equals("gif");
    }

    private static ImageIcon transferImageFileToImageIcon(File file)
    {
        ImageIcon icon = new ImageIcon(file.getAbsolutePath());
        icon.setDescription(file.getAbsolutePath());
        return icon;
    }

    /**
     * 清除剪切板缓存文件
     */
    public static void clearCache()
    {
        System.out.println("清除剪切板缓存文件...");
        File file = new File(CLIPBOARD_TEMP_DIR);
        File[] files = file.listFiles();
        for (File f : files)
        {
            f.delete();
        }
    }
}

class ImageTransferable implements Transferable
{
    private Image image;

    public ImageTransferable(Image image)
    {
        this.image = image;
    }

    public DataFlavor[] getTransferDataFlavors()
    {
        return new DataFlavor[]{DataFlavor.imageFlavor};
    }

    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        return DataFlavor.imageFlavor.equals(flavor);
    }

    public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException, IOException
    {
        if (isDataFlavorSupported(flavor))
            return image;
        throw new UnsupportedFlavorException(flavor);
    }
}

class FileTransferable implements Transferable
{
    private File file;

    public FileTransferable(File file)
    {
        this.file = file;
    }

    DataFlavor[] dataFlavors = new DataFlavor[]{DataFlavor.javaFileListFlavor};

    @Override
    public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException, IOException
    {
        return new ArrayList<>().add(file);
    }

    @Override
    public DataFlavor[] getTransferDataFlavors()
    {
        return dataFlavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        for (int i = 0; i < dataFlavors.length; i++)
        {
            if (dataFlavors[i].equals(flavor))
            {
                return true;
            }
        }
        return false;
    }
}