package com.rc.components.message;

import java.awt.*;

/**
 * 右侧文本聊天气泡
 */
public class RCRightTextMessageBubble extends RCTextMessageBubble
{
    public RCRightTextMessageBubble()
    {
        NinePatchImageIcon backgroundNormal = new NinePatchImageIcon(this.getClass().getResource("/image/right.9.png"));
        NinePatchImageIcon backgroundActive = new NinePatchImageIcon(this.getClass().getResource("/image/right_active.9.png"));
        setBackgroundNormalIcon(backgroundNormal);
        setBackgroundActiveIcon(backgroundActive);
        setBackgroundIcon(backgroundNormal);
    }

    @Override
    public Insets getInsets()
    {
        return new Insets(2, 2, 5, 8);
    }
}
