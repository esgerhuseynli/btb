package az.btb.mobilebanking.ui.products.orders;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.adapters.ItemsAdapter;
import az.btb.mobilebanking.databinding.FragmentProductOrdersBinding;
import az.btb.mobilebanking.databinding.ProductOrderItemBinding;
import az.btb.mobilebanking.models.ProductOrder;
import az.btb.mobilebanking.ui.products.ProductItemsView;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.ItemPropsBinder;
import az.btb.mobilebanking.utils.Utils;
import kotlin.Pair;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static android.graphics.PorterDuff.Mode.SRC_IN;
import static az.btb.mobilebanking.di.Scopes.SERVER_SCOPE;

public class ProductOrdersFragment extends Fragment<FragmentProductOrdersBinding> implements ProductItemsView<ProductOrder> {

    private boolean isDataShown = true;

    @InjectPresenter ProductOrdersPresenter presenter;

    @ProvidePresenter ProductOrdersPresenter providePresenter() {
        return Toothpick.openScope(SERVER_SCOPE).getInstance(ProductOrdersPresenter.class);
    }

    final ItemPropsBinder<ProductOrderItemBinding, ProductOrder> itemPropsBinder = (binding, product) -> {
        binding.orderName.setText(product.getProductHeaderName());

        Pair<Integer, Integer> statusInfo = getOrderStatusIndicator(product.getOrderStatus());
        binding.orderStatus.setText(statusInfo.component1());
        binding.orderStatus.setTextColor(getResources().getColor(statusInfo.component2()));

        binding.getRoot().setOnClickListener(v -> presenter.goToOrderDetailsScreen(product));
    };

    final ItemsAdapter<ProductOrderItemBinding, ProductOrder> adapter = new ItemsAdapter<>(
        R.layout.product_order_item, new ArrayList<>(), itemPropsBinder
    );

    @NonNull
    public static ProductOrdersFragment getInstance() {
        return new ProductOrdersFragment();
    }

    public ProductOrdersFragment() {
        super(R.layout.fragment_product_orders);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (binding != null)
            binding.products.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        isDataShown = true;

        binding.goBack.setOnClickListener(v -> presenter.goBack());
        presenter.getProductOrders(0, 0);

        binding.setShowFilterVisible(true);
        binding.filter.setOnClickListener(v -> {
            if (binding.getShowFilterVisible())
                binding.filter.setColorFilter(Color.parseColor("#383336"));
            else
                binding.filter.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark),
                    SRC_IN
                );

            binding.setShowFilterVisible(!binding.getShowFilterVisible());
        });

        binding.productTypes.setAdapter(
            new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, android.R.id.text1,
                getResources().getStringArray(R.array.product_types)
            )
        );
        binding.productStatuses.setAdapter(
            new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, android.R.id.text1,
                getResources().getStringArray(R.array.product_statuses)
            )
        );

        binding.productTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isDataShown)
                    presenter.getProductOrders(position, binding.productStatuses.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        binding.productStatuses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isDataShown)
                    presenter.getProductOrders(position, binding.productTypes.getSelectedItemPosition());

                isDataShown = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        binding.setShowFilterVisible(false);

        if (binding.products.getAdapter() == null)
            binding.products.setAdapter(adapter);
    }

    @Override
    public void showError(@NonNull String msg) {
        binding.progressBar.setVisibility(View.GONE);
        if (!msg.isEmpty())
            Utils.snackbar(binding.getRoot(), msg);
    }

    @Override
    public void showItemsList(@NonNull List<ProductOrder> products) {
        if (products.size() == 0) {
            binding.progressBar.setVisibility(View.GONE);
            binding.products.setVisibility(View.GONE);
            binding.noItem.setVisibility(View.VISIBLE);
            return;
        }

        adapter.submitList(products);

        binding.progressBar.setVisibility(View.GONE);
        binding.noItem.setVisibility(View.GONE);
        binding.products.setVisibility(View.VISIBLE);
    }

    @NonNull
    private Pair<Integer, Integer> getOrderStatusIndicator(int orderStatus) {
        switch (orderStatus) {
            case 1:
                return new Pair<>(R.string.registered_order, R.color.registered_order);
            case 2:
                return new Pair<>(R.string.failed_order, R.color.colorAccent);
            case 3:
                return new Pair<>(R.string.success_order, R.color.success_order);
            default:
                return new Pair<>(R.string.no_any_item, R.color.white);
        }
    }
}
