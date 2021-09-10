package com.example.serverapp.model.dao

import androidx.room.*
import com.example.connectorlibrary.enitity.User

@Dao
interface IUserDao {

    @Query("SELECT * FROM user WHERE user_id= :userId")
    fun getUser(userId: Int): User

    @Query("SELECT * FROM user")
    fun getListUser(): List<User>

    @Insert
    fun userSignUp(user: User): Long

    @Query("SELECT EXISTS(SELECT * FROM user WHERE phone_number= :phone_number)")
    fun userSignIn(phone_number: String): Boolean

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUser(user: User): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun lockUser(user: User): Int

    @Delete
    fun deleteUser(user: User): Int
}