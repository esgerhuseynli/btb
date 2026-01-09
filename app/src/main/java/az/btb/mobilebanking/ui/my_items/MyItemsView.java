package az.btb.mobilebanking.ui.my_items;

import java.util.List;

import moxy.MvpView;

public interface MyItemsView<Item> extends MvpView {
    void showError(String msg);
    void showItemsList(List<Item> itemList);
}
