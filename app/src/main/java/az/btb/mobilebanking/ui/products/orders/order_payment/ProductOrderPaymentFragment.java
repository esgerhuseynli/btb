package az.btb.mobilebanking.ui.products.orders.order_payment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.adapters.SpinnerBankAccountsAdapter;
import az.btb.mobilebanking.adapters.SpinnerBankCardsAdapter;
import az.btb.mobilebanking.databinding.FragmentProductOrderPaymentBinding;
import az.btb.mobilebanking.databinding.ProductOrderCompleteWindowBinding;
import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import az.btb.mobilebanking.models.ProductOrder;
import az.btb.mobilebanking.models.ProductOrdererInfo;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.di.Scopes.SERVER_SCOPE;
import static az.btb.mobilebanking.utils.Constants.MoneySourceTypes.ACCOUNT;
import static az.btb.mobilebanking.utils.Constants.MoneySourceTypes.CARD;

public class ProductOrderPaymentFragment extends Fragment<FragmentProductOrderPaymentBinding> implements ProductOrderPaymentView {

    private ProductOrder order;

    private List<BankAccount> accounts = null;
    private boolean isAccountsSelected = false;

    @InjectPresenter ProductOrderPaymentPresenter presenter;

    @ProvidePresenter ProductOrderPaymentPresenter providePresenter() {
        return Toothpick.openScope(SERVER_SCOPE).getInstance(ProductOrderPaymentPresenter.class);
    }

    @NonNull
    public static ProductOrderPaymentFragment getInstance(ProductOrder order) {
        Bundle b = new Bundle();
        b.putSerializable("order", order);

        ProductOrderPaymentFragment fragment = new ProductOrderPaymentFragment();
        fragment.setArguments(b);
        return fragment;
    }

    public ProductOrderPaymentFragment() {
        super(R.layout.fragment_product_order_payment);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        order = (ProductOrder) getArguments().getSerializable("order");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        isAccountsSelected = false;

        setupViewParts();

        presenter.getBankCards();
        Utils.modifyChildrenEnableStatus(binding.fromItems, false);

        binding.setProductHeaderName(order.getProductHeaderName());

        binding.goBack.setOnClickListener(v -> presenter.goBack());

        binding.order.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            ProductOrdererInfo payerInfo = new ProductOrdererInfo();
            if (isAccountsSelected) {
                payerInfo.setFromIbanAccount(((BankAccount) binding.fromAccounts.getSelectedItem()).getIbanAccount());
                payerInfo.setProductPaymentType(ACCOUNT);
            } else {
                payerInfo.setFromIdCard(((BankCard) binding.fromCards.getSelectedItem()).getIdCard());
                payerInfo.setProductPaymentType(CARD);
            }

            Utils.modifyChildrenEnableStatus(binding.root, false);
            presenter.pay(payerInfo, order.getIdOrder());
        });
    }

    private void setupViewParts() {
        binding.fromCard.setOnClickListener(v -> {
            isAccountsSelected = false;

            binding.fromCardsParent.setVisibility(View.VISIBLE);
            binding.fromAccountsParent.setVisibility(View.GONE);

            binding.fromCard.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_my_cards, 0, 0);
            binding.fromAccount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bank_transfer, 0, 0);
        });

        binding.fromAccount.setOnClickListener(v -> {
            isAccountsSelected = true;

            binding.fromCardsParent.setVisibility(View.GONE);

            if (accounts == null) {
                Utils.modifyChildrenEnableStatus(binding.fromItems, false);
                binding.progressBar.setVisibility(View.VISIBLE);
                presenter.getBankAccounts();
            } else
                binding.fromAccountsParent.setVisibility(View.VISIBLE);

            binding.fromCard.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_card, 0, 0);
            binding.fromAccount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bank_transfer_red, 0, 0);
        });
    }

    @Override
    public void showError(@NonNull String msg) {
        binding.progressBar.setVisibility(View.GONE);
        if (!msg.isEmpty())
            Utils.snackbar(binding.getRoot(), msg);
    }

    @Override
    public void showCards(@NonNull List<BankCard> bankCards) {
        binding.progressBar.setVisibility(View.GONE);
        binding.fromCards.setAdapter(new SpinnerBankCardsAdapter(requireContext(), bankCards));
        binding.fromCardsParent.setVisibility(View.VISIBLE);

        Utils.modifyChildrenEnableStatus(binding.fromItems, true);
    }

    @Override
    public void showAccounts(@NonNull List<BankAccount> bankAccounts) {
        accounts = bankAccounts;

        binding.progressBar.setVisibility(View.GONE);
        binding.fromAccounts.setAdapter(new SpinnerBankAccountsAdapter(requireContext(), bankAccounts));
        binding.fromAccountsParent.setVisibility(View.VISIBLE);

        Utils.modifyChildrenEnableStatus(binding.fromItems, true);
    }

    @Override
    public void showOrderResult(int plasticCardOrderStatus) {
        Utils.modifyChildrenEnableStatus(binding.root, false);

        binding.progressBar.setVisibility(View.GONE);

        final AlertDialog dialog = new AlertDialog.Builder(requireContext()).create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        ProductOrderCompleteWindowBinding windowBinding =
                ProductOrderCompleteWindowBinding.inflate(getLayoutInflater());

        if (plasticCardOrderStatus == 2) {
            windowBinding.successText.setText(R.string.failed_order);
            windowBinding.successText.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_error_big, 0, 0);
            windowBinding.finishText.setText(R.string.close);
        }

        windowBinding.finish.setOnClickListener(v -> {
            dialog.dismiss();
            presenter.goToProductsScreen();
        });

        dialog.setView(windowBinding.getRoot());
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
