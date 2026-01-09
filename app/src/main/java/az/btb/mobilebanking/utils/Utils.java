package az.btb.mobilebanking.utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;

import az.btb.mobilebanking.AppData;
import az.btb.mobilebanking.R;
import az.btb.mobilebanking.models.RequestInfo;

import static android.content.Context.KEYGUARD_SERVICE;
import static az.btb.mobilebanking.utils.Constants.ALLOWED_LOGIN_TIMEOUT;
import static az.btb.mobilebanking.utils.Constants.Currency.AZN;
import static az.btb.mobilebanking.utils.Constants.Currency.EUR;
import static az.btb.mobilebanking.utils.Constants.Currency.GBP;
import static az.btb.mobilebanking.utils.Constants.Currency.RUB;
import static az.btb.mobilebanking.utils.Constants.Currency.TRY;
import static az.btb.mobilebanking.utils.Constants.Currency.USD;
import static az.btb.mobilebanking.utils.Constants.FCM_NOTIFICATION_TOKEN;
import static az.btb.mobilebanking.utils.Constants.KEY_PIN_FINGERPRINT_SCREEN_BYPASS;

public class Utils {

    public static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ROOT);

    private static final Map<Integer, String> langCodes = new HashMap<>();

    static {
        langCodes.put(0, "az");
        langCodes.put(1, "en");
        langCodes.put(2, "ru");
    }

    @NonNull
    public static String passwordHash(@NonNull String password) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
            byte[] digest = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest)
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));

            return sb.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static boolean shouldReverse = false;

    @NonNull
    public static String appHash() {
        String template = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        if (shouldReverse)
            template = "AB0CD1EF2GH3IJ4KL5MN6OP7QR8ST9UVWXYZ";

        shouldReverse = !shouldReverse;

        SecureRandom rnd = new SecureRandom();
        int len = 128;

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(template.charAt(rnd.nextInt(template.length())));
        return sb.toString();
    }

    public static void snackbar(View view, @StringRes int msg) {
        Snackbar
            .make(view, msg, Snackbar.LENGTH_LONG)
            .setTextColor(view.getContext().getResources().getColor(R.color.white))
            .show();
    }

    public static void snackbar(View view, String msg) {
        Snackbar
            .make(view, msg, Snackbar.LENGTH_LONG)
            .setTextColor(view.getContext().getResources().getColor(R.color.white))
            .show();
    }

    public static void toast(Context ctx, @StringRes int msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }
    
    public static void toast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }
    
    /**
     * Checks whether user's device supports Fingerprint Enrollment or not.
     *
     * @param context an application context
     * @return `true` if device has fingerprint sensor &&
     *                device running on higher than Android 6.0 &&
     *                user has been enabled fingerprint in his/her device settings,
     *         `false` in all other cases.
     */
    public static boolean isFingerprintServiceAvailable(Context context) {
        // because of fingerprint api only available on from Android 6.0
        // we should check if we're running on Android 6.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            KeyguardManager keyguardManager =
                (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
            FingerprintManager fingerprintManager =
                (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
            if (fingerprintManager == null || !fingerprintManager.isHardwareDetected() ||
                keyguardManager == null || !keyguardManager.isKeyguardSecure())
                return false;
            else
                // user has enrolled fingerprints to authenticate with
                // and everything is ready for fingerprint authentication.
                return fingerprintManager.hasEnrolledFingerprints();
        }

        return false;
    }

    @NonNull
    public static String getCurrency(@Constants.Currency int currencyNumber) {
        switch (currencyNumber) {
            case AZN:
                return "AZN";
            case USD:
                return "USD";
            case EUR:
                return "EUR";
            case RUB:
                return "RUB";
            case GBP:
                return "GBP";
            case TRY:
                return "TRY";
            default:
                return "Unknown currency";
        }
    }

    public static int monthsCountBetween(String startDate, String endDate) {
        try {
            Date start = dateFormatter.parse(startDate);
            Date end = dateFormatter.parse(endDate);

            Calendar cal = Calendar.getInstance();
            if (start.before(end)) // her ehtimal yoxlamaq lazimdi
                cal.setTime(start);
            else {
                cal.setTime(end);
                end = start;
            }

            int c = 0;
            while (cal.getTime().before(end)) {
                cal.add(Calendar.MONTH, 1);
                c++;
            }

            return c - 1;
        } catch (ParseException pe) {
            return 0;
        }
    }

    public static void hideKeyboardFrom(@NonNull Context context, @NonNull View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideSubmitButton(@NonNull View rootView, @NonNull View button) {
        final boolean[] isKeyboardShowing = {false};

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            int screenHeight = rootView.getRootView().getHeight();

            // r.bottom is the position above soft keypad or device button.
            // if keypad is shown, the r.bottom is smaller than that before.
            int keypadHeight = screenHeight - r.bottom;

            if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                // keyboard is opened
                if (!isKeyboardShowing[0]) {
                    isKeyboardShowing[0] = true;
                    button.setVisibility(View.GONE);
                }
            } else {
                // keyboard is closed
                if (isKeyboardShowing[0]) {
                    isKeyboardShowing[0] = false;
                    button.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private static byte[] getBase64Bytes(String base64) {
        return Base64.decode(base64, Base64.DEFAULT);
    }

    public static void setImageToImageView(ImageView view, @NonNull String base64) {
        setImageToImageView(view, getBase64Bytes(base64));
    }

    public static void setImageToImageView(@NonNull ImageView view, @NonNull byte[] bytes) {
        view.post(() -> view.setImageBitmap(getBitmapFromBytes(bytes)));
    }

    private static Bitmap getBitmapFromBytes(@NonNull byte[] bytes) {
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return Bitmap.createScaledBitmap(bmp, bmp.getWidth(), bmp.getHeight(), false);
    }

    private static Bitmap getBitmapFromBytesForView(@NonNull byte[] bytes, @NonNull View view) {
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return Bitmap.createScaledBitmap(bmp, view.getWidth(), view.getHeight(), false);
    }

    @NonNull
    public static String capitalize(String input) {
        input = input.toLowerCase();
        if (!input.isEmpty())
            return input.substring(0, 1).toUpperCase() + input.substring(1);
        else
            return input;
    }

    @StringRes
    public static int getDayName(int dayCode) {
        switch (dayCode) {
            case 1:
                return R.string.monday;
            case 2:
                return R.string.tuesday;
            case 3:
                return R.string.wednesday;
            case 4:
                return R.string.thursday;
            case 5:
                return R.string.friday;
            case 6:
                return R.string.saturday;
            case 7:
                return R.string.sunday;
            case 8:
                return R.string.all_days;
            default:
                return R.string.na;
        }
    }

    // slide the view from below itself to the current position
//    public static void slideUp(@NonNull View view, int marginBottom) {
//        view.setVisibility(View.VISIBLE);
//        TranslateAnimation animate = new TranslateAnimation(
//            0,      // fromXDelta
//            0,        // toXDelta
//            view.getHeight() + marginBottom, // fromYDelta
//            0);       // toYDelta
//        animate.setDuration(750);
//        animate.setFillAfter(true);
//        view.startAnimation(animate);
//    }

    // slide the view from its current position to below itself
//    public static void slideDown(@NonNull View view, int marginBottom) {
//        if (view.getVisibility() == View.VISIBLE) {
//            TranslateAnimation animate = new TranslateAnimation(
//                0,       // fromXDelta
//                0,         // toXDelta
//                0,       // fromYDelta
//                view.getHeight() + marginBottom); // toYDelta
//            animate.setDuration(750);
//            animate.setFillAfter(true);
//            view.startAnimation(animate);
//        }
//    }

    @NonNull
    public static MarkerOptions createMarker(LatLng locationPoint) {
        return new MarkerOptions().position(locationPoint).icon(getSmallPinIcon());
    }

    @NonNull
    public static BitmapDescriptor getSmallPinIcon() {
        return BitmapDescriptorFactory.fromResource(R.drawable.ic_small_pin);
    }

    @NonNull
    public static BitmapDescriptor getBigPinIcon() {
        return BitmapDescriptorFactory.fromResource(R.drawable.ic_big_pin);
    }

    @NonNull
    public static RequestInfo getCommonRequest() {
        RequestInfo requestInfo = AppData.getInstance().getRequestInfo();
        requestInfo.getMobileUser().setSaltSignature(AppData.getInstance().getSessionKey());
        requestInfo.getMobileUser().setUsername("");
        requestInfo.getMobileUser().setPasswordHash("");
        return requestInfo;
    }

    public static void setDateField(int year, int month, int day, @NonNull TextView view) {
        view.setText(
            String.format(
                view.getContext().getString(R.string.date_of_birth_format),
                day, month + 1, year
            )
        );
    }

    public static Drawable getDrawable(@NonNull Context ctx, @DrawableRes int drawableId) {
        return ResourcesCompat.getDrawable(ctx.getResources(), drawableId, ctx.getTheme());
    }

    public static void showAlertDialogWith(@NonNull View root, @NonNull Context ctx, @NonNull View dialogCloser) {
        final AlertDialog dialog = new AlertDialog.Builder(ctx).create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialogCloser.setOnClickListener(v -> dialog.dismiss());

        dialog.setView(root);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public static int tryParseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    public static void modifyChildrenEnableStatus(@NonNull android.view.ViewGroup root, boolean state, @NonNull android.view.View... exclusions) {
        final int exclusionCount = exclusions.length;
        if (exclusionCount > 0) {
            for (int i = 0; i < root.getChildCount(); i++) {
                View c = root.getChildAt(i);
                if (c instanceof ViewGroup) {
                    modifyChildrenEnableStatus((ViewGroup) c, state);
                }

                for (View exclusion : exclusions) {
                    if (exclusion != c)
                        c.setEnabled(state);
                }
            }

            return;
        }

        for (int i = 0; i < root.getChildCount(); i++) {
            View c = root.getChildAt(i);
            if (c instanceof ViewGroup)
                modifyChildrenEnableStatus((ViewGroup) c, state);

            c.setEnabled(state);
        }
    }

    public static int dp2Px(float dp) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @NonNull
    public static Spannable getMustBePayedItemTitle(@NonNull String itemOriginalTitle) {
        final int len = itemOriginalTitle.length();
        
        SpannableString spannable = SpannableString.valueOf(itemOriginalTitle + "*");
        spannable.setSpan(
            new ForegroundColorSpan(Color.parseColor("#ed3338")),
            len, len + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }
    
    /**
     * QR kod scan, card number scan kimi emeliyyatlarin sonunda startActivityForResult
     * invoke olundugu ucun, root activity (bu appda MainActivity class-i) recreate olur.
     * Recreate olundugu ucun de onResume metoduna dusur en sonda. Daha onceden user login
     * oldugu ucun sistemde, useri avtomatik PIN sehifesine yonlendirir. Dogrusu ise budur
     * ki, eger startActivityForResult function-u hansisa fragmentde invoke olunubsa,
     * useri en son fragmentde saxlamaq lazimdir. Bunun ucunde Android-e "demek" lazimdir
     * ki, activity-ni recreate etsen de, PIN sehifesine yonlendirme, eksine oldugun sehifede
     * (state-da) qal.
     *
     * Bu helper function yuxarida yazdigim funksionalligi yerine yetirir.
     *
     * @param activity base activity
     */
    public static void forceBypassPinFingerprintScreen(@NonNull FragmentActivity activity) {
        activity.getIntent().putExtra(KEY_PIN_FINGERPRINT_SCREEN_BYPASS, true);
    }
    
    public static void stopForceBypassPinFingerprintScreen(@NonNull FragmentActivity activity) {
        activity.getIntent().putExtra(KEY_PIN_FINGERPRINT_SCREEN_BYPASS, false);
    }
    
    /**
     * Eger bu function cagirilibsa, value return olunmamisdan once system levelde revert
     * olunduqdan sonra return olunur. Critical security buglarinin qarsisini almaqdan otru
     * implicitly etmek lazimdir bunu.
     *
     * @param activity base activity
     * @return `true` or `false` value depending on intent data
     */
    public static boolean isPinFingerprintScreenMustBeBypassed(@NonNull FragmentActivity activity) {
        final boolean value = activity.getIntent().getBooleanExtra(KEY_PIN_FINGERPRINT_SCREEN_BYPASS, false);
        stopForceBypassPinFingerprintScreen(activity);
        return value;
    }

    /**
     * Checks if userEnteredAmount less than minimum amount or not.
     *
     * @param userEnteredAmount the amount entered by user.
     * @param minimumAmount     predefined minimum value.
     * @return `true` if userEnteredAmount less than minimumAmount, `false` otherwise.
     */
    public static boolean lt(@NonNull final BigDecimal userEnteredAmount, @NonNull final BigDecimal minimumAmount) {
        return userEnteredAmount.compareTo(minimumAmount) < 0;
    }

    /**
     * Checks if userEnteredAmount greater than maximum amount or not.
     *
     * @param userEnteredAmount the amount entered by user.
     * @param maximumAmount     predefined amount.
     * @return `false` if userEnteredAmount greater than maximumAmount, `true` otherwise.
     */
    public static boolean gt(@NonNull final BigDecimal userEnteredAmount, @NonNull final BigDecimal maximumAmount) {
        return userEnteredAmount.compareTo(maximumAmount) > 0;
    }

    /**
     * Checks if userEnteredAmount equals to another or not.
     *
     * @param userEnteredAmount the amount entered by user.
     * @param another           the amount will be compared with userEnteredAmount.
     * @return `true` if userEnteredAmount equals to another.
     */
    public static boolean eq(@NonNull final BigDecimal userEnteredAmount, @NonNull final BigDecimal another) {
        return userEnteredAmount.compareTo(another) == 0;
    }

    public static void openInBrowser(@NonNull FragmentActivity activity, @NonNull final String url) {
        Utils.forceBypassPinFingerprintScreen(activity);

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

        final int c = Color.parseColor("#ed3338");
        CustomTabColorSchemeParams.Builder params = new CustomTabColorSchemeParams.Builder();
        params.setToolbarColor(c);
        params.setNavigationBarColor(c);

        builder.setDefaultColorSchemeParams(params.build());

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(activity, Uri.parse(url));
    }

    public static boolean shouldAskPin(final long lastLoginTimestamp) {
        if (lastLoginTimestamp == -1)
            return true;
        return SystemClock.elapsedRealtime() - lastLoginTimestamp >= ALLOWED_LOGIN_TIMEOUT;
    }

    public static String getCapsSentences(String tagName) {
        String[] splits = tagName.toLowerCase().split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < splits.length; i++) {
            String eachWord = splits[i];
            if (i > 0 && eachWord.length() > 0) {
                sb.append(" ");
            }
            String cap = eachWord.substring(0, 1).toUpperCase()
                    + eachWord.substring(1);
            sb.append(cap);
        }
        return sb.toString();
    }

    public static void postSignOutCleanUp(@NonNull final FragmentActivity activity) {
        // Clear all stored shared preferences excluding FCM token.
        clearSharedPrefs(PreferenceManager.getDefaultSharedPreferences(activity));
        // Remove all notifications that currently visible to the user.
        NotificationManagerCompat.from(activity).cancelAll();
    }

    public static void clearSharedPrefs(@NonNull final SharedPreferences preferences) {
        final SharedPreferences.Editor editor = preferences.edit();
        for (String prefKey : preferences.getAll().keySet())
            if (!prefKey.equals(FCM_NOTIFICATION_TOKEN))
                editor.remove(prefKey);
        editor.apply();
    }

    public static boolean isOnline() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockAddress = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockAddress, timeoutMs);
            sock.close();

            return true;
        } catch (IOException e) { return false; }
    }

    @NonNull
    public static String getAppLanguage(final int langCode) {
        final String lang = langCodes.get(langCode);
        if (lang == null)
            return "az";
        else
            return lang;
    }

    public static int getAppLanguageReversed(final String lang) {
        try {
            return new ArrayList<>(langCodes.values()).indexOf(lang);
        } catch (NoSuchElementException | IndexOutOfBoundsException e) {
            return 0;
        }
    }
}
