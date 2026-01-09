package az.btb.mobilebanking.api.interceptors;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

import static az.btb.mobilebanking.utils.Constants.COOKIE_KEY;

public class ResponseCookiesInterceptor implements Interceptor {

    private final SharedPreferences preferences;

    public ResponseCookiesInterceptor(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            for (String header : originalResponse.headers("Set-Cookie")) {
                if (header.trim().contains(";")){
                    String[] cookiesStr = header.trim().split(";");

                    if (cookiesStr[0].contains(".AspNetCore.Session")) {
                        preferences.edit().putString(COOKIE_KEY, cookiesStr[0]).apply();
                    }
                }
            }
        }

        return originalResponse;
    }
}
