package com.example.taskmanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

/**
 * BroadcastReceiver для отправки уведомлений.
 */
public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Получение менеджера уведомлений
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Создание канала уведомлений для Android версии 8 (Oreo) и выше
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "Default Channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Создание уведомления
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default")
                .setSmallIcon(R.drawable.ic_notification) // Установка маленькой иконки
                .setContentTitle("Задача") // Установка заголовка уведомления
                .setContentText("Время выполнить задачу!") // Установка текста уведомления
                .setPriority(NotificationCompat.PRIORITY_DEFAULT); // Установка приоритета уведомления

        // Отправка уведомления
        notificationManager.notify(0, builder.build());
    }
}
