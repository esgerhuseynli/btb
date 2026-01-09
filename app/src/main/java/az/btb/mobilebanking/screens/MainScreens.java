package az.btb.mobilebanking.screens;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.math.BigDecimal;

import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import az.btb.mobilebanking.models.BankDeposit;
import az.btb.mobilebanking.models.BankLoan;
import az.btb.mobilebanking.models.BankNews;
import az.btb.mobilebanking.models.CheckTransferBeforeReceiveInfo;
import az.btb.mobilebanking.models.ForeignAccountTransfer;
import az.btb.mobilebanking.models.LocalAccountTransfer;
import az.btb.mobilebanking.models.NewMobileUserData;
import az.btb.mobilebanking.models.PaymentCommonInvoiceInfo;
import az.btb.mobilebanking.models.ProductOrder;
import az.btb.mobilebanking.models.ServicePoint;
import az.btb.mobilebanking.ui.allowed_devices.AllowedDevicesFragment;
import az.btb.mobilebanking.ui.card_statements.CardStatementsFragment;
import az.btb.mobilebanking.ui.change_email.ChangeEmailFragment;
import az.btb.mobilebanking.ui.change_password.ChangePasswordFragment;
import az.btb.mobilebanking.ui.change_phone_number.ChangePhoneNumberFragment;
import az.btb.mobilebanking.ui.contacts.ContactsFragment;
import az.btb.mobilebanking.ui.exchange_rates.ExchangeRatesFragment;
import az.btb.mobilebanking.ui.home.HomeFragment;
import az.btb.mobilebanking.ui.home_nav.HomeNavFragment;
import az.btb.mobilebanking.ui.international_transfers.InternationalTransfersFragment;
import az.btb.mobilebanking.ui.intro.IntroFragment;
import az.btb.mobilebanking.ui.local_transfers.LocalTransfersFragment;
import az.btb.mobilebanking.ui.money_transfers.MoneyTransfersFragment;
import az.btb.mobilebanking.ui.money_transfers.history.MoneyTransferHistoryFragment;
import az.btb.mobilebanking.ui.money_transfers.receive.step1.MoneyTransferReceiveStep1Fragment;
import az.btb.mobilebanking.ui.money_transfers.receive.step2.MoneyTransferReceiveStep2Fragment;
import az.btb.mobilebanking.ui.money_transfers.receive.step3.MoneyTransferReceiveStep3Fragment;
import az.btb.mobilebanking.ui.money_transfers.search.MoneyTransferSearchFragment;
import az.btb.mobilebanking.ui.my_items.my_account_info.MyAccountInfoFragment;
import az.btb.mobilebanking.ui.my_items.my_accounts.MyAccountsFragment;
import az.btb.mobilebanking.ui.my_items.my_card_info.MyCardInfoFragment;
import az.btb.mobilebanking.ui.my_items.my_cards.MyCardsFragment;
import az.btb.mobilebanking.ui.my_items.my_deposit_info.MyDepositInfoFragment;
import az.btb.mobilebanking.ui.my_items.my_deposits.MyDepositsFragment;
import az.btb.mobilebanking.ui.my_items.my_loan_info.MyLoanInfoFragment;
import az.btb.mobilebanking.ui.my_items.my_loans.MyLoansFragment;
import az.btb.mobilebanking.ui.news.NewsFragment;
import az.btb.mobilebanking.ui.news_details.NewsDetailsFragment;
import az.btb.mobilebanking.ui.notifications.NotificationsFragment;
import az.btb.mobilebanking.ui.operations_history.OperationsHistoryFragment;
import az.btb.mobilebanking.ui.operations_history.card_to_card.CardToCardHistoryFragment;
import az.btb.mobilebanking.ui.operations_history.international_transfer.InternationalTransfersHistoryFragment;
import az.btb.mobilebanking.ui.operations_history.international_transfer.details.InternationalTransferDetailsFragment;
import az.btb.mobilebanking.ui.operations_history.local_transfer.LocalTransfersHistoryFragment;
import az.btb.mobilebanking.ui.operations_history.local_transfer.details.LocalTransferDetailsFragment;
import az.btb.mobilebanking.ui.operations_history.payment.PaymentHistoryFragment;
import az.btb.mobilebanking.ui.other_card_transfers.OtherCardTransfersFragment;
import az.btb.mobilebanking.ui.own_card_transfers.OwnCardTransfersFragment;
import az.btb.mobilebanking.ui.payments.PaymentsFragment;
import az.btb.mobilebanking.ui.payments.obsiy.ObsiyPaymentFragment;
import az.btb.mobilebanking.ui.payments.payment_info.PaymentInfoFragment;
import az.btb.mobilebanking.ui.payments.payments_providers.PaymentProvidersFragment;
import az.btb.mobilebanking.ui.payments.payments_source_selection.PaymentsSourceSelectionFragment;
import az.btb.mobilebanking.ui.pin_change.PinChangeFragment;
import az.btb.mobilebanking.ui.products.ProductsFragment;
import az.btb.mobilebanking.ui.products.deposits.ProductDepositsFragment;
import az.btb.mobilebanking.ui.products.deposits.order.ProductOrderDepositFragment;
import az.btb.mobilebanking.ui.products.details.ProductDetailsFragment;
import az.btb.mobilebanking.ui.products.loans.ProductLoansFragment;
import az.btb.mobilebanking.ui.products.loans.order.ProductOrderLoanFragment;
import az.btb.mobilebanking.ui.products.orders.ProductOrdersFragment;
import az.btb.mobilebanking.ui.products.orders.order_details.ProductOrderDetailsFragment;
import az.btb.mobilebanking.ui.products.orders.order_payment.ProductOrderPaymentFragment;
import az.btb.mobilebanking.ui.products.plastic_cards.ProductPlasticCardsFragment;
import az.btb.mobilebanking.ui.products.plastic_cards.order.ProductOrderPlasticCardFragment;
import az.btb.mobilebanking.ui.products.references.ProductReferencesFragment;
import az.btb.mobilebanking.ui.products.references.embassy_references.order.ProductOrderEmbassyReferenceFragment;
import az.btb.mobilebanking.ui.products.references.financial_references.order.ProductOrderFinancialReferenceFragment;
import az.btb.mobilebanking.ui.profile.ProfileFragment;
import az.btb.mobilebanking.ui.service_points.ServicePointsFragment;
import az.btb.mobilebanking.ui.service_points.atms.AtmDetailsFragment;
import az.btb.mobilebanking.ui.service_points.branches.BranchDetailsFragment;
import az.btb.mobilebanking.ui.settings.SettingsFragment;
import az.btb.mobilebanking.ui.transfer_submission.TransferSubmissionFragment;
import az.btb.mobilebanking.ui.transfers.TransfersFragment;
import az.btb.mobilebanking.ui.verify_profile_changes.VerifyProfileChangesFragment;
import az.btb.mobilebanking.utils.Constants;
import az.btb.mobilebanking.utils.OtherCardTransferData4Accounts;
import az.btb.mobilebanking.utils.PaymentInfoData;
import az.btb.mobilebanking.utils.Product;
import ru.terrakok.cicerone.android.support.SupportAppScreen;

