package com.rc.components.message;

import java.awt.*;

/**
 * 左侧文本聊天气泡
 */
public class RCLeftTextMessageBubble extends RCTextMessageBubble
{
    public RCLeftTextMessageBubble()
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
        return new Insets(10, 15, 10, 10);
    }
}
