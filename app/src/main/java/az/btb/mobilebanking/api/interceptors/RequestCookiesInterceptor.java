package az.btb.mobilebanking.api.interceptors;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.io.IOException;

import az.btb.mobilebanking.utils.Constants;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestCookiesInterceptor implements Interceptor {

    private final SharedPreferences preferences;

    public RequestCookiesInterceptor(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        String savedCookie = preferences.getString(Constants.COOKIE_KEY,"");
        if(!savedCookie.isEmpty())
            builder.addHeader(Constants.COOKIE_KEY, savedCookie);

        return chain.proceed(builder.build());
    }
}