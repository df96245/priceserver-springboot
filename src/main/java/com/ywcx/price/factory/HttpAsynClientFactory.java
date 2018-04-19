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
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ywcx.price.config.HttpConf;
@Component
public class HttpAsynClientFactory extends AbstractHttpClientFactory<CloseableHttpAsyncClient> {
	
	private final static Object syncLock = new Object();
	
	private CloseableHttpAsyncClient httpAsynClient = null;
	
	private static PoolingNHttpClientConnectionManager asynCM;
	
	@Autowired
	private HttpConf conf;
	
	@PostConstruct
	public void init() throws IOReactorException {
		asynCM = new PoolingNHttpClientConnectionManager(new DefaultConnectingIOReactor());
		asynCM.setMaxTotal(conf.getMaxTotal());
		asynCM.setDefaultMaxPerRoute(conf.getDefaultMaxPerRoute());
	}
	
	
	@Override
	public CloseableHttpAsyncClient getClient() {
		if (httpAsynClient == null) {
			synchronized (syncLock) {
				if (httpAsynClient == null) {
					httpAsynClient = createHttpClient();
				}
			}
		}
		return  httpAsynClient;
	}
	
	@SuppressWarnings("unchecked")
	private CloseableHttpAsyncClient createHttpClient() {
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(conf.getSocketTimeout()).setConnectTimeout(conf.getSocketTimeout()).build();
		return HttpAsyncClients.custom().setConnectionManager(asynCM).setDefaultRequestConfig(requestConfig).build();
		
	}


}
