package az.btb.mobilebanking.ui.home;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

import az.btb.mobilebanking.BuildConfig;
import az.btb.mobilebanking.R;
import az.btb.mobilebanking.adapters.BankCardAndAccountsAdapter;
import az.btb.mobilebanking.databinding.BankCardAndAccountItemBinding;
import az.btb.mobilebanking.databinding.FragmentHomeBinding;
import az.btb.mobilebanking.databinding.QrCodeVerificationResultDialogBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import az.btb.mobilebanking.models.BankCardAndAccount;
import az.btb.mobilebanking.utils.Constants;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.ItemPropsBinder;
import az.btb.mobilebanking.utils.Utils;
import az.btb.mobilebanking.utils.carouselmanager.CarouselLayoutManager;
import az.btb.mobilebanking.utils.carouselmanager.CarouselZoomPostLayoutListener;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class HomeFragment extends Fragment<FragmentHomeBinding> implements HomeView {

    private final PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();

    private final ItemPropsBinder<BankCardAndAccountItemBinding, BankCardAndAccount> itemPropsBinder = (binding, item) -> {
        binding.setIsCardItem(item.isCardItem());

        switch (item.getItemColor()) {
            case 0:
            case 1:
                binding.itemColorLayout.setBackground(Utils.getDrawable(requireContext(), R.drawable.ic_red_item));
                break;
            case 2:
                binding.itemColorLayout.setBackground(Utils.getDrawable(requireContext(), R.drawable.ic_green_item));
                break;
            case 3:
                binding.itemColorLayout.setBackground(Utils.getDrawable(requireContext(), R.drawable.ic_blue_item));
                break;
            case 4:
                binding.itemColorLayout.setBackground(Utils.getDrawable(requireContext(), R.drawable.ic_purple_item));
                break;
        }

        binding.setItemBalance(item.getItemBalance());
        binding.setItemCurrency(item.getItemCurrency());
        binding.setItemAltName(item.getItemAltName());
        if (item.isCardItem()) {
            binding.setItemType(getString(R.string.card_account));
            binding.setItemNumber(
                String.format(
                    getString(R.string.item_card_pattern),
                    item.getItemNumber().substring(0, 4),
                    item.getItemNumber().substring(item.getItemNumber().length() - 4)
                )
            );
            binding.setCardExpireDate(item.getCardExpireDate());
            binding.cardType.setImageResource(item.getCardType() == 1 ? R.drawable.ic_visa : R.drawable.ic_mastercard);
        } else {
            binding.setItemType(getString(R.string.account));
            binding.setItemNumber(item.getItemNumber());
        }
    };

    final BankCardAndAccountsAdapter cardsAccountsAdapter = new BankCardAndAccountsAdapter(itemPropsBinder);
    private List<BankCardAndAccount> fullList;
    private int cardsCount = 0;
    private int adapterPos = 0;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @NonNull public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @InjectPresenter HomePresenter presenter;

    @ProvidePresenter HomePresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(HomePresenter.class);
    }

    // DO NOT REMOVE THIS METHOD
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = FragmentHomeBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        adapterPos = 0;
        cardsCount = 0;

        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());

        binding.cardsAccountsList.setLayoutManager(layoutManager);

        if (binding.cardsAccountsList.getOnFlingListener() == null)
            pagerSnapHelper.attachToRecyclerView(binding.cardsAccountsList);

        binding.cardsAccountsList.setAdapter(cardsAccountsAdapter);
        binding.indicator.attachToRecyclerView(binding.cardsAccountsList, pagerSnapHelper);

        binding.cardsAccountsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    adapterPos = ((CarouselLayoutManager) recyclerView.getLayoutManager()).getCenterItemPosition();
                    if (adapterPos <= cardsCount - 1) {
                        binding.cardItems.setVisibility(View.VISIBLE);
                        binding.accountItems.setVisibility(View.GONE);
                        binding.cardStatements.setVisibility(View.VISIBLE);
                        if (!fullList.isEmpty()) {
                            binding.cardDetails.setOnClickListener(v -> presenter.goToCardDetailsScreen(fullList.get(adapterPos).getObject()));
                            binding.cardStatements.setOnClickListener(v -> {
                                final BankCardAndAccount currentCard = cardsAccountsAdapter.getCurrentList().get(adapterPos);
                                presenter.goToCardStatementsScreen(
                                    currentCard.getCardId(),
                                    String.format(
                                        getString(R.string.operation_type_card_number),
                                        currentCard.getCardFormattedName(),
                                        currentCard.getCardNumber().substring(0, 4),
                                        currentCard.getCardNumber().substring(currentCard.getCardNumber().length() - 4)
                                    )
                                );
                            });
                        }
                    } else {
                        binding.cardItems.setVisibility(View.GONE);
                        binding.accountItems.setVisibility(View.VISIBLE);
                        binding.cardStatements.setVisibility(View.GONE);
                        if (!fullList.isEmpty())
                            binding.cardDetails.setOnClickListener(v -> presenter.goToAccountDetailsScreen(fullList.get(adapterPos).getObject()));
                    }
                }
            }
        });

        setBankCardList(obtainBankCards());

        binding.qrPayment1.setOnClickListener(v -> initiateScan());
        binding.qrPayment2.setOnClickListener(v -> initiateScan());
        binding.cardBetweenMyCardsAccounts.setOnClickListener(v -> presenter.goToOwnCardTransfersScreen());
        binding.cardBetweenMyCardsAccounts2.setOnClickListener(v -> presenter.goToOwnCardTransfersScreen());
        binding.toOtherCard.setOnClickListener(v -> presenter.goToOtherCardTransfersScreen(false));
        binding.toOtherCard2.setOnClickListener(v -> presenter.goToOtherCardTransfersScreen(false));
        binding.toOtherAccount.setOnClickListener(v -> presenter.goToOtherCardTransfersScreen(true));

        binding.cardMoneyTransfers.setVisibility(BuildConfig.FLAVOR == "dev" ? View.VISIBLE : View.GONE);
        binding.accountMoneyTransfers.setVisibility(BuildConfig.FLAVOR == "dev" ? View.VISIBLE : View.GONE);
        binding.cardMoneyTransfers.setOnClickListener(v -> presenter.goToMoneyTransfersScreen());
        binding.accountMoneyTransfers.setOnClickListener(v -> presenter.goToMoneyTransfersScreen());

        binding.payments.setOnClickListener(v -> presenter.goToPaymentsScreen());
        binding.payments2.setOnClickListener(v -> presenter.goToPaymentsScreen());
        
        binding.localTransfers.setOnClickListener(v -> presenter.goToLocalTransfers());
        binding.internationalTransfers.setOnClickListener(v -> presenter.goToInternationalTransfers());
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null)
                Utils.snackbar(binding.getRoot(), R.string.qr_code_scan_cancelled);
            else {
                Utils.modifyChildrenEnableStatus(binding.root, false);
                presenter.validateQrCode(result.getContents());
            }
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    private void setBankCardList(List<BankCard> cardList) {
        refreshBankCards(cardList);

        List<BankCardAndAccount> mapped = presenter.getMappedCards(cardList);
        this.fullList = mapped;
        cardsCount = cardList.size();

        if (cardsCount == 0)
            binding.cardStatements.setVisibility(View.GONE);

        cardsAccountsAdapter.submitList(mapped);

        binding.cardsAccountsList.setAdapter(cardsAccountsAdapter);
        binding.indicator.attachToRecyclerView(binding.cardsAccountsList, pagerSnapHelper);

        setBankAccountList(obtainBankAccounts());

        binding.cardDetails.setOnClickListener(v -> presenter.goToCardDetailsScreen(fullList.get(adapterPos).getObject()));
        binding.cardStatements.setOnClickListener(v -> {
            final BankCardAndAccount currentCard = cardsAccountsAdapter.getCurrentList().get(adapterPos);
            presenter.goToCardStatementsScreen(
                currentCard.getCardId(),
                String.format(
                    getString(R.string.operation_type_card_number),
                    currentCard.getCardFormattedName(),
                    currentCard.getCardNumber().substring(0, 4),
                    currentCard.getCardNumber().substring(currentCard.getCardNumber().length() - 4)
                )
            );
        });
    }

    private void setBankAccountList(List<BankAccount> accountList) {
        List<BankCardAndAccount> mapped = presenter.getMappedAccounts(accountList);
        fullList.addAll(mapped);
        cardsAccountsAdapter.submitList(fullList);

        binding.cardsAccountsList.setAdapter(cardsAccountsAdapter);
        binding.indicator.attachToRecyclerView(binding.cardsAccountsList, pagerSnapHelper);
    }

    @Override
    public void showError(@NonNull String msg) {
        if (!msg.isEmpty())
            Utils.snackbar(binding.getRoot(), msg);
    }

    @Override
    public void showError(int msg) {
        Utils.snackbar(binding.getRoot(), msg);
    }

    @Override
    public void showQrCodeErrorResult(int qrCodeValidationResultCode) {
        Utils.modifyChildrenEnableStatus(binding.root, true);
        switch (qrCodeValidationResultCode) {
            case Constants.QrCodeValidationResults.QR_CODE_FAILED:
                showQrCodeResultWindow(R.string.wrong_qr_code_input);
                break;
            case Constants.QrCodeValidationResults.NO_SUCH_PAYMENT_PROVIDER:
                showQrCodeResultWindow(R.string.payment_provider_not_exists);
                break;
            case Constants.QrCodeValidationResults.NONE:
            case Constants.QrCodeValidationResults.QR_CODE_SUCCESS:
            default:
                break;
        }
    }
    
    private void showQrCodeResultWindow(@StringRes int resultMsg) {
        final AlertDialog dialog = new AlertDialog.Builder(requireContext()).create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        
        final QrCodeVerificationResultDialogBinding resultDialogBinding =
            QrCodeVerificationResultDialogBinding.inflate(getLayoutInflater());
        
        resultDialogBinding.setMessage(getString(resultMsg));
        resultDialogBinding.closeDialog.setOnClickListener(close -> dialog.dismiss());
        resultDialogBinding.rescanQrCode.setOnClickListener(confirm -> {
            dialog.dismiss();
            initiateScan();
        });
        
        dialog.setView(resultDialogBinding.getRoot());
        dialog.setCancelable(false);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
    
    private void initiateScan() {
        Utils.forceBypassPinFingerprintScreen(requireActivity());
        
        IntentIntegrator.forSupportFragment(this)
            .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            .setPrompt("QR Pay")
            .setCameraId(0)
            .setOrientationLocked(false)
            .setBeepEnabled(false)
            .setBarcodeImageEnabled(false) // set to true to enable saving the barcode image and sending its path in the result Intent
            .initiateScan();
    }
}
