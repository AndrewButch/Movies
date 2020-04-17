package com.andrewbutch.movies.di;

import com.andrewbutch.movies.MoviesAPI;
import com.andrewbutch.movies.utils.Constatnts;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
abstract class AppModule {

    @Provides
    static Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Constatnts.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    static MoviesAPI provideMovieApi(Retrofit retrofit) {
        return retrofit.create(MoviesAPI.class);
    }
}
