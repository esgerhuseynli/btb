package az.btb.mobilebanking.utils;

import java.math.BigDecimal;

public class ExchangeRateData {
    public int currency;
    public BigDecimal buyRate;
    public BigDecimal sellRate;

    public ExchangeRateData(int currency, BigDecimal buyRate, BigDecimal sellRate) {
        this.currency = currency;
        this.buyRate = buyRate;
        this.sellRate = sellRate;
    }
}
