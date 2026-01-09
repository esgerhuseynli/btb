package az.btb.mobilebanking;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import java.util.List;

import javax.inject.Inject;

import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.AppInfo;
import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import az.btb.mobilebanking.models.DeviceInfo;
import az.btb.mobilebanking.models.MobileUser;
import az.btb.mobilebanking.models.RequestInfo;
import az.btb.mobilebanking.utils.Utils;
import moxy.MvpAppCompatActivity;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.android.support.SupportAppNavigator;
import toothpick.Toothpick;

import static az.btb.mobilebanking.utils.Constants.LANGUAGE_CHANGE_EVENT_TOKEN;

public class MainActivity extends MvpAppCompatActivity implements MainView {

    @Inject NavigatorHolder navigatorHolder;
    @InjectPresenter MainPresenter mainPresenter;

    private final Navigator navigator = new SupportAppNavigator(this, R.id.fragment_container);

    @ProvidePresenter MainPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(MainPresenter.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);

        // prevent taking screenshots
       /* if (BuildConfig.FLAVOR == "prod" && !BuildConfig.DEBUG)
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);*/

        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        Toothpick.inject(this, Toothpick.openScope(Scopes.APP_SCOPE));
        setAppData();
    }

    private void setAppData() {
        String appName = getResources().getString(R.string.app_name);
        String appVersion = BuildConfig.VERSION_NAME;
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceModel = android.os.Build.MODEL;
        String vendor = Build.MANUFACTURER;
        String osVersion = String.valueOf(Build.VERSION.SDK_INT);
        String osName = "android";
        int appLanguage = mainPresenter.getLanguage();
        AppInfo appInfo = new AppInfo(1, appName, appVersion);
        DeviceInfo deviceInfo = new DeviceInfo(deviceId, vendor, deviceModel, osName, osVersion);
        MobileUser mobileUser = new MobileUser();
        RequestInfo requestInfo = new RequestInfo(mobileUser, deviceInfo, appInfo, appLanguage);
        AppData.getInstance().setRequestInfo(requestInfo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigatorHolder.setNavigator(navigator);

        if (!Utils.isPinFingerprintScreenMustBeBypassed(this)) {
            mainPresenter.redirectToProperPage(getIntent().getBooleanExtra(LANGUAGE_CHANGE_EVENT_TOKEN, false));
            getIntent().putExtra(LANGUAGE_CHANGE_EVENT_TOKEN, false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        navigatorHolder.removeNavigator();
    }

    public void saveSessionTime() {
        mainPresenter.saveSession();
    }

    public void refreshBankCardsAndAccounts() {
        mainPresenter.getBankCards();
    }

    @Override
    public void setAppBankCards(List<BankCard> bankCards) {
        ((App) getApplication()).BANK_CARDS.clear();
        ((App) getApplication()).BANK_CARDS.addAll(bankCards);
    }

    @Override
    public void setAppBankAccounts(List<BankAccount> bankAccounts) {
        ((App) getApplication()).BANK_ACCOUNTS.clear();
        ((App) getApplication()).BANK_ACCOUNTS.addAll(bankAccounts);
    }
}
