package eu.f3rog.dagger.modifier.weave;

import eu.f3rog.javassist.JavassistHelper;

/**
 * Class {@link BaseWeaver} is used for bytecode weaving.
 *
 * @author FrantisekGazo
 */
public abstract class BaseWeaver
        implements IWeaver {

    private boolean mDebug;
    private JavassistHelper mJavassistHelper;

    /**
     * Constructor
     *
     * @param debug If <code>true</code>, than logs will be shown.
     */
    public BaseWeaver(boolean debug) {
        this.mDebug = debug;
        this.mJavassistHelper = new JavassistHelper();
    }

    protected boolean isDebug() {
        return mDebug;
    }

    protected JavassistHelper getJavassistHelper() {
        return mJavassistHelper;
    }

    protected void log(String msg, Object... args) {
        internalLog(msg, false, args);
    }

    protected void lognl(String msg, Object... args) {
        internalLog(msg, true, args);
    }

    private void internalLog(String msg, boolean newLine, Object... args) {
        if (mDebug) {
            System.out.printf(msg + (newLine ? "\n" : ""), args);
        }
    }

}
