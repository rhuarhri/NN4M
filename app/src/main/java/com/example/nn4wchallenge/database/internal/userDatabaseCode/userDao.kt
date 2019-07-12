package com.example.nn4wchallenge.database.internal.userDatabaseCode
import androidx.room.*

@Dao
interface userDao {

    @Query("SELECT * FROM user")
    fun getAll(): Array<user>

    @Insert
    fun insert(vararg newUser: user)

    @Update
    fun update(vararg newUser: user)

    @Delete
    fun delete(oldUser: user)

}