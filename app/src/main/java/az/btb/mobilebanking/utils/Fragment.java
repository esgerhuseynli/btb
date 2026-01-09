package az.btb.mobilebanking.utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import java.util.List;

import az.btb.mobilebanking.App;
import az.btb.mobilebanking.MainActivity;
import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import kotlin.collections.CollectionsKt;
import moxy.MvpAppCompatFragment;

public class Fragment<Binding extends ViewDataBinding> extends MvpAppCompatFragment {

    protected Binding binding;
    private @LayoutRes final int layoutId;
    private final boolean mShouldSaveLastUseTimestamp;

    public Fragment(@LayoutRes int layoutRes) {
        this(layoutRes, true);
    }

    public Fragment(@LayoutRes int layoutRes, final boolean shouldSaveLastUseTimestamp) {
        layoutId = layoutRes;
        mShouldSaveLastUseTimestamp = shouldSaveLastUseTimestamp;
    }

    @CallSuper
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false);
        return binding.getRoot();
    }

    protected void refreshBankCardsAndAccounts() {
        ((MainActivity) requireActivity()).refreshBankCardsAndAccounts();
    }

    protected List<BankCard> obtainBankCards() {
        List<BankCard> cards = ((App) requireActivity().getApplication()).BANK_CARDS;
        if (cards == null)
            return CollectionsKt.emptyList();
        else
            return cards;
    }

    protected void refreshBankCards(@NonNull final List<BankCard> bankCards) {
        ((App) requireActivity().getApplication()).BANK_CARDS = bankCards;
    }

    protected List<BankAccount> obtainBankAccounts() {
        List<BankAccount> accounts = ((App) requireActivity().getApplication()).BANK_ACCOUNTS;
        if (accounts == null)
            return CollectionsKt.emptyList();
        else
            return accounts;
    }

    protected void refreshBankAccounts(@NonNull final List<BankAccount> bankAccounts) {
        ((App) requireActivity().getApplication()).BANK_ACCOUNTS = bankAccounts;
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mShouldSaveLastUseTimestamp) {
            ((MainActivity) requireActivity()).saveSessionTime();
        }
    }
}
