package az.btb.mobilebanking.ui.allowed_devices;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import java.util.Iterator;
import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.adapters.ItemsAdapter;
import az.btb.mobilebanking.databinding.DeviceOperationDialogBinding;
import az.btb.mobilebanking.databinding.FragmentAllowedDevicesBinding;
import az.btb.mobilebanking.databinding.OtherDevicesListItemBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.MobileDevice;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.ItemPropsBinder;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.utils.Constants.DEVICE_STATUS_DISABLE;
import static az.btb.mobilebanking.utils.Constants.DEVICE_STATUS_ENABLE;

public class AllowedDevicesFragment extends Fragment<FragmentAllowedDevicesBinding> implements AllowedDevicesView {

    @NonNull
    public static AllowedDevicesFragment getInstance() {
        return new AllowedDevicesFragment();
    }

    @InjectPresenter AllowedDevicesPresenter presenter;

    @ProvidePresenter AllowedDevicesPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(AllowedDevicesPresenter.class);
    }

    public AllowedDevicesFragment() {
        super(R.layout.fragment_allowed_devices);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.setIsLoadingDone(View.GONE);

        binding.goBack.setOnClickListener(v -> presenter.goBack());
        presenter.getAllDevices();
    }

    @Override
    public void showAllDevices(@NonNull List<MobileDevice> devices) {
        binding.progressBar.setVisibility(View.GONE);
        binding.setIsLoadingDone(View.VISIBLE);

        for (Iterator<MobileDevice> iterator = devices.iterator(); iterator.hasNext(); ) {
            MobileDevice currentDevice = iterator.next();

            if (currentDevice.getCurrentDevice() == 1) {
                binding.currentDevice.setText(currentDevice.getUserFriendlyName());
                iterator.remove();
            }

            // we should display both disabled & enabled devices
            //if (currentDevice.getDeviceStatus() != 1)
            //    iterator.remove();
        }

        ItemPropsBinder<OtherDevicesListItemBinding, MobileDevice> itemPropsBinder = (binding, device) -> {
            binding.device.setText(device.getUserFriendlyName());

            if (device.getDeviceStatus() == DEVICE_STATUS_DISABLE)
                binding.disableDevice.setVisibility(View.GONE);
            else
                binding.enableDevice.setVisibility(View.GONE);

            binding.disableDevice.setOnClickListener(v -> showDeviceOperationAlert(device, DEVICE_STATUS_DISABLE));
            binding.enableDevice.setOnClickListener(v -> showDeviceOperationAlert(device, DEVICE_STATUS_ENABLE));
        };
        ItemsAdapter<OtherDevicesListItemBinding, MobileDevice> adapter =
            new ItemsAdapter<>(R.layout.other_devices_list_item, devices, itemPropsBinder);
        binding.devicesList.setAdapter(adapter);
    }

    @Override
    public void showError(String msg) {
        if (!msg.isEmpty())
            Utils.snackbar(binding.getRoot(), msg);
    }

    public void showResultMsg(@StringRes int msg) {
        Utils.snackbar(binding.getRoot(), msg);
    }

    private void showDeviceOperationAlert(@NonNull MobileDevice device, int newStatus) {
        final AlertDialog dialog = new AlertDialog.Builder(requireContext()).create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        final DeviceOperationDialogBinding successDialogBinding = DeviceOperationDialogBinding.inflate(getLayoutInflater());
        successDialogBinding.setDeviceName(device.getUserFriendlyName());
        successDialogBinding.setNewStatus(newStatus);

        successDialogBinding.no.setOnClickListener(v -> dialog.dismiss());
        successDialogBinding.yes.setOnClickListener(v -> {
            dialog.dismiss();

            binding.progressBar.setVisibility(View.VISIBLE);
            presenter.changeDeviceStatus(device.getDeviceID(), newStatus);
        });

        dialog.setView(successDialogBinding.getRoot());
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
