package com.andrewbutch.movies.di;

import android.app.Application;

import com.andrewbutch.movies.BaseApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules =
        {
                AndroidSupportInjectionModule.class,
                ActivitiesBuilderModule.class,
                AppModule.class,
        })
public interface AppComponent extends AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
