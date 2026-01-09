package az.btb.mobilebanking.ui.service_points.branches;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentBranchDetailsBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.ServicePoint;
import az.btb.mobilebanking.ui.service_points.ServicePointsPresenter;
import az.btb.mobilebanking.ui.service_points.ServicePointsView;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class BranchDetailsFragment extends Fragment<FragmentBranchDetailsBinding> implements ServicePointsView {

    private ServicePoint point;

    @InjectPresenter ServicePointsPresenter presenter;

    @ProvidePresenter ServicePointsPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(ServicePointsPresenter.class);
    }

    public BranchDetailsFragment() {
        super(R.layout.fragment_branch_details);
    }

    @NonNull
    public static BranchDetailsFragment getInstance(ServicePoint point) {
        Bundle b = new Bundle();
        b.putSerializable("point", point);

        BranchDetailsFragment fragment = new BranchDetailsFragment();
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

        final String phone = point.getBranchPhones().get(0);
        binding.callServicePointCallCenter.setOnClickListener(v -> openDialPad(phone));

        binding.setBranchName(point.getName());
        binding.setAddress(point.getAddress().getAddress());

        final String workingDaysAndHours =
            String.format(
                getString(R.string.working_hours),
                getString(Utils.getDayName(point.getWorkingDays().getFrom())),
                getString(Utils.getDayName(point.getWorkingDays().getTo())),
                point.getWorkingHours().getFrom(),
                point.getWorkingHours().getTo()
            );
        binding.setWorkingDaysAndHours(workingDaysAndHours);

        final String breaks =
            String.format(
                getString(R.string.break_hours),
                Utils.capitalize(point.getWorkingHours().getExceptionDescription()),
                point.getWorkingHours().getExceptionFrom(),
                point.getWorkingHours().getExceptionTo()
            );
        binding.setBreaks(breaks);

        StringBuilder dayOffs = new StringBuilder();
        List<Integer> daysOffList = point.getWorkingDays().getDayOffs();
        dayOffs.append(getString(Utils.getDayName(daysOffList.get(0))));
        for (int i = 1; i < daysOffList.size(); i++)
            dayOffs.append(", ").append(getString(Utils.getDayName(daysOffList.get(i))));
        binding.setDayOffs(dayOffs.toString());

        binding.setPhone(phone);
        binding.setFax(point.getBranchFaxes().get(0).trim());
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void setServicePointsData(Pair<List<ServicePoint>, List<ServicePoint>> servicePoints) {

    }

    private void openDialPad(final String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }
}
