package com.andrewbutch.movies.di;

import android.app.Application;

import com.andrewbutch.movies.data.MovieLoader;
import com.andrewbutch.movies.data.MoviesAPI;
import com.andrewbutch.movies.data.MoviesRepository;
import com.andrewbutch.movies.domain.Repository;
import com.andrewbutch.movies.utils.Constatnts;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
abstract class AppModule {
    @Singleton
    @Provides
    static Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Constatnts.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    static MoviesAPI provideMovieApi(Retrofit retrofit) {
        return retrofit.create(MoviesAPI.class);
    }

    @Singleton
    @Provides
    static Repository provideRepository(MovieLoader loader, Application application) {
        return new MoviesRepository(loader, application);
    }


}
