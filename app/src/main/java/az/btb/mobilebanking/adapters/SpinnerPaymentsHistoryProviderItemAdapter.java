package az.btb.mobilebanking.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import az.btb.mobilebanking.utils.PaymentHistoryProviderItem;

/**
 * https://stackoverflow.com/a/8207788/4057688
 */
public class SpinnerPaymentsHistoryProviderItemAdapter extends BaseAdapter {

    private final List<PaymentHistoryProviderItem> items;
    private final LayoutInflater inflater;

    public SpinnerPaymentsHistoryProviderItemAdapter(List<PaymentHistoryProviderItem> itemList, LayoutInflater inflater) {
        items = itemList;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public PaymentHistoryProviderItem getItem(int position) {
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

        final PaymentHistoryProviderItem item = items.get(position);

        if (convertView == null) {
            mHolder = new MainListHolder();
            v = inflater.inflate(android.R.layout.simple_list_item_1, null);
            mHolder.name = v.findViewById(android.R.id.text1);
            v.setTag(mHolder);
        } else
            mHolder = (MainListHolder) v.getTag();

        mHolder.name.setText(item.getName());

        return v;
    }

    static class MainListHolder {
        private TextView name;
    }
}
