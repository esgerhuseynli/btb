package az.btb.mobilebanking.ui.news;

import java.util.List;

import az.btb.mobilebanking.models.BankNews;
import moxy.MvpView;

interface NewsView extends MvpView {
    void showError(String message);
    void showNews(/*Map<Long, */List<BankNews> bankNews);
}
