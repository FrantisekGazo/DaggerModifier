package eu.f3rog.dagger.modifier;

import javax.inject.Provider;

import dagger.Lazy;

public interface InjectionMiddleMan<T> {

    T get(Object o, Provider<T> provider);

    Lazy<T> provider(Object o, Provider<T> provider);

    Provider<T> lazy(Object o, Provider<T> provider);

    Provider<Lazy<T>> providerOfLazy(Object o, Provider<T> provider);
}
