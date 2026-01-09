package az.btb.mobilebanking.ui.notifications;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.UserNotificationsRequest;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class NotificationsPresenter extends MvpPresenter<NotificationsView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject NotificationsPresenter(AuthService authService, Router router) {
        this.authService = authService;
        this.router = router;
    }

    void getNotifications(String fromDate, String toDate) {
        UserNotificationsRequest request = new UserNotificationsRequest(Utils.getCommonRequest(), fromDate, toDate);

        compositeDisposable.add(
                authService
                        .getUserNotifications(request)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
//                        SortedMap<Long, List<UserNotification>> dateListSortedMap = new TreeMap<>(Collections.reverseOrder());
//                        for (UserNotification notification : response.getUserNotifications()) {
//                            final long timestamp = notification.getPublishTimestamp();
//                            if (dateListSortedMap.containsKey(timestamp))
//                                dateListSortedMap.get(timestamp).add(notification);
//                            else {
//                                List<UserNotification> list = new ArrayList<>();
//                                list.add(notification);
//
//                                dateListSortedMap.put(timestamp, list);
//                            }
//                        }
//
//                        Map<Long, List<UserNotification>> finalMap = new LinkedHashMap<>();
//                        for (final long date : dateListSortedMap.keySet())
//                            finalMap.put(date, dateListSortedMap.get(date));

                                    getViewState().showNotifications(response.getUserNotifications());
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
