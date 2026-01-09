package az.btb.mobilebanking.ui.operations_history.card_to_card;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.adapters.ItemsAdapter;
import az.btb.mobilebanking.adapters.SpinnerBankCardsAdapter;
import az.btb.mobilebanking.databinding.FragmentCardToCardHistoryBinding;
import az.btb.mobilebanking.databinding.OperationItemDetailsBinding;
import az.btb.mobilebanking.databinding.OperationsHistoryListItemBinding;
import az.btb.mobilebanking.models.BankCard;
import az.btb.mobilebanking.models.BankCardOperation;
import az.btb.mobilebanking.utils.DatePickerFragment;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.ItemPropsBinder;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static android.graphics.PorterDuff.Mode.SRC_IN;
import static az.btb.mobilebanking.di.Scopes.SERVER_SCOPE;

public class CardToCardHistoryFragment extends Fragment<FragmentCardToCardHistoryBinding> implements CardToCardHistoryView {

    private final String[] OPERATION_TYPES = { "0", "1", "2" };

    private boolean isAllowed = false;

    private List<BankCard> allCards;

    private ItemsAdapter<OperationsHistoryListItemBinding, BankCardOperation> adapter;

    @InjectPresenter CardToCardHistoryPresenter presenter;

    @ProvidePresenter CardToCardHistoryPresenter providePresenter() {
        return Toothpick.openScope(SERVER_SCOPE).getInstance(CardToCardHistoryPresenter.class);
    }

    public CardToCardHistoryFragment() {
        super(R.layout.fragment_card_to_card_history);
    }

    @NonNull
    public static CardToCardHistoryFragment getInstance() {
        return new CardToCardHistoryFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Utils.modifyChildrenEnableStatus(binding.root, false);

        ItemPropsBinder<OperationsHistoryListItemBinding, BankCardOperation> itemPropsBinder = (binding, bankCardOperation) -> {
            binding.setOperationName(bankCardOperation.getOperationDescription());
            binding.setOperationTimestamp(bankCardOperation.getOperationDate());
            binding.setOperationStatus(bankCardOperation.getBankCardOperationStatus());
            binding.getRoot().setOnClickListener(v -> showOperationDetails(bankCardOperation));
        };
        adapter = new ItemsAdapter<>(R.layout.operations_history_list_item, new ArrayList<>(), itemPropsBinder);
        binding.operationHistoryList.setAdapter(adapter);

        setBankCards(obtainBankCards());

        final Calendar calendar = Calendar.getInstance();
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int m = calendar.get(Calendar.MONTH);
        int y = calendar.get(Calendar.YEAR);

        Utils.setDateField(y - 1, m, d, binding.fromDate);
        Utils.setDateField(y, m, d, binding.toDate);

        setFromToDateClickListener(binding.fromDate);
        setFromToDateClickListener(binding.toDate);

        binding.goBack.setOnClickListener(v -> presenter.goBack());

        binding.setShowFilterVisible(true);
        binding.filter.setOnClickListener(v -> {
            if (binding.getShowFilterVisible()) {
                binding.setShowFilterVisible(false);
                binding.filter.setColorFilter(Color.parseColor("#383336"));
            } else {
                binding.setShowFilterVisible(true);
                binding.filter.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark), SRC_IN
                );
            }
        });
    }

    @Override
    public void setBankCards(@NonNull List<BankCard> bankCards) {
        allCards = bankCards;

        binding.operationType.setAdapter(
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.operation_types,
                android.R.layout.simple_list_item_1
            )
        );
        binding.operationType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isAllowed) {
                    Utils.modifyChildrenEnableStatus(binding.root, false);
                    presenter.getOperationsHistory(
                        bankCards.get(binding.cards.getSelectedItemPosition()).getIdCard(),
                        OPERATION_TYPES[position],
                        binding.fromDate.getText().toString(),
                        binding.toDate.getText().toString()
                    );
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        SpinnerBankCardsAdapter cardsAdapter = new SpinnerBankCardsAdapter(requireContext(), bankCards);
        binding.cards.setAdapter(cardsAdapter);
        binding.cards.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Utils.modifyChildrenEnableStatus(binding.root, false);
                presenter.getOperationsHistory(
                    bankCards.get(position).getIdCard(),
                    OPERATION_TYPES[binding.operationType.getSelectedItemPosition()],
                    binding.fromDate.getText().toString(),
                    binding.toDate.getText().toString()
                );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        isAllowed = true;

        Utils.modifyChildrenEnableStatus(binding.root, true);
    }

    @Override
    public void setOperationsHistory(@NonNull List<BankCardOperation> bankCardOperations) {
        binding.progressBar.setVisibility(View.GONE);

        Utils.modifyChildrenEnableStatus(binding.root, true);

        if (bankCardOperations.size() == 0) {
            binding.operationHistoryList.setVisibility(View.GONE);
            binding.noItem.setVisibility(View.VISIBLE);
            return;
        }

        binding.noItem.setVisibility(View.GONE);
        binding.operationHistoryList.setVisibility(View.VISIBLE);

        adapter.submitList(bankCardOperations);
    }

    @Override
    public void showError(@NonNull String responseMessage) {
        Utils.modifyChildrenEnableStatus(binding.root, true);
        if (!responseMessage.isEmpty()) {
            binding.progressBar.setVisibility(View.GONE);
            Utils.snackbar(binding.getRoot(), responseMessage);
        }
    }

    private void setFromToDateClickListener(@NonNull final TextView field) {
        final DatePickerDialog.OnDateSetListener listener = (view1, year, month, day) -> {
            Utils.setDateField(year, month, day, field);

            binding.progressBar.setVisibility(View.VISIBLE);

            // get exchange rates for selected date immediately
            presenter.getOperationsHistory(
                ((BankCard) binding.cards.getSelectedItem()).getIdCard(),
                OPERATION_TYPES[binding.operationType.getSelectedItemPosition()],
                binding.fromDate.getText().toString(),
                binding.toDate.getText().toString()
            );
        };

        field.setOnClickListener(v -> {
            DialogFragment newFragment = new DatePickerFragment(field.getText().toString(), listener);
            newFragment.show(getParentFragmentManager(), "datePicker");
        });
    }

    private void showOperationDetails(@NonNull BankCardOperation item) {
        for (BankCard sourceCard: allCards) {
            if (sourceCard.getIdCard().equals(item.getFromIdCard()))
                item.setFormattedFromCardNumber(getFormattedCardNumber(sourceCard));

            if (sourceCard.getIdCard().equals(item.getToIdCard()))
                item.setToCardNumber(getFormattedCardNumber(sourceCard));
        }

        final OperationItemDetailsBinding detailsBinding = OperationItemDetailsBinding.inflate(getLayoutInflater());
        detailsBinding.setOperationDetails(item);

        Utils.showAlertDialogWith(detailsBinding.getRoot(), requireContext(), detailsBinding.closeDialog);
    }

    private String getFormattedCardNumber(@NonNull final BankCard card) {
        return String.format(
            getString(R.string.operation_type_card_number),
            card.getCardServiceName().substring(0, card.getCardServiceName().indexOf(' ')),
            card.getCardNumber().substring(0, 4),
            card.getCardNumber().substring(card.getCardNumber().length() - 4)
        );
    }
}
