package com.firatech.app; 

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.RemoteViews;
import androidx.core.app.NotificationCompat;
import com.onesignal.notifications.INotificationReceivedEvent;
import com.onesignal.notifications.INotificationServiceExtension;
import java.io.InputStream;
import java.net.URL;

public class CustomNotificationService implements INotificationServiceExtension {
    @Override
    public void onNotificationReceived(INotificationReceivedEvent event) {
        Context context = event.getContext();
        
        // Extract custom image URL sent from GitHub Actions
        String customImgUrl = null;
        if (event.getNotification().getAdditionalData() != null) {
            customImgUrl = event.getNotification().getAdditionalData().optString("custom_heads_up_image", null);
        }

        if (customImgUrl != null) {
            try {
                // Download the image
                InputStream in = new URL(customImgUrl).openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(in);

                // Build custom RemoteViews layout
                RemoteViews customView = new RemoteViews(context.getPackageName(), R.layout.custom_heads_up_notification);
                customView.setImageViewBitmap(R.id.img_main, bitmap);

                // Attach custom layout directly to the Heads-Up notification
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, event.getNotification().getAndroidNotificationChannelId());
                builder.setCustomHeadsUpContentView(customView);
                builder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
