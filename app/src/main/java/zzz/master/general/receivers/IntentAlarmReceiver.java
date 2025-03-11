package zzz.master.general.receivers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import zzz.master.general.MainActivity;
import zzz.master.general.R;
import zzz.master.general.utils.ShPreferencesUtils;

public class IntentAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        // Actualizar SharedPreferences
        ShPreferencesUtils shPreferencesUtils = new ShPreferencesUtils(context);
        shPreferencesUtils.setString("MF_t1_state", "waited");

        // Mostrar notificación
        sendNotification(context);
    }

    private void sendNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Validad Oreo o superior para configurar el canal de notificación
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("general_app_notification", "Notificaciones funcionales", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent( context, MainActivity.class);
        intent.putExtra("open_fragment", "MemoryFragment");
        intent.putExtra("tab_position", 0);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Construir y enviar la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "general_app_notification")
                .setSmallIcon(R.drawable.nargaroth_icon)
                .setContentTitle("È ora")
                .setContentText("Dimostra che ancora ricordi il numero.")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        notificationManager.notify(1, builder.build());
    }

}
