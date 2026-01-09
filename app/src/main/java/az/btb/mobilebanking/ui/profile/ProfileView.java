package az.btb.mobilebanking.ui.profile;

import az.btb.mobilebanking.models.MobileUserData;
import moxy.MvpView;

interface ProfileView extends MvpView {
    void setProfileInfo(MobileUserData response);
    void showLoading(boolean isLoading);
}
