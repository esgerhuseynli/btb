package az.btb.mobilebanking.ui.products.plastic_cards;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.adapters.ItemsAdapter;
import az.btb.mobilebanking.databinding.FragmentProductPlasticCardsBinding;
import az.btb.mobilebanking.databinding.ProductsItemBinding;
import az.btb.mobilebanking.models.PlasticCardProduct;
import az.btb.mobilebanking.ui.products.ProductItemsView;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.ItemPropsBinder;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.di.Scopes.SERVER_SCOPE;

public class ProductPlasticCardsFragment extends Fragment<FragmentProductPlasticCardsBinding> implements ProductItemsView<PlasticCardProduct> {

    @InjectPresenter ProductPlasticCardsPresenter presenter;

    @ProvidePresenter ProductPlasticCardsPresenter providePresenter() {
        return Toothpick.openScope(SERVER_SCOPE).getInstance(ProductPlasticCardsPresenter.class);
    }

    @NonNull
    public static ProductPlasticCardsFragment getInstance() {
        return new ProductPlasticCardsFragment();
    }

    public ProductPlasticCardsFragment() {
        super(R.layout.fragment_product_plastic_cards);
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
    public void showItemsList(@NonNull List<PlasticCardProduct> products) {
        if (products.size() == 0) {
            binding.progressBar.setVisibility(View.GONE);
            binding.products.setVisibility(View.GONE);
            binding.noItem.setVisibility(View.VISIBLE);
            return;
        }

        final ItemPropsBinder<ProductsItemBinding, PlasticCardProduct> itemPropsBinder = (binding, product) -> {
            Utils.setImageToImageView(binding.productImage, product.getProductLogoImage());
            binding.productTitle.setText(product.getProductName());
            binding.getRoot().setOnClickListener(v -> presenter.goToProductDetails(product));
        };

        final ItemsAdapter<ProductsItemBinding, PlasticCardProduct> adapter = new ItemsAdapter<>(
            R.layout.products_item, products, itemPropsBinder
        );

        binding.products.setAdapter(adapter);

        binding.progressBar.setVisibility(View.GONE);
        binding.noItem.setVisibility(View.GONE);
        binding.products.setVisibility(View.VISIBLE);
    }
}
