package az.btb.mobilebanking.di.modules;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import az.btb.mobilebanking.di.providers.GsonProvider;
import az.btb.mobilebanking.di.providers.OkHttpProvider;
import az.btb.mobilebanking.di.providers.RetrofitProvider;
import az.btb.mobilebanking.di.providers.RxJavaCallAdapterProvider;
import kotlin.jvm.functions.Function0;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import toothpick.config.Module;

public class ServerModule extends Module {

    public ServerModule(@NonNull Function0<Void> internetConnectivityMsgDeliverer) {

        // OkHttpProvider class-inin icindeki InternetConnectionInterceptor icerisinde
        // istifade edilir. OkHttpProvider class-inda context obyektini inject etmek
        // duzgun olmazdi. Sebeb? Cunki context UI-ye bagli bir mevhumdur, bu sebebden
        // onu "alt tebeqede" istifade etmek evezine bele bir usuldan istifade etdim.
        bind(Function0.class).toInstance(internetConnectivityMsgDeliverer);

        //Retrofit dependencies
        bind(RxJava2CallAdapterFactory.class).toProvider(RxJavaCallAdapterProvider.class).providesSingleton();
        bind(Gson.class).toProvider(GsonProvider.class).providesSingleton();
        bind(OkHttpClient.class).toProvider(OkHttpProvider.class).providesSingleton();

        //Retrofit itself
        bind(Retrofit.class).toProvider(RetrofitProvider.class).providesSingleton();
    }
}
