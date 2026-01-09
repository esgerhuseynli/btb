package az.btb.mobilebanking.ui.money_transfers;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.math.BigDecimal;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentMoneyTransfersBinding;
import az.btb.mobilebanking.utils.Constants.Currency;
import az.btb.mobilebanking.utils.Constants.MoneySourceTypes;
import az.btb.mobilebanking.utils.Constants.MoneyTransferPointTypes;
import az.btb.mobilebanking.utils.Constants.MoneyTransferUniqueCodes;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.MvpView;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.di.Scopes.SERVER_SCOPE;

public class MoneyTransfersFragment extends Fragment<FragmentMoneyTransfersBinding> implements MvpView {

    @InjectPresenter MoneyTransfersPresenter presenter;

    @ProvidePresenter MoneyTransfersPresenter providePresenter() {
        return Toothpick.openScope(SERVER_SCOPE).getInstance(MoneyTransfersPresenter.class);
    }

    public MoneyTransfersFragment() {
        super(R.layout.fragment_money_transfers);
    }

    @NonNull
    public static MoneyTransfersFragment getInstance() {
        return new MoneyTransfersFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.goBack.setOnClickListener(v -> presenter.goBack());
        binding.transfer.setOnClickListener(v -> presenter.goToMoneyTransferring());
        binding.receive.setOnClickListener(v -> presenter.goToReceiveScreen());
        binding.history.setOnClickListener(v -> presenter.goToHistoryScreen());
        binding.search.setOnClickListener(v -> presenter.goToSearchScreen());
    }

    public static final class MoneyTransferData implements Serializable {

        private MoneyTransferData() {
            // Keep private.
        }

        @NonNull
        public static Builder getInstance() {
            return new Builder();
        }

        private boolean isAccount;

        @MoneySourceTypes
        private int transferPaymentType;

        private CardOrAccountData cardOrAccountData;

        @MoneyTransferPointTypes
        private int pointType;

        private PointData fromPointData;
        private PointData toPointData;

        @MoneyTransferUniqueCodes
        private String transferUniqueName;

        public boolean getIsAccount() {
            return isAccount;
        }

        public int getTransferPaymentType() {
            return transferPaymentType;
        }

        public CardOrAccountData getCardOrAccountData() {
            return cardOrAccountData;
        }

        public int getPointType() {
            return pointType;
        }

        public PointData getFromPointData() {
            return fromPointData;
        }

        public PointData getToPointData() {
            return toPointData;
        }

        public String getTransferUniqueName() {
            return transferUniqueName;
        }

        public static final class Builder {
            private final MoneyTransferData moneyTransferData;

            private Builder() {
                moneyTransferData = new MoneyTransferData();
                moneyTransferData.cardOrAccountData = new CardOrAccountData();
            }

            public Builder setPaymentType(@MoneySourceTypes final int paymentType) {
                moneyTransferData.transferPaymentType = paymentType;
                return this;
            }

            public Builder setIsCardOrAccount(final boolean isAccountSelected) {
                moneyTransferData.isAccount = isAccountSelected;
                moneyTransferData.cardOrAccountData.icon =
                    isAccountSelected ? R.drawable.ic_my_bank_account : R.drawable.ic_my_cards;
                return this;
            }

            public Builder setCardId(final String cardId) {
                moneyTransferData.cardOrAccountData.cardId = cardId;
                return this;
            }

            public Builder setAccountIBAN(final String accountIBAN) {
                moneyTransferData.cardOrAccountData.accountIBAN = accountIBAN;
                return this;
            }

            public Builder setCardNameOrAccountCurrencyName(final String cardServiceNameOrAccountCurrency) {
                moneyTransferData.cardOrAccountData.cardNameOrAccountCurrencyName = cardServiceNameOrAccountCurrency;
                return this;
            }

            public Builder setCardNameWithNumberOrAccountNumber(final String cardNameWithNumberOrAccountNumber) {
                moneyTransferData.cardOrAccountData.cardNameWithNumberOrAccountNumber = cardNameWithNumberOrAccountNumber;
                return this;
            }

