package az.btb.mobilebanking.ui.money_transfers.transferring.step1;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.adapters.SpinnerBankAccountsAdapter;
import az.btb.mobilebanking.adapters.SpinnerBankCardsAdapter;
import az.btb.mobilebanking.databinding.FragmentMoneyTransferringStep1Binding;
import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import az.btb.mobilebanking.models.MoneyTransferCountry;
import az.btb.mobilebanking.models.MtPoint;
import az.btb.mobilebanking.models.MtPointCity;
import az.btb.mobilebanking.ui.money_transfers.MoneyTransfersFragment;
import az.btb.mobilebanking.utils.Constants;
import az.btb.mobilebanking.utils.Constants.Currency;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.di.Scopes.SERVER_SCOPE;
import static az.btb.mobilebanking.utils.Constants.MoneySourceTypes.ACCOUNT;
import static az.btb.mobilebanking.utils.Constants.MoneySourceTypes.CARD;
import static az.btb.mobilebanking.utils.Constants.MoneyTransferUniqueCodes.MONEX;
import static az.btb.mobilebanking.utils.Constants.MoneyTransferUniqueCodes.ZOLOTAYA_KORONA;

public class MoneyTransferringFragment extends Fragment<FragmentMoneyTransferringStep1Binding> implements MoneyTransferringStep1View {

    private List<BankAccount> accounts = null;
    private boolean isAccountsSelected = false;

    private List<MoneyTransferCountry> countries;
    private List<MtPoint> toPoints;
    private List<MtPointCity> toPointCities;

    @Constants.MoneyTransferUniqueCodes
    private String moneyTransferUniqueName = ZOLOTAYA_KORONA;

    @InjectPresenter MoneyTransferringPresenter presenter;

    @ProvidePresenter MoneyTransferringPresenter providePresenter() {
        return Toothpick.openScope(SERVER_SCOPE).getInstance(MoneyTransferringPresenter.class);
    }

    public MoneyTransferringFragment() {
        super(R.layout.fragment_money_transferring_step1);
    }

