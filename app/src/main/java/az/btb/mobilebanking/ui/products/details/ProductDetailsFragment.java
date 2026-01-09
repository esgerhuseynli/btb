package az.btb.mobilebanking.ui.products.details;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentProductDetailsBinding;
import az.btb.mobilebanking.utils.Constants;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Product;
import az.btb.mobilebanking.utils.Utils;
import moxy.MvpView;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.di.Scopes.SERVER_SCOPE;

public class ProductDetailsFragment extends Fragment<FragmentProductDetailsBinding> implements MvpView {

    private Product product;

    @InjectPresenter ProductDetailsPresenter presenter;

    @ProvidePresenter ProductDetailsPresenter providePresenter() {
        return Toothpick.openScope(SERVER_SCOPE).getInstance(ProductDetailsPresenter.class);
    }

    public ProductDetailsFragment() {
        super(R.layout.fragment_product_details);
    }

    @NonNull
    public static ProductDetailsFragment getInstance(Product product) {
        Bundle b = new Bundle();
        b.putParcelable("product", product);

        ProductDetailsFragment fragment = new ProductDetailsFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        product = requireArguments().getParcelable("product");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.setProduct(product);
        binding.executePendingBindings();

        binding.goBack.setOnClickListener(v -> presenter.goBack());
        Utils.setImageToImageView(binding.productImage, product.image);
        switch (product.type) {
            case Constants.ProductTypes.PLASTIC_CARD:
                binding.order.setOnClickListener(v -> presenter.goToOrderPlasticCardScreen(product.id, product.headerName));
                break;
            case Constants.ProductTypes.LOAN:
                binding.order.setOnClickListener(v -> presenter.goToOrderLoanScreen(product.id, product.headerName, product.orderData));
                break;
            case Constants.ProductTypes.DEPOSIT:
                binding.order.setOnClickListener(v -> presenter.goToOrderDepositScreen(product.id, product.headerName, product.orderData));
                break;
            case Constants.ProductTypes.EMBASSY_REFERENCE:
                binding.order.setOnClickListener(v -> presenter.goToOrderEmbassyReferenceScreen(product.id, product.headerName));
                break;
            case Constants.ProductTypes.FINANCIAL_REFERENCE:
                binding.order.setOnClickListener(v -> presenter.goToOrderFinancialReferenceScreen(product.id, product.headerName));
                break;
        }
    }
}
