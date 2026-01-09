package az.btb.mobilebanking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.utils.Utils;

/**
 * https://stackoverflow.com/a/8207788/4057688
 */
public class SpinnerBankAccountsAdapter extends BaseAdapter {

    private List<BankAccount> items;
    private Context ctx;
    private LayoutInflater inflater;

    public SpinnerBankAccountsAdapter(Context context, List<BankAccount> itemList) {
        ctx = context;
        items = itemList;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public BankAccount getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MainListHolder mHolder;
        View v = convertView;

        final BankAccount item = items.get(position);

        if (convertView == null) {
            mHolder = new MainListHolder();
            v = inflater.inflate(R.layout.spinner_custom_layout, null);
            mHolder.accountAltName = v.findViewById(R.id.item_alt_name);
            mHolder.balanceWithCurrency = v.findViewById(R.id.balance_with_currency);
            mHolder.accountIban = v.findViewById(R.id.item_full_info);
            v.setTag(mHolder);
        } else
            mHolder = (MainListHolder) v.getTag();

        mHolder.accountAltName.setText(item.getAccountAltName());
        mHolder.balanceWithCurrency.setText(
            String.format(
                ctx.getString(R.string.my_items_item_balance),
                item.getCurrency() == 0 ? item.getBalanceInLC() : item.getBalanceInFC(),
                Utils.getCurrency(item.getCurrency())
            )
        );
        mHolder.accountIban.setText(item.getIbanAccount());

        return v;
    }

    static class MainListHolder {
        private TextView accountAltName;
        private TextView balanceWithCurrency;
        private TextView accountIban;
    }
}
