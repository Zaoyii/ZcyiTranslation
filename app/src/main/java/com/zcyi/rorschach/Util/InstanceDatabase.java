package com.zcyi.rorschach.Util;

import android.content.Context;

import androidx.room.Room;

public class InstanceDatabase {
    public static BaseRoomDatabase baseRoomDatabase;

    public static BaseRoomDatabase getInstance(Context context) {
        if (baseRoomDatabase == null && context != null) {
            baseRoomDatabase = Room.databaseBuilder(context, BaseRoomDatabase.class, "zcyi_database.db").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }
        return baseRoomDatabase;
    }
}
