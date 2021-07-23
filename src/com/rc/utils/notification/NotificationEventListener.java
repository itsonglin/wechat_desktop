package com.rc.utils.notification;

import com.rc.panels.NotificationPopup;

/**
 * @author song
 * @date 19-9-29 09:27
 * @description
 * @since
 */
public interface NotificationEventListener
{
    void onShown(NotificationPopup src);

    void onClosed(NotificationPopup src);
}
