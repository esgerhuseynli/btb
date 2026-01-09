package az.btb.mobilebanking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FcmTokenRequest {
	@SerializedName("RequestInfo")
	@Expose
	private RequestInfo requestInfo;
	@SerializedName("DevicePushInfoToken")
	@Expose
	private String devicePushInfoToken;

	public FcmTokenRequest(RequestInfo requestInfo, String devicePushInfoToken) {
		this.requestInfo = requestInfo;
		this.devicePushInfoToken = devicePushInfoToken;
	}

	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	public String getDevicePushInfoToken() {
		return devicePushInfoToken;
	}

	public void setDevicePushInfoToken(String devicePushInfoToken) {
		this.devicePushInfoToken = devicePushInfoToken;
	}
}
