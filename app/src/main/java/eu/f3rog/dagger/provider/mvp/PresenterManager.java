package eu.f3rog.dagger.provider.mvp;


import javax.inject.Provider;

import eu.f3rog.dagger.provider.ui.MainActivity;

public class PresenterManager {

    public static String wrap(MainActivity i, Provider<String> p) {
        return "Changed text: " + i.toString();
    }

}
