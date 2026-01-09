package az.btb.mobilebanking;

import az.btb.mobilebanking.models.RequestInfo;
import az.btb.mobilebanking.utils.Utils;

public class AppData {

    private static final AppData ourInstance = new AppData();

    private RequestInfo requestInfo;
    private String sessionKey;
    private String signUpPan;
    private String signUpCif;
    private String dateOfBirth;
    private boolean firstLaunch = true;

    public boolean isFirstLaunch() {
        return firstLaunch;
    }

    public void setFirstLaunch(boolean firstLaunch) {
        this.firstLaunch = firstLaunch;
    }

    public String getSignUpPan() {
        return signUpPan;
    }

    public void setSignUpPan(String signUpPan) {
        this.signUpPan = signUpPan;
    }

    public String getSignUpCif() {
        return signUpCif;
    }

    public void setSignUpCif(String signUpCif) {
        this.signUpCif = signUpCif;
    }

    public String getSignUpDateOfBirth() {
        return dateOfBirth;
    }

    public void setSignUpDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public static AppData getInstance() {
        return ourInstance;
    }

    public RequestInfo getRequestInfo() {
        requestInfo.getAppInfo().setApiHash(Utils.appHash());
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

}
