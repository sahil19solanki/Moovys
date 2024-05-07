package com.android.moovys.movieList.data.local.movie

import androidx.room.Dao
import androidx.room.Upsert
import retrofit2.http.Query

//@Dao
//interface MovieDao {
//    @Upsert
//    suspend fun upsertMoviesList(movieList : List<MovieEntity>)
//
//    @Query("SELECT * FROM MovieEntity WHERE id = :id")
//    suspend fun getMovieById(id : Int) : MovieEntity
//
//    @Query("SELECT * FROM MovieEntity WHERE category = :category")
//    suspend fun getMovieByCategory(category : String) : List<MovieEntity>
//}

@Dao
interface MovieDao {
    @Upsert
    suspend fun upsertMovieList(movieList: List<MovieEntity>)

    @Query("SELECT * FROM MovieEntity WHERE id = :id")
    suspend fun getMovieById(id: Int): MovieEntity

    @Query("SELECT * FROM MovieEntity WHERE category = :category")
    suspend fun getMovieListByCategory(category: String): List<MovieEntity>
}