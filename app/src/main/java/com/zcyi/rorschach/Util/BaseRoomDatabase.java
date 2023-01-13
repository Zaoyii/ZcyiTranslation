package com.zcyi.rorschach.Util;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.zcyi.rorschach.Dao.UserDao;
import com.zcyi.rorschach.Dao.WordsDao;
import com.zcyi.rorschach.MyEntity.User;
import com.zcyi.rorschach.MyEntity.Words;

@Database(entities = {User.class, Words.class}, version = 1, exportSchema = false)
public abstract class BaseRoomDatabase extends RoomDatabase {
    public abstract UserDao getUserDao();
    public abstract WordsDao getWordsDao();
}
