package az.btb.mobilebanking.ui.my_items.my_cards;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.adapters.ItemsAdapter;
import az.btb.mobilebanking.databinding.FragmentMyItemsBinding;
import az.btb.mobilebanking.databinding.MyCardsListItemBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.BankCard;
import az.btb.mobilebanking.ui.my_items.MyItemsView;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.ItemPropsBinder;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class MyCardsFragment extends Fragment<FragmentMyItemsBinding> implements MyItemsView<BankCard> {

    public MyCardsFragment() {
        super(R.layout.fragment_my_items);
    }

    public static MyCardsFragment getInstance() {
        return new MyCardsFragment();
    }

    @InjectPresenter MyCardsPresenter presenter;

    @ProvidePresenter MyCardsPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(MyCardsPresenter.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.setPageTitle(getString(R.string.nav_menu_item_my_cards));

        binding.goBack.setOnClickListener(v -> presenter.goBack());

        presenter.getMyCardsData();
    }

    @Override
    public void showItemsList(@NonNull List<BankCard> list) {
        if (list.size() == 0) {
            binding.myItemsList.setVisibility(View.GONE);
            binding.noItem.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
            binding.setMissingItem(getString(R.string.no_my_cards));
            return;
        }

        refreshBankCards(list);

        final ItemPropsBinder<MyCardsListItemBinding, BankCard> itemPropsBinder = (binding, bankCard) -> {
            final String cardFullName = bankCard.getCardServiceName(); // MasterCard Standard AZN
            final String cardCurrency = Utils.getCurrency(bankCard.getCurrency()); // AZN
            final String cardName = cardFullName.substring(0, cardFullName.indexOf(' ')); // MasterCard

            final String cardNumberFirst4Digit = bankCard.getCardNumber().substring(0, 4);
            final String cardNumberLast4Digit = bankCard.getCardNumber().substring(7);

            binding.setCardAltName(bankCard.getCardAltName());
            binding.setCardType4CardNumber(cardName);
            binding.setCardNumberFirst4Digit(cardNumberFirst4Digit);
            binding.setCardNumberLast4Digit(cardNumberLast4Digit);
            binding.setCardBalance(bankCard.getCardBalance());
            binding.setCardCurrency(cardCurrency);

            binding.getRoot().setOnClickListener(v -> presenter.showMyCardInfo(bankCard));
        };

        final ItemsAdapter<MyCardsListItemBinding, BankCard> adapter = new ItemsAdapter<>(
            R.layout.my_cards_list_item, list, itemPropsBinder
        );

        binding.myItemsList.setAdapter(adapter);

        binding.progressBar.setVisibility(View.GONE);
        binding.noItem.setVisibility(View.GONE);
        binding.myItemsList.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String msg) {
        binding.progressBar.setVisibility(View.GONE);
        if (!msg.isEmpty())
            Utils.snackbar(binding.getRoot(), msg);
    }
}
