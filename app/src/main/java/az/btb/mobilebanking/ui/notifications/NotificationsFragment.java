package az.btb.mobilebanking.ui.notifications;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.adapters.ItemsAdapter;
import az.btb.mobilebanking.databinding.FragmentNotificationsBinding;
import az.btb.mobilebanking.databinding.NotificationDetailsDialogBinding;
import az.btb.mobilebanking.databinding.NotificationsListItemBinding;
import az.btb.mobilebanking.models.UserNotification;
import az.btb.mobilebanking.utils.DatePickerFragment;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.ItemPropsBinder;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.di.Scopes.SERVER_SCOPE;

public class NotificationsFragment extends Fragment<FragmentNotificationsBinding> implements NotificationsView {

    @InjectPresenter NotificationsPresenter presenter;

    @ProvidePresenter NotificationsPresenter providePresenter() {
        return Toothpick.openScope(SERVER_SCOPE).getInstance(NotificationsPresenter.class);
    }

    public NotificationsFragment() {
        super(R.layout.fragment_notifications);
    }

    @NonNull
    public static NotificationsFragment getInstance() {
        return new NotificationsFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.goBack.setOnClickListener(v -> presenter.goBack());
        final Calendar calendar = Calendar.getInstance();
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int m = calendar.get(Calendar.MONTH);
        int y = calendar.get(Calendar.YEAR);

        Utils.setDateField(y, m, d, binding.fromDate);
        Utils.setDateField(y, m, d, binding.toDate);

        setFromToDateClickListener(binding.fromDate);
        setFromToDateClickListener(binding.toDate);

        presenter.getNotifications(binding.fromDate.getText().toString(), binding.toDate.getText().toString());
    }

    @Override
    public void showError(@NonNull String message) {
        if (!message.isEmpty())
            Utils.snackbar(binding.getRoot(), message);
    }

    @Override
    public void showNotifications(@NonNull /*Map<Long, */List<UserNotification>/*>*/ notificationList) {
        if (notificationList.size() == 0) {
            binding.progressBar.setVisibility(View.GONE);
            binding.groupedNotifications.setVisibility(View.GONE);
            binding.noItem.setVisibility(View.VISIBLE);
            return;
        }

        //Your RecyclerView
        binding.groupedNotifications.setHasFixedSize(true);
//        binding.groupedNotifications.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));

//        List<UserNotification> allNotifications = new ArrayList<>();
//        for (List<UserNotification> notifications : dateSortedMap.values())
//            allNotifications.addAll(notifications);

        ItemPropsBinder<NotificationsListItemBinding, UserNotification> binder = (binding, userNotification) -> {
            binding.setEventName(userNotification.getHeader());
            binding.setEventTimestamp(userNotification.getPublishingDate());

            binding.getRoot().setOnClickListener(v -> showNotificationDetails(userNotification));
        };

        //Your RecyclerView.Adapter
        ItemsAdapter<NotificationsListItemBinding, UserNotification> mAdapter = new ItemsAdapter<>(
            R.layout.notifications_list_item, notificationList, binder
        );

        // This is the code to provide a sectioned list
//        List<SectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();

//        List<Long> allDates = new ArrayList<>(dateSortedMap.keySet());
//        final int count = allDates.size();

        //Sections
//        sections.add(
//            new SectionedRecyclerViewAdapter.Section(
//                0,
//                Utils.dateFormatter.format(new Date(allDates.get(0)))
//            )
//        );

//        int previousPosition = 0;
//        for (int i = 1; i < count; i++) {
//            int position = previousPosition + dateSortedMap.get(allDates.get(i - 1)).size();
//            previousPosition = position;
//
//            sections.add(
//                new SectionedRecyclerViewAdapter.Section(
//                    position,
//                    Utils.dateFormatter.format(new Date(allDates.get(i)))
//                )
//            );
//        }

        //Add your adapter to the sectionAdapter
//        SectionedRecyclerViewAdapter.Section[] sectionsData = new SectionedRecyclerViewAdapter.Section[sections.size()];
//        SectionedRecyclerViewAdapter mSectionedAdapter = new
//            SectionedRecyclerViewAdapter(
//            requireContext(),
//            R.layout.notifications_list_header,
//            R.id.notification_date,
//            mAdapter
//        );
//        mSectionedAdapter.setSections(sections.toArray(sectionsData));

        // Apply this adapter to the RecyclerView
        binding.groupedNotifications.setAdapter(mAdapter);

        binding.progressBar.setVisibility(View.GONE);
        binding.noItem.setVisibility(View.GONE);
        binding.groupedNotifications.setVisibility(View.VISIBLE);
    }

    private void setFromToDateClickListener(@NonNull final TextView field) {
        final DatePickerDialog.OnDateSetListener listener = (view1, year, month, day) -> {
            Utils.setDateField(year, month, day, field);

            binding.progressBar.setVisibility(View.VISIBLE);

            // get exchange rates for selected date immediately
            presenter.getNotifications(
                binding.fromDate.getText().toString(),
                binding.toDate.getText().toString()
            );
        };

        field.setOnClickListener(v -> {
            DialogFragment newFragment = new DatePickerFragment(field.getText().toString(), listener);
            newFragment.show(getParentFragmentManager(), "notificationsDatePicker");
        });
    }

    private void showNotificationDetails(@NonNull UserNotification notification) {
        final NotificationDetailsDialogBinding successDialogBinding = NotificationDetailsDialogBinding.inflate(getLayoutInflater());
        successDialogBinding.setTitle(notification.getHeader());
        successDialogBinding.setDetails(notification.getText());

        Utils.showAlertDialogWith(successDialogBinding.getRoot(), requireContext(), successDialogBinding.closeDialog);
    }
}
