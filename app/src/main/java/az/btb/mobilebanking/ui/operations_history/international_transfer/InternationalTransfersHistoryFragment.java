package az.btb.mobilebanking.ui.operations_history.international_transfer;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import az.btb.mobilebanking.adapters.SpinnerBankAccountsAdapter;
import az.btb.mobilebanking.adapters.SpinnerBankCardsAdapter;
import az.btb.mobilebanking.databinding.FragmentInternationalTransfersHistoryBinding;
import az.btb.mobilebanking.databinding.OperationsHistoryListItemBinding;
import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import az.btb.mobilebanking.models.ForeignAccountTransfer;
import az.btb.mobilebanking.utils.DatePickerFragment;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.ItemPropsBinder;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static android.graphics.PorterDuff.Mode.SRC_IN;
import static az.btb.mobilebanking.di.Scopes.SERVER_SCOPE;

public class InternationalTransfersHistoryFragment
    extends Fragment<FragmentInternationalTransfersHistoryBinding>
    implements InternationalTransfersHistoryView
{
    private boolean isDataShown = true;
    private boolean isAccountsLoaded = false;
    private boolean isFromAccount = false;

    private final ItemPropsBinder<OperationsHistoryListItemBinding, ForeignAccountTransfer> itemPropsBinder =
        (binding, transferItem) -> {
            binding.setOperationName(transferItem.getOperationDescription());
            binding.setOperationTimestamp(transferItem.getOperationDate());
            binding.setOperationStatus(transferItem.getForeignAccountTransferStatus());
            binding.getRoot().setOnClickListener(v -> showHistoryDetails(transferItem));
        };

    private final ItemsAdapter<OperationsHistoryListItemBinding, ForeignAccountTransfer> adapter =
        new ItemsAdapter<>(R.layout.operations_history_list_item, new ArrayList<>(), itemPropsBinder);

    @InjectPresenter InternationalTransfersHistoryPresenter presenter;

    @ProvidePresenter InternationalTransfersHistoryPresenter providePresenter() {
        return Toothpick.openScope(SERVER_SCOPE).getInstance(InternationalTransfersHistoryPresenter.class);
    }

    @NonNull
    public static InternationalTransfersHistoryFragment getInstance() {
        return new InternationalTransfersHistoryFragment();
    }

    public InternationalTransfersHistoryFragment() {
        super(R.layout.fragment_international_transfers_history);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        isDataShown = true;
        isAccountsLoaded = false;
        isFromAccount = false;

        setupViewParts();

        final Calendar calendar = Calendar.getInstance();
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int m = calendar.get(Calendar.MONTH);
        int y = calendar.get(Calendar.YEAR);

        Utils.setDateField(y, m, d, binding.fromDate);
        Utils.setDateField(y, m, d, binding.toDate);

        setFromToDateClickListener(binding.fromDate);
        setFromToDateClickListener(binding.toDate);

        Utils.modifyChildrenEnableStatus(binding.root, false);

        binding.goBack.setOnClickListener(v -> presenter.goBack());
        presenter.getCards();
        presenter.getHistory("", "", binding.fromDate.getText().toString(), binding.toDate.getText().toString());

        binding.setShowFilterVisible(true);
        binding.filter.setOnClickListener(v -> {
            if (binding.getShowFilterVisible())
                binding.filter.setColorFilter(Color.parseColor("#383336"));
            else
                binding.filter.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark),
                    SRC_IN
                );

            binding.setShowFilterVisible(!binding.getShowFilterVisible());
        });

        binding.fromCards.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isDataShown) {
                    Utils.modifyChildrenEnableStatus(binding.root, false);
                    presenter.getHistory(
                        ((BankCard) binding.fromCards.getSelectedItem()).getIdCard(),
                        "",
                        binding.fromDate.getText().toString(),
                        binding.toDate.getText().toString()
                    );
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        binding.fromAccounts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isDataShown) {
                    Utils.modifyChildrenEnableStatus(binding.root, false);

                    presenter.getHistory(
                        "",
                        ((BankAccount) binding.fromAccounts.getSelectedItem()).getIbanAccount(),
                        binding.fromDate.getText().toString(),
                        binding.toDate.getText().toString()
                    );
                }

                isDataShown = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        binding.setShowFilterVisible(false);

        binding.histories.setAdapter(adapter);
    }

    private void setupViewParts() {
        /* BEGIN: From items */
        binding.fromCard.setOnClickListener(v -> {
            isFromAccount = false;

            binding.fromCardsParent.setVisibility(View.VISIBLE);
            binding.fromAccountsParent.setVisibility(View.GONE);

            binding.fromCard.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_my_cards, 0, 0);
            binding.fromAccount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bank_transfer, 0, 0);
        });

        binding.fromAccount.setOnClickListener(v -> {
            isFromAccount = true;

            binding.fromCardsParent.setVisibility(View.GONE);

            binding.fromCard.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_card, 0, 0);
            binding.fromAccount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bank_transfer_red, 0, 0);

            if (!isAccountsLoaded) {
                Utils.modifyChildrenEnableStatus(binding.root, false);
                binding.progressBar.setVisibility(View.VISIBLE);
                presenter.getBankAccounts();
            } else
                binding.fromAccountsParent.setVisibility(View.VISIBLE);
        });
        /* END: From items */
    }

    private void setFromToDateClickListener(@NonNull final TextView field) {
        final DatePickerDialog.OnDateSetListener listener = (view1, year, month, day) -> {
            Utils.setDateField(year, month, day, field);

            binding.progressBar.setVisibility(View.VISIBLE);

            Utils.modifyChildrenEnableStatus(binding.root, false);

            // get exchange rates for selected date immediately
            presenter.getHistory(
                isFromAccount ? "" : ((BankCard) binding.fromCards.getSelectedItem()).getIdCard(),
                isFromAccount ? ((BankAccount) binding.fromAccounts.getSelectedItem()).getIbanAccount() : "",
                binding.fromDate.getText().toString(),
                binding.toDate.getText().toString()
            );
        };

        field.setOnClickListener(v -> {
            DialogFragment newFragment = new DatePickerFragment(field.getText().toString(), listener);
            newFragment.show(getParentFragmentManager(), "datePicker");
        });
    }

    private void showHistoryDetails(ForeignAccountTransfer transferItem) {
        presenter.showDetails(transferItem);
    }

    @Override
    public void showError(@NonNull String msg) {
        Utils.modifyChildrenEnableStatus(binding.root, true);

        binding.progressBar.setVisibility(View.GONE);
        if (!msg.isEmpty())
            Utils.snackbar(binding.getRoot(), msg);
    }

    @Override
    public void showCards(@NonNull List<BankCard> bankCards) {
        Utils.modifyChildrenEnableStatus(binding.root, true);

        binding.fromCards.setAdapter(new SpinnerBankCardsAdapter(requireContext(), bankCards));

        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showAccounts(@NonNull List<BankAccount> bankAccounts) {
        Utils.modifyChildrenEnableStatus(binding.root, true);

        isAccountsLoaded = true;

        binding.fromAccounts.setAdapter(new SpinnerBankAccountsAdapter(requireContext(), bankAccounts));

        binding.progressBar.setVisibility(View.GONE);
        binding.fromAccountsParent.setVisibility(View.VISIBLE);
    }

    @Override
    public void showHistory(@NonNull List<ForeignAccountTransfer> itemList) {
        Utils.modifyChildrenEnableStatus(binding.root, true);

        binding.progressBar.setVisibility(View.GONE);

        if (itemList.size() == 0) {
            binding.histories.setVisibility(View.GONE);
            binding.noItem.setVisibility(View.VISIBLE);
            return;
        }

        binding.noItem.setVisibility(View.GONE);
        binding.histories.setVisibility(View.VISIBLE);
        adapter.submitList(itemList);
    }
}
