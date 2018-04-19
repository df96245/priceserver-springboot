package com.ywcx.price.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
public class HttpConf {
	@Value("${http.socket.timeout}")
	private Integer socketTimeout;
	@Value("${http.connect.timeout}")
	private Integer connectTimeout;
	@Value("${http.retry.times}")
	private Integer retryTimes;
	@Value("${http.retry.interval}")
	private Integer retryInterval;
	@Value("${http.connection.max.total}")
	private Integer maxTotal;
	@Value("${http.default.max.pre.route}")
	private Integer defaultMaxPerRoute;
	

	public Integer getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(Integer socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public Integer getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public Integer getRetryTimes() {
		return retryTimes;
	}

	public void setRetryTimes(Integer retryTimes) {
		this.retryTimes = retryTimes;
	}

	public Integer getRetryInterval() {
		return retryInterval;
	}

	public void setRetryInterval(Integer retryInterval) {
		this.retryInterval = retryInterval;
	}

	public Integer getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(Integer maxTotal) {
		this.maxTotal = maxTotal;
	}

	public Integer getDefaultMaxPerRoute() {
		return defaultMaxPerRoute;
	}

	public void setDefaultMaxPerRoute(Integer defaultMaxPerRoute) {
		this.defaultMaxPerRoute = defaultMaxPerRoute;
	}

}
