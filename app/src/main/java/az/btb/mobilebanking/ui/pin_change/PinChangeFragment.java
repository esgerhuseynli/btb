package az.btb.mobilebanking.ui.pin_change;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentChangePinBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class PinChangeFragment extends Fragment<FragmentChangePinBinding> implements PinChangeView, View.OnTouchListener {

    private String pin = "";
    private String pin1 = "";
    private String pin2 = "";

    private boolean buttons = true;

    @InjectPresenter PinChangePresenter presenter;

    @ProvidePresenter PinChangePresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(PinChangePresenter.class);
    }

    public PinChangeFragment() {
        super(R.layout.fragment_change_pin);
    }

    @NonNull
    public static PinChangeFragment getInstance() {
        return new PinChangeFragment();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.numberX.setOnClickListener(v -> {
            binding.pinText.setText(getResources().getString(R.string.put_pin_code));

            pin = "";
            pin1 = "";
            pin2 = "";
            clearPins();
        });

        binding.number0.setOnTouchListener(this);
        binding.number1.setOnTouchListener(this);
        binding.number2.setOnTouchListener(this);
        binding.number3.setOnTouchListener(this);
        binding.number4.setOnTouchListener(this);
        binding.number5.setOnTouchListener(this);
        binding.number6.setOnTouchListener(this);
        binding.number7.setOnTouchListener(this);
        binding.number8.setOnTouchListener(this);
        binding.number9.setOnTouchListener(this);
    }

    private void clearPins() {
        binding.pin1.setBackground(Utils.getDrawable(requireContext(), R.drawable.pin_count_not_selected));
        binding.pin2.setBackground(Utils.getDrawable(requireContext(), R.drawable.pin_count_not_selected));
        binding.pin3.setBackground(Utils.getDrawable(requireContext(), R.drawable.pin_count_not_selected));
        binding.pin4.setBackground(Utils.getDrawable(requireContext(), R.drawable.pin_count_not_selected));
    }

    @Override
    public void showError(String error) {
        binding.pinText2.setVisibility(View.VISIBLE);
        binding.pinText2.setText(error);
    }

    @Override
    public void disableButtons(boolean disabled) {
        buttons = false;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (buttons) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setBackground(Utils.getDrawable(requireContext(), R.drawable.shape_pin_number_selected));
                pin = pin + v.getTag();
                final int pinLength = pin.length();
                if (pinLength == 1)
                    binding.pin1.setBackground(Utils.getDrawable(requireContext(), R.drawable.pin_count_selected));
                else if (pinLength == 2)
                    binding.pin2.setBackground(Utils.getDrawable(requireContext(), R.drawable.pin_count_selected));
                else if (pinLength == 3)
                    binding.pin3.setBackground(Utils.getDrawable(requireContext(), R.drawable.pin_count_selected));
                else if (pinLength == 4) {
                    if (!pin1.isEmpty()) {
                        pin2 = pin;
                        pin = "";
                        binding.pin4.setBackground(Utils.getDrawable(requireContext(), R.drawable.pin_count_selected));
                    }
                    if (pin1.isEmpty()) {
                        pin1 = pin;
                        pin = "";
                        binding.pinText.setText(R.string.put_pin_code_again);
                        clearPins();
                    }
                }

                if (pin1.length() == 4 && pin2.length() == 4) {
                    if (!pin1.equals(pin2)) {
                        binding.numberX.performClick();
                        binding.pinText.setText(R.string.put_pin_wrong);
                    } else {
                        binding.pinText2.setText(R.string.put_pin_correct);

                        presenter.changePin(pin2);

                        v.setBackground(Utils.getDrawable(requireContext(), R.drawable.shape_pin_number_not_selected));
                    }
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP)
                v.setBackground(Utils.getDrawable(requireContext(), R.drawable.shape_pin_number_not_selected));
        }
        return true;
    }
}
