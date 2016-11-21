package eu.f3rog.dagger.modifier.plugin

import eu.f3rog.dagger.modifier.DMWeaver
import eu.f3rog.dagger.modifier.weave.IWeaver

/**
 * Transforms compiled classes
 *
 * @author FrantisekGazo
 */
public final class DMTransformer
        extends BaseTransformer {

    public DMTransformer(boolean debug) {
        super(debug)
    }

    @Override
    String getName() {
        return "DaggerModifier"
    }

    @Override
    IWeaver getWeaver(boolean debug) {
        return new DMWeaver(debug)
    }
}