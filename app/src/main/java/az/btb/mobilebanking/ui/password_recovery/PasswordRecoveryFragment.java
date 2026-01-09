package az.btb.mobilebanking.ui.password_recovery;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import az.btb.mobilebanking.AppData;
import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentRecoveryPasswordBinding;
import az.btb.mobilebanking.databinding.IdCardFinCodeHelpLayoutBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.ForgotPasswordRequest;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.utils.Constants.PASSWORD_RECOVERY_TYPE_FIN;
import static az.btb.mobilebanking.utils.Constants.PASSWORD_RECOVERY_TYPE_PAN;

public class PasswordRecoveryFragment extends Fragment<FragmentRecoveryPasswordBinding> implements PasswordRecoveryView {

    private int passwordRecoveryType;

    @InjectPresenter PasswordRecoveryPresenter presenter;

    @ProvidePresenter PasswordRecoveryPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(PasswordRecoveryPresenter.class);
    }
    
    public PasswordRecoveryFragment() {
        super(R.layout.fragment_recovery_password, false);
    }
    
    public static PasswordRecoveryFragment getInstance(int passwordRecoveryType) {
        Bundle args = new Bundle(1);
        args.putInt("passwordRecoveryType", passwordRecoveryType);

        PasswordRecoveryFragment fragment = new PasswordRecoveryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        passwordRecoveryType = requireArguments().getInt("passwordRecoveryType");
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Utils.hideSubmitButton(binding.getRoot(), binding.submit);

        binding.goBack.setOnClickListener(v -> presenter.goBack());

        binding.finOrPan.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE))
                binding.submit.performClick();

            return false;
        });
        if (passwordRecoveryType == PASSWORD_RECOVERY_TYPE_PAN) {
            binding.finOrPan.setHint(R.string.password_recovery_enter_pan);
            binding.finOrPan.setInputType(InputType.TYPE_CLASS_TEXT);
            binding.finOrPan.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
            binding.finOrPan.setFilters(new InputFilter[] { new InputFilter.LengthFilter(4) });
            binding.finOrPan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        if (passwordRecoveryType == PASSWORD_RECOVERY_TYPE_FIN) {
            binding.finOrPan.setHint(R.string.password_recovery_enter_fin);
            binding.finOrPan.setFilters(new InputFilter[] { new InputFilter.LengthFilter(7) });
            binding.finOrPan.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_info_red, 0);
            binding.finOrPan.setOnTouchListener((v, event) -> {
                final int DRAWABLE_RIGHT = 2;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (binding.finOrPan.getRight() - binding.finOrPan.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        final IdCardFinCodeHelpLayoutBinding dialogViewBinding = IdCardFinCodeHelpLayoutBinding.inflate(getLayoutInflater());

                        Utils.showAlertDialogWith(dialogViewBinding.getRoot(), requireContext(), dialogViewBinding.closeDialog);

                        return true;
                    }
                }
                return false;
            });
        }

        binding.submit.setOnClickListener(v -> {
            if (isFormValid()) {
                showLoading(true);

                AppData.getInstance().getRequestInfo().getMobileUser().setUsername(binding.username.getText().toString());
                ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest(
                    AppData.getInstance().getRequestInfo(),
                    passwordRecoveryType,
                    binding.finOrPan.getText().toString()
                );
                presenter.send(forgotPasswordRequest);
            }
        });
    }

    private boolean isFormValid() {
        if (binding.username.getText().toString().trim().isEmpty()) {
            showError(getString(R.string.password_recovery_enter_username_is_empty));
            return false;
        }

        if (passwordRecoveryType == PASSWORD_RECOVERY_TYPE_PAN) {
            if (binding.finOrPan.getText().toString().length() != 4) {
                showError(getString(R.string.password_recovery_enter_pan_is_empty));
                return false;
            }
            return true;
        }

        if (passwordRecoveryType == PASSWORD_RECOVERY_TYPE_FIN) {
            if (binding.finOrPan.getText().toString().length() != 7) {
                showError(getString(R.string.password_recovery_enter_fin_is_empty));
                return false;
            }
            return true;
        }

        return false;
    }

    @Override
    public void showError(String responseMessage) {
        if (!responseMessage.isEmpty())
            Utils.snackbar(binding.getRoot(), responseMessage);
    }

    @Override
    public void showLoading(boolean check) {
        Utils.modifyChildrenEnableStatus(binding.root, !check);
        binding.progressBar.setVisibility(check ? View.VISIBLE : View.INVISIBLE);
        binding.submit.setEnabled(!check);
        binding.submit.setClickable(!check);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        
        Utils.forceBypassPinFingerprintScreen(requireActivity());
    }
}
