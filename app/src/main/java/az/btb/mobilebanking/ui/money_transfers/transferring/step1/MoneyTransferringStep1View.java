package az.btb.mobilebanking.ui.money_transfers.transferring.step1;

import java.util.List;

import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import az.btb.mobilebanking.models.MoneyTransferCountry;
import az.btb.mobilebanking.models.MtPoint;
import az.btb.mobilebanking.models.MtPointCity;
import moxy.MvpView;

public interface MoneyTransferringStep1View extends MvpView {
    void showCards(List<BankCard> bankCards);
    void showAccounts(List<BankAccount> bankAccounts);
    void showError(String responseMessage);
    void showCountries(List<MoneyTransferCountry> moneyTransferCountries);
    void showToPoints(List<MtPoint> mtPoints);
    void showToPointCities(List<MtPointCity> mtPointCities);
}
