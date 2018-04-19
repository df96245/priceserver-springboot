package com.ywcx.price.factory;

import javax.annotation.PostConstruct;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ywcx.price.config.HttpConf;
import com.ywcx.price.http.RetryHandler;
@Component
public class HttpSynClientFactory extends AbstractHttpClientFactory<CloseableHttpClient> {
	
	private final static Object syncLock = new Object();
	private static PoolingHttpClientConnectionManager cm;
	private CloseableHttpClient httpClient = null;

	@Autowired
	private HttpConf conf;
	
	@PostConstruct
	public void init()  {
		cm= new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(conf.getMaxTotal());
		cm.setDefaultMaxPerRoute(conf.getDefaultMaxPerRoute());
	}
	

	public CloseableHttpClient getClient() {
		if (httpClient == null) {
			synchronized (syncLock) {
				if (httpClient == null) {
					httpClient = createHttpClient();
				}
			}
		}
		return  httpClient;
	}
	
	@SuppressWarnings("unchecked")
	private CloseableHttpClient createHttpClient() {
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(conf.getSocketTimeout()).setConnectTimeout(conf.getSocketTimeout()).build();
		return HttpClients.custom().setConnectionManager(cm).setDefaultRequestConfig(requestConfig).setRetryHandler(new RetryHandler()).build();
		
	}

}
