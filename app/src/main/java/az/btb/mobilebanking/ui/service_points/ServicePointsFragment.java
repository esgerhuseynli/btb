package az.btb.mobilebanking.ui.service_points;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.adapters.FilterableServicePointsAdapter;
import az.btb.mobilebanking.adapters.ServicePointsPagerAdapter;
import az.btb.mobilebanking.databinding.FragmentServicePointsBinding;
import az.btb.mobilebanking.databinding.ServicePointSearchItemBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.ServicePoint;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.ItemPropsBinder;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.utils.Constants.SERVICE_POINT_TYPE_ATM;
import static az.btb.mobilebanking.utils.Constants.SERVICE_POINT_TYPE_BRANCH;

public class ServicePointsFragment extends Fragment<FragmentServicePointsBinding> implements ServicePointsView {

    private int previousTab = 0;
    private boolean isSearchActivated = false;
    private FilterableServicePointsAdapter filterAdapter = null;

    @InjectPresenter ServicePointsPresenter presenter;

    @ProvidePresenter ServicePointsPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(ServicePointsPresenter.class);
    }

    public ServicePointsFragment() {
        super(R.layout.fragment_service_points);
    }

    @NonNull
    public static ServicePointsFragment getInstance() {
        return new ServicePointsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.goBack.setOnClickListener(v -> {
            if (!isSearchActivated)
                presenter.goBack();
            else {
                binding.searchPage.setVisibility(View.GONE);
                binding.searchForServicePoints.setVisibility(View.VISIBLE);
                binding.servicePointsTab.setVisibility(View.VISIBLE);
                binding.servicePointsPager.setVisibility(View.VISIBLE);
                isSearchActivated = !isSearchActivated;
            }
        });

        presenter.getServicePoints();
    }

    @Override
    public void showError(@NonNull String message) {
        binding.progressBar.setVisibility(View.INVISIBLE);
        if (!message.isEmpty())
            Utils.snackbar(binding.getRoot(), message);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void setServicePointsData(Pair<List<ServicePoint>, List<ServicePoint>> servicePoints) {
        final ServicePointsPagerAdapter pagerAdapter = new ServicePointsPagerAdapter(this, servicePoints);
        binding.servicePointsPager.setAdapter(pagerAdapter);
        binding.servicePointsPager.setUserInputEnabled(false);
        binding.servicePointsPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0)
                    previousTab = 1;
                else
                    previousTab = 0;
            }
        });

        binding.servicePointsPager.setCurrentItem(0, true);

        new TabLayoutMediator(binding.servicePointsTab, binding.servicePointsPager, (tab, position) -> {
            if (position == 0)
                tab.setText(R.string.branch);
            else
                tab.setText(R.string.atm);
        }).attach();

        ItemPropsBinder<ServicePointSearchItemBinding, ServicePoint> itemPropsBinder = (itemBinding, servicePoint) -> {
            itemBinding.setType(servicePoint.getServicePointType());
            itemBinding.setName(servicePoint.getName());
            itemBinding.setCity(servicePoint.getAddress().getCityName());
            itemBinding.setDistrict(servicePoint.getAddress().getDisctrictName());
            itemBinding.setStreet(servicePoint.getAddress().getAddress());

            itemBinding.getRoot().setOnClickListener(v -> {
                binding.searchForServicePoints.setVisibility(View.VISIBLE);
                binding.searchPage.setVisibility(View.GONE);
                binding.servicePointsTab.setVisibility(View.VISIBLE);
                binding.servicePointsPager.setVisibility(View.VISIBLE);
                isSearchActivated = false;

                if (servicePoint.getServicePointType() == SERVICE_POINT_TYPE_ATM) {
                    // atm tab-ini select ele
                    binding.servicePointsPager.setCurrentItem(1, true);
                    pagerAdapter.setSelectedAtmPoint(servicePoint);
                } else {
                    // branch tab-ini select ele
                    binding.servicePointsPager.setCurrentItem(0, true);
                    pagerAdapter.setSelectedBranchPoint(servicePoint);
                }
            });
        };

        filterAdapter = new FilterableServicePointsAdapter(itemPropsBinder);
        binding.searchResultsList.setAdapter(filterAdapter);

        binding.searchForServicePoints.setOnClickListener(v -> {
            if (previousTab != binding.servicePointsPager.getCurrentItem()) {
                binding.searchText.setText("");
                filterAdapter.submitList(new ArrayList<>());
            }

            binding.searchPage.setVisibility(View.VISIBLE);
            binding.searchForServicePoints.setVisibility(View.GONE);
            binding.servicePointsTab.setVisibility(View.GONE);
            binding.servicePointsPager.setVisibility(View.GONE);
            isSearchActivated = !isSearchActivated;
        });

        binding.searchText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Utils.hideKeyboardFrom(requireContext(), binding.getRoot());
                return searchForItems(binding.searchText.getText().toString(), servicePoints, pagerAdapter);
            }
            return false;
        });

        binding.progressBar.setVisibility(View.GONE);
    }

    private boolean searchForItems(
        String searchable,
        Pair<List<ServicePoint>, List<ServicePoint>> servicePoints,
        ServicePointsPagerAdapter pagerAdapter
    ) {
        ArrayList<ServicePoint> allServicePoints = new ArrayList<>(servicePoints.first);
        allServicePoints.addAll(servicePoints.second);

        if (searchable.isEmpty())
            filterAdapter.submitList(allServicePoints);
        else {
            searchable = searchable.toLowerCase();
            List<ServicePoint> filteredList = new ArrayList<>();

            if (binding.servicePointsPager.getCurrentItem() == 0) {
                for (ServicePoint point : allServicePoints)
                    if (point.getServicePointType() == SERVICE_POINT_TYPE_BRANCH &&
                        (point.getName().toLowerCase().contains(searchable) ||
                        point.getAddress().getDisctrictName().toLowerCase().contains(searchable) ||
                        point.getAddress().getAddress().toLowerCase().contains(searchable)))
                        filteredList.add(point);
            } else {
                for (ServicePoint point : allServicePoints)
                    if (point.getServicePointType() == SERVICE_POINT_TYPE_ATM && (
                        point.getName().toLowerCase().contains(searchable) ||
                        point.getAddress().getDisctrictName().toLowerCase().contains(searchable) ||
                        point.getAddress().getAddress().toLowerCase().contains(searchable)))
                        filteredList.add(point);
            }

            filterAdapter.submitList(filteredList);
        }

        return true;
    }
}
