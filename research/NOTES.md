Dagger2 generated classes
-------------------------

For each class `T` that contains `@Inject` Dagger2 generates `T_MembersInjector`.
 
e.g. for class

```
class T {
    @Inject
    F field;
}
```

Dagger2 would generate
 
```
import dagger.MembersInjector;
import dagger.internal.DoubleCheck;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class T_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<F> fieldProvider;

  public T_MembersInjector(Provider<F> fieldProvider) {
    assert fieldProvider != null;
    this.fieldProvider = fieldProvider;
  }

  public static MembersInjector<T> create(Provider<F> fieldProvider) {
    return new T_MembersInjector(fieldProvider);
  }

  @Override
  public void injectMembers(T instance) {
    if (instance == null) {
      throw new NullPointerException("Cannot inject members into a null reference");
    }
    instance.field = fieldProvider.get();
  }

  public static void injectField(T instance, Provider<F> fieldProvider) {
    instance.field = fieldProvider.get();
  }
}
```

User of Dagger2 can inject a field in more ways and each time only body of `inject...()` methods change.

 - `@Inject T field;` => `instance.field = fieldProducer.get();`
 
 - `@Inject Lazy<T> field;` => `instance.field = DoubleCheck.lazy(fieldProvider);`
 
 - `@Inject Provider<T> field;` => `instance.field = fieldProvider;`
 
 - `@Inject Provider<Lazy<T>> field;` => `instance.field = ProviderOfLazy.create(fieldProvider);`
 

Injection Middleware
--------------------

```
interface InjectionMiddleware<T> {
    T get(Object instance, Provider<T> provider);
    Lazy<T> lazy(Object instance, Provider<T> provider);
    Provider<T> provider(Object instance, Provider<T> provider);
    Provider<Lazy<T>> providerOfLazy(Object instance, Provider<T> provider);
}
```

Implementation of this class has to also provide method for retrieving instance of it: `InjectionMiddleware<T> getInstance() {}`

This library can then wrap all desired calls by custom implementation of this interface.

e.g. if user defines `FieldInjectionMiddleware`, then code in `T_MembersInjector` would change to this:

 - `@Inject T field;` => `instance.field = FieldInjectionMiddleware.getInstance().get(instance, fieldProducer);`
 
 - `@Inject Lazy<T> field;` => `instance.field = FieldInjectionMiddleware.getInstance().lazy(instance, fieldProducer);`
 
 - `@Inject Provider<T> field;` => `instance.field = FieldInjectionMiddleware.getInstance().provider(instance, fieldProducer);`
 
 - `@Inject Provider<Lazy<T>> field;` => `instance.field = FieldInjectionMiddleware.getInstance().providerOfLazy(instance, fieldProducer);`
 

Future features
---------------
 - `Producer`s support