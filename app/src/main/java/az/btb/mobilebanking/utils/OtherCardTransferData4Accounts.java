package az.btb.mobilebanking.utils;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Card ve Accountlar arasi kocurmeler ucun
 * istifade edile bilecek model.
 */
public class OtherCardTransferData4Accounts implements Serializable {
    public boolean isFromCard;
    public boolean isToCard;
    public String operationType = "";
    public String sourceCardId = "";
    public String sourceCardAltName = "";
    public String sourceCardFormattedNumber = "";
    public BigDecimal sourceCardBalance = BigDecimal.ZERO;
    public String destinationCardId = "";
    public String destinationCardNumber = "";
    public BigDecimal amount = BigDecimal.ZERO;
    public String amountCurrency = "";
    public String notes = "";
    public String sourceAccountIban = "";
    public String sourceAccountAltName = "";
    public String destinationAccountIban = "";
    public BigDecimal sourceAccountBalance = BigDecimal.ZERO;
}
