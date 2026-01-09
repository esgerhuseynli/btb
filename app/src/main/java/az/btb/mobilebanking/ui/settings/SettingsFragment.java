package az.btb.mobilebanking.ui.settings;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.yariksoffice.lingver.Lingver;

import az.btb.mobilebanking.BuildConfig;
import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FingerprintRemovalDialogBinding;
import az.btb.mobilebanking.databinding.FragmentSettingsBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static android.content.Intent.makeRestartActivityTask;
import static az.btb.mobilebanking.utils.Constants.LANGUAGE_CHANGE_EVENT_TOKEN;

public class SettingsFragment extends Fragment<FragmentSettingsBinding> implements SettingsView {

    public SettingsFragment() {
        super(R.layout.fragment_settings);
    }

    @NonNull
    public static SettingsFragment getInstance() {
        return new SettingsFragment();
    }

    @InjectPresenter SettingsPresenter presenter;

    @ProvidePresenter SettingsPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(SettingsPresenter.class);
    }

    private int previouslySelectedLangOption = 0;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.appVersionCode.setText(BuildConfig.VERSION_NAME);

        previouslySelectedLangOption = Utils.getAppLanguageReversed(Lingver.getInstance().getLanguage());

        if (Utils.isFingerprintServiceAvailable(getContext())) {
            binding.fingerprintSettingIcon.setVisibility(View.VISIBLE);
            binding.fingerprintSetting.setVisibility(View.VISIBLE);
            binding.fingerprintSettingStatusChange.setVisibility(View.VISIBLE);
            if (presenter.doesFingerprintLoginActivated()) {
                binding.fingerprintSettingStatusChange.setText(R.string.remove_fingerprint);
                binding.fingerprintSettingStatusChange.setOnClickListener(
                    v -> showFingerprintRemovalAlert()
                );
            } else
                binding.fingerprintSettingStatusChange.setOnClickListener(
                    v -> presenter.goToFingerprintActivationScreen()
                );
        }

        binding.goBack.setOnClickListener(v -> presenter.goBack());

        final String[] listItems = getResources().getStringArray(R.array.language_options);

        binding.language.setText(listItems[previouslySelectedLangOption]);
        binding.langChoice.setOnClickListener(v -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
            alertDialog.setSingleChoiceItems(listItems, previouslySelectedLangOption, (dialog, which) -> {
                if (previouslySelectedLangOption != which) {
                    previouslySelectedLangOption = which;

                    binding.language.setText(listItems[which]);

                    Lingver.getInstance().setLocale(requireContext(), Utils.getAppLanguage(which));

                    Intent intent = requireContext().getPackageManager().getLaunchIntentForPackage(requireContext().getPackageName());
                    ComponentName componentName = intent.getComponent();
                    Intent mainActivityIntent = makeRestartActivityTask(componentName);
                    mainActivityIntent.putExtra(LANGUAGE_CHANGE_EVENT_TOKEN, true);
                    startActivity(mainActivityIntent);

//                    startActivity(new Intent(requireContext(), MainActivity.class));
                }
                dialog.dismiss();
            });

            AlertDialog customAlertDialog = alertDialog.create();
            customAlertDialog.show();
        });

        binding.pin.setOnClickListener(v -> presenter.goToPinChangeScreen());
        binding.allowedDevices.setOnClickListener(v -> presenter.manageAllowedDevices());
        binding.aboutApp.setOnClickListener(v -> {
            String url = "https://www.btb.az/" + Lingver.getInstance().getLanguage() + "/license-agreement";
            Utils.openInBrowser(requireActivity(), url);
        });
    }

    private void showFingerprintRemovalAlert() {
        final AlertDialog dialog = new AlertDialog.Builder(requireContext()).create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        final FingerprintRemovalDialogBinding successDialogBinding =
            FingerprintRemovalDialogBinding.inflate(getLayoutInflater());

        successDialogBinding.no.setOnClickListener(v -> dialog.dismiss());
        successDialogBinding.yes.setOnClickListener(v -> {
            presenter.deactivateFingerprintLogin();
            dialog.dismiss();
            getParentFragmentManager().beginTransaction().detach(this).attach(this).commit();
        });

        dialog.setView(successDialogBinding.getRoot());
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
