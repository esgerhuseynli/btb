package az.btb.mobilebanking.ui.notifications;

import java.util.List;

import az.btb.mobilebanking.models.UserNotification;
import moxy.MvpView;

interface NotificationsView extends MvpView {
    void showError(String message);
    void showNotifications(/*Map<Long, */List<UserNotification>/*>*/ notificationList);
}
