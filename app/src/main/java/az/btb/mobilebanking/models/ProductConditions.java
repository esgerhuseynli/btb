package az.btb.mobilebanking.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class ProductConditions implements Parcelable {
	@SerializedName("currency")
	@Expose
	private int currency;
	@SerializedName("minimalPercent")
	@Expose
	private float minimalPercent;
	@SerializedName("maximalPercent")
	@Expose
	private float maximalPercent;
	@SerializedName("minimalTerm")
	@Expose
	private int minimalTerm;
	@SerializedName("maximalTerm")
	@Expose
	private int maximalTerm;
	@SerializedName("minimalAmount")
	@Expose
	private BigDecimal minimalAmount;
	@SerializedName("maximalAmount")
	@Expose
	private BigDecimal maximalAmount;
	@SerializedName("percentStepSize")
	@Expose
	private float percentStepSize;
	
//	@VisibleForTesting
//	@TestOnly
//	public ProductConditions(int currency, float minimalPercent, float maximalPercent, int minimalTerm, int maximalTerm, double minimalAmount, double maximalAmount) {
//		this.currency = currency;
//		this.minimalPercent = minimalPercent;
//		this.maximalPercent = maximalPercent;
//		this.minimalTerm = minimalTerm;
//		this.maximalTerm = maximalTerm;
//		this.minimalAmount = BigDecimal.valueOf(minimalAmount);
//		this.maximalAmount = BigDecimal.valueOf(maximalAmount);
//	}
	
	protected ProductConditions(Parcel in) {
		currency = in.readInt();
		minimalPercent = in.readFloat();
		maximalPercent = in.readFloat();
		percentStepSize = in.readFloat();
		minimalTerm = in.readInt();
		maximalTerm = in.readInt();
		minimalAmount = (BigDecimal) in.readSerializable();
		maximalAmount = (BigDecimal) in.readSerializable();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(currency);
		dest.writeFloat(minimalPercent);
		dest.writeFloat(maximalPercent);
		dest.writeFloat(percentStepSize);
		dest.writeInt(minimalTerm);
		dest.writeInt(maximalTerm);
		dest.writeSerializable(minimalAmount);
		dest.writeSerializable(maximalAmount);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Creator<ProductConditions> CREATOR = new Creator<ProductConditions>() {
		@Override
		public ProductConditions createFromParcel(Parcel in) {
			return new ProductConditions(in);
		}
		
		@Override
		public ProductConditions[] newArray(int size) {
			return new ProductConditions[size];
		}
	};
	
	public int getCurrency() {
		return currency;
	}

	public void setCurrency(int currency) {
		this.currency = currency;
	}

	public float getMinimalPercent() {
		return minimalPercent;
	}

	public void setMinimalPercent(float minimalPercent) {
		this.minimalPercent = minimalPercent;
	}

	public float getMaximalPercent() {
		return maximalPercent;
	}

	public void setMaximalPercent(float maximalPercent) {
		this.maximalPercent = maximalPercent;
	}

	public int getMinimalTerm() {
		return minimalTerm;
	}

	public void setMinimalTerm(int minimalTerm) {
		this.minimalTerm = minimalTerm;
	}

	public int getMaximalTerm() {
		return maximalTerm;
	}

	public void setMaximalTerm(int maximalTerm) {
		this.maximalTerm = maximalTerm;
	}

	public BigDecimal getMinimalAmount() {
		return minimalAmount;
	}

	public void setMinimalAmount(BigDecimal minimalAmount) {
		this.minimalAmount = minimalAmount;
	}

	public BigDecimal getMaximalAmount() {
		return maximalAmount;
	}

	public void setMaximalAmount(BigDecimal maximalAmount) {
		this.maximalAmount = maximalAmount;
	}

	public float getPercentStepSize() {
		return percentStepSize;
	}

	public void setPercentStepSize(float percentStepSize) {
		this.percentStepSize = percentStepSize;
	}
}
