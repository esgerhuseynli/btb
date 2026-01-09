package az.btb.mobilebanking.ui.products;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentProductsBinding;
import az.btb.mobilebanking.utils.Fragment;
import moxy.MvpView;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.di.Scopes.SERVER_SCOPE;

public class ProductsFragment extends Fragment<FragmentProductsBinding> implements MvpView {

    @InjectPresenter ProductsPresenter presenter;

    @ProvidePresenter ProductsPresenter providePresenter() {
        return Toothpick.openScope(SERVER_SCOPE).getInstance(ProductsPresenter.class);
    }

    @NonNull
    public static ProductsFragment getInstance() {
        return new ProductsFragment();
    }

    public ProductsFragment() {
        super(R.layout.fragment_products);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.goBack.setOnClickListener(v -> presenter.goBack());
        binding.myProductOrders.setOnClickListener(v -> presenter.goToProductOrdersScreen());
        binding.plasticCards.setOnClickListener(v -> presenter.goToProductCardsScreen());
        binding.loans.setOnClickListener(v -> presenter.goToProductLoansScreen());
        binding.deposits.setOnClickListener(v -> presenter.goToProductDepositsScreen());
        binding.references.setOnClickListener(v -> presenter.goToProductReferencesScreen());
    }
}
