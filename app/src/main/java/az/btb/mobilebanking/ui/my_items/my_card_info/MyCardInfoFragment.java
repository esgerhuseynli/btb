package az.btb.mobilebanking.ui.my_items.my_card_info;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.ChangeItemAltNameDialogBinding;
import az.btb.mobilebanking.databinding.FragmentMyCardInfoBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.BankCard;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class MyCardInfoFragment extends Fragment<FragmentMyCardInfoBinding> implements MyCardInfoView {

    private BankCard bankCard;

    public MyCardInfoFragment() {
        super(R.layout.fragment_my_card_info);
    }

    @NonNull
    public static MyCardInfoFragment getInstance(BankCard bankCard) {
        Bundle args = new Bundle();
        args.putSerializable("bankCard", bankCard);
        MyCardInfoFragment fragment = new MyCardInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @InjectPresenter MyCardInfoPresenter presenter;

    @ProvidePresenter MyCardInfoPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(MyCardInfoPresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bankCard = (BankCard) getArguments().getSerializable("bankCard");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.setBankCard(bankCard);
        binding.executePendingBindings();

        /* BEGIN WARNING: KEEP THESE LINES JUST AFTER `binding.executePendingBindings();` LINE! */
        setListenerTo(binding.cardColor1, 1);
        setListenerTo(binding.cardColor2, 2);
        setListenerTo(binding.cardColor3, 4);
        setListenerTo(binding.cardColor4, 3);
        /* ********************************** END OF WARNING ********************************** */

        binding.goBack.setOnClickListener(v -> presenter.goBack());
        binding.changeCardAltName.setOnClickListener(v -> {
            final AlertDialog dialog = new AlertDialog.Builder(requireContext()).create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            final ChangeItemAltNameDialogBinding itemNameChangeDialogBinding =
                ChangeItemAltNameDialogBinding.inflate(getLayoutInflater());

            itemNameChangeDialogBinding.setTitle(getString(R.string.card_name));
            itemNameChangeDialogBinding.setSubTitle(getString(R.string.change_item_card));
            itemNameChangeDialogBinding.setItemCurrentAltName(bankCard.getCardAltName());
            itemNameChangeDialogBinding.closeDialog.setOnClickListener(close -> dialog.dismiss());
            itemNameChangeDialogBinding.confirm.setOnClickListener(confirm -> {
                binding.progressBar.setVisibility(View.VISIBLE);
                presenter.changeCardData(
                    bankCard.getIdCard(),
                    itemNameChangeDialogBinding.itemNewAltName.getText().toString(),
                    bankCard.getCardColor(),
                    1
                );
                dialog.dismiss();
            });

            dialog.setView(itemNameChangeDialogBinding.getRoot());
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        });
    }

    @Override
    public void showResult(String accountNewAltName, int accountNewColor) {
        refreshBankCardsAndAccounts();

        binding.progressBar.setVisibility(View.GONE);

        bankCard.setCardAltName(accountNewAltName);
        bankCard.setCardColor(accountNewColor);
        binding.setBankCard(bankCard);
    }

    @Override
    public void showError(String message) {
        binding.progressBar.setVisibility(View.GONE);

        if (!message.isEmpty())
            Utils.snackbar(binding.getRoot(), message);
    }

    private void setListenerTo(@NonNull final RadioButton viewItem, final int cardNewColor) {
        viewItem.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // because of the view is RadioButton, there is no way to `uncheck`.
                // call to api ONLY and ONLY IF checked, otherwise do nothing.
                binding.progressBar.setVisibility(View.VISIBLE);
                presenter.changeCardData(
                    bankCard.getIdCard(),
                    bankCard.getCardAltName(),
                    cardNewColor,
                    2);
            }
        });
    }
}
