package az.btb.mobilebanking.ui.card_statements;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.adapters.ItemsAdapter;
import az.btb.mobilebanking.databinding.CardStatementDetailsBinding;
import az.btb.mobilebanking.databinding.CardStatementsListItemBinding;
import az.btb.mobilebanking.databinding.FragmentCardStatementsBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.BankCardStatement;
import az.btb.mobilebanking.utils.DatePickerFragment;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.ItemPropsBinder;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class CardStatementsFragment extends Fragment<FragmentCardStatementsBinding> implements CardStatementsView {

    private String fromCardId;
    private String fromCardName;

    private ItemsAdapter<CardStatementsListItemBinding, BankCardStatement> adapter;

    public CardStatementsFragment() {
        super(R.layout.fragment_card_statements);
    }

    @NonNull
    public static CardStatementsFragment getInstance(String cardId, String fromCardName) {
        Bundle b = new Bundle();
        b.putString("fromCardId", cardId);
        b.putString("fromCardName", fromCardName);

        CardStatementsFragment fragment = new CardStatementsFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @InjectPresenter CardStatementsPresenter presenter;

    @ProvidePresenter CardStatementsPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(CardStatementsPresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fromCardId = requireArguments().getString("fromCardId");
        fromCardName = requireArguments().getString("fromCardName");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int m = calendar.get(Calendar.MONTH);
        int y = calendar.get(Calendar.YEAR);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(new Date());
        calendar1.add(Calendar.DAY_OF_YEAR, -7);
        int d1 = calendar1.get(Calendar.DAY_OF_MONTH);
        int m1 = calendar1.get(Calendar.MONTH);
        int y1 = calendar1.get(Calendar.YEAR);

        Utils.setDateField(y1, m1, d1, binding.fromDate);
        Utils.setDateField(y, m, d, binding.toDate);

        setFromToDateClickListener(binding.fromDate);
        setFromToDateClickListener(binding.toDate);

        binding.goBack.setOnClickListener(v -> presenter.goBack());

        Utils.modifyChildrenEnableStatus(binding.root, false);

        ItemPropsBinder<CardStatementsListItemBinding, BankCardStatement> itemPropsBinder = (binding, cardStatement) -> {
            binding.setDescription(cardStatement.getOperationDescription());
            binding.setTimestamp(cardStatement.getOperationDate());
            binding.setAmount(cardStatement.getAmount());
            binding.setAmountCurrency(Utils.getCurrency(cardStatement.getCurrency()));

            binding.getRoot().setOnClickListener(v -> showDetails(cardStatement));
        };
        adapter = new ItemsAdapter<>(R.layout.card_statements_list_item, new ArrayList<>(), itemPropsBinder);
        binding.cardStatements.setAdapter(adapter);

        presenter.cardStatements.observe(getViewLifecycleOwner(), bankCardStatements -> {
            binding.progressBar.setVisibility(View.GONE);
            Utils.modifyChildrenEnableStatus(binding.root, true);

            if (bankCardStatements.size() == 0) {
                binding.cardStatements.setVisibility(View.GONE);
                binding.noItem.setVisibility(View.VISIBLE);
            } else {
                binding.noItem.setVisibility(View.GONE);
                binding.cardStatements.setVisibility(View.VISIBLE);
            }

            adapter.submitList(bankCardStatements);
        });

        presenter.getCardStatements(
            fromCardId,
            binding.fromDate.getText().toString(),
            binding.toDate.getText().toString()
        );
    }

    @Override
    public void showError(String msg) {
        binding.progressBar.setVisibility(View.GONE);
        if (!msg.isEmpty())
            Utils.snackbar(binding.getRoot(), msg);
    }

    private void showDetails(@NonNull BankCardStatement cardStatement) {
        final CardStatementDetailsBinding successDialogBinding = CardStatementDetailsBinding.inflate(getLayoutInflater());
        successDialogBinding.setOperationDate(cardStatement.getOperationDate());
        successDialogBinding.setSourceCardFormattedNumber(fromCardName);
        successDialogBinding.setAmount(cardStatement.getAmount());
        successDialogBinding.setAmountCurrency(Utils.getCurrency(cardStatement.getCurrency()));
        successDialogBinding.setCommission(cardStatement.getAmountBilling());
        successDialogBinding.setCommissionCurrency(Utils.getCurrency(cardStatement.getCurrencyBilling()));
        successDialogBinding.setStatementType(cardStatement.getPaymentType());
        successDialogBinding.setNote(cardStatement.getOperationDescription());

        Utils.showAlertDialogWith(successDialogBinding.getRoot(), requireContext(), successDialogBinding.closeDialog);
    }

    private void setFromToDateClickListener(@NonNull final TextView field) {
        final DatePickerDialog.OnDateSetListener listener = (view1, year, month, day) -> {
            Utils.setDateField(year, month, day, field);

            binding.progressBar.setVisibility(View.VISIBLE);

            presenter.getCardStatements(
                fromCardId,
                binding.fromDate.getText().toString(),
                binding.toDate.getText().toString()
            );
        };

        field.setOnClickListener(v -> {
            DialogFragment newFragment = new DatePickerFragment(field.getText().toString(), listener);
            newFragment.show(getParentFragmentManager(), "accountStatementsDatePicker");
        });
    }
}
