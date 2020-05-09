package com.devraj.action.searchAction;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;

public class NotifierUtil {
    private static final NotificationGroup NG = new NotificationGroup("com.devraj.action.notificationG", NotificationDisplayType.STICKY_BALLOON, true);

    public static void notifyUser(Project project, String title, String subtitle, String message) {
        Notification notification = NG.createNotification(title, subtitle,
               message, NotificationType.INFORMATION);
        notification.notify(project);
    }
}
