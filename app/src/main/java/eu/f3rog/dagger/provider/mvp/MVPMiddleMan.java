package eu.f3rog.dagger.provider.mvp;

import javax.inject.Provider;

import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.ProviderOfLazy;
import eu.f3rog.dagger.modifier.InjectionMiddleMan;
import eu.f3rog.dagger.modifier.UseMiddleMan;
import eu.f3rog.dagger.provider.ui.MainActivity;

@UseMiddleMan(
        value = {
                MainActivity.class
        },
        except = {
                MainActivity.class
        }
)
public final class MVPMiddleMan
        implements InjectionMiddleMan<String> {


    private static final MVPMiddleMan INSTANCE = new MVPMiddleMan();

    public static MVPMiddleMan getInstance() {
        return INSTANCE;
    }

    @Override
    public String get(Object o, Provider<String> provider) {
        return provider.get();
    }

    @Override
    public Lazy<String> provider(Object o, Provider<String> provider) {
        return DoubleCheck.lazy(provider);
    }

    @Override
    public Provider<String> lazy(Object o, Provider<String> provider) {
        return provider;
    }

    @Override
    public Provider<Lazy<String>> providerOfLazy(Object o, Provider<String> provider) {
        return ProviderOfLazy.create(provider);
    }
}