public class MainScreens {
	public static final class IntroScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return IntroFragment.getInstance();
		}
	}
	
	public static final class HomeScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return HomeFragment.getInstance();
		}
	}
	
	public static final class HomeNavScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return HomeNavFragment.getInstance();
		}
	}
	
	public static final class NotificationsScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return NotificationsFragment.getInstance();
		}
	}
	
	public static final class ProfileScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return ProfileFragment.getInstance();
		}
	}
	
	public static final class MyCardsScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return MyCardsFragment.getInstance();
		}
	}
	
	public static final class MyCardInfoScreen extends SupportAppScreen {
		private final BankCard bankCard;
		
		public MyCardInfoScreen(BankCard bankCard) {
			this.bankCard = bankCard;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return MyCardInfoFragment.getInstance(bankCard);
		}
	}
	
	public static final class MyAccountsScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return MyAccountsFragment.getInstance();
		}
	}
	
	public static final class MyAccountInfoScreen extends SupportAppScreen {
		private final BankAccount bankAccount;
		
		public MyAccountInfoScreen(BankAccount bankAccount) {
			this.bankAccount = bankAccount;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return MyAccountInfoFragment.getInstance(bankAccount);
		}
	}
	
	public static final class MyLoansScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return MyLoansFragment.getInstance();
		}
	}
	
	public static final class MyLoanInfoScreen extends SupportAppScreen {
		private final BankLoan bankLoan;
		
		public MyLoanInfoScreen(BankLoan bankLoan) {
			this.bankLoan = bankLoan;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return MyLoanInfoFragment.getInstance(bankLoan);
		}
	}
	
	public static final class MyDepositsScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return MyDepositsFragment.getInstance();
		}
	}
	
	public static final class MyDepositInfoScreen extends SupportAppScreen {
		
		private final BankDeposit bankDeposit;
		
		public MyDepositInfoScreen(BankDeposit bankDeposit) {
			this.bankDeposit = bankDeposit;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return MyDepositInfoFragment.getInstance(bankDeposit);
		}
	}
	
	public static final class ProductsScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return ProductsFragment.getInstance();
		}
	}
	
	public static final class ExchangeRatesScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return ExchangeRatesFragment.getInstance();
		}
	}
	
	public static final class NewsScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return NewsFragment.getInstance();
		}
	}
	
	public static final class NewsDetailsScreen extends SupportAppScreen {
		private final BankNews bankNews;
		
		public NewsDetailsScreen(BankNews bankNews) {
			this.bankNews = bankNews;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return NewsDetailsFragment.getInstance(bankNews);
		}
	}

