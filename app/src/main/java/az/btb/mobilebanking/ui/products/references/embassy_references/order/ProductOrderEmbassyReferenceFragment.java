package az.btb.mobilebanking.ui.products.references.embassy_references.order;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.Collections;
import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.adapters.SpinnerBankAccountsAdapter;
import az.btb.mobilebanking.adapters.SpinnerBankCardsAdapter;
import az.btb.mobilebanking.databinding.FragmentProductOrderEmbassyReferenceBinding;
import az.btb.mobilebanking.databinding.ProductOrderCompleteWindowBinding;
import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import az.btb.mobilebanking.models.EmbassyCountry;
import az.btb.mobilebanking.models.EmbassyPoint;
import az.btb.mobilebanking.models.ProductOrdererInfo;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import kotlin.collections.CollectionsKt;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.di.Scopes.SERVER_SCOPE;
import static az.btb.mobilebanking.utils.Constants.MoneySourceTypes.ACCOUNT;
import static az.btb.mobilebanking.utils.Constants.MoneySourceTypes.CARD;

public class ProductOrderEmbassyReferenceFragment extends Fragment<FragmentProductOrderEmbassyReferenceBinding> implements ProductOrderEmbassyReferenceView {

    private int productId;
    private String productHeaderName;

    private boolean isCashSelected = true;
    private List<BankCard> cards = null;
    private List<BankAccount> accounts = null;
    private boolean isAccountsSelected = false;

    private List<EmbassyCountry> embassyCountries;
    private List<EmbassyPoint> embassyPoints;

    @InjectPresenter ProductOrderEmbassyReferencePresenter presenter;

    @ProvidePresenter ProductOrderEmbassyReferencePresenter providePresenter() {
        return Toothpick.openScope(SERVER_SCOPE).getInstance(ProductOrderEmbassyReferencePresenter.class);
    }

    public ProductOrderEmbassyReferenceFragment() {
        super(R.layout.fragment_product_order_embassy_reference);
    }

