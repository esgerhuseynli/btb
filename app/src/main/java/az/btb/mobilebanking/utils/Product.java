package az.btb.mobilebanking.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import az.btb.mobilebanking.models.ProductConditions;

import static az.btb.mobilebanking.utils.Constants.ProductTypes;

public class Product implements Parcelable {
    public int id;
    public String image;
    public String headerName;
    public String information;
    public String tariff;
    public BigDecimal cost;
    public int costCurrency;
    public Boolean hasOnlinePayment;
    public @ProductTypes int type;
    
    public OrderData orderData;
    
    public Product() {
        orderData = new OrderData();
    }

    protected Product(Parcel in) {
        id = in.readInt();
        image = in.readString();
        headerName = in.readString();
        information = in.readString();
        tariff = in.readString();
        cost = (BigDecimal) in.readSerializable();
        costCurrency = in.readInt();
        hasOnlinePayment = (boolean) in.readSerializable();
        type = in.readInt();
        orderData = in.readParcelable(OrderData.class.getClassLoader());
    }
    
    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }
        
        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(image);
        dest.writeString(headerName);
        dest.writeString(information);
        dest.writeString(tariff);
        dest.writeSerializable(cost);
        dest.writeInt(costCurrency);
        dest.writeSerializable(hasOnlinePayment);
        dest.writeInt(type);
        dest.writeParcelable(orderData, 0);
    }
    
    public static final class OrderData implements Parcelable {
        public List<Integer> availableCurrencies = new ArrayList<>();
        public Map<Integer, ProductConditions> currencyAssociatedProductCondition;
    
        public OrderData() {}
        protected OrderData(Parcel in) {
            in.readList(availableCurrencies, int.class.getClassLoader());
    
            int currencyAssociatedProductConditionSize = in.readInt();
            currencyAssociatedProductCondition = new HashMap<>(currencyAssociatedProductConditionSize);
            for (int i = 0; i < currencyAssociatedProductConditionSize; i++) {
                Integer key = in.readInt();
                ProductConditions value = in.readParcelable(ProductConditions.class.getClassLoader());
                currencyAssociatedProductCondition.put(key, value);
            }
        }
    
        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeList(availableCurrencies);
            dest.writeInt(currencyAssociatedProductCondition.size());
            for (Map.Entry<Integer, ProductConditions> entry : currencyAssociatedProductCondition.entrySet()) {
                dest.writeInt(entry.getKey());
                dest.writeParcelable(entry.getValue(), 0);
            }
        }
    
        @Override
        public int describeContents() {
            return 0;
        }
    
        public static final Creator<OrderData> CREATOR = new Creator<OrderData>() {
            @Override
            public OrderData createFromParcel(Parcel in) {
                return new OrderData(in);
            }
        
            @Override
            public OrderData[] newArray(int size) {
                return new OrderData[size];
            }
        };
    }
}
