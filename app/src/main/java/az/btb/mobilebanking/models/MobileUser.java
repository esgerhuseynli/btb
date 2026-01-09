package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MobileUser {

    @SerializedName("Username")
    @Expose
    private String username;
    @SerializedName("PasswordHash")
    @Expose
    private String passwordHash;
    @SerializedName("SessionKey")
    @Expose
    private String sessionKey;
    @SerializedName("SaltSignature")
    @Expose
    private String saltSignature;

    public MobileUser(String username, String passwordHash, String sessionKey, String saltSignature) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.sessionKey = sessionKey;
        this.saltSignature = saltSignature;
    }

    public MobileUser(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getSaltSignature() {
        return saltSignature;
    }

    public void setSaltSignature(String saltSignature) {
        this.saltSignature = saltSignature;
    }

}