package az.btb.mobilebanking.ui.sign_up_by_card;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yariksoffice.lingver.Lingver;

import az.btb.mobilebanking.AppData;
import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentSignUpByCardBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.CardSendRequest;
import az.btb.mobilebanking.utils.Constants;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import az.btb.mobilebanking.utils.card_edittext.OtherCardTextWatcher;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class SignUpByCardFragment extends Fragment<FragmentSignUpByCardBinding> implements SignUpByCardView {

    private static final int CARD_SCAN_REQUEST_CODE = 40;

    @InjectPresenter SignUpByCardPresenter presenter;

    @ProvidePresenter SignUpByCardPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(SignUpByCardPresenter.class);
    }
    
    public SignUpByCardFragment() {
        super(R.layout.fragment_sign_up_by_card, false);
    }
    
    public static SignUpByCardFragment getInstance() {
        return new SignUpByCardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        final String firstPart = getString(R.string.by_pressing_continue);
        final int firstPartLength = firstPart.length();
        spannableStringBuilder.append(firstPart);

        final String secondPart = getString(R.string.privacy_and_policy);
        final int secondPartLength = secondPart.length();
        spannableStringBuilder.append(secondPart);
        spannableStringBuilder.setSpan(
            new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)),
            firstPartLength + 1, firstPartLength + secondPartLength - 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        spannableStringBuilder.setSpan(
            new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    String url = "https://www.btb.az/" + Lingver.getInstance().getLanguage() + "/license-agreement";
//                    presenter.goToWebView(url);
                    Utils.openInBrowser(requireActivity(), url);
                }
            },
            firstPartLength + 1, firstPartLength + secondPartLength - 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        spannableStringBuilder.append(getString(R.string.accepting_with));

        binding.privacyPolicy.setText(spannableStringBuilder);
        binding.privacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.goBack.setOnClickListener(v -> presenter.goBack());

        // DO NOT REMOVE THIS LINE!!
        showLoading(true);

        binding.scanCard.setOnClickListener(v -> scanCard());

        Utils.modifyChildrenEnableStatus(binding.root, true);
        Utils.hideSubmitButton(binding.getRoot(), binding.nextButton);

        binding.pan.addTextChangedListener(new OtherCardTextWatcher(binding.pan, binding.nextButton));
        binding.pan.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE))
                binding.nextButton.performClick();

            return false;
        });
        binding.nextButton.setOnClickListener(v -> {
            showProgressBar(true);
            showLoading(true);

            CardSendRequest request = new CardSendRequest(
                AppData.getInstance().getRequestInfo(),
                Constants.SIGN_UP_TYPE_PAN,
                binding.pan.getText().toString().replaceAll("\\s+", ""),
                "", "", "", ""
            );
            presenter.signUp(request);
        });
    }

    @Override
    public void showLoading(boolean check) {
        Utils.modifyChildrenEnableStatus(binding.root, !check);
        binding.nextButton.setEnabled(!check);
        binding.nextButton.setClickable(!check);
    }

    @Override
    public void showError(String error) {
        if (!error.isEmpty())
            Utils.snackbar(binding.getRoot(), error);
    }

    @Override
    public void showProgressBar(boolean b) {
        binding.progressBar.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        
        Utils.forceBypassPinFingerprintScreen(requireActivity());
    }

    private void scanCard() {
        Utils.forceBypassPinFingerprintScreen(requireActivity());

        Intent scanIntent = new Intent(requireActivity(), CardIOActivity.class);

        //scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, false); // default: false
        //scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
        //scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false

        startActivityForResult(scanIntent, CARD_SCAN_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CARD_SCAN_REQUEST_CODE) {
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                final String cardNumber = scanResult.getFormattedCardNumber();
                binding.pan.setText(cardNumber == null ? "" : cardNumber);
            }
            else
                Utils.snackbar(binding.getRoot(), R.string.card_scan_cancelled);
        }
    }
}
