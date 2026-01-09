package az.btb.mobilebanking.api.interceptors;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public abstract class InternetConnectionInterceptor implements Interceptor {

	public abstract boolean isInternetAvailable();

	public abstract void onInternetUnavailable();

	@NonNull
	@Override
	public Response intercept(@NonNull Chain chain) throws IOException {
		Request request = chain.request();
		if (!isInternetAvailable()) {
			onInternetUnavailable();
			throw new IOException("");
		}

		return chain.proceed(request);
	}
}
