package com.android.moovys.movieList.data.repository

import coil.network.HttpException
import com.android.moovys.movieList.data.local.movie.MovieDatabase
import com.android.moovys.movieList.data.local.movie.MovieEntity
import com.android.moovys.movieList.data.mappers.toMovie
import com.android.moovys.movieList.data.mappers.toMovieEntity
import com.android.moovys.movieList.data.remote.MovieApi
import com.android.moovys.movieList.domain.model.Movie
import com.android.moovys.movieList.domain.repository.MovieListRepository
import com.android.moovys.movieList.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

class MovieListRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase,
) : MovieListRepository {
    override suspend fun getMovieList(
        forceFetchRemote: Boolean,
        category: String,
        page: Int,
    ): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))

            val localMovieList = movieDatabase.movieDao.getMovieListByCategory(category)

            val shouldLoadFromLocal = localMovieList.isNotEmpty() && !forceFetchRemote

            if (shouldLoadFromLocal) {
                emit(
                    Resource.Success(
                        data = localMovieList.map { movieEntity ->
                            movieEntity.toMovie(category)
                        }
                    )
                )
                return@flow
            }

            val getMovieListFromApi = try {
                movieApi.getMovieList(category, page)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Movie load failed"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Movie load failed"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error("Movie load failed"))
                return@flow
            }
            val movieEntities = getMovieListFromApi.results.let {
                it.map { movieDto ->
                    movieDto.toMovieEntity(category)
                }
            }

            movieDatabase.movieDao.upsertMovieList(movieEntities)

            emit(Resource.Success(movieEntities.map { movieEntity -> movieEntity.toMovie(category) }))

            emit(Resource.Loading(false))
        }
    }

    override suspend fun getMovie(id: Int): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading(true))

            val movieEntity = movieDatabase.movieDao.getMovieById(id)

            if (movieEntity != null){
                emit(Resource.Success(movieEntity.toMovie(movieEntity.category)))
                emit(Resource.Loading(false))
                return@flow
            }

            emit(Resource.Error("No such item"))
            emit(Resource.Loading(false))
        }
    }
}