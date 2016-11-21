package eu.f3rog.dagger.modifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// TODO remove ?!
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface UseMiddleMan {

    /**
     * If one or more classes are specified, annotated {@link InjectionMiddleMan} implementation will be used only on their injected field/s.
     * <p/>
     * Empty by default. Is left empty, annotated {@link InjectionMiddleMan} implementation will be used on all possible classes.
     * <p/>
     * Can be used in combination with 'except'.
     */
    Class[] value() default {};

    /**
     * List of classes which will be ignored and annotated {@link InjectionMiddleMan} implementation won't be used on their injected field/s.
     * <p/>
     * Empty by default.
     */
    Class[] except() default {};
}