//    public static final class ProposalsScreen extends SupportAppScreen {
//        @Override
//        public Fragment getFragment() {
//            return ProposalsFragment.getInstance();
//        }
//    }
	
	public static final class ServicePointsScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return ServicePointsFragment.getInstance();
		}
	}
	
	public static final class AtmDetailsScreen extends SupportAppScreen {
		private final ServicePoint point;
		
		public AtmDetailsScreen(ServicePoint atmPoint) {
			point = atmPoint;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return AtmDetailsFragment.getInstance(point);
		}
	}
	
	public static final class BranchDetailsScreen extends SupportAppScreen {
		private final ServicePoint point;
		
		public BranchDetailsScreen(ServicePoint branchPoint) {
			point = branchPoint;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return BranchDetailsFragment.getInstance(point);
		}
	}
	
	public static final class ContactsScreen extends SupportAppScreen {
		@Override
		public Fragment getFragment() {
			return ContactsFragment.getInstance();
		}
	}
	
	public static final class ChangeEmailScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return ChangeEmailFragment.getInstance();
		}
	}
	
	public static final class ChangePhoneNumberScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return ChangePhoneNumberFragment.getInstance();
		}
	}
	
	public static final class ChangePasswordScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return ChangePasswordFragment.getInstance();
		}
	}
	
	public static final class ProfileUpdateVerificationScreen extends SupportAppScreen {
		private final NewMobileUserData newMobileUserData;
		
		public ProfileUpdateVerificationScreen(NewMobileUserData newMobileUserData) {
			this.newMobileUserData = newMobileUserData;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return VerifyProfileChangesFragment.getInstance(newMobileUserData);
		}
	}
	
	public static final class SettingsScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return SettingsFragment.getInstance();
		}
	}
	
	public static final class AllowedDevicesScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return AllowedDevicesFragment.getInstance();
		}
	}
	
	public static final class PinChangeScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return PinChangeFragment.getInstance();
		}
	}
	
	public static final class PaymentsScreen extends SupportAppScreen {
		private final boolean isComeFromBottomMenu;
		
		public PaymentsScreen(boolean isComeFromBottomMenu) {
			this.isComeFromBottomMenu = isComeFromBottomMenu;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return PaymentsFragment.getInstance(isComeFromBottomMenu);
		}
	}
	
	public static final class OperationsHistoryScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return OperationsHistoryFragment.getInstance();
		}
	}
	
	public static final class CardToCardHistoryScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return CardToCardHistoryFragment.getInstance();
		}
	}
	
	public static final class OwnCardTransfersScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return OwnCardTransfersFragment.getInstance();
		}
	}
	
	public static final class TransferSubmissionScreen extends SupportAppScreen {
		
		private final OtherCardTransferData4Accounts data;
		
		public TransferSubmissionScreen(OtherCardTransferData4Accounts data) {
			this.data = data;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return TransferSubmissionFragment.getInstance(data);
		}
	}
	
	public static final class CardStatementsScreen extends SupportAppScreen {
		private final String cardId;
		private final String cardName;
		
		public CardStatementsScreen(final String fromCardId, final String fromCardName) {
			cardId = fromCardId;
			cardName = fromCardName;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return CardStatementsFragment.getInstance(cardId, cardName);
		}
	}
	
	public static final class OtherCardTransfersScreen extends SupportAppScreen {
		private final boolean isToAccount;
		
		public OtherCardTransfersScreen(final boolean isToAccount) {
			this.isToAccount = isToAccount;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return OtherCardTransfersFragment.getInstance(isToAccount);
		}
	}
	
	public static final class MoneyTransfersScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return MoneyTransfersFragment.getInstance();
		}
	}
	
	public static final class MoneyTransferringStep1Screen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return az.btb.mobilebanking.ui.money_transfers.transferring.step1.MoneyTransferringFragment.getInstance();
		}
	}
	
	public static final class MoneyTransferringStep2Screen extends SupportAppScreen {
		private final MoneyTransfersFragment.MoneyTransferData moneyTransferData;
		
		public MoneyTransferringStep2Screen(MoneyTransfersFragment.MoneyTransferData moneyTransferData) {
			this.moneyTransferData = moneyTransferData;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return az.btb.mobilebanking.ui.money_transfers.transferring.step2.MoneyTransferringFragment.getInstance(moneyTransferData);
		}
	}
	
	public static final class MoneyTransferringStep3Screen extends SupportAppScreen {
		private final MoneyTransfersFragment.MoneyTransferData moneyTransferData;
		private final MoneyTransfersFragment.MoneyTransferReceiverData moneyTransferReceiverData;
		private final BigDecimal calculatedCommission;
		
		public MoneyTransferringStep3Screen(
			final MoneyTransfersFragment.MoneyTransferData moneyTransferData,
			final MoneyTransfersFragment.MoneyTransferReceiverData moneyTransferReceiverData,
			final BigDecimal calculatedCommission
		) {
			this.moneyTransferData = moneyTransferData;
			this.moneyTransferReceiverData = moneyTransferReceiverData;
			this.calculatedCommission = calculatedCommission;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return az.btb.mobilebanking.ui.money_transfers.transferring.step3.MoneyTransferringFragment.getInstance(
				moneyTransferData,
				moneyTransferReceiverData,
				calculatedCommission
			);
		}
	}
	
	public static final class MoneyTransferReceiveStep1Screen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return MoneyTransferReceiveStep1Fragment.getInstance();
		}
	}
	
	public static final class MoneyTransferReceiveStep2Screen extends SupportAppScreen {
		private final CheckTransferBeforeReceiveInfo data;
		
		public MoneyTransferReceiveStep2Screen(final CheckTransferBeforeReceiveInfo checkTransferBeforeReceiveInfo) {
			data = checkTransferBeforeReceiveInfo;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return MoneyTransferReceiveStep2Fragment.getInstance(data);
		}
	}
	
	public static final class MoneyTransferReceiveStep3Screen extends SupportAppScreen {
		private final CheckTransferBeforeReceiveInfo checkTransferBeforeReceiveInfo;
		private final int transferPaymentType;
		private final String id;
		private final String formatted;
		
		public MoneyTransferReceiveStep3Screen(CheckTransferBeforeReceiveInfo checkTransferBeforeReceiveInfo, int transferPaymentType, String id, String formatted) {
			this.checkTransferBeforeReceiveInfo = checkTransferBeforeReceiveInfo;
			this.transferPaymentType = transferPaymentType;
			this.id = id;
			this.formatted = formatted;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return MoneyTransferReceiveStep3Fragment.getInstance(checkTransferBeforeReceiveInfo, transferPaymentType, id, formatted);
		}
	}
	
	public static final class MoneyTransferHistoryScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return MoneyTransferHistoryFragment.getInstance();
		}
	}
	
	public static final class MoneyTransferSearchScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return MoneyTransferSearchFragment.getInstance();
		}
	}
	
	public static final class PaymentProvidersScreen extends SupportAppScreen {
		private final int paymentProviderGroupId;
		private final String paymentProviderGroupName;
		
		public PaymentProvidersScreen(final int paymentProviderGroupId, final String paymentProviderGroupName) {
			this.paymentProviderGroupId = paymentProviderGroupId;
			this.paymentProviderGroupName = paymentProviderGroupName;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return PaymentProvidersFragment.getInstance(paymentProviderGroupId, paymentProviderGroupName);
		}
	}
	
	public static final class ObsiyPaymentsScreen extends SupportAppScreen {
		private final int providerGroupId;
		private final int providerId;
		private final String paymentProviderName;
		
		public ObsiyPaymentsScreen(int providerGroupId, int providerId, String paymentProviderName) {
			this.providerGroupId = providerGroupId;
			this.providerId = providerId;
			this.paymentProviderName = paymentProviderName;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return ObsiyPaymentFragment.getInstance(providerGroupId, providerId, paymentProviderName);
		}
	}
	
	public static final class PaymentsSourceSelectionScreen extends SupportAppScreen {
		private final String paymentProviderName;
		private final PaymentCommonInvoiceInfo paymentCommonInvoiceInfo;
		private final int providerId;
		private final boolean isQrCodeScanned;
		private final String qrCodeValue;
		private final PaymentInfoData pid;
		
		public PaymentsSourceSelectionScreen(
			final String paymentProviderName,
			final PaymentCommonInvoiceInfo paymentCommonInvoiceInfo,
			final int providerId,
			final boolean isQrCodeScanned,
			final String qrCodeValue,
			final PaymentInfoData pid
		) {
			this.paymentProviderName = paymentProviderName;
			this.paymentCommonInvoiceInfo = paymentCommonInvoiceInfo;
			this.providerId = providerId;
			this.isQrCodeScanned = isQrCodeScanned;
			this.qrCodeValue = qrCodeValue;
			this.pid = pid;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return PaymentsSourceSelectionFragment.getInstance(
				paymentProviderName,
				paymentCommonInvoiceInfo,
				providerId,
				isQrCodeScanned,
				qrCodeValue,
				pid
			);
		}
	}

	public static final class PaymentInfoScreen extends SupportAppScreen {
		private final @NonNull PaymentCommonInvoiceInfo paymentCommonInvoiceInfo;
		private final int providerId;
		private final @Constants.MoneySourceTypes int sourceType;
		private final String fromIdCard;
		private final String fromIbanAccount;
		private final BigDecimal amount;
		private final boolean isQrCodeScanned;
		private final String qrCodeValue;
		private final PaymentInfoData pid;
		private final boolean isMultiInvoicePayment;
		
		public PaymentInfoScreen(
			@NonNull PaymentCommonInvoiceInfo paymentCommonInvoiceInfo, int providerId,
			@Constants.MoneySourceTypes int sourceType, String fromIdCard, String fromIbanAccount,
			BigDecimal amount, boolean isQrCodeScanned, String qrCodeValue, PaymentInfoData pid,
			boolean isMultiInvoicePayment
		) {
			this.paymentCommonInvoiceInfo = paymentCommonInvoiceInfo;
			this.providerId = providerId;
			this.sourceType = sourceType;
			this.fromIdCard = fromIdCard;
			this.fromIbanAccount = fromIbanAccount;
			this.amount = amount;
			this.isQrCodeScanned = isQrCodeScanned;
			this.qrCodeValue = qrCodeValue;
			this.pid = pid;
			this.isMultiInvoicePayment = isMultiInvoicePayment;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return PaymentInfoFragment.getInstance(
				paymentCommonInvoiceInfo,
				providerId,
				sourceType,
				fromIdCard,
				fromIbanAccount,
				amount,
				isQrCodeScanned,
				qrCodeValue,
				pid,
				isMultiInvoicePayment
			);
		}
	}
	
	public static final class ProductOrdersScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return ProductOrdersFragment.getInstance();
		}
	}
	
	public static final class ProductPlasticCardsScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return ProductPlasticCardsFragment.getInstance();
		}
	}
	
	public static final class ProductLoansScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return ProductLoansFragment.getInstance();
		}
	}
	
	public static final class ProductDepositsScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return ProductDepositsFragment.getInstance();
		}
	}
	
	public static final class ProductReferencesScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return ProductReferencesFragment.getInstance();
		}
	}
	
	public static final class ProductDetailsScreen extends SupportAppScreen {
		private final Product product;
		
		public ProductDetailsScreen(final Product product) {
			this.product = product;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return ProductDetailsFragment.getInstance(product);
		}
	}
	
	public static final class ProductOrderPlasticCardScreen extends SupportAppScreen {
		private final int productId;
		private final String productHeaderName;
		
		public ProductOrderPlasticCardScreen(final int productId, String productHeaderName) {
			this.productId = productId;
			this.productHeaderName = productHeaderName;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return ProductOrderPlasticCardFragment.getInstance(productId, productHeaderName);
		}
	}
	
	public static final class ProductOrderLoanScreen extends SupportAppScreen {
		private final int productId;
		private final String productHeaderName;
		private final @NonNull Product.OrderData orderData;
		
		public ProductOrderLoanScreen(final int productId, final String productHeaderName, @NonNull Product.OrderData orderData) {
			this.productId = productId;
			this.productHeaderName = productHeaderName;
			this.orderData = orderData;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return ProductOrderLoanFragment.getInstance(productId, productHeaderName, orderData);
		}
	}
	
	public static final class ProductOrderDepositScreen extends SupportAppScreen {
		private final int productId;
		private final String productHeaderName;
		private final @NonNull Product.OrderData orderData;
		
		public ProductOrderDepositScreen(final int productId, final String productHeaderName, @NonNull Product.OrderData orderData) {
			this.productId = productId;
			this.productHeaderName = productHeaderName;
			this.orderData = orderData;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return ProductOrderDepositFragment.getInstance(productId, productHeaderName, orderData);
		}
	}
	
	public static final class ProductOrderEmbassyReferenceScreen extends SupportAppScreen {
		private final int productId;
		private final String productHeaderName;
		
		public ProductOrderEmbassyReferenceScreen(final int productId, final String productHeaderName) {
			this.productId = productId;
			this.productHeaderName = productHeaderName;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return ProductOrderEmbassyReferenceFragment.getInstance(productId, productHeaderName);
		}
	}
	
	public static final class ProductOrderFinancialReferenceScreen extends SupportAppScreen {
		private final int productId;
		private final String productHeaderName;
		
		public ProductOrderFinancialReferenceScreen(final int productId, final String productHeaderName) {
			this.productId = productId;
			this.productHeaderName = productHeaderName;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return ProductOrderFinancialReferenceFragment.getInstance(productId, productHeaderName);
		}
	}
	
	public static final class ProductOrderDetailsScreen extends SupportAppScreen {
		private final ProductOrder product;
		
		public ProductOrderDetailsScreen(ProductOrder product) {
			this.product = product;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return ProductOrderDetailsFragment.getInstance(product);
		}
	}
	
	public static final class ProductOrderPaymentScreen extends SupportAppScreen {
		private final ProductOrder product;
		
		public ProductOrderPaymentScreen(ProductOrder product) {
			this.product = product;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return ProductOrderPaymentFragment.getInstance(product);
		}
	}
	
	public static final class TransfersScreen extends SupportAppScreen {
		private final boolean isComeFromBottomMenu;
		
		public TransfersScreen(boolean isComeFromBottomMenu) {
			this.isComeFromBottomMenu = isComeFromBottomMenu;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return TransfersFragment.getInstance(isComeFromBottomMenu);
		}
	}
	
	public static final class InternationalTransfersScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return InternationalTransfersFragment.getInstance();
		}
	}
	
	public static final class LocalTransfersScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return LocalTransfersFragment.getInstance();
		}
	}
	
	public static final class InternationalTransfersHistoryScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return InternationalTransfersHistoryFragment.getInstance();
		}
	}
	
	public static final class InternationalTransferDetailsScreen extends SupportAppScreen {
		private final ForeignAccountTransfer transferItem;
		
		public InternationalTransferDetailsScreen(final ForeignAccountTransfer transferItem) {
			this.transferItem = transferItem;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return InternationalTransferDetailsFragment.getInstance(transferItem);
		}
	}
	
	public static final class LocalTransfersHistoryScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return LocalTransfersHistoryFragment.getInstance();
		}
	}
	
	public static final class LocalTransferDetailsScreen extends SupportAppScreen {
		private final LocalAccountTransfer transferItem;
		
		public LocalTransferDetailsScreen(final LocalAccountTransfer transferItem) {
			this.transferItem = transferItem;
		}
		
		@NonNull
		@Override
		public Fragment getFragment() {
			return LocalTransferDetailsFragment.getInstance(transferItem);
		}
	}


	public static final class PaymentHistoryScreen extends SupportAppScreen {
		@NonNull
		@Override
		public Fragment getFragment() {
			return PaymentHistoryFragment.getInstance();
		}
	}

//	public static final class WebViewScreen extends SupportAppScreen {
//		private final String url;
//
//		public WebViewScreen(String url) {
//			this.url = url;
//		}
//
//		@NonNull
//		@Override
//		public Fragment getFragment() {
//			return WebViewFragment.newInstance(url);
//		}
//	}
}
