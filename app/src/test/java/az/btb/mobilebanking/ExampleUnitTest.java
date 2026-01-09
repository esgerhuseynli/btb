package az.btb.mobilebanking;

import android.util.Pair;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import az.btb.mobilebanking.adapters.SectionedRecyclerViewAdapter;
import az.btb.mobilebanking.models.UserNotification;
import az.btb.mobilebanking.models.UserNotificationsResponse;
import az.btb.mobilebanking.utils.Utils;
import kotlin.collections.CollectionsKt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void test_formatApiCustomDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            System.out.println(format.format(format.parse("2020-11-20T10:24:46.1399582+04:00")));
        } catch (ParseException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    @Test
    public void testHash() {
        System.out.println(Utils.passwordHash("test123"));

        assertEquals(Utils.passwordHash("test123"), Utils.passwordHash("test123".replace(" ", "")));
    }

    @Test
    public void testUppercaseHash() {
        //System.out.println(Utils.passwordHash("test123"));

        assertEquals(
            "79C377501595E6A0964F9531A661C1672BF3EF74798C130673B8D9E25DC1FD765B8EEE93F291A38518C9CA3B198AEDBEBD0A81E1B1C5780A60D9EB2F78209D81",
            Utils.passwordHash("test123".toUpperCase().replace(" ", ""))
        );
    }

    @Test
    public void mergeLists() {
        List<String> a = new ArrayList<String>();
        a.add("a");
        a.add("b");

        List<String> b = new ArrayList<String>();
        b.add("1");
        b.add("2");

        List<String> c = new ArrayList<String>();
        c.add("a");
        c.add("b");

        List<String> d = new ArrayList<String>();
        d.add("1");
        d.add("2");
        Pair<List<String>, List<String>> servicePoints = new Pair<>(a, b);

        c.addAll(d);
        System.out.println(c);
        servicePoints.first.addAll(servicePoints.second);
        System.out.println(servicePoints.first);
    }

    @Test
    public void testJoinWithString() {
        List<String> a = new ArrayList<>();
        a.add("a");
        a.add("b");
        a.add("c");

        String last = a.get(0);
        for (int i = 1; i < a.size(); i++) {
            last += ", " + a.get(i);
        }

        System.out.println(last);
    }

    @Test
    public void generateAppHash() {
//        RequestInfoRequest atmz = getAtmRequest();
//        for (int i = 0; i<10000000; i++) { }
//        RequestInfoRequest branchz = getBranchRequest();
//        for (int i = 0; i < 4; i++) {
//            System.out.println(Utils.appHash());
//            System.out.println(Utils.appHash());
//        }
//        assertEquals("Miri", Utils.capitalize("miri"));
        assertEquals("Nahar fasiləsi: 13:00 - 14:00", String.format("%s fasiləsi: %d:00 - %d:00", Utils.capitalize("NAHAR"), 13, 14));
    }

    @Test
    public void fixPhoneFormatting() {
        //"[\n\r]", ""
        String a = "(+99412) 499-79-95\n"
            .trim()
//            .replace("+", "")
            .replace("(", "")
            .replace(")", "")
            .replace(" ", "")
            .replaceAll("-", "");
        System.out.println(a + "--");
    }

    @Test
    public void addition_isCorrect() {
        try {
            String start = "04-10-2020";
            String end = "03-11-2020";
            System.out.println("Months: " + Utils.monthsCountBetween(start, end));

            System.out.println("26-12-2019 18:26:59".substring(11,16));

            System.out.println(new SimpleDateFormat("dd MMMM yyyy", new Locale("az")).format(new Date(2020, 10, 5)));
        } catch (Exception e) {

        }
        System.out.println(String.format("%.2f %s / %.2f%%", 1000.0, "AZN", 6.0));
    }

    @Test
    public void sortNotificationsByDate() {
        try {
            String stringTooLong = getFileContent();

            Class<UserNotificationsResponse> c = UserNotificationsResponse.class;
            UserNotificationsResponse r = new Gson().fromJson(stringTooLong, c);

            SortedMap<Long, List<UserNotification>> dateListSortedMap = new TreeMap<>(Collections.reverseOrder());
            for (UserNotification notification : r.getUserNotifications()) {
                final long date = notification.getPublishTimestamp();
                if (!dateListSortedMap.containsKey(date)) {
                    List<UserNotification> list = new ArrayList<>();
                    list.add(notification);

                    dateListSortedMap.put(date, list);
                } else
                    dateListSortedMap.get(date).add(notification);
            }

            Map<String, List<UserNotification>> stringListMap = new LinkedHashMap<>();
            for (long date : dateListSortedMap.keySet())
                stringListMap.put(Utils.dateFormatter.format(date), dateListSortedMap.get(date));

            for (String date : stringListMap.keySet())
                System.out.println(date + ": " + stringListMap.get(date).size());

            List<SectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();

            List<String> allDates = new ArrayList<>(stringListMap.keySet());
            final int count = allDates.size();

            //Sections
            sections.add(new SectionedRecyclerViewAdapter.Section(0, allDates.get(0)));

            for (int i = 1; i < count; i++) {
                sections.add(
                    new SectionedRecyclerViewAdapter.Section(
                        stringListMap.get(allDates.get(i - 1)).size(),
                        allDates.get(i)
                    )
                );
            }

            System.out.println();
        } catch (Exception ignored) {

        }
    }

    @NotNull
    private static String getFileContent() throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("src/test/res/list.json"), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            return sb.toString();
        }
    }

    private boolean isBefore(String newDateStr, String oldDateStr) {
        try {
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ROOT);
            Date newDate = formatter.parse(newDateStr);
            Date oldDate = formatter.parse(oldDateStr);

            return newDate.after(oldDate);
        } catch (Exception e) {
            return false;
        }
    }

    @Test
    public void format_result_check() {
        BigDecimal kreditMeblegi = new BigDecimal("11347.20");
        BigDecimal oniki = new BigDecimal(12).divide(new BigDecimal(100));
        double km = 10000.00;
        float fd = 24;
        int m = 12;
        //                   KM  * (FD / 12 / 100 * (1 + FD / 12 / 100) ^ (M - GM)) / ((1 + FD / 12 / 100) ^ (M - GM) - 1)
        //   System.out.println(   km * (fd / 12 / 100 * (1 + fd / 12 / 100) ^ m)        / ((1 + fd / 12 / 100) ^ m - 1));
        //                 10000 * (24 / 12 / 100 * (1 + 24 / 12 / 100) ^ (12 - 0)) / ((1 + 24 / 12 / 100) ^ (12 - 0) - 1)
    
    
        System.out.println(kreditMeblegi.divide(oniki, BigDecimal.ROUND_HALF_DOWN));
        System.out.println(kreditMeblegi.divide(oniki, BigDecimal.ROUND_CEILING));
        float r = fd / m / 100;
        System.out.println(r);
    }
