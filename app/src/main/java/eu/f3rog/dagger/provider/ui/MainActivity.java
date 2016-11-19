package eu.f3rog.dagger.provider.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import javax.inject.Inject;
import javax.inject.Named;

import eu.f3rog.dagger.modifier.ModifiedBy;
import eu.f3rog.dagger.provider.R;
import eu.f3rog.dagger.provider.di.Components;
import eu.f3rog.dagger.provider.mvp.PresenterManger;

public final class MainActivity
        extends AppCompatActivity {

    @Inject
    @ModifiedBy(PresenterManger.class)
    @Named("Scoped")
    String mScopedNumber;
    @Inject
    String mNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Components.getAppComponent().inject(this);

        TextView textView = (TextView) findViewById(R.id.txt);
        textView.setText(mScopedNumber + "\n\n" + mNumber);
    }
}
