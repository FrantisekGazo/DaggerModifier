package eu.f3rog.dagger.provider.di.module.data;

import java.util.Random;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import eu.f3rog.dagger.provider.di.AppScope;

@Module
public class DataModule {

    @Provides
    @Named("Prefix")
    public String provideText() {
        return "Number: ";
    }

    @Provides
    @AppScope
    @Named("Scoped")
    public String provideScopedNumber(@Named("Prefix") String prefix) {
        return "Scoped " + prefix + Math.round(new Random().nextDouble() * 1000);
    }

    @Provides
    public String provideNumber(@Named("Prefix") String prefix) {
        return prefix + Math.round(new Random().nextDouble() * 1000);
    }
}
