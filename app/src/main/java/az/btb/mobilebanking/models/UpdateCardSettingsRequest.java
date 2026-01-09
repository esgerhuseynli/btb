package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateCardSettingsRequest {
    @SerializedName("RequestInfo")
    @Expose
    private RequestInfo requestInfo;
    @SerializedName("IdCard")
    @Expose
    private String cardId;
    @SerializedName("CardAltName")
    @Expose
    private String cardNewAltName;
    @SerializedName("CardColor")
    @Expose
    private int cardNewColor;
    @SerializedName("ChangeCardOptions")
    @Expose
    private int cardChangeOption;

    public UpdateCardSettingsRequest(RequestInfo requestInfo, String cardId, String cardNewAltName, int cardNewColor, int cardChangeOption) {
        this.requestInfo = requestInfo;
        this.cardId = cardId;
        this.cardNewAltName = cardNewAltName;
        this.cardNewColor = cardNewColor;
        this.cardChangeOption = cardChangeOption;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardNewAltName() {
        return cardNewAltName;
    }

    public void setCardNewAltName(String cardNewAltName) {
        this.cardNewAltName = cardNewAltName;
    }

    public int getCardNewColor() {
        return cardNewColor;
    }

    public void setCardNewColor(int cardNewColor) {
        this.cardNewColor = cardNewColor;
    }

    public int getCardChangeOption() {
        return cardChangeOption;
    }

    public void setCardChangeOption(int cardChangeOption) {
        this.cardChangeOption = cardChangeOption;
    }
}
