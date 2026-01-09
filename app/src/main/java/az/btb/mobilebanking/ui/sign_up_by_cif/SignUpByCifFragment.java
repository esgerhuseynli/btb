package az.btb.mobilebanking.ui.sign_up_by_cif;

import android.app.DatePickerDialog;
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
import androidx.fragment.app.DialogFragment;

import com.yariksoffice.lingver.Lingver;

import az.btb.mobilebanking.AppData;
import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentSignUpByCifBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.CardSendRequest;
import az.btb.mobilebanking.utils.Constants;
import az.btb.mobilebanking.utils.DatePickerFragment;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class SignUpByCifFragment extends Fragment<FragmentSignUpByCifBinding> implements SignUpByCifView {

    @InjectPresenter SignUpByCifPresenter presenter;

    @ProvidePresenter SignUpByCifPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(SignUpByCifPresenter.class);
    }
    
    public SignUpByCifFragment() {
        super(R.layout.fragment_sign_up_by_cif, false);
    }
    
    public static SignUpByCifFragment getInstance() {
        return new SignUpByCifFragment();
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
        Utils.hideSubmitButton(binding.getRoot(), binding.nextButton);

        final DatePickerDialog.OnDateSetListener listener = (view1, year, month, day) -> {
            binding.dateOfBirth.setText(
                String.format(
                    getString(R.string.date_of_birth_format),
                    day, month + 1, year
                )
            );
            Utils.hideKeyboardFrom(requireContext(), binding.getRoot());
            // perform submit button click when date is selected
            binding.nextButton.performClick();
        };

        binding.dateOfBirth.setOnClickListener(v -> {
            DialogFragment newFragment = new DatePickerFragment(binding.dateOfBirth.getText().toString(), listener);
            newFragment.show(getParentFragmentManager(), "signUpByCifDatePicker");
        });

        binding.goBack.setOnClickListener(v -> presenter.goBack());

        binding.cif.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_NEXT)) {
                Utils.hideKeyboardFrom(requireContext(), binding.getRoot());
                binding.dateOfBirth.performClick();
            }

            return false;
        });

        binding.nextButton.setOnClickListener(v -> {
            showLoading(true);

            if (binding.cif.length() == 6 && binding.dateOfBirth.length() > 0) {
                CardSendRequest request = new CardSendRequest(
                    AppData.getInstance().getRequestInfo(),
                    Constants.SIGN_UP_TYPE_CIF,
                    "",
                    binding.cif.getText().toString(), binding.dateOfBirth.getText().toString(),
                    "", ""
                );

                presenter.signUp(request);
            } else {
                showError(getString(R.string.fill_all_fields));
                showLoading(false);
            }
        });
    }

    @Override
    public void showLoading(boolean check) {
        Utils.modifyChildrenEnableStatus(binding.root, !check);
        binding.progressBar.setVisibility(check ? View.VISIBLE : View.INVISIBLE);
        binding.nextButton.setEnabled(!check);
        binding.nextButton.setClickable(!check);
    }

    @Override
    public void showError(String error) {
        if (!error.isEmpty())
            Utils.snackbar(binding.getRoot(), error);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        
        Utils.forceBypassPinFingerprintScreen(requireActivity());
    }
}
