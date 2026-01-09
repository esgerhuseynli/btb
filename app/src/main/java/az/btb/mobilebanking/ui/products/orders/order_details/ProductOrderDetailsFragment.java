package az.btb.mobilebanking.ui.products.orders.order_details;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentProductOrderDetailsBinding;
import az.btb.mobilebanking.models.ProductOrder;
import az.btb.mobilebanking.utils.Constants.ProductTypes;
import az.btb.mobilebanking.utils.Fragment;
import kotlin.Pair;
import moxy.MvpView;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.di.Scopes.SERVER_SCOPE;

public class ProductOrderDetailsFragment extends Fragment<FragmentProductOrderDetailsBinding> implements MvpView {

    private ProductOrder order;

    @InjectPresenter ProductOrderDetailsPresenter presenter;

    @ProvidePresenter ProductOrderDetailsPresenter providePresenter() {
        return Toothpick.openScope(SERVER_SCOPE).getInstance(ProductOrderDetailsPresenter.class);
    }

    @NonNull
    public static ProductOrderDetailsFragment getInstance(ProductOrder order) {
        Bundle b = new Bundle();
        b.putSerializable("order", order);

        ProductOrderDetailsFragment fragment = new ProductOrderDetailsFragment();
        fragment.setArguments(b);
        return fragment;
    }

    public ProductOrderDetailsFragment() {
        super(R.layout.fragment_product_order_details);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        order = (ProductOrder) getArguments().getSerializable("order");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.setOrder(order);
        binding.executePendingBindings();

        switch (order.getProductType()) {
            case ProductTypes.LOAN:
                binding.orderAmount.setText(String.format(getString(R.string.my_items_item_balance), order.getLoanAmount(), "AZN"));
                binding.orderPercent.setText(String.format(getString(R.string.float_percent), order.getLoanPercent()));
                binding.orderDuration.setText(order.getLoanTerm() + "");
                binding.setProductType(getString(R.string.loan));
                binding.loanPurpose.setVisibility(View.VISIBLE);
                binding.orderPercentParent.setVisibility(View.VISIBLE);
                break;
            case ProductTypes.DEPOSIT:
                binding.orderAmount.setText(String.format(getString(R.string.my_items_item_balance), order.getDepositAmount(), "AZN"));
                binding.orderPercent.setText(String.format(getString(R.string.float_percent), order.getDepositPercent()));
                binding.orderDuration.setText(order.getDepositTerm() + "");
                binding.setProductType(getString(R.string.deposit));
                binding.orderPercentParent.setVisibility(View.VISIBLE);
                break;
            case ProductTypes.PLASTIC_CARD:
                binding.setProductType(getString(R.string.plastic_card));
                binding.orderDuration.setText(order.getPlasticCardTerm() + "");
                binding.orderPaymentType.setVisibility(View.VISIBLE);
                break;
            case ProductTypes.EMBASSY_REFERENCE:
                binding.setProductType(getString(R.string.embassy_reference));
                binding.orderDuration.setText(order.getEmbassyReferenceTerm() + "");
                binding.orderPaymentType.setVisibility(View.VISIBLE);
                break;
            case ProductTypes.FINANCIAL_REFERENCE:
                binding.setProductType(getString(R.string.financial_reference));
                binding.orderDuration.setText(order.getFinancialReferenceTerm() + "");
                binding.orderPaymentType.setVisibility(View.VISIBLE);
                break;
        }

        // show button IF AND ONLY IF product order status is "Registered" and type is neither Loan nor Deposit.
        binding.setIsPayable(
            order.getOrderStatus() == 1 &&
            order.getProductType() != ProductTypes.LOAN &&
            order.getProductType() != ProductTypes.DEPOSIT
        );

        final Pair<Integer, Integer> orderStatusIndicators = getOrderStatusIndicator(order.getOrderStatus());
        binding.orderStatus.setText(orderStatusIndicators.component1());
        binding.orderStatus.setTextColor(getResources().getColor(orderStatusIndicators.component2()));

        binding.goBack.setOnClickListener(v -> presenter.goBack());
        binding.makeOrder.setOnClickListener(v -> presenter.goToPayment(order));
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