            public Builder setCardOrAccountBalance(final BigDecimal balance) {
                moneyTransferData.cardOrAccountData.balance = balance;
                return this;
            }

            public Builder setCardOrAccountCurrency(@Currency final int currency) {
                moneyTransferData.cardOrAccountData.currency = currency;
                return this;
            }

            public Builder setTransferUniqueName(@MoneyTransferUniqueCodes final String transferUniqueName) {
                moneyTransferData.transferUniqueName = transferUniqueName;
                return this;
            }

            public Builder setPointType(@MoneyTransferPointTypes final int pointType) {
                moneyTransferData.pointType = pointType;
                return this;
            }

            public Builder setFromPointData(final String pointId, final String pointName, final String countryIso) {
                moneyTransferData.fromPointData = new PointData(pointId, pointName, countryIso);
                return this;
            }

            public Builder setToPointData(final String pointId, final String pointName) {
                moneyTransferData.toPointData = new PointData(pointId, pointName, "");
                return this;
            }

            public MoneyTransferData build() {
                return moneyTransferData;
            }
        }

        public static final class CardOrAccountData implements Serializable {
            private @DrawableRes int icon;
            private String cardId;
            private String accountIBAN;
            private @Currency int currency;
            private String cardNameOrAccountCurrencyName;
            private String cardNameWithNumberOrAccountNumber;
            private BigDecimal balance;

            public @DrawableRes int getIcon() {
                return icon;
            }

            @NonNull
            public String getCurrency() {
                return Utils.getCurrency(currency);
            }

            public String getCardId() {
                return cardId;
            }

            public String getAccountIBAN() {
                return accountIBAN;
            }

            public String getCardNameOrAccountCurrencyName() {
                return cardNameOrAccountCurrencyName;
            }

            public String getCardNameWithNumberOrAccountNumber() {
                return cardNameWithNumberOrAccountNumber;
            }

            public BigDecimal getBalance() {
                return balance;
            }
        }

        public static final class PointData implements Serializable {
            private final String pointId, pointName, countryIso;

            private PointData(final String id, final String name, String countryIso) {
                pointId = id;
                pointName = name;
                this.countryIso = countryIso;
            }

            public String getPointId() {
                return pointId;
            }

            public String getPointName() {
                return pointName;
            }

            public String getCountryIso() {
                return countryIso;
            }
        }
    }

    public static final class MoneyTransferReceiverData implements Serializable {
        private String name;
        private String surname;
        private String fatherName;
        private String phoneNumber;
        private BigDecimal amount;
        private int currency;

        private MoneyTransferReceiverData() {
            // keep private.
        }

        @NonNull
        public static Builder getInstance() {
            return new Builder();
        }

        public String getName() {
            return name;
        }

        public String getSurname() {
            return surname;
        }

        public String getFatherName() {
            return fatherName;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public int getCurrency() {
            return currency;
        }

        public static final class Builder {
            private final MoneyTransferReceiverData moneyTransferReceiverData;

            private Builder() {
                moneyTransferReceiverData = new MoneyTransferReceiverData();
            }

            public Builder setName(final String name) {
                moneyTransferReceiverData.name = name;
                return this;
            }

            public Builder setSurname(final String surname) {
                moneyTransferReceiverData.surname = surname;
                return this;
            }

            public Builder setFathername(final String fatherName) {
                moneyTransferReceiverData.fatherName = fatherName;
                return this;
            }

            public Builder setPhoneNumber(final String phoneNumber) {
                moneyTransferReceiverData.phoneNumber = phoneNumber;
                return this;
            }

            public Builder setTransferAmount(final BigDecimal amount) {
                moneyTransferReceiverData.amount = amount;
                return this;
            }

            public Builder setTransferCurrency(final int currency) {
                moneyTransferReceiverData.currency = currency;
                return this;
            }

            public MoneyTransferReceiverData build() {
                return moneyTransferReceiverData;
            }
        }
    }
}
