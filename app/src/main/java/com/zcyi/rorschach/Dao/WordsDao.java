package com.zcyi.rorschach.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.zcyi.rorschach.MyEntity.Words;

import java.util.List;

@Dao
public interface WordsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWords(Words words);

    @Query("select * from t_words where words_query = :query and user_id = :userId")
    Words selectWordByQueryUserId(String query, int userId);

    @Query("select * from t_words where user_id = :userId")
    List<Words> selectAllWordByUserId(int userId);

    @Delete
    int DeleteWords(Words words);

}
