package com.zcyi.rorschach.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.zcyi.rorschach.MyEntity.User;

@Dao
public interface UserDao {
    @Insert
    void addUser(User users);

    @Query("select * from t_user where user_name = :userName and user_password = :userPassword")
    User login(String userName, String userPassword);

    @Query("select * from t_user where user_name = :userName ")
    User checkUsername(String userName);

}