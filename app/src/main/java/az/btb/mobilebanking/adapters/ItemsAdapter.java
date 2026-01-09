package az.btb.mobilebanking.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import az.btb.mobilebanking.utils.ItemPropsBinder;

public class ItemsAdapter<Binding extends ViewDataBinding, Item> extends
    ListAdapter<Item, ItemsAdapter.MyItemsAdapterViewHolder<Binding, Item>> {

    private List<Item> items;
    private final ItemPropsBinder<Binding, Item> itemPropsBinder;
    @LayoutRes private final int layoutId;

    public ItemsAdapter(@LayoutRes int layout, List<Item> myItems, ItemPropsBinder<Binding, Item> itemPropsBinder) {
        super(new DiffUtil.ItemCallback<Item>() {
            @Override
            public boolean areItemsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
                return oldItem == newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
                return (oldItem + "").equals(newItem + "");
            }
        });

        layoutId = layout;
        items = myItems;
        this.itemPropsBinder = itemPropsBinder;
    }

    @NonNull
    @Override
    public MyItemsAdapterViewHolder<Binding, Item> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Binding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutId, parent, false);
        return new MyItemsAdapterViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemsAdapterViewHolder<Binding, Item> holder, int position) {
        holder.bindItem(items.get(position), itemPropsBinder);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void submitList(@Nullable List<Item> list) {
        items = list;
        super.submitList(list);
    }

    @Override
    public void submitList(@Nullable List<Item> list, @Nullable Runnable commitCallback) {
        items = list;
        super.submitList(list, commitCallback);
    }

    static class MyItemsAdapterViewHolder<DataBinding extends ViewDataBinding, ItemType> extends RecyclerView.ViewHolder {

        private final DataBinding binding;

        private MyItemsAdapterViewHolder(@NonNull DataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindItem(ItemType item, @NonNull ItemPropsBinder<DataBinding, ItemType> itemPropsBinder) {
            itemPropsBinder.bindItem(binding, item);

            binding.executePendingBindings();
        }
    }
}
