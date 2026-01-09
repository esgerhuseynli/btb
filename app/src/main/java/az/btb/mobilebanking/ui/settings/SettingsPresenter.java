package az.btb.mobilebanking.ui.settings;

import android.content.SharedPreferences;

import javax.inject.Inject;

import az.btb.mobilebanking.screens.AuthScreens;
import az.btb.mobilebanking.screens.MainScreens;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

import static az.btb.mobilebanking.utils.Constants.IS_FINGERPRINT_ENABLED;

@InjectViewState
public class SettingsPresenter extends MvpPresenter<SettingsView> {

    private final Router router;
    private final SharedPreferences sharedPreferences;

    @Inject
    SettingsPresenter(Router router, SharedPreferences sharedPreferences) {
        this.router = router;
        this.sharedPreferences = sharedPreferences;
    }

    boolean doesFingerprintLoginActivated() {
        return sharedPreferences.getBoolean(IS_FINGERPRINT_ENABLED, false);
    }

    void deactivateFingerprintLogin() {
        sharedPreferences.edit().putBoolean(IS_FINGERPRINT_ENABLED, false).apply();
    }

    void goToFingerprintActivationScreen() {
        router.navigateTo(new AuthScreens.FingerprintScreen(false));
    }

    void manageAllowedDevices() {
        router.navigateTo(new MainScreens.AllowedDevicesScreen());
    }

    void goToPinChangeScreen() {
        router.navigateTo(new MainScreens.PinChangeScreen());
    }

    void goBack() {
        router.backTo(new MainScreens.HomeNavScreen());
    }
}
