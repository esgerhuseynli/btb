package az.btb.mobilebanking.di.providers;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Provider;

import az.btb.mobilebanking.api.interceptors.RequestCookiesInterceptor;
import az.btb.mobilebanking.api.interceptors.ResponseCookiesInterceptor;
import az.btb.mobilebanking.api.interceptors.InternetConnectionInterceptor;
import az.btb.mobilebanking.utils.Utils;
import kotlin.jvm.functions.Function0;
import android.util.Log;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpProvider  implements Provider<OkHttpClient> {

    private final SharedPreferences preferences;
    private final Function0<Void> mInternetConnectivityMsgDeliverer;

    @Inject
    public OkHttpProvider(SharedPreferences preferences, @NonNull Function0<Void> internetConnectivityMsgDeliverer) {
        this.preferences = preferences;
        mInternetConnectivityMsgDeliverer = internetConnectivityMsgDeliverer;
    }

    @Override
    public OkHttpClient get() {
        // Test log to verify this method is called
        Log.d("OkHttpProvider", "Creating OkHttpClient with logging interceptor");

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                // Use Log.d for DEBUG level - make sure logcat filter includes DEBUG level
                // Filter in logcat: tag:OkHttp or tag:HTTP_REQUEST
                Log.d("OkHttp", message);
                // Also log with a more visible tag
                Log.d("HTTP_REQUEST", message);
            }
        });
        // Set to BODY level to log request/response headers and body
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
//            .certificatePinner(
                // https://owasp.org/www-community/controls/Certificate_and_Public_Key_Pinning#
                // https://mailapurvpandey.medium.com/ssl-pinning-in-android-90dddfa3e051
                // https://blog.approov.io/securing-https-with-certificate-pinning-on-android
                // https://www.netguru.com/codestories/3-ways-how-to-implement-certificate-pinning-on-android
//                new CertificatePinner.Builder()
//                    .add("mobilebanking.btb.az", "sha256/bWNMLlnfhAh6bKgltaWiyuHdN3d7fBzZhdStzAe5qcY=")
//                    .build()
//            )
            // Add logging interceptor first to capture all requests and responses
            .addInterceptor(loggingInterceptor)
            .addInterceptor(new InternetConnectionInterceptor() {
                @Override
                public boolean isInternetAvailable() {
                    return Utils.isOnline();
                }

                @Override
                public void onInternetUnavailable() {
                    mInternetConnectivityMsgDeliverer.invoke();
                }
            })
            .addInterceptor(new ResponseCookiesInterceptor(preferences))
            .addInterceptor(new RequestCookiesInterceptor(preferences))
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();
    }
}
