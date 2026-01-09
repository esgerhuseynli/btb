package az.btb.mobilebanking.adapters;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

import az.btb.mobilebanking.models.ServicePoint;
import az.btb.mobilebanking.ui.service_points.atms.ATMsFragment;
import az.btb.mobilebanking.ui.service_points.branches.BranchesFragment;
import az.btb.mobilebanking.utils.ServicePointsBaseFragment;

public class ServicePointsPagerAdapter extends FragmentStateAdapter {

    private ServicePointsBaseFragment fragment;
    private final Pair<List<ServicePoint>, List<ServicePoint>> servicePoints;

    public ServicePointsPagerAdapter(
        @NonNull Fragment fragment,
        @NonNull Pair<List<ServicePoint>, List<ServicePoint>> servicePoints
    ) {
        super(fragment);
        this.servicePoints = servicePoints;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0)
            fragment = new BranchesFragment(servicePoints.second);
        else
            fragment = new ATMsFragment(servicePoints.first);

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public void setSelectedAtmPoint(ServicePoint point) {
        fragment.selectServicePoint(point);
    }

    public void setSelectedBranchPoint(ServicePoint point) {
        fragment.selectServicePoint(point);
    }

    public void resetView() {
        fragment.resetView();
    }
}
