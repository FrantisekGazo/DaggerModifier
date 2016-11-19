package eu.f3rog.dagger.provider.di;

import android.app.Application;
import android.support.annotation.NonNull;

import eu.f3rog.dagger.provider.di.component.AppComponent;
import eu.f3rog.dagger.provider.di.component.DaggerAppComponent;
import eu.f3rog.dagger.provider.di.module.app.AppModule;
import eu.f3rog.dagger.provider.di.module.data.DataModule;

public final class Components {

    private static AppComponent sAppComponent;

    private Components() {
    }

    //@NonNull
    public static AppComponent getAppComponent() {
        if (sAppComponent == null) {
            throw new IllegalStateException("App component was not initialized!");
        }
        return sAppComponent;
    }

    public static void initAppComponent(@NonNull Application app) {
        sAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(app))
                .dataModule(new DataModule())
                .build();
    }
}
