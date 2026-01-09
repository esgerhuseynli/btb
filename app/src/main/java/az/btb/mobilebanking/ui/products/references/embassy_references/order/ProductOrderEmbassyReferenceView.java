package az.btb.mobilebanking.ui.products.references.embassy_references.order;

import java.util.List;

import az.btb.mobilebanking.models.EmbassyCountry;
import az.btb.mobilebanking.models.EmbassyPoint;
import moxy.MvpView;

public interface ProductOrderEmbassyReferenceView extends MvpView {
    void showError(String msg);
    void showEmbassyCountries(List<EmbassyCountry> embassyCountries);
    void showEmbassyPoints(List<EmbassyPoint> embassyPoints);
    void showOrderResult(int plasticCardOrderStatus);
}
