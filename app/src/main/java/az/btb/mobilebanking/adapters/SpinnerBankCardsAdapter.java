package az.btb.mobilebanking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.models.BankCard;
import az.btb.mobilebanking.utils.Utils;

/**
 * https://stackoverflow.com/a/8207788/4057688
 */
public class SpinnerBankCardsAdapter extends BaseAdapter {

    private final List<BankCard> items;
    private final Context ctx;
    private final LayoutInflater inflater;

    public SpinnerBankCardsAdapter(Context context, List<BankCard> itemList) {
        ctx = context;
        items = itemList;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public BankCard getItem(int position) {
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

        final BankCard item = items.get(position);
        final String cardFullName = item.getCardServiceName();

        if (convertView == null) {
            mHolder = new MainListHolder();
            v = inflater.inflate(R.layout.spinner_custom_layout, null);
            mHolder.cardAltName = v.findViewById(R.id.item_alt_name);
            mHolder.balanceWithCurrency = v.findViewById(R.id.balance_with_currency);
            mHolder.cardFullInfo = v.findViewById(R.id.item_full_info);
            v.setTag(mHolder);
        } else
            mHolder = (MainListHolder) v.getTag();

        mHolder.cardAltName.setText(item.getCardAltName());
        mHolder.balanceWithCurrency.setText(
            String.format(
                ctx.getString(R.string.my_items_item_balance),
                item.getCardBalance(),
                Utils.getCurrency(item.getCurrency())
            )
        );
        mHolder.cardFullInfo.setText(
            String.format(
                ctx.getString(R.string.my_cards_card_number),
                cardFullName,
                item.getCardNumber().substring(0, 4),
                item.getCardNumber().substring(item.getCardNumber().length() - 4)
            )
        );

        return v;
    }

    static class MainListHolder {
        private TextView cardAltName;
        private TextView balanceWithCurrency;
        private TextView cardFullInfo;
    }
}
