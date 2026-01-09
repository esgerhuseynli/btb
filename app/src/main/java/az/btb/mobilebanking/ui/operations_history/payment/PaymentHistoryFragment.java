package az.btb.mobilebanking.ui.operations_history.payment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.adapters.ItemsAdapter;
import az.btb.mobilebanking.adapters.PaymentHistoryProviderSpinnerAdapter;
import az.btb.mobilebanking.adapters.SpinnerBankAccountsAdapter;
import az.btb.mobilebanking.adapters.SpinnerBankCardsAdapter;
import az.btb.mobilebanking.databinding.FragmentPaymentHistoryBinding;
import az.btb.mobilebanking.databinding.PaymentHistoryItemDetailsWindowBinding;
import az.btb.mobilebanking.databinding.PaymentOperationHistoryListItemBinding;
import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import az.btb.mobilebanking.models.MobilePayment;
import az.btb.mobilebanking.utils.Constants;
import az.btb.mobilebanking.utils.DatePickerFragment;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.ItemPropsBinder;
import az.btb.mobilebanking.utils.PaymentHistoryProviderItem;
import az.btb.mobilebanking.utils.Utils;
import kotlin.Pair;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static android.graphics.PorterDuff.Mode.SRC_IN;
import static az.btb.mobilebanking.di.Scopes.SERVER_SCOPE;

public class PaymentHistoryFragment extends Fragment<FragmentPaymentHistoryBinding> implements PaymentHistoryView {
//    public static final String TAG = "TAG";

    private boolean isAccountsLoaded = false;
    private boolean isCardsLoaded = false;
    private boolean isFromAccount = false;
    private boolean isFilterDataLoaded = false;
    
    private final ItemPropsBinder<PaymentOperationHistoryListItemBinding, MobilePayment> itemPropsBinder =
        (binding, transferItem) -> {
            binding.setOperationName(transferItem.getPaymentProviderName());
            binding.setOperationTimestamp(transferItem.getPaymentDateTime());
            binding.operationIcon.setImageResource(statusSmallIcon(transferItem.getMobilePaymentStatus()));
            binding.paymentHistoryItemRoot.setOnClickListener(v -> showHistoryDetails(transferItem));
        };
    
    private final ItemsAdapter<PaymentOperationHistoryListItemBinding, MobilePayment> adapter =
        new ItemsAdapter<>(R.layout.payment_operation_history_list_item, new ArrayList<>(), itemPropsBinder);
    
    @InjectPresenter PaymentHistoryPresenter presenter;
    
    @ProvidePresenter PaymentHistoryPresenter providePresenter() {
        return Toothpick.openScope(SERVER_SCOPE).getInstance(PaymentHistoryPresenter.class);
    }
    
    @NonNull
    public static PaymentHistoryFragment getInstance() {
        return new PaymentHistoryFragment();
    }
    
    public PaymentHistoryFragment() {
        super(R.layout.fragment_payment_history);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        isCardsLoaded = false;
        isAccountsLoaded = false;
        isFromAccount = false;
        isFilterDataLoaded = false;
        
        setupViewParts();
        
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DAY_OF_YEAR, -7);
        int sd = calendar1.get(Calendar.DAY_OF_MONTH);
        int sm = calendar1.get(Calendar.MONTH);
        int sy = calendar1.get(Calendar.YEAR);
        
        final Calendar eCalendar = Calendar.getInstance();
        int ed = eCalendar.get(Calendar.DAY_OF_MONTH);
        int em = eCalendar.get(Calendar.MONTH);
        int ey = eCalendar.get(Calendar.YEAR);
        
        Utils.setDateField(sy, sm, sd, binding.fromDate);
        Utils.setDateField(ey, em, ed, binding.toDate);
        
        setFromToDateClickListener(binding.fromDate);
        setFromToDateClickListener(binding.toDate);
        
        Utils.modifyChildrenEnableStatus(binding.root, false);
        
        binding.goBack.setOnClickListener(v -> presenter.goBack());
    
        presenter.getHistory(Constants.MoneySourceTypes.NONE, "", "", binding.fromDate.getText().toString(), binding.toDate.getText().toString(), 0, 0, 0, 0);
    
