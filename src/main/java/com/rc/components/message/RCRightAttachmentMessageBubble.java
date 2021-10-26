package com.rc.components.message;

import java.awt.*;

/**
 * 右侧附件聊天气泡
 */
public class RCRightAttachmentMessageBubble extends RCAttachmentMessageBubble
{
    public RCRightAttachmentMessageBubble()
    {
        NinePatchImageIcon backgroundNormal = new NinePatchImageIcon(this.getClass().getResource("/image/right_white.9.png"));
        NinePatchImageIcon backgroundActive = new NinePatchImageIcon(this.getClass().getResource("/image/right_white_active.9.png"));
        setBackgroundNormalIcon(backgroundNormal);
        setBackgroundActiveIcon(backgroundActive);
        setBackgroundIcon(backgroundNormal);
    }

    @Override
    public Insets getInsets()
    {
        return new Insets(2, 2, 2, 8);
    }
}
