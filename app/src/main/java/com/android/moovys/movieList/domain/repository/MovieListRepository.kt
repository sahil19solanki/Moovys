package com.android.moovys.movieList.domain.repository

import com.android.moovys.movieList.domain.model.Movie
import com.android.moovys.movieList.util.Resource
import kotlinx.coroutines.flow.Flow

interface MovieListRepository {
    suspend fun getMovieList(
        forceFetchRemote: Boolean,
        category: String,
        page: Int,
    ): Flow<Resource<List<Movie>>>

    suspend fun getMovie(id:Int):Flow<Resource<Movie>>
}