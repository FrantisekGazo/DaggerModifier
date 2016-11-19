package eu.f3rog.dagger.provider.di.component;

import dagger.Component;
import eu.f3rog.dagger.provider.di.module.data.DataModule;
import eu.f3rog.dagger.provider.ui.MainActivity;
import eu.f3rog.dagger.provider.di.AppScope;
import eu.f3rog.dagger.provider.di.module.app.AppModule;

@AppScope
@Component(modules = {
        AppModule.class,
        DataModule.class,
})
public interface AppComponent {

    void inject(MainActivity activity);
}
