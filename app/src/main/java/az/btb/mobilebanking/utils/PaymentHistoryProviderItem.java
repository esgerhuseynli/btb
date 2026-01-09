package az.btb.mobilebanking.utils;

public class PaymentHistoryProviderItem {
    private final int id;
    private final String name;

    public PaymentHistoryProviderItem(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
