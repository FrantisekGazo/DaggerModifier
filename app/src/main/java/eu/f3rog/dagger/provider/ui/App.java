package eu.f3rog.dagger.provider.ui;

import android.app.Application;

import eu.f3rog.dagger.provider.di.Components;

public final class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Components.initAppComponent(this);
    }
}
