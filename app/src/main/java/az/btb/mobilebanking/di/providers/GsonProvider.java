package az.btb.mobilebanking.di.providers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Inject;
import javax.inject.Provider;

public class GsonProvider implements Provider<Gson> {
    @Inject
    public GsonProvider() {
    }

    @Override
    public Gson get() {
        return new GsonBuilder().setLenient().create();
    }
}
