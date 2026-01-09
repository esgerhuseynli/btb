package az.btb.mobilebanking.models;

import android.os.SystemClock;

import java.math.BigDecimal;

/**
 * This class holds is being used in HomeFragment.
 */
public class BankCardAndAccount {
    private long id;

    // Common elements both for card & account.
    private final boolean isCardItem;
    private int itemColor;
    private BigDecimal itemBalance;
    private String itemCurrency;
    private String itemNumber;
    private String itemAltName;

    // Card specific elements.
    private String cardId;
    private String cardExpireDate;
    private int cardType;
    private String cardFormattedName;
    private String cardNumber;

    private Object object;

    private BankCardAndAccount(boolean isCardItem) {
        this.isCardItem = isCardItem;
    }

    public long getId() {
        return id;
    }

    public boolean isCardItem() {
        return isCardItem;
    }

    public int getItemColor() {
        return itemColor;
    }

    public BigDecimal getItemBalance() {
        return itemBalance;
    }

    public String getItemCurrency() {
        return itemCurrency;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public String getCardId() {
        return cardId;
    }

    public String getCardFormattedName() {
        return cardFormattedName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getItemAltName() {
        return itemAltName;
    }

    public String getCardExpireDate() {
        return cardExpireDate;
    }

    public int getCardType() {
        return cardType;
    }

    public Object getObject() {
        return object;
    }

    public static class Builder {
        private BankCardAndAccount parentInstance;

        private Builder() {}

        public Builder(boolean isCardItem) {
            parentInstance = new BankCardAndAccount(isCardItem);
            parentInstance.id = SystemClock.elapsedRealtimeNanos();
        }

        public Builder setItemColor(int itemColor) {
            parentInstance.itemColor = itemColor;
            return this;
        }

        public Builder setItemBalance(BigDecimal itemBalance) {
            parentInstance.itemBalance = itemBalance;
            return this;
        }

        public Builder setItemCurrency(String itemCurrency) {
            parentInstance.itemCurrency = itemCurrency;
            return this;
        }

        public Builder setItemNumber(String itemNumber) {
            parentInstance.itemNumber = itemNumber;
            return this;
        }

        public Builder setCardId(String cardId) {
            parentInstance.cardId = cardId;
            return this;
        }

        public Builder setCardFormattedName(String cardFormattedName) {
            parentInstance.cardFormattedName = cardFormattedName;
            return this;
        }

        public Builder setCardNumber(String cardNumber) {
            parentInstance.cardNumber = cardNumber;
            return this;
        }

        public Builder setCardExpireDate(String cardExpireDate) {
            parentInstance.cardExpireDate = cardExpireDate;
            return this;
        }

        public Builder setCardType(int cardType) {
            parentInstance.cardType = cardType;
            return this;
        }

        public Builder setItemAltName(String itemAltName) {
            parentInstance.itemAltName = itemAltName;
            return this;
        }

        public Builder setObject(Object object) {
            parentInstance.object = object;
            return this;
        }

        public BankCardAndAccount build() {
            return parentInstance;
        }
    }
}
