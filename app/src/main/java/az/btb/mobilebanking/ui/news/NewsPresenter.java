package az.btb.mobilebanking.ui.news;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.BankNews;
import az.btb.mobilebanking.models.BankNewsRequest;
import az.btb.mobilebanking.screens.MainScreens;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class NewsPresenter extends MvpPresenter<NewsView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject NewsPresenter(Router router, AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void getNews(String fromDate, String toDate) {
        BankNewsRequest request = new BankNewsRequest(
            Utils.getCommonRequest(), fromDate, toDate, 1, 1, 1);

        compositeDisposable.add(
            authService
                .getBankNews(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
//                        SortedMap<Long, List<BankNews>> dateListSortedMap = new TreeMap<>(Collections.reverseOrder());
//                        for (BankNews news : response.getBankNews()) {
//                            final long date = news.getPublishTimestamp();
//                            if (!dateListSortedMap.containsKey(date)) {
//                                List<BankNews> list = new ArrayList<>();
//                                list.add(news);
//
//                                dateListSortedMap.put(date, list);
//                            } else
//                                dateListSortedMap.get(date).add(news);
//                        }
//
//                        Map<Long, List<BankNews>> finalMap = new LinkedHashMap<>();
//                        for (long date : dateListSortedMap.keySet())
//                            finalMap.put(date, dateListSortedMap.get(date));

                        getViewState().showNews(response.getBankNews());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    void goBack() {
        router.exit();
    }

    void showNewsDetails(BankNews bankNews) {
        router.navigateTo(new MainScreens.NewsDetailsScreen(bankNews));
    }

    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
            compositeDisposable.dispose();
        }
    }
}
