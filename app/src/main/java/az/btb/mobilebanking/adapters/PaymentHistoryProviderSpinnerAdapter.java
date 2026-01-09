package az.btb.mobilebanking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import az.btb.mobilebanking.utils.PaymentHistoryProviderItem;

public class PaymentHistoryProviderSpinnerAdapter extends BaseAdapter {
	
	private final List<PaymentHistoryProviderItem> items;
	private final LayoutInflater inflater;
	
	public PaymentHistoryProviderSpinnerAdapter(Context context, List<PaymentHistoryProviderItem> itemList) {
		items = itemList;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			mHolder.itemView = v.findViewById(android.R.id.text1);
			v.setTag(mHolder);
		} else
			mHolder = (MainListHolder) v.getTag();
		
		mHolder.itemView.setText(item.getName());
		
		return v;
	}
	
	static class MainListHolder {
		private TextView itemView;
	}
}
