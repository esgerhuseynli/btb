package az.btb.mobilebanking.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import az.btb.mobilebanking.databinding.BankCardAndAccountItemBinding;
import az.btb.mobilebanking.models.BankCardAndAccount;
import az.btb.mobilebanking.utils.ItemPropsBinder;

public class BankCardAndAccountsAdapter extends ListAdapter<BankCardAndAccount, BankCardAndAccountsAdapter.ViewHolder> {

    private final ItemPropsBinder<BankCardAndAccountItemBinding, BankCardAndAccount> itemPropsBinder;

    public BankCardAndAccountsAdapter(ItemPropsBinder<BankCardAndAccountItemBinding, BankCardAndAccount> itemPropsBinder) {
        super(new DiffUtil.ItemCallback<BankCardAndAccount>() {
            @Override
            public boolean areItemsTheSame(@NonNull BankCardAndAccount oldItem, @NonNull BankCardAndAccount newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull BankCardAndAccount oldItem, @NonNull BankCardAndAccount newItem) {
                return /*oldItem.isCardItem() == newItem.isCardItem() &&*/ oldItem.getItemNumber().equals(newItem.getItemNumber());
            }
        });

        this.itemPropsBinder = itemPropsBinder;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BankCardAndAccountItemBinding elementBinding = BankCardAndAccountItemBinding.inflate(
            LayoutInflater.from(
                parent.getContext()
            )
        );
        return new ViewHolder(elementBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindValues(getItem(position), itemPropsBinder);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final BankCardAndAccountItemBinding binding;

        private ViewHolder(@NonNull BankCardAndAccountItemBinding itemViewBinding) {
            super(itemViewBinding.getRoot());
            binding = itemViewBinding;
        }

        public void bindValues(BankCardAndAccount item, @NonNull ItemPropsBinder<BankCardAndAccountItemBinding, BankCardAndAccount> binder) {
            binder.bindItem(binding, item);
            binding.executePendingBindings();
        }
    }
}
