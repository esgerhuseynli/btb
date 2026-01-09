package az.btb.mobilebanking.ui.service_points.branches;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.adapters.ItemsAdapter;
import az.btb.mobilebanking.databinding.FragmentBranchesBinding;
import az.btb.mobilebanking.databinding.ServicePointItemBinding;
import az.btb.mobilebanking.databinding.ServicePointListItemBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.ServicePoint;
import az.btb.mobilebanking.ui.service_points.ServicePointsPresenter;
import az.btb.mobilebanking.ui.service_points.ServicePointsView;
import az.btb.mobilebanking.utils.ItemPropsBinder;
import az.btb.mobilebanking.utils.ServicePointsBaseFragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class BranchesFragment extends ServicePointsBaseFragment<FragmentBranchesBinding> implements OnMapReadyCallback, OnMarkerClickListener, ServicePointsView {

    @InjectPresenter ServicePointsPresenter presenter;

    @ProvidePresenter ServicePointsPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(ServicePointsPresenter.class);
    }

    private GoogleMap map;
    private Marker previousMarker = null;
    private final Map<Integer, Marker> indexesOfMarkers = new HashMap<>();

    private List<ServicePoint> bankBranchList;

    public BranchesFragment() {
        super(R.layout.fragment_branches);
    }

    public BranchesFragment(List<ServicePoint> bankBranchList) {
        super(R.layout.fragment_branches);
        this.bankBranchList = bankBranchList;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.mapView.onCreate(savedInstanceState);
        binding.mapView.onResume();
        binding.mapView.getMapAsync(this);

        /* BEGIN: Bottom recycler view stuff */
        ItemPropsBinder<ServicePointItemBinding, ServicePoint> itemPropsBinder = (itemBinding, servicePoint) -> {
            itemBinding.setServicePoint(servicePoint);
            itemBinding.callServicePointCallCenter.setOnClickListener(v -> openDialPad(servicePoint.getBranchPhones().get(0)));

            itemBinding.getRoot().setOnClickListener(v -> {
                final Marker marker = indexesOfMarkers.get(bankBranchList.indexOf(servicePoint));
                if (previousMarker != marker) {
                    onMarkerClick(marker);

                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));
                }
            });
        };

        ItemsAdapter<ServicePointItemBinding, ServicePoint> adapter = new ItemsAdapter<>(
            R.layout.service_point_item, bankBranchList, itemPropsBinder
        );
        binding.branchesList.setAdapter(adapter);
        /* END: Bottom recycler view stuff */


        /* BEGIN: List of branches recycler view stuff */
        prepareDetailedBranchesList();
        /* END: List of branches recycler view stuff */
    }

    private void prepareDetailedBranchesList() {
        binding.showListOrMap.setOnClickListener(button -> {
            if (binding.detailedBranchList.getVisibility() == View.GONE) {
                // eger daha onceden adapter set olunmayibsa, onda adapteri yarat ve set ele. eks halini yazmaq lazim deyil.
                if (binding.detailedBranchList.getAdapter() == null) {
                    ItemPropsBinder<ServicePointListItemBinding, ServicePoint> itemPropsBinder = (itemBinding, servicePoint) -> {
                        itemBinding.servicePointIcon.setImageDrawable(Utils.getDrawable(requireContext(), R.drawable.ic_service_point_branch));
                        itemBinding.setServicePoint(servicePoint);

                        itemBinding.callToBranch.setOnClickListener(v -> openDialPad(servicePoint.getBranchPhones().get(0)));

                        itemBinding.getRoot().setOnClickListener(v -> presenter.goToBranchDetails(servicePoint));
                    };

                    ItemsAdapter<ServicePointListItemBinding, ServicePoint> detailedBranchListAdapter = new ItemsAdapter<>(
                        R.layout.service_point_list_item, bankBranchList, itemPropsBinder
                    );
                    binding.detailedBranchList.setAdapter(detailedBranchListAdapter);
                }

                binding.branchesList.setVisibility(View.GONE);
                binding.mapView.setVisibility(View.GONE);
                binding.detailedBranchList.setVisibility(View.VISIBLE);
                binding.showListOrMap.setImageDrawable(Utils.getDrawable(requireContext(), R.drawable.ic_show_service_points_in_map));
            } else {
                binding.branchesList.setVisibility(View.VISIBLE);
                binding.mapView.setVisibility(View.VISIBLE);
                binding.detailedBranchList.setVisibility(View.GONE);
                binding.showListOrMap.setImageDrawable(Utils.getDrawable(requireContext(), R.drawable.ic_show_service_points_in_list));
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        final int addressesCount = bankBranchList.size();
        for (int i = 0; i < addressesCount; i++) {
            LatLng coordinates = new LatLng(
                bankBranchList.get(i).getAddress().getX(),
                bankBranchList.get(i).getAddress().getY()
            );

            final MarkerOptions markerOptions = Utils.createMarker(coordinates);
            final Marker marker = googleMap.addMarker(markerOptions);
            marker.setTag(i); // this value will be used onRecyclerItemClick event.

            indexesOfMarkers.put(i, marker);

            googleMap.addMarker(markerOptions);
        }

        googleMap.setOnMarkerClickListener(this);

        // https://btb.az/az/officees linkine bax. orda onlar ozleri bu saheni zoom veribler.
        // men de ordan goturmusem bu erazini.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.3833027, 49.8454219), 10));
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        try {
            if (previousMarker != null)
                previousMarker.setIcon(Utils.getSmallPinIcon());

            marker.setIcon(Utils.getSmallPinIcon());
            marker.setIcon(Utils.getBigPinIcon());

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));

            binding.branchesList.smoothScrollToPosition((int) marker.getTag());

            previousMarker = marker;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    private void openDialPad(final String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void setServicePointsData(Pair<List<ServicePoint>, List<ServicePoint>> servicePoints) {

    }

    /**
     *
     *
     * @param point A service point item to be selected in map & horizontal scrollview.
     */
    @Override
    public void selectServicePoint(ServicePoint point) {
        binding.detailedBranchList.setVisibility(View.GONE);
        binding.mapView.setVisibility(View.VISIBLE);
        binding.branchesList.setVisibility(View.VISIBLE);
        binding.showListOrMap.setImageDrawable(Utils.getDrawable(requireContext(), R.drawable.ic_show_service_points_in_list));
        final Marker marker = indexesOfMarkers.get(bankBranchList.indexOf(point));
        onMarkerClick(marker);
    }

    @Override
    public void resetView() {
        if (binding.detailedBranchList.getVisibility() != View.GONE) {
            binding.branchesList.setVisibility(View.VISIBLE);
            binding.mapView.setVisibility(View.VISIBLE);
            binding.detailedBranchList.setVisibility(View.GONE);
            binding.showListOrMap.setImageDrawable(Utils.getDrawable(requireContext(), R.drawable.ic_show_service_points_in_list));
        }
    }
}
