package az.btb.mobilebanking.ui.products.loans.order;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.math.BigDecimal;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentProductOrderLoanBinding;
import az.btb.mobilebanking.databinding.ProductOrderCompleteWindowBinding;
import az.btb.mobilebanking.models.ProductConditions;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Product;
import az.btb.mobilebanking.utils.Utils;
import kotlin.collections.CollectionsKt;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.di.Scopes.SERVER_SCOPE;
import static java.lang.Math.pow;

public class ProductOrderLoanFragment extends Fragment<FragmentProductOrderLoanBinding> implements ProductOrderLoanView {

    private int productId;
    private String productHeaderName;
    private Product.OrderData orderData;
    private ProductConditions productOrderDetails;

    private static final int GUZESHT_MUDDETI = 0;

    private double kreditMeblegi = 0;
    private int muddet = 0;
    private float faizDerecesi = 0f;
    private int selectedCurrency;

    @InjectPresenter ProductOrderLoanPresenter presenter;

    @ProvidePresenter ProductOrderLoanPresenter providePresenter() {
        return Toothpick.openScope(SERVER_SCOPE).getInstance(ProductOrderLoanPresenter.class);
    }

    public ProductOrderLoanFragment() {
        super(R.layout.fragment_product_order_loan);
    }

    @NonNull
    public static ProductOrderLoanFragment getInstance(int productId, String productHeaderName, @NonNull Product.OrderData orderData) {
        Bundle b = new Bundle();
        b.putInt("productId", productId);
        b.putString("productHeaderName", productHeaderName);
        b.putParcelable("orderData", orderData);

        ProductOrderLoanFragment fragment = new ProductOrderLoanFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productId = requireArguments().getInt("productId");
        productHeaderName = requireArguments().getString("productHeaderName");
        orderData = requireArguments().getParcelable("orderData");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.setProductHeaderName(productHeaderName);

        binding.monthsSelector.addOnChangeListener((slider, value, fromUser) -> {
            muddet = (int) value;
            binding.setMonths((int) value);
            calculateAndSetAmounts();
        });
    
        binding.percentSelector.addOnChangeListener((slider, value, fromUser) -> {
            faizDerecesi = value;
            binding.setPercent(value);
            calculateAndSetAmounts();
        });
        
        binding.currencies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCurrency = orderData.availableCurrencies.get(position);
                if (orderData.currencyAssociatedProductCondition.containsKey(selectedCurrency)) {
                    productOrderDetails = orderData.currencyAssociatedProductCondition.get(selectedCurrency);
                    
                    binding.monthsSelector.setValue(productOrderDetails.getMinimalTerm());
                    binding.monthsSelector.setValueFrom(productOrderDetails.getMinimalTerm());
                    binding.monthsSelector.setValueTo(productOrderDetails.getMaximalTerm());
                    
                    binding.percentSelector.setValue(productOrderDetails.getMinimalPercent());
                    binding.percentSelector.setValueFrom(productOrderDetails.getMinimalPercent());
                    if (productOrderDetails.getMinimalPercent() == productOrderDetails.getMaximalPercent())
                        binding.percentSelector.setValueTo(productOrderDetails.getMaximalPercent() + 0.00001f);
                    else
                        binding.percentSelector.setValueTo(productOrderDetails.getMaximalPercent());
                    binding.percentSelector.setStepSize(productOrderDetails.getPercentStepSize());
    
                    calculateAndSetAmounts();
                }
            }
        
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        binding.currencies.setAdapter(
            new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                CollectionsKt.map(orderData.availableCurrencies, Utils::getCurrency)
            )
        );

        binding.depositAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    kreditMeblegi = Double.parseDouble(s.toString());
                } catch (NumberFormatException ignored) {
                } finally {
                    calculateAndSetAmounts();
                }
            }
        });

        binding.goBack.setOnClickListener(v -> presenter.goBack());

        binding.order.setOnClickListener(v -> {
            BigDecimal userEnteredAmount = BigDecimal.ZERO;
            try {
                userEnteredAmount = new BigDecimal(binding.depositAmount.getText().toString());
            } catch (NumberFormatException ignored) {
            }

            if (Utils.lt(userEnteredAmount, productOrderDetails.getMinimalAmount())) {
                Utils.snackbar(
                    binding.root,
                    String.format(
                        getString(R.string.min_transfer_amount_error),
                        productOrderDetails.getMinimalAmount(),
                        Utils.getCurrency(productOrderDetails.getCurrency())
                    )
                );
            } else if (Utils.gt(userEnteredAmount, productOrderDetails.getMaximalAmount())) {
                Utils.snackbar(
                    binding.root,
                    String.format(
                        getString(R.string.max_transfer_amount_error),
                        productOrderDetails.getMaximalAmount(),
                        Utils.getCurrency(productOrderDetails.getCurrency())
                    )
                );
            } else {
                if (faizDerecesi > 0) {
                    if (muddet > 0) {
                        binding.progressBar.setVisibility(View.VISIBLE);
                        Utils.modifyChildrenEnableStatus(binding.root, false);

                        presenter.makeLoanOrder(
                            productId,
                            userEnteredAmount,
                            (int) binding.monthsSelector.getValue(),
                            binding.percentSelector.getValue(),
                            binding.loanPurpose.getText().toString(),
                            selectedCurrency
                        );
                    } else
                        Utils.snackbar(binding.getRoot(), R.string.enter_duration_in_months);
                } else
                    Utils.snackbar(binding.getRoot(), R.string.enter_percent);
            }
        });
    }

    @Override
    public void showError(@NonNull String msg) {
        Utils.modifyChildrenEnableStatus(binding.root, true);
        binding.progressBar.setVisibility(View.GONE);
        if (!msg.isEmpty())
            Utils.snackbar(binding.getRoot(), msg);
    }

    @Override
    public void showOrderResult(int plasticCardOrderStatus) {
        Utils.modifyChildrenEnableStatus(binding.root, true);
        binding.progressBar.setVisibility(View.GONE);

        final AlertDialog dialog = new AlertDialog.Builder(requireContext()).create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        ProductOrderCompleteWindowBinding windowBinding =
                ProductOrderCompleteWindowBinding.inflate(getLayoutInflater());

        windowBinding.finish.setOnClickListener(v -> {
            dialog.dismiss();
            presenter.goToProductsScreen();
        });

        dialog.setView(windowBinding.getRoot());
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void calculateAndSetAmounts() {
        // KM*(FD/12/100*(1+FD/12/100)^(M - GM)) / ((1+FD/12/100)^(M - GM) - 1)
        double percentInCommon = (double) faizDerecesi / Double.parseDouble("12") / Double.parseDouble("100");
        int durationInCommon = muddet - GUZESHT_MUDDETI;

        double numerator = percentInCommon * pow(1 + percentInCommon, durationInCommon);

        double denominator = pow(1 + percentInCommon, durationInCommon) - 1;

        double monthlyPayment = kreditMeblegi * numerator / (denominator == 0 ? 1 : denominator);
        double totalPayment = monthlyPayment * ((double) muddet);

        binding.setMonthly(monthlyPayment);
        binding.setTotal(totalPayment);
        binding.setCurrency(Utils.getCurrency(selectedCurrency));
    }
}
