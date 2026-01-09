package az.btb.mobilebanking.ui.products.deposits;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.adapters.ItemsAdapter;
import az.btb.mobilebanking.databinding.FragmentProductDepositsBinding;
import az.btb.mobilebanking.databinding.ProductsItemBinding;
import az.btb.mobilebanking.models.DepositProduct;
import az.btb.mobilebanking.ui.products.ProductItemsView;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.ItemPropsBinder;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.di.Scopes.SERVER_SCOPE;

public class ProductDepositsFragment extends Fragment<FragmentProductDepositsBinding> implements ProductItemsView<DepositProduct> {

    @InjectPresenter ProductDepositsPresenter presenter;

    @ProvidePresenter ProductDepositsPresenter providePresenter() {
        return Toothpick.openScope(SERVER_SCOPE).getInstance(ProductDepositsPresenter.class);
    }

    @NonNull
    public static ProductDepositsFragment getInstance() {
        return new ProductDepositsFragment();
    }

    public ProductDepositsFragment() {
        super(R.layout.fragment_product_deposits);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.getProducts();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.goBack.setOnClickListener(v -> presenter.goBack());
    }

    @Override
    public void showError(@NonNull String msg) {
        binding.progressBar.setVisibility(View.GONE);
        if (!msg.isEmpty())
            Utils.snackbar(binding.getRoot(), msg);
    }

    @Override
    public void showItemsList(@NonNull List<DepositProduct> products) {
        if (products.size() == 0) {
            binding.progressBar.setVisibility(View.GONE);
            binding.products.setVisibility(View.GONE);
            binding.noItem.setVisibility(View.VISIBLE);
            return;
        }

        final ItemPropsBinder<ProductsItemBinding, DepositProduct> itemPropsBinder = (binding, product) -> {
            Utils.setImageToImageView(binding.productImage, product.getProductLogoImage());
            binding.productTitle.setText(product.getProductName());
            binding.getRoot().setOnClickListener(v -> presenter.goToProductDetails(product));
        };

        final ItemsAdapter<ProductsItemBinding, DepositProduct> adapter = new ItemsAdapter<>(
            R.layout.products_item, products, itemPropsBinder
        );

        binding.products.setAdapter(adapter);

        binding.progressBar.setVisibility(View.GONE);
        binding.noItem.setVisibility(View.GONE);
        binding.products.setVisibility(View.VISIBLE);
    }
}
