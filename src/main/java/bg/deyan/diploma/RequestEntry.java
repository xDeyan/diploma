package bg.deyan.diploma;

import java.net.Inet4Address;
import java.net.URL;
import java.time.LocalDateTime;

public class RequestEntry {

	private Long id;
	private Inet4Address ipAddress;
	private int responseCode;
	private URL requestedUrl;
	private LocalDateTime requestTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Inet4Address getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(Inet4Address ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public URL getRequestedUrl() {
		return requestedUrl;
	}

	public void setRequestedUrl(URL requestedUrl) {
		this.requestedUrl = requestedUrl;
	}

	public LocalDateTime getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(LocalDateTime requestTime) {
		this.requestTime = requestTime;
	}

	@Override
	public String toString() {
		return "RequestEntry [id=" + id + ", ipAddress=" + ipAddress + ", responseCode=" + responseCode
				+ ", requestedUrl=" + requestedUrl + ", requestTime=" + requestTime + "]";
	}

}