        binding.setShowFilterVisible(true);
        binding.filter.setOnClickListener(v -> {
            if (binding.getShowFilterVisible())
                binding.filter.setColorFilter(Color.parseColor("#383336"));
            else
                binding.filter.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark),
                    SRC_IN
                );
    
            if (!isCardsLoaded) {
                binding.progressBar.setVisibility(View.VISIBLE);
                Utils.modifyChildrenEnableStatus(binding.root, false);
                presenter.getCards();
            }
            
            binding.setShowFilterVisible(!binding.getShowFilterVisible());
        });
        
        binding.statuses.setAdapter(
            new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                getResources().getStringArray(R.array.payment_history_statuses)
            )
        );
        binding.statuses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFilterDataLoaded) {
        //            Log.e(TAG, "statuses onItemSelected: ");
                    binding.progressBar.setVisibility(View.VISIBLE);
                    Utils.modifyChildrenEnableStatus(binding.root, false);
                    getHistory();
                }
            }
    
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        
        binding.paymentTypes.setAdapter(
            new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                getResources().getStringArray(R.array.payment_types)
            )
        );
        binding.paymentTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFilterDataLoaded) {
        //            Log.e(TAG, "paymentTypes onItemSelected: ");
                    binding.progressBar.setVisibility(View.VISIBLE);
                    Utils.modifyChildrenEnableStatus(binding.root, false);
                    getHistory();
                }
            }
        
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    
        binding.providers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFilterDataLoaded) {
        //            Log.e(TAG, "providers onItemSelected: ");
                    binding.progressBar.setVisibility(View.VISIBLE);
                    Utils.modifyChildrenEnableStatus(binding.root, false);
                    getHistory();
                } else {
                    isFilterDataLoaded = true; // bizarre workaround for stupid situation...
                }
            }
        
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
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
    
            if (isFilterDataLoaded) {
                binding.progressBar.setVisibility(View.VISIBLE);
                Utils.modifyChildrenEnableStatus(binding.root, false);
                getHistory();
            }
        };
        
        field.setOnClickListener(v -> {
            DialogFragment newFragment = new DatePickerFragment(field.getText().toString(), listener);
            newFragment.show(getParentFragmentManager(), "datePicker");
        });
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
    
        presenter.getPaymentProviderGroups();
    
        binding.fromCards.setAdapter(new SpinnerBankCardsAdapter(requireContext(), bankCards));

        binding.fromCards.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFilterDataLoaded) {
     //               Log.e(TAG, "fromCards onItemSelected: ");
                    binding.progressBar.setVisibility(View.VISIBLE);
                    Utils.modifyChildrenEnableStatus(binding.root, false);
                    getHistory();
                }
            }
            
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        
        binding.progressBar.setVisibility(View.GONE);
    
        isCardsLoaded = true;
     //   Log.e(TAG, "showCards bitdi");
        //  isFilterDataLoaded = true;
    }

    @Override
    public void showAccounts(@NonNull List<BankAccount> bankAccounts) {
        Utils.modifyChildrenEnableStatus(binding.root, true);
        
        isAccountsLoaded = true;
        
        binding.fromAccounts.setAdapter(new SpinnerBankAccountsAdapter(requireContext(), bankAccounts));
        
        binding.fromAccounts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFilterDataLoaded) {
        //            Log.e(TAG, "fromAccounts onItemSelected: ");
                    binding.progressBar.setVisibility(View.VISIBLE);
                    Utils.modifyChildrenEnableStatus(binding.root, false);
                    getHistory();
                }
            }
            
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        
        binding.progressBar.setVisibility(View.GONE);
        binding.fromAccountsParent.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPaymentProviderGroups(@NonNull List<PaymentHistoryProviderItem> groups) {
        binding.progressBar.setVisibility(View.GONE);
        Utils.modifyChildrenEnableStatus(binding.root, true);
        
        ArrayList<PaymentHistoryProviderItem> data = new ArrayList<>();
        data.add(new PaymentHistoryProviderItem(0, getString(R.string.none)));
        data.addAll(groups);
        
        binding.providerGroups.setAdapter(new PaymentHistoryProviderSpinnerAdapter(requireContext(), data));
    
        binding.providerGroups.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.progressBar.setVisibility(View.VISIBLE);
                Utils.modifyChildrenEnableStatus(binding.root, false);
                presenter.getPaymentProviders(((PaymentHistoryProviderItem) binding.providerGroups.getSelectedItem()).getId());
            }
        
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
     //   Log.e(TAG, "showPaymentProviderGroups bitdi");
    }
    
    @Override
    public void showPaymentProviders(List<PaymentHistoryProviderItem> providers) {
       // Log.e(TAG, "showPaymentProviders --> isFilterDataLoaded =  "+ isFilterDataLoaded);
    
        ArrayList<PaymentHistoryProviderItem> data = new ArrayList<>();
        data.add(new PaymentHistoryProviderItem(0, getString(R.string.none)));
        data.addAll(providers);
        
        binding.providers.setAdapter(new PaymentHistoryProviderSpinnerAdapter(requireContext(), data));
        
        //Log.e(TAG, "showPaymentProviders bitdi");

        binding.progressBar.setVisibility(View.GONE);
        Utils.modifyChildrenEnableStatus(binding.root, true);
    }
    
    @Override
    public void showHistory(@NonNull List<MobilePayment> itemList) {
        binding.progressBar.setVisibility(View.GONE);
        Utils.modifyChildrenEnableStatus(binding.root, true);
        //System.out.println("size: " + itemList.size());
        if (itemList.size() == 0) {
            binding.histories.setVisibility(View.GONE);
            binding.noItem.setVisibility(View.VISIBLE);
            return;
        }

        binding.noItem.setVisibility(View.GONE);
        binding.histories.setVisibility(View.VISIBLE);
        adapter.submitList(itemList);
    }
    
    private void getHistory() {
        presenter.getHistory(
            isFromAccount ? Constants.MoneySourceTypes.ACCOUNT : Constants.MoneySourceTypes.CARD,
            isFromAccount ? "" : ((BankCard) binding.fromCards.getSelectedItem()).getIdCard(),
            isFromAccount ? ((BankAccount) binding.fromAccounts.getSelectedItem()).getIbanAccount() : "",
            binding.fromDate.getText().toString(),
            binding.toDate.getText().toString(),
            ((PaymentHistoryProviderItem) binding.providerGroups.getSelectedItem()).getId(),
            ((PaymentHistoryProviderItem) binding.providers.getSelectedItem()).getId(),
            binding.statuses.getSelectedItemPosition(),
            binding.paymentTypes.getSelectedItemPosition()
        );
    }
    
    private @DrawableRes int statusSmallIcon(int status) {
        switch (status) {
            case 1:
                return R.drawable.ic_received;
            case 2:
                return R.drawable.ic_success;
            case 3:
                return R.drawable.ic_failure;
            case 4:
                return R.drawable.ic_pending;
            default:
                return 0;
        }
    }
    
    @NonNull
    private Pair<Integer, Integer> statusData(int status) {
        switch (status) {
            case 1:
                return new Pair<>(R.drawable.ic_received_big, R.string.money_transfer_status_received);
            case 2:
                return new Pair<>(R.drawable.ic_success_big, R.string.success_order);
            case 3:
                return new Pair<>(R.drawable.ic_failure_big, R.string.money_transfer_status_failure);
            case 4:
                return new Pair<>(R.drawable.ic_pending_big, R.string.money_transfer_status_pending);
            default:
                return new Pair<>(0, 0);
        }
    }
    
    private void showHistoryDetails(@NonNull MobilePayment transferItem) {
        //System.out.println(new Gson().toJson(transferItem));
        final PaymentHistoryItemDetailsWindowBinding successDialogBinding =
            PaymentHistoryItemDetailsWindowBinding.inflate(getLayoutInflater());
    
        Pair<Integer, Integer> statusData = statusData(transferItem.getMobilePaymentStatus());
        successDialogBinding.paymentStatusIcon.setImageResource(statusData.getFirst());
        successDialogBinding.setStatus(statusData.getSecond());
        successDialogBinding.setMobilePayment(transferItem);
        
        Utils.showAlertDialogWith(successDialogBinding.getRoot(), requireContext(), successDialogBinding.closeDialog);
    }
}
