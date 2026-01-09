package az.btb.mobilebanking.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import az.btb.mobilebanking.databinding.ServicePointSearchItemBinding;
import az.btb.mobilebanking.models.ServicePoint;
import az.btb.mobilebanking.utils.ItemPropsBinder;

public class FilterableServicePointsAdapter
    extends ListAdapter<ServicePoint, FilterableServicePointsAdapter.FilterableServicePointsViewHolder> {

    private ItemPropsBinder<ServicePointSearchItemBinding, ServicePoint> itemPropsBinder;

    public FilterableServicePointsAdapter(
        ItemPropsBinder<ServicePointSearchItemBinding, ServicePoint> itemPropsBinder
    ) {
        super(new DiffUtil.ItemCallback<ServicePoint>() {
            @Override
            public boolean areItemsTheSame(@NonNull ServicePoint oldItem, @NonNull ServicePoint newItem) {
                return oldItem == newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull ServicePoint oldItem, @NonNull ServicePoint newItem) {
                // eger koordinatlar eynidirse, demeli eyni pointdi :))
                return
                    oldItem.getAddress().getX().equals(newItem.getAddress().getX()) &&
                    oldItem.getAddress().getY().equals(newItem.getAddress().getY());
            }
        });

        this.itemPropsBinder = itemPropsBinder;
    }

    @NonNull
    @Override
    public FilterableServicePointsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ServicePointSearchItemBinding binding = ServicePointSearchItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new FilterableServicePointsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterableServicePointsViewHolder holder, int position) {
        holder.bindValues(getItem(position), itemPropsBinder);
    }

    static class FilterableServicePointsViewHolder extends RecyclerView.ViewHolder {

        private ServicePointSearchItemBinding binding;

        FilterableServicePointsViewHolder(@NonNull ServicePointSearchItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        void bindValues(@NonNull ServicePoint point, @NonNull ItemPropsBinder<ServicePointSearchItemBinding, ServicePoint> itemPropsBinder) {
            itemPropsBinder.bindItem(binding, point);

            binding.executePendingBindings();
        }
    }
}
