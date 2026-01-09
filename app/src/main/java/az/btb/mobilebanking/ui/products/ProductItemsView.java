package az.btb.mobilebanking.ui.products;

import java.util.List;

import moxy.MvpView;

public interface ProductItemsView<Item> extends MvpView {
    void showError(String msg);
    void showItemsList(List<Item> itemList);
}
