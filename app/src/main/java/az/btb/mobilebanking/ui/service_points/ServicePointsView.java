package az.btb.mobilebanking.ui.service_points;

import android.util.Pair;

import java.util.List;

import az.btb.mobilebanking.models.ServicePoint;
import moxy.MvpView;

public interface ServicePointsView extends MvpView {
    void showError(String message);
    void setServicePointsData(Pair<List<ServicePoint>, List<ServicePoint>> servicePoints);
}
