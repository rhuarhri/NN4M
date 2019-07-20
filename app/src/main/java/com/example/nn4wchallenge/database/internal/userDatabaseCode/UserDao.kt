package com.example.nn4wchallenge.database.internal.userDatabaseCode
import androidx.room.*

@Dao
interface UserDao {

    @Query("SELECT * FROM User")
    fun getAll(): Array<User>

    @Insert
    fun insert(vararg newUser: User)

    @Update
    fun update(vararg newUser: User)

    @Delete
    fun delete(oldUser: User)

}