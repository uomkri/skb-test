package ru.uomkri.skbtest.repo.net

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.rxjava3.core.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import ru.uomkri.skbtest.utils.Config.BASE_URL

interface ReposResourceService {
    @GET("repositories")
    fun getAllRepos(
        @Query("since") since: Int
    ): Observable<List<Repo>>

    @GET("search/repositories")
    fun getSearchResult(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") limit: Int = 20
    ): Observable<SearchResponse>

    @GET("repos/{owner}/{repo}")
    fun getRepo(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Observable<Repo>
}
