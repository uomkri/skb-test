package ru.uomkri.skbtest.di.module

import android.app.Application
import androidx.room.Room
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.uomkri.skbtest.repo.Repository
import ru.uomkri.skbtest.repo.db.RepoDao
import ru.uomkri.skbtest.repo.db.RepoDatabase
import ru.uomkri.skbtest.repo.net.RepoRemoteDataSourceFactory
import ru.uomkri.skbtest.repo.net.ReposResourceService
import ru.uomkri.skbtest.utils.Config
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(moshi: Moshi, httpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .baseUrl(Config.BASE_URL)
        .client(httpClient)
        .build()

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    fun provideLogging() = HttpLoggingInterceptor()

    @Provides
    fun provideHttpClient(logging: HttpLoggingInterceptor) =
        OkHttpClient.Builder().addInterceptor(logging.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()

    @Provides
    fun provideReposService(retrofit: Retrofit): ReposResourceService =
        retrofit.create(ReposResourceService::class.java)

    @Provides
    @Singleton
    fun provideRepoDB(application: Application) =
        Room.databaseBuilder(application, RepoDatabase::class.java, "favRepos")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideRepoDao(db: RepoDatabase) = db.repoDao()

    @Provides
    @Singleton
    fun provideRepository(
        remoteDataSource: ReposResourceService,
        localDataSource: RepoDao
    ) = Repository(
        remoteDataSource = remoteDataSource,
        localDataSource = localDataSource
    )

    @Provides
    fun provideDataSourceFactory(query: String, repo: Repository) =
        RepoRemoteDataSourceFactory(repo, query)

    @Provides
    fun provideDefaultQuery(): String = ""

    @Provides
    @Singleton
    fun provideOAuthProvider() = OAuthProvider.newBuilder("github.com")

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideGoogleAuth() = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
}
