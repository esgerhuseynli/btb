package az.btb.mobilebanking.ui.allowed_devices;

import androidx.annotation.StringRes;

import java.util.List;

import az.btb.mobilebanking.models.MobileDevice;
import moxy.MvpView;

interface AllowedDevicesView extends MvpView {
    void showAllDevices(List<MobileDevice> devices);
    void showError(String msg);
    void showResultMsg(@StringRes int msg);
}
