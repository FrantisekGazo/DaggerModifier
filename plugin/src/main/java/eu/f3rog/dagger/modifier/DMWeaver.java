package eu.f3rog.dagger.modifier;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import eu.f3rog.dagger.modifier.weave.BaseWeaver;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;

/**
 * Changes classes using bytecode weaving
 *
 * @author FrantisekGazo
 */
public final class DMWeaver
        extends BaseWeaver {

    public DMWeaver(boolean debug) {
        super(debug);
    }

    @Override
    public void before(ClassPool classPool, Set<String> classNames) {
    }

    @Override
    public void weave(CtClass cls) {
        if (cls.getSimpleName().endsWith("_MembersInjector")) {
            final ClassPool classPool = cls.getClassPool();

            try {
                final CtClass injectedClass = classPool.get(cls.getName().replace("_MembersInjector", ""));

                lognl(" ~ %s", cls.getName());

                MethodAnalyzer analyzer = new MethodAnalyzer();
                getJavassistHelper().editMethod(analyzer, cls, "injectMembers", injectedClass);

                StringBuilder body = new StringBuilder();
                body.append("{");

                body.append("if (instance == null) { throw new NullPointerException(\"Cannot inject members into a null reference\"); }");

                List<String> fields = analyzer.getFields();
                for (int i = 1; i < fields.size(); i += 2) {
                    String providerField = fields.get(i-1);
                    String injectedField = fields.get(i);
                    ModifyConfig modifyConfig = null;//getMofify(injectedClass, injectedField);
                    if (modifyConfig != null) {
                        body.append("instance.").append(injectedField)
                                .append(" = ")
                                .append(modifyConfig.getModifyingClass().getName())
                                .append(".wrap(")
                                .append("instance")
                                .append(", ")
                                .append(providerField)
                                .append(");");
                    } else {
                        body.append("instance.").append(injectedField)
                                .append(" = ")
                                .append("(String)this.").append(providerField).append(".get();");
                    }
                }

                body.append("return;");

                body.append("}");

                getJavassistHelper().insertBeforeBody(body.toString(), cls, "injectMembers", injectedClass);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    private final static class ModifyConfig {

        private final CtClass mModifyingClass;

        private ModifyConfig(CtClass modifyingClass) {
            mModifyingClass = modifyingClass;
        }

        public CtClass getModifyingClass() {
            return mModifyingClass;
        }
    }

    private ModifyConfig getMofify(CtClass injectedClass, String fieldName)
            throws NotFoundException, ClassNotFoundException, NoSuchMethodException {
        CtField field = injectedClass.getField(fieldName);

        lognl(" > %s", field.getName());

        if (field.hasAnnotation(UseMiddleMan.class)) {
            Annotation annotation = (Annotation) field.getAnnotation(UseMiddleMan.class);

            CtClass valueClass = invokeAnnotationMethodReturningClass(injectedClass.getClassPool(), annotation, "value");
            lognl("   value = %s", valueClass.getName());

            return new ModifyConfig(valueClass);
        }

        return null;
    }

    private CtClass invokeAnnotationMethodReturningClass(ClassPool classPool, Annotation annotation, String methodName)
            throws NoSuchMethodException, NotFoundException {
        Class type = annotation.annotationType();
        Method valueMethod = type.getMethod(methodName, new Class[0]);

        try {
            Class annotationValue = (Class) valueMethod.invoke(annotation);
            return classPool.get(annotationValue.getCanonicalName()); // this won't be executed :(
        } catch (Exception e) {
            // java.lang.ClassNotFoundException inside java.lang.reflect.UndeclaredThrowableException inside java.lang.reflect.InvocationTargetException :) should be thrown
            String className = getClassNotFoundExceptionMessage(e);

            return classPool.get(className);
        }
    }

    private String getClassNotFoundExceptionMessage(final Throwable throwable) {
        Throwable t = throwable;
        while (t != null) {
            if (t instanceof ClassNotFoundException) {
                ClassNotFoundException clsNotFound = (ClassNotFoundException) t;
                return clsNotFound.getMessage();
            }

            t = t.getCause();
        }
        return null;
    }

    private static final class MethodAnalyzer extends ExprEditor {

        private List<String> mFields = new ArrayList<>();

        @Override
        public void edit(FieldAccess f) throws CannotCompileException {
            super.edit(f);

            String fieldName = f.getFieldName();
            System.out.printf("field access '%s' ...\n", fieldName);

            mFields.add(fieldName);
        }

        public List<String> getFields() {
            return mFields;
        }
    }

    private static final class E extends ExprEditor {

        private String mLastProviderName;


        @Override
        public void edit(FieldAccess f) throws CannotCompileException {
            super.edit(f);

            String fieldName = f.getFieldName();

            System.out.printf("field access '%s' ...\n", fieldName);


            mLastProviderName = fieldName;
        }

        @Override
        public void edit(MethodCall m) throws CannotCompileException {
            super.edit(m);
            System.out.printf("method call '%s' ...\n", m.getMethodName());

            if (m.getMethodName().equals("get")) {
                System.out.printf("replacing %s ...\n", m.getMethodName());

                String statement = String.format("{ $_ = %s.%s(%s, %s); }",
                        "eu.f3rog.dagger.provider.mvp.PresenterManager", "wrap", "instance", mLastProviderName);
                m.replace(statement);
            }
        }
    }
}
