package az.btb.mobilebanking.di.providers;

import javax.inject.Inject;
import javax.inject.Provider;

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RxJavaCallAdapterProvider implements Provider<RxJava2CallAdapterFactory> {

    @Inject
    public RxJavaCallAdapterProvider() {
    }

    @Override
    public RxJava2CallAdapterFactory get() {
        return RxJava2CallAdapterFactory.create();
    }
}