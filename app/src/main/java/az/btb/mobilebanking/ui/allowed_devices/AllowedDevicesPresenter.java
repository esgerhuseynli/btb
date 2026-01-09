package az.btb.mobilebanking.ui.allowed_devices;

import javax.inject.Inject;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.ChangeDeviceSettingsRequest;
import az.btb.mobilebanking.models.RequestInfoRequest;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

import static az.btb.mobilebanking.utils.Constants.DEVICE_STATUS_DISABLE;

@InjectViewState
public class AllowedDevicesPresenter extends MvpPresenter<AllowedDevicesView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject AllowedDevicesPresenter(Router router, AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void getAllDevices() {
        compositeDisposable.add(
            authService
                .getMobileDevices(new RequestInfoRequest(Utils.getCommonRequest()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            getViewState().showAllDevices(response.getMobileDevices());
                        else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    void changeDeviceStatus(String deviceId, int newStatus) {
        ChangeDeviceSettingsRequest request = new ChangeDeviceSettingsRequest(
            Utils.getCommonRequest(),
            deviceId,
            1,
            newStatus
        );

        compositeDisposable.add(
            authService
                .alterDevice(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0) {
                            getAllDevices();

                            if (newStatus == DEVICE_STATUS_DISABLE)
                                getViewState().showResultMsg(R.string.device_unregistered);
                            else
                                getViewState().showResultMsg(R.string.device_registered);
                        } else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    void goBack() {
        router.exit();
    }

    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
            compositeDisposable.dispose();
        }
    }
}
