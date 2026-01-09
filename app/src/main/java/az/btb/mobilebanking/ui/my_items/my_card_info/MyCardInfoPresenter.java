package az.btb.mobilebanking.ui.my_items.my_card_info;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.UpdateCardSettingsRequest;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class MyCardInfoPresenter extends MvpPresenter<MyCardInfoView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    MyCardInfoPresenter(Router router, AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void goBack() {
        router.exit();
    }

    // cardChangeOption - 1 dise adini deyishirsen, 2 dise coloru
    void changeCardData(String cardId, String cardNewAltName, int cardNewColor, int cardChangeOption) {
        // apini neter yaziblarsa, day sozum yoxdu...
        // apinin logici beledi:
        //   gonderilen cardNewAltName-in uzunluguna baxirlar, hansi char-lari gondermeyinden ASILI OLMAYARAQ
        //   eger 3den azdirsa, request is invalid return edirler. eks halda success olur.
        //   meselen: "qw" yazib gondersen, islemeyecek. "qwe" yazsan isleyecek -__-
        //   ona gore asagida trim, daha sonra isEmpty check edirem ve bele oldugu halda "   " (3 space) gonderirem.
        //   bele bele ishler....

        String a = cardNewAltName;
        int len = a.trim().length();
        if (len < 3) {
            StringBuilder cardNewAltNameBuilder = new StringBuilder(cardNewAltName);
            for (int i = 3; i > len; i--)
                cardNewAltNameBuilder.append(" ");
            a = cardNewAltNameBuilder.toString();
        }

        UpdateCardSettingsRequest request = new UpdateCardSettingsRequest(
            Utils.getCommonRequest(), cardId, a, cardNewColor, cardChangeOption
        );

   //     System.out.println(new Gson().toJson(request));

        compositeDisposable.add(
            authService
                .updateCardSettings(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            getViewState().showResult(cardNewAltName, cardNewColor);
                        else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
            compositeDisposable.dispose();
        }
    }
}
