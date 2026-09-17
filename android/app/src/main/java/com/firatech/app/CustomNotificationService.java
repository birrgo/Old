package com.firatech.app; 

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.RemoteViews;
import androidx.core.app.NotificationCompat;

import com.onesignal.notifications.INotificationReceivedEvent;
import com.onesignal.notifications.INotificationServiceExtension;
import com.onesignal.notifications.IMutableNotification;

import java.io.InputStream;
import java.net.URL;

public class CustomNotificationService implements INotificationServiceExtension {
    @Override
    public void onNotificationReceived(INotificationReceivedEvent event) {
        Context context = event.getContext();
        IMutableNotification mutableNotification = event.getNotification();

        String customImgUrl = null;
        if (mutableNotification.getAdditionalData() != null) {
            customImgUrl = mutableNotification.getAdditionalData().optString("custom_heads_up_image", null);
        }

        if (customImgUrl != null) {
            try {
                InputStream in = new URL(customImgUrl).openStream();
                final Bitmap bitmap = BitmapFactory.decodeStream(in);

                // Use OneSignal's v5 built-in extender
                mutableNotification.setExtender(builder -> {
                    RemoteViews customView = new RemoteViews(context.getPackageName(), R.layout.custom_heads_up_notification);
                    customView.setImageViewBitmap(R.id.img_main, bitmap);
                    builder.setCustomHeadsUpContentView(customView);
                    builder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());
                    return builder;
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
