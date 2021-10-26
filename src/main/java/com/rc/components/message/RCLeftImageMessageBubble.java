package com.rc.components.message;

import java.awt.*;

/**
 * 右侧文本聊天气泡
 */
public class RCLeftImageMessageBubble extends RCAttachmentMessageBubble
{
    public RCLeftImageMessageBubble()
    {
        NinePatchImageIcon backgroundNormal = new NinePatchImageIcon(this.getClass().getResource("/image/left.9.png"));
        NinePatchImageIcon backgroundActive = new NinePatchImageIcon(this.getClass().getResource("/image/left_active.9.png"));
        setBackgroundNormalIcon(backgroundNormal);
        setBackgroundActiveIcon(backgroundActive);
        setBackgroundIcon(backgroundNormal);
    }

    @Override
    public Insets getInsets()
    {
        return new Insets(2, 9, 3, 2);
        //return new Insets(9,9,9,9);
    }
}