//    suallar:
//        05 yazanda "El" cixsin deyirsen.
//    sual1: "05" "E" yaxud "El"-i  identifikasiya edir yoxsa nece?
//    sual1.1: name valuesinin uzunlugu code-in uzunluguna beraber olacaq deye bele deyirsen?
    BigDecimal divide(BigDecimal val, int by) {
        return val.divide(new BigDecimal(by), 2, BigDecimal.ROUND_HALF_EVEN);
    }
    
    @Test
    public void testGroupByCurrency() {
//        List<Integer> productAvailableCurrencies = Arrays.asList(0, 1);
//
//        ProductConditions pc0 = new ProductConditions(0, 0, 0, 0, 0, 0, 0);
//        ProductConditions pc1 = new ProductConditions(1, 1, 1, 1, 1, 1, 1);
//        ProductConditions pc2 = new ProductConditions(2, 2, 2, 2, 2, 2, 2);
//        List<ProductConditions> pcs = Arrays.asList(pc0, pc1, pc2);
//
//        Map<Integer, ProductConditions> map = CollectionsKt.associateBy(
//            pcs,
//            ProductConditions::getCurrency
//        );
//
//        System.out.println(new Gson().toJson(map));
    }

    @Test
    public void testBigDecimalComparison() {
        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal one = BigDecimal.ONE;

        isAmountSetToInvoice(new BigDecimal("212"), zero, new BigDecimal("34002"));
    }

    private void isAmountSetToInvoice(@NonNull final BigDecimal userEnteredAmount, @NonNull final BigDecimal minAmount, @NonNull final BigDecimal maxAmount) {
        if (userEnteredAmount.compareTo(minAmount) > -1) {
            if (userEnteredAmount.compareTo(maxAmount) > 0)
                System.out.println("coxdu");
            else
                System.out.println("correct");
        } else
            System.out.println("azdi");
    }

    @Test
    public void testBooleanArray() {
        List<Boolean> spa = CollectionsKt.mutableListOf();

        spa.add(true);

        assertTrue(CollectionsKt.all(spa, bool -> true));
    }

    @Test
    public void testToMillis() {
        assertEquals(2L * 60 * 1000, TimeUnit.MINUTES.toMillis(2));
    }
}
