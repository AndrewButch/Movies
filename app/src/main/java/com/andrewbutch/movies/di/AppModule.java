package com.andrewbutch.movies.di;

import com.andrewbutch.movies.utils.Constatnts;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public abstract class AppModule {

    @Provides
    static Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Constatnts.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
