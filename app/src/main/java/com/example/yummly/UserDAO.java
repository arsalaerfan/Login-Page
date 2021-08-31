package com.example.yummly;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert
    void registerUser(User user);
    //login method
    @Query("SELECT * FROM users WHERE userId=(:userId) and password=(:password)")
    User login(String userId, String password);

    @Query("SELECT * FROM users")
    public List<User> getAllUsers();

    @Insert
    void insert(User...users);

}
