package com.android.moovys.movieList.data.remote

import com.android.moovys.movieList.data.remote.respond.MovieListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * @author Sahil Solanki 02.05.2024
 */
interface MovieApi {

    @GET("movie/{category}")
    suspend fun getMovieList(
        @Path("category") category : String,
        @Query("page") page : Int,
        @Query("apikey") apiKey : String = API_KEY
    ) : MovieListDto

    companion object {
        const val BASE_URL = ""
        const val IMAGE_BASE_URL = ""
        const val API_KEY = ""
    }
}