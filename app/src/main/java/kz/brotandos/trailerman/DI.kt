package kz.brotandos.trailerman

import androidx.room.Room
import com.facebook.stetho.okhttp3.StethoInterceptor
import kz.brotandos.trailerman.actors.repository.ActorRepository
import kz.brotandos.trailerman.actors.repository.ActorService
import kz.brotandos.trailerman.actors.actor.ActorDetailsViewModel
import kz.brotandos.trailerman.common.db.AppDatabase
import kz.brotandos.trailerman.common.api.DiscoverService
import kz.brotandos.trailerman.common.api.LiveDataCallAdapterFactory
import kz.brotandos.trailerman.common.api.RequestInterceptor
import kz.brotandos.trailerman.common.repository.DiscoverRepository
import kz.brotandos.trailerman.main.MainActivityViewModel
import kz.brotandos.trailerman.movies.repository.MovieRepository
import kz.brotandos.trailerman.movies.repository.MovieService
import kz.brotandos.trailerman.movies.movie.MovieDetailsViewModel
import kz.brotandos.trailerman.series.repository.SeriesService
import kz.brotandos.trailerman.series.repository.SeriesRepository
import kz.brotandos.trailerman.series.detail.SeriesDetailViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val uiModule = module {
    viewModel { MainActivityViewModel(get(), get()) }
    viewModel { MovieDetailsViewModel(get()) }
    viewModel { ActorDetailsViewModel(get()) }
    viewModel { SeriesDetailViewModel(get()) }
}

val retrofitModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(RequestInterceptor())
            .addNetworkInterceptor(StethoInterceptor())
            .build()
    }
    single {
        Retrofit.Builder()
            .client(get())
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
    }
}

val serviceModule = module {
    single { get<Retrofit>().create(DiscoverService::class.java) }
    single { get<Retrofit>().create(ActorService::class.java) }
    single { get<Retrofit>().create(MovieService::class.java) }
    single { get<Retrofit>().create(SeriesService::class.java) }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "Trailerman.db")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
}

val repositoryModule = module {
    single { get<AppDatabase>().movieDao() }
    single { get<AppDatabase>().seriesDao() }
    single { get<AppDatabase>().actorDao() }
    single { DiscoverRepository(get(), get(), get()) }
    single { MovieRepository(get(), get()) }
    single { ActorRepository(get(), get()) }
    single { SeriesRepository(get(), get()) }
}