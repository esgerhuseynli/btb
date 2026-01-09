package az.btb.mobilebanking;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.yariksoffice.lingver.Lingver;

import java.util.List;

import az.btb.mobilebanking.di.modules.AppModule;
import az.btb.mobilebanking.di.modules.LocalNavigationModule;
import az.btb.mobilebanking.di.modules.RetrofitServiceModule;
import az.btb.mobilebanking.di.modules.ServerModule;
import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import az.btb.mobilebanking.utils.Utils;
import retrofit2.Retrofit;
import toothpick.Scope;
import toothpick.Toothpick;

import static az.btb.mobilebanking.di.Scopes.APP_SCOPE;
import static az.btb.mobilebanking.di.Scopes.LOCAL_NAV_MODULE;
import static az.btb.mobilebanking.di.Scopes.SERVER_SCOPE;
import static az.btb.mobilebanking.utils.Constants.APP_LANGUAGE;

public class App extends Application {

    public List<BankCard> BANK_CARDS = null;
    public List<BankAccount> BANK_ACCOUNTS = null;

    @Override
    public void onCreate() {
        super.onCreate();

        Scope appScope = Toothpick.openScope(APP_SCOPE);
        appScope.installModules(new AppModule(this));

        Scope localNavigationScope = Toothpick.openScopes(APP_SCOPE, LOCAL_NAV_MODULE);
        localNavigationScope.installModules(new LocalNavigationModule());

        final Scope serverScope = Toothpick.openScopes(APP_SCOPE, SERVER_SCOPE);
        serverScope.installModules(new ServerModule(this::deliverInternetNotAvailableMsg));

        Retrofit retrofit = serverScope.getInstance(Retrofit.class);
        serverScope.installModules(new RetrofitServiceModule(retrofit));

        Lingver.init(
            this,
            Utils.getAppLanguage(
                PreferenceManager
                    .getDefaultSharedPreferences(this)
                    .getInt(APP_LANGUAGE, 0)
            )
        );
    }

    @Nullable
    private Void deliverInternetNotAvailableMsg() {
        // OkHttpProvider classinin icerisindeki InternetConnectionInterceptor
        // ishler worker thread-da (RxCachedThreadScheduler) fire olundugu ucun,
        // main thread-a "eli catmir". Bu sebebden mainLooper-dan istifade edib
        // Handler icerisinde toast-i gosterirem.
        new Handler(Looper.getMainLooper()).post(() -> {
            Utils.toast(getBaseContext(), R.string.no_internet_connection);
        });

        return null;
    }
}
