package az.btb.mobilebanking;

import android.content.SharedPreferences;
import android.os.SystemClock;

import com.yariksoffice.lingver.Lingver;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.RequestInfoRequest;
import az.btb.mobilebanking.screens.AuthScreens;
import az.btb.mobilebanking.screens.MainScreens;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

import static az.btb.mobilebanking.utils.Constants.ALLOWED_LOGIN_TIMEOUT;
import static az.btb.mobilebanking.utils.Constants.HAS_ACTIVE_SESSION;
import static az.btb.mobilebanking.utils.Constants.LAST_LOGIN;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

	private final Router mRouter;
	private final SharedPreferences mPreferences;
	private final AuthService mAuthService;
	private final CompositeDisposable compositeDisposable = new CompositeDisposable();

	@Inject public MainPresenter(final Router router, final SharedPreferences preferences, final AuthService authService) {
		mRouter = router;
		mPreferences = preferences;
		mAuthService = authService;
	}

	void getBankCards() {
		compositeDisposable.add(
			mAuthService
				.listBankCards(new RequestInfoRequest(Utils.getCommonRequest()))
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
					response -> {
						if (response.getResponseInfo().getResponseType() == 0) {
							// IMMEDIATELY START THE REQUEST !!
							getBankAccounts();
							getViewState().setAppBankCards(response.getBankCards());
						}
					},
					error -> { }
				)
		);
	}

	private void getBankAccounts() {
		compositeDisposable.add(
			mAuthService
				.listBankAccounts(new RequestInfoRequest(Utils.getCommonRequest()))
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
					response -> {
						if (response.getResponseInfo().getResponseType() == 0)
							getViewState().setAppBankAccounts(response.getBankAccounts());
					},
					error -> { }
				)
		);
	}

	void redirectToProperPage(boolean is_language_changed) {
		if (mPreferences.getBoolean(HAS_ACTIVE_SESSION, false)) {
			if (AppData.getInstance().isFirstLaunch() || SystemClock.elapsedRealtime() - mPreferences.getLong(LAST_LOGIN, 0) >= ALLOWED_LOGIN_TIMEOUT || is_language_changed) {
				AppData.getInstance().setFirstLaunch(false);
				mRouter.newRootScreen(new AuthScreens.SignInPinFingerprintScreen());
			}
		} else
			mRouter.newRootScreen(new MainScreens.IntroScreen());
	}

	void saveSession() {
		mPreferences.edit().putLong(LAST_LOGIN, SystemClock.elapsedRealtime()).apply();
	}

	int getLanguage() {
		return Utils.getAppLanguageReversed(Lingver.getInstance().getLanguage()) + 1;
	}

	@Override
	public void onDestroy() {
		if (!compositeDisposable.isDisposed()) {
			compositeDisposable.clear();
			compositeDisposable.dispose();
		}
	}
}
