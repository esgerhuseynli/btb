package az.btb.mobilebanking.ui.service_points.atms;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentAtmDetailsBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.ServicePoint;
import az.btb.mobilebanking.ui.service_points.ServicePointsPresenter;
import az.btb.mobilebanking.ui.service_points.ServicePointsView;
import az.btb.mobilebanking.utils.Fragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class AtmDetailsFragment extends Fragment<FragmentAtmDetailsBinding> implements ServicePointsView {

    private ServicePoint point;

    @InjectPresenter ServicePointsPresenter presenter;

    @ProvidePresenter ServicePointsPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(ServicePointsPresenter.class);
    }

    public AtmDetailsFragment() {
        super(R.layout.fragment_atm_details);
    }

    @NonNull
    public static AtmDetailsFragment getInstance(ServicePoint point) {
        Bundle b = new Bundle();
        b.putSerializable("point", point);

        AtmDetailsFragment fragment = new AtmDetailsFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        point = (ServicePoint) getArguments().getSerializable("point");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.goBack.setOnClickListener(v -> presenter.goBack());

        binding.setAtmName(point.getName());
        binding.setAddress(point.getAddress().getAddress());
        binding.setStatus(point.getStatus() == 0);
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void setServicePointsData(Pair<List<ServicePoint>, List<ServicePoint>> servicePoints) {

    }
}
