package az.btb.mobilebanking.utils;

import androidx.databinding.ViewDataBinding;

public interface ItemPropsBinder<Binding extends ViewDataBinding, Item> {
    void bindItem(Binding binding, Item item);
}
