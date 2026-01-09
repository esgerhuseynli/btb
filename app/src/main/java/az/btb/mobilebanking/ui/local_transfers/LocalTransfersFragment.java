package az.btb.mobilebanking.ui.local_transfers;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.adapters.ItemsAdapter;
import az.btb.mobilebanking.adapters.SpinnerBankAccountsAdapter;
import az.btb.mobilebanking.adapters.SpinnerBankCardsAdapter;
import az.btb.mobilebanking.databinding.BranchSelectionDialogBinding;
import az.btb.mobilebanking.databinding.BudgetDestinationSelectionDialogBinding;
import az.btb.mobilebanking.databinding.BudgetLevelSelectionDialogBinding;
import az.btb.mobilebanking.databinding.DestinationLevelItemBinding;
import az.btb.mobilebanking.databinding.FragmentLocalTransfersBinding;
import az.btb.mobilebanking.databinding.LocalBranchFiltersWindowBinding;
import az.btb.mobilebanking.databinding.LocalBranchesItemBinding;
import az.btb.mobilebanking.databinding.TransactionResultWindowBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import az.btb.mobilebanking.models.BudgetDestination;
import az.btb.mobilebanking.models.BudgetLevel;
import az.btb.mobilebanking.models.BudgetPaymentInfo;
import az.btb.mobilebanking.models.LocalBankBranch;
import az.btb.mobilebanking.models.LocalReceiverInfo;
import az.btb.mobilebanking.models.PayerInfo;
import az.btb.mobilebanking.utils.Constants.MoneySourceTypes;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class LocalTransfersFragment extends Fragment<FragmentLocalTransfersBinding> implements LocalTransfersView {
    
    private boolean isAccountsLoaded = false;
    private boolean isFromAccount = false;
    private boolean hasIntermediateAgent = false;
    
    //region Branches related stuff (part 1)
    private LocalBankBranch localBankBranch;
    
    private int searchCriteria = -1;
    
    private BranchSelectionDialogBinding branchSelectorBinding = null;
    private AlertDialog branchSelector;
    
    private final ItemsAdapter<LocalBranchesItemBinding, LocalBankBranch> branchesAdapter =
        new ItemsAdapter<>(
            R.layout.local_branches_item,
            new ArrayList<>(),
            (branchesItemBinding, localBranch) -> {
            branchesItemBinding.setBranchName(localBranch.getBranchName());
            branchesItemBinding.setBranchCode(localBranch.getBranchCode());
            branchesItemBinding.setBranchTaxNumber(localBranch.getBranchTaxNumber());
    
            branchesItemBinding.getRoot().setOnClickListener(v -> {
                localBankBranch = localBranch;
                branchSelector.dismiss();
                binding.branchName.setText(localBranch.getBranchName());
            });
        }
        );
    //endregion Branches related stuff (part 1)
    
    //region Budget destination related stuff (part 1)
    private BudgetDestination selectedBudgetDestination;
    
    private BudgetDestinationSelectionDialogBinding destinationSelectorDialogBinding = null;
    private AlertDialog budgetDestinationSelector;

    private final ItemsAdapter<DestinationLevelItemBinding, BudgetDestination> budgetDestinationAdapter =
        new ItemsAdapter<>(
            R.layout.destination_level_item,
            new ArrayList<>(),
            (itemBinding, budgetDestination) -> {
                itemBinding.setItemName(budgetDestination.getDestination() + " - " + budgetDestination.getCode());

                itemBinding.getRoot().setOnClickListener(v -> {
                    selectedBudgetDestination = budgetDestination;
                    budgetDestinationSelector.dismiss();
                    binding.budgetDestination.setText(budgetDestination.getCode());
                });
            }
        );
    //endregion Budget destination related stuff (part 1)
    
    //region Budget level related stuff (part 1)
    private BudgetLevel selectedBudgetLevel;

    private BudgetLevelSelectionDialogBinding levelSelectorDialogBinding = null;
    private AlertDialog budgetLevelSelector;

    private final ItemsAdapter<DestinationLevelItemBinding, BudgetLevel> budgetLevelAdapter =
        new ItemsAdapter<>(
            R.layout.destination_level_item,
            new ArrayList<>(),
            (itemBinding, budgetLevel) -> {
                itemBinding.setItemName(budgetLevel.getLevelName());

                itemBinding.getRoot().setOnClickListener(v -> {
                    selectedBudgetLevel = budgetLevel;
                    budgetLevelSelector.dismiss();
                    binding.budgetLevel.setText(budgetLevel.getLevelName());
                });
            }
        );
    //endregion Budget level related stuff (part 1)
    
    @InjectPresenter LocalTransfersPresenter presenter;

    @ProvidePresenter LocalTransfersPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(LocalTransfersPresenter.class);
    }

    public LocalTransfersFragment() {
        super(R.layout.fragment_local_transfers);
    }

    @NonNull
    public static LocalTransfersFragment getInstance() {
        return new LocalTransfersFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        isAccountsLoaded = false;
        isFromAccount = false;
        hasIntermediateAgent = false;

        setupViewParts();

        binding.goBack.setOnClickListener(v -> presenter.goBack());

        binding.branchName.setOnClickListener(v -> showBranchSelectionDialog());
        binding.budgetDestination.setOnClickListener(v -> showBudgetDestinationsDialog());
        binding.budgetLevel.setOnClickListener(v -> showBudgetLevelsDialog());

        Utils.modifyChildrenEnableStatus(binding.root, false);

        presenter.getCards();

        binding.isBudgetPayment.setOnCheckedChangeListener((buttonView, isChecked) -> {
            hasIntermediateAgent = isChecked;

            if (isChecked)
                binding.budgetFields.setVisibility(View.VISIBLE);
            else
                binding.budgetFields.setVisibility(View.GONE);
        });

        binding.makeTransfer.setOnClickListener(v -> {
            if (localBankBranch == null) {
                Utils.snackbar(binding.root, R.string.choose_bank_branch);
                return;
            }

            BudgetPaymentInfo budgetPaymentInfo = new BudgetPaymentInfo(0, "", "");

            final String accountName = binding.accountName.getText().toString();
            final String transferNumber = binding.transferNumber.getText().toString();
            final String ibanCode = binding.ibanCode.getText().toString();

            if (!accountName.isEmpty()) {
                if (!transferNumber.isEmpty()) {
                    if (!ibanCode.isEmpty()) {
                        if (hasIntermediateAgent) {
                            if (selectedBudgetDestination != null) {
                                if (selectedBudgetLevel != null) {
                                    budgetPaymentInfo.setBudgetPayment(1);
                                    budgetPaymentInfo.setBudgetDestinationCode(selectedBudgetDestination.getCode());
                                    budgetPaymentInfo.setBudgetLevelCode(selectedBudgetLevel.getCode());
                                } else {
                                    Utils.snackbar(binding.getRoot(), R.string.choose_budget_level);
                                    return;
                                }
                            } else {
                                Utils.snackbar(binding.getRoot(), R.string.choose_budget_destination);
                                return;
                            }
                        }

                        try {
                            BigDecimal amount = new BigDecimal(binding.transferAmount.getText().toString());

                            final String note = binding.operationDescription.getText().toString().trim();

                            Utils.modifyChildrenEnableStatus(binding.root, false);

                            presenter.makeLocalTransfer(
                                new PayerInfo(
                                    isFromAccount ? MoneySourceTypes.ACCOUNT : MoneySourceTypes.CARD,
                                    isFromAccount ? "" : ((BankCard) binding.fromCards.getSelectedItem()).getIdCard(),
                                    isFromAccount ? ((BankAccount) binding.fromAccounts.getSelectedItem()).getIbanAccount() : ""
                                ),
                                transferNumber,
                                new LocalReceiverInfo(
                                    localBankBranch.getBranchCode(),
                                    accountName,
                                    ibanCode,
                                    "",
                                    localBankBranch.getBranchTaxNumber()
                                ),
                                amount,
                                note.isEmpty() ? " " : note,
                                budgetPaymentInfo
                            );
                        } catch (NumberFormatException ignored) {
                            Utils.snackbar(binding.getRoot(), R.string.enter_amount_properly);
                        }
                    } else
                        Utils.snackbar(binding.getRoot(), R.string.enter_iban);
                } else
                    Utils.snackbar(binding.getRoot(), R.string.enter_transfer_number);
            } else
                Utils.snackbar(binding.getRoot(), R.string.enter_client_name);
        });
    }
    
    private void showBranchSelectionDialog() {
        branchSelector = new AlertDialog.Builder(requireContext()).create();
        
        branchSelectorBinding = BranchSelectionDialogBinding.inflate(getLayoutInflater());
        presenter.branches.observe(getViewLifecycleOwner(), localBranches -> {
            if (!localBranches.isEmpty()) {
                branchesAdapter.submitList(localBranches, () -> branchSelectorBinding.localBranches.scrollToPosition(0));
            }
        });
        branchSelectorBinding.localBranchSearchProp.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                final String s = branchSelectorBinding.localBranchSearchProp.getText().toString().trim();
                if (searchCriteria == -1)
                    presenter.searchBranchesByAny(s);
                else if (searchCriteria == 0)
                    presenter.searchBranchesByName(s);
                else if (searchCriteria == 1)
                    presenter.searchBranchesByCode(s);
                else
                    presenter.searchBranchesByTaxNumber(s);
                
                Utils.hideKeyboardFrom(requireContext(), binding.getRoot());
                
                return true;
            }
            return false;
        });
        branchSelectorBinding.filterOptions.setOnClickListener(v -> showFilterOptions());
        branchSelectorBinding.localBranches.setAdapter(branchesAdapter);
        
        branchSelector.setView(branchSelectorBinding.getRoot());
        
        branchSelector.setOnDismissListener(dialog -> presenter.branches.removeObservers(getViewLifecycleOwner()));
        
        branchSelector.show();
    }
    
    private void showFilterOptions() {
        final AlertDialog dialog = new AlertDialog.Builder(requireContext()).create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        
        final String q = branchSelectorBinding.localBranchSearchProp.getText().toString();
        
        LocalBranchFiltersWindowBinding filtersWindowBinding =
            LocalBranchFiltersWindowBinding.inflate(getLayoutInflater());
        
        changeCheckStatus(filtersWindowBinding.criteria1, searchCriteria == 0);
        changeCheckStatus(filtersWindowBinding.criteria2, searchCriteria == 1);
        changeCheckStatus(filtersWindowBinding.criteria3, searchCriteria == 2);
        
        filtersWindowBinding.criteria1.setOnClickListener(v -> {
            if (searchCriteria == 0) {
                searchCriteria = -1;
                presenter.searchBranchesByAny(q);
            } else {
                searchCriteria = 0;
                presenter.searchBranchesByName(q);
            }
            dialog.dismiss();
        });
        filtersWindowBinding.criteria2.setOnClickListener(v -> {
            if (searchCriteria == 1) {
                searchCriteria = -1;
                presenter.searchBranchesByAny(q);
            } else {
                searchCriteria = 1;
                presenter.searchBranchesByCode(q);
            }
            dialog.dismiss();
        });
        filtersWindowBinding.criteria3.setOnClickListener(v -> {
            if (searchCriteria == 2) {
                searchCriteria = -1;
                presenter.searchBranchesByAny(q);
            } else {
                searchCriteria = 2;
                presenter.searchBranchesByTaxNumber(q);
            }
            dialog.dismiss();
        });
        
        filtersWindowBinding.closeDialog.setOnClickListener(v -> dialog.dismiss());
        
        dialog.setView(filtersWindowBinding.getRoot());
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
    
    private void changeCheckStatus(@NonNull TextView view, boolean shouldCheck) {
        view.setCompoundDrawablesWithIntrinsicBounds(0, 0, shouldCheck ? R.drawable.ic_tick_red : 0, 0);
    }
    
    private void showBudgetDestinationsDialog() {
        budgetDestinationSelector = new AlertDialog.Builder(requireContext()).create();
    
        destinationSelectorDialogBinding = BudgetDestinationSelectionDialogBinding.inflate(getLayoutInflater());
        presenter.budgetDestinations.observe(getViewLifecycleOwner(), budgetDestinations -> {
            if (!budgetDestinations.isEmpty()) {
                budgetDestinationAdapter.submitList(
                    budgetDestinations,
                    () -> destinationSelectorDialogBinding.destinations.scrollToPosition(0)
                );
            }
        });
        destinationSelectorDialogBinding.destinationSearchProp.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                presenter.searchBudgetDestinationsBy(
                    destinationSelectorDialogBinding.destinationSearchProp.getText().toString().trim()
                );
            
                Utils.hideKeyboardFrom(requireContext(), binding.getRoot());
            
                return true;
            }
            return false;
        });
        destinationSelectorDialogBinding.destinations.setAdapter(budgetDestinationAdapter);
    
        budgetDestinationSelector.setView(destinationSelectorDialogBinding.getRoot());
    
        budgetDestinationSelector.setOnDismissListener(dialog -> presenter.budgetDestinations.removeObservers(getViewLifecycleOwner()));
    
        budgetDestinationSelector.show();
    }
    
    private void showBudgetLevelsDialog() {
        budgetLevelSelector = new AlertDialog.Builder(requireContext()).create();
    
        levelSelectorDialogBinding = BudgetLevelSelectionDialogBinding.inflate(getLayoutInflater());
        presenter.budgetLevels.observe(getViewLifecycleOwner(), budgetLevels -> {
            if (!budgetLevels.isEmpty()) {
                budgetLevelAdapter.submitList(
                    budgetLevels,
                    () -> levelSelectorDialogBinding.levels.scrollToPosition(0)
                );
            }
        });
        levelSelectorDialogBinding.levelSearchProp.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                presenter.searchBudgetLevelsBy(
                    levelSelectorDialogBinding.levelSearchProp.getText().toString().trim()
                );

                Utils.hideKeyboardFrom(requireContext(), binding.getRoot());

                return true;
            }
            return false;
        });
        levelSelectorDialogBinding.levels.setAdapter(budgetLevelAdapter);

        budgetLevelSelector.setView(levelSelectorDialogBinding.getRoot());

        budgetLevelSelector.setOnDismissListener(dialog -> presenter.budgetLevels.removeObservers(getViewLifecycleOwner()));

        budgetLevelSelector.show();
    }
    
    @Override
    public void showError(@NonNull String msg) {
        Utils.modifyChildrenEnableStatus(binding.root, true);
        binding.progressBar.setVisibility(View.GONE);
        if (!msg.isEmpty())
            Utils.snackbar(binding.getRoot(), msg);
        binding.makeTransfer.setEnabled(true);
    }

    @Override
    public void showSuccessResult(String transferNumber) {
        Utils.modifyChildrenEnableStatus(binding.root, true);

        final AlertDialog dialog = new AlertDialog.Builder(requireContext()).create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        TransactionResultWindowBinding windowBinding =
            TransactionResultWindowBinding.inflate(getLayoutInflater());

        windowBinding.setWasSucceeded(true);
        windowBinding.secondLine.setVisibility(View.GONE);
        windowBinding.accountOrCard.setText(R.string.money_transfer_number);
        windowBinding.setDestinationCardFormattedNumber(transferNumber);

        windowBinding.closeDialog.setOnClickListener(v -> {
            dialog.dismiss();
            presenter.goHome();
        });

        dialog.setView(windowBinding.getRoot());
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void showCards(List<BankCard> bankCards) {
        binding.fromCards.setAdapter(new SpinnerBankCardsAdapter(requireContext(), bankCards));

        binding.makeTransfer.setEnabled(true);
        binding.progressBar.setVisibility(View.GONE);
        Utils.modifyChildrenEnableStatus(binding.root, true);
    }

    @Override
    public void showAccounts(List<BankAccount> bankAccounts) {
        isAccountsLoaded = true;

        binding.fromAccounts.setAdapter(new SpinnerBankAccountsAdapter(requireContext(), bankAccounts));

        binding.progressBar.setVisibility(View.GONE);
        binding.makeTransfer.setEnabled(true);
        binding.fromAccountsParent.setVisibility(View.VISIBLE);
        Utils.modifyChildrenEnableStatus(binding.root, true);
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
                binding.makeTransfer.setEnabled(false);
                binding.progressBar.setVisibility(View.VISIBLE);
                presenter.getBankAccounts();
            } else
                binding.fromAccountsParent.setVisibility(View.VISIBLE);
        });
        /* END: From items */
    }
}
