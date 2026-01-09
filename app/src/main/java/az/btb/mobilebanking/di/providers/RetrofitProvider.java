package az.btb.mobilebanking.di.providers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Inject;
import javax.inject.Provider;

import az.btb.mobilebanking.utils.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitProvider implements Provider<Retrofit> {

    private final RxJava2CallAdapterFactory callAdapterFactory;
    private final OkHttpClient okHttpClient;
    private final Gson gson;

    @Inject
    public RetrofitProvider(RxJava2CallAdapterFactory callAdapterFactory, OkHttpClient okHttpClient, Gson gson) {
        this.callAdapterFactory = callAdapterFactory;
        this.okHttpClient = okHttpClient;
        this.gson = gson;
    }

    @Override
    public Retrofit get() {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(callAdapterFactory)// First: Handles plain text
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

}