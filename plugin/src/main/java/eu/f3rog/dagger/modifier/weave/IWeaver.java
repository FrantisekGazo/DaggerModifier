package eu.f3rog.dagger.modifier.weave;

import java.util.Set;

import javassist.ClassPool;
import javassist.CtClass;

/**
 * Interface {@link IWeaver} used for bytecode weaving.
 *
 * @author FrantisekGazo
 */
public interface IWeaver {

    void before(ClassPool classPool, Set<String> classNames);

    void weave(CtClass cls);
}