    @NonNull
    public static MoneyTransferringFragment getInstance() {
        return new MoneyTransferringFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setupViewParts();

        isAccountsSelected = false;

        showCards(obtainBankCards());

        binding.goBack.setOnClickListener(v -> presenter.goBack());

        binding.zolotayaKorona.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Utils.modifyChildrenEnableStatus(binding.root, false);

                moneyTransferUniqueName = ZOLOTAYA_KORONA;
                presenter.getCountriesBy(ZOLOTAYA_KORONA);
            }
        });
        binding.monex.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Utils.modifyChildrenEnableStatus(binding.root, false);

                moneyTransferUniqueName = MONEX;
                presenter.getCountriesBy(MONEX);
            }
        });

        binding.goToStep2.setOnClickListener(v -> {
            if (!isAccountsSelected /*&& binding.fromCards.getSelectedItemPosition() > 0*/) {
                final BankCard card = ((BankCard) binding.fromCards.getSelectedItem());
                final String cardName = card.getCardServiceName();
                checkGate2(
                    card.getIdCard(),
                    "",
                    cardName.substring(0, cardName.length() - 4),
                    String.format(
                        getString(R.string.operation_type_card_number),
                        cardName.substring(0, cardName.indexOf(' ')),
                        card.getCardNumber().substring(0, 4),
                        card.getCardNumber().substring(card.getCardNumber().length() - 4)
                    ),
                    card.getCardBalance(),
                    card.getCurrency()
                );
            } else if (isAccountsSelected /*&& binding.fromAccounts.getSelectedItemPosition() > 0*/) {
                final BankAccount account = ((BankAccount) binding.fromAccounts.getSelectedItem());
                checkGate2(
                    "",
                    account.getIbanAccount(),
                    Utils.getCurrency(account.getCurrency()),
                    account.getAccountNumber(),
                    account.getCurrency() == 0 ? account.getBalanceInLC() : account.getBalanceInFC(),
                    account.getCurrency()
                );
            } else
                Utils.snackbar(binding.root, R.string.choose_card_or_account);
        });

        binding.zolotayaKorona.setChecked(true);
    }

    private void checkGate2(
        final String cardId, final String accountIBAN,
        final String cardNameOrAccountCurrencyName,
        final String cardNameWithNumberOrAccountNumber,
        final BigDecimal cardOrAccountBalance,
        final @Currency int cardOrAccountCurrency
    ) {
        if (binding.fromCountries.getSelectedItemPosition() != 0) {
            final MoneyTransferCountry transferCountry = countries.get(binding.fromCountries.getSelectedItemPosition() - 1);
            if (binding.toPoints.getSelectedItemPosition() != 0) {
                MoneyTransfersFragment.MoneyTransferData.Builder moneyTransferDataBuilder =
                    MoneyTransfersFragment.MoneyTransferData.getInstance();

                moneyTransferDataBuilder
                    .setPaymentType(isAccountsSelected ? ACCOUNT : CARD)
                    .setIsCardOrAccount(isAccountsSelected)
                    .setCardId(cardId)
                    .setAccountIBAN(accountIBAN)
                    .setTransferUniqueName(moneyTransferUniqueName)
                    .setPointType(transferCountry.getMtPointsType())
                    .setFromPointData("", transferCountry.getCountryName(), transferCountry.getCountryISO3Code());

                if (transferCountry.getMtPointsType() == 2) {
                    MtPointCity toCity = toPointCities.get(binding.toPoints.getSelectedItemPosition() - 1);
                    moneyTransferDataBuilder.setToPointData(toCity.getIdCity(), toCity.getCityName());
                } else if (transferCountry.getMtPointsType() == 1) {
                    MtPoint toPoint = toPoints.get(binding.toPoints.getSelectedItemPosition() - 1);
                    moneyTransferDataBuilder.setToPointData(toPoint.getIdPoint(), toPoint.getCityName());
                }

                presenter.goToStep2(
                    moneyTransferDataBuilder
                        .setCardNameOrAccountCurrencyName(cardNameOrAccountCurrencyName)
                        .setCardNameWithNumberOrAccountNumber(cardNameWithNumberOrAccountNumber)
                        .setCardOrAccountBalance(cardOrAccountBalance)
                        .setCardOrAccountCurrency(cardOrAccountCurrency)
                        .build()
                );
            } else
                Utils.snackbar(binding.root, R.string.choose_to_point);
        } else
            Utils.snackbar(binding.root, R.string.choose_from_country);
    }

    private void setupViewParts() {
        /* BEGIN: From items */
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
            binding.fromAccountsParent.setVisibility(View.VISIBLE);

            if (accounts == null)
                showAccounts(obtainBankAccounts());

            binding.fromCard.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_card, 0, 0);
            binding.fromAccount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bank_transfer_red, 0, 0);
        });
        /* END: From items */
    }

    @Override
    public void showCards(List<BankCard> bankCards) {
        Utils.modifyChildrenEnableStatus(binding.root, true);
        binding.fromCards.setAdapter(new SpinnerBankCardsAdapter(requireContext(), bankCards));
        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showAccounts(List<BankAccount> bankAccounts) {
        accounts = bankAccounts;

        binding.fromAccounts.setAdapter(new SpinnerBankAccountsAdapter(requireContext(), bankAccounts));

        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showCountries(@NonNull List<MoneyTransferCountry> moneyTransferCountries) {
        countries = moneyTransferCountries;

        List<String> countries = new ArrayList<>();
        countries.add(getString(R.string.no_country));
        for (MoneyTransferCountry country : moneyTransferCountries)
            countries.add(country.getCountryName());

        ArrayAdapter<String> fromCountriesAdapter = new ArrayAdapter<>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            countries
        );
        binding.fromCountries.setAdapter(fromCountriesAdapter);
        binding.fromCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Utils.modifyChildrenEnableStatus(binding.root, false);

                    MoneyTransferCountry country = moneyTransferCountries.get(position - 1);
                    presenter.getToPointsBy(
                        country.getMtPointsType(),
                        moneyTransferUniqueName,
                        country.getCountryISO3Code()
                    );
                } else
                    setPointsDataToSpinner(Collections.singletonList(getString(R.string.choose_to_point)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        Utils.modifyChildrenEnableStatus(binding.root, true);

        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showToPoints(@NonNull List<MtPoint> mtPoints) {
        toPoints = mtPoints;

        List<String> data = new ArrayList<>();
        data.add(getString(R.string.choose_to_point));
        for (MtPoint mtPoint : mtPoints)
            data.add(mtPoint.getCityName());

        setPointsDataToSpinner(data);
    }

    @Override
    public void showToPointCities(@NonNull List<MtPointCity> mtPointCities) {
        toPointCities = mtPointCities;

        List<String> data = new ArrayList<>();
        data.add(getString(R.string.choose_to_point));
        for (MtPointCity mtPointCity : mtPointCities)
            data.add(mtPointCity.getCityName());

        setPointsDataToSpinner(data);
    }

    @Override
    public void showError(@NonNull String responseMessage) {
        binding.progressBar.setVisibility(View.GONE);
        Utils.modifyChildrenEnableStatus(binding.root, true);
        if (!responseMessage.isEmpty())
            Utils.snackbar(binding.root, responseMessage);
    }

    private void setPointsDataToSpinner(List<String> items) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            items
        );
        binding.toPoints.setAdapter(dataAdapter);

        Utils.modifyChildrenEnableStatus(binding.root, true);

        binding.progressBar.setVisibility(View.GONE);
    }
}
