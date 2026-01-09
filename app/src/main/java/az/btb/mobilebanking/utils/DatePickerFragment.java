package az.btb.mobilebanking.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DatePickerFragment extends DialogFragment {
    private DatePickerDialog.OnDateSetListener listener;

    private final Calendar c = Calendar.getInstance();
    private int year = c.get(Calendar.YEAR);
    private int month = c.get(Calendar.MONTH);
    private int day = c.get(Calendar.DAY_OF_MONTH);

    public DatePickerFragment(@NonNull String previouslySelectedDate, DatePickerDialog.OnDateSetListener listener) {
        if (!previouslySelectedDate.isEmpty()) {
            try {
                final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                c.setTime(formatter.parse(previouslySelectedDate));
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
            } catch (Exception e) {  }
        }
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), listener, year, month, day);
        dialog.getDatePicker().setMaxDate(new Date().getTime());
        return dialog;
    }
}