    @NonNull
    public static ProductOrderEmbassyReferenceFragment getInstance(int productId, String productHeaderName) {
        Bundle b = new Bundle();
        b.putInt("productId", productId);
        b.putString("productHeaderName", productHeaderName);

        ProductOrderEmbassyReferenceFragment fragment = new ProductOrderEmbassyReferenceFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productId = requireArguments().getInt("productId");
        productHeaderName = requireArguments().getString("productHeaderName");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        isCashSelected = true;
        isAccountsSelected = false;

        binding.setProductHeaderName(productHeaderName);

        presenter.getEmbassyCountries();

        binding.goBack.setOnClickListener(v -> presenter.goBack());

        binding.fromCountry.setAdapter(
            new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                Collections.singletonList(getString(R.string.choose))
            )
        );
        binding.fromCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    presenter.getEmbassyPoints(embassyCountries.get(position - 1).getIdCountry());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        binding.fromPoint.setAdapter(
            new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                Collections.singletonList(getString(R.string.choose))
            )
        );

        setupViewParts();

        binding.order.setOnClickListener(v -> {
            ProductOrdererInfo payerInfo = new ProductOrdererInfo();
            if (isCashSelected)
                makeOrder(payerInfo, 1);
            else {
                if (isAccountsSelected) {
//                    if (binding.fromAccounts.getSelectedItemPosition() > 0) {
                        payerInfo.setProductPaymentType(ACCOUNT);
                        payerInfo.setFromIbanAccount(((BankAccount) binding.fromAccounts.getSelectedItem()).getIbanAccount());
                        makeOrder(payerInfo, 2);
//                    } else
//                        Utils.showSnackbar(binding.getRoot(), R.string.choose_account);
                } else {
//                    if (binding.fromCards.getSelectedItemPosition() > 0) {
                        payerInfo.setProductPaymentType(CARD);
                        payerInfo.setFromIdCard(((BankCard) binding.fromCards.getSelectedItem()).getIdCard());
                        makeOrder(payerInfo, 2);
//                    } else
//                        Utils.showSnackbar(binding.getRoot(), R.string.choose_card);
                }
            }
        });
    }

    private void makeOrder(ProductOrdererInfo payerInfo, int orderType) {
        final int countryPos = binding.fromCountry.getSelectedItemPosition();
        if (countryPos > 0) {
            final int pointPos = binding.fromPoint.getSelectedItemPosition();
            if (pointPos > 0) {
                final int duration = Utils.tryParseInt(binding.duration.getText().toString());
                if (duration > 0) {
                    binding.progressBar.setVisibility(View.VISIBLE);

                    Utils.modifyChildrenEnableStatus(binding.root, false);
                    presenter.makeEmbassyReferenceOrder(
                        payerInfo,
                        orderType,
                        embassyPoints.get(pointPos - 1).getIdEmbassy(),
                        productId,
                        duration
                    );
                } else
                    Utils.snackbar(binding.getRoot(), R.string.enter_duration_in_months);
            } else
                Utils.snackbar(binding.getRoot(), R.string.choose_extra_info);
        } else
            Utils.snackbar(binding.getRoot(), R.string.choose_country);
    }

    private void setupViewParts() {
        binding.fromCash.setOnClickListener(v -> {
            isCashSelected = true;
            isAccountsSelected = false;

            binding.fromCardsParent.setVisibility(View.GONE);
            binding.fromAccountsParent.setVisibility(View.GONE);

            binding.fromCash.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_cash_red, 0, 0);
            binding.fromCard.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_card, 0, 0);
            binding.fromAccount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bank_transfer, 0, 0);
        });

        binding.fromCard.setOnClickListener(v -> {
            isCashSelected = false;
            isAccountsSelected = false;

            binding.fromAccountsParent.setVisibility(View.GONE);

            if (cards == null) {
                showCards(obtainBankCards());
            } else
                binding.fromCardsParent.setVisibility(View.VISIBLE);

            binding.fromCash.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_cash, 0, 0);
            binding.fromCard.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_my_cards, 0, 0);
            binding.fromAccount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bank_transfer, 0, 0);
        });

        binding.fromAccount.setOnClickListener(v -> {
            isCashSelected = false;
            isAccountsSelected = true;

            binding.fromCardsParent.setVisibility(View.GONE);

            if (accounts == null) {
                showAccounts(obtainBankAccounts());
            } else
                binding.fromAccountsParent.setVisibility(View.VISIBLE);

            binding.fromCash.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_cash, 0, 0);
            binding.fromCard.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_card, 0, 0);
            binding.fromAccount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bank_transfer_red, 0, 0);
        });
    }

    @Override
    public void showError(@NonNull String msg) {
        Utils.modifyChildrenEnableStatus(binding.root, true);
        binding.progressBar.setVisibility(View.GONE);
        if (!msg.isEmpty())
            Utils.snackbar(binding.getRoot(), msg);
    }

    private void showCards(List<BankCard> bankCards) {
        cards = bankCards;

        binding.progressBar.setVisibility(View.GONE);
        binding.fromCards.setAdapter(new SpinnerBankCardsAdapter(requireContext(), bankCards));
        binding.fromCardsParent.setVisibility(View.VISIBLE);
    }

    private void showAccounts(List<BankAccount> bankAccounts) {
        accounts = bankAccounts;

        binding.progressBar.setVisibility(View.GONE);
        binding.fromAccounts.setAdapter(new SpinnerBankAccountsAdapter(requireContext(), bankAccounts));
        binding.fromAccountsParent.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmbassyCountries(List<EmbassyCountry> embassyCountries) {
        this.embassyCountries = embassyCountries;
        List<String> countryNames = CollectionsKt.map(embassyCountries, EmbassyCountry::getCountryName);
        countryNames.add(0, getString(R.string.choose));

        binding.fromCountry.setAdapter(
            new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                countryNames
            )
        );

        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showEmbassyPoints(List<EmbassyPoint> embassyPoints) {
        this.embassyPoints = embassyPoints;
        List<String> pointNames = CollectionsKt.map(embassyPoints, embassyPoint -> String.format(
            getString(R.string.embassy_point_format),
            embassyPoint.getCountryName(),
            embassyPoint.getEmbassyName(),
            embassyPoint.getEmbassyAddress()
        ));
        pointNames.add(0, getString(R.string.choose));

        binding.fromPoint.setAdapter(
            new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                pointNames
            )
        );

        binding.progressBar.setVisibility(View.GONE);
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
}
