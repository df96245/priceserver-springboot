package com.ywcx.price.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ywcx.price.callback.FutureCallbackImpl;
import com.ywcx.price.common.Constants;
import com.ywcx.price.config.HttpConf;
import com.ywcx.price.factory.AbstractHttpClientFactory;
import com.ywcx.price.util.JsonUtil;
import com.ywcx.price.util.ParamValidator;

@Component
public class HttpHelper {
	private static Logger logger = LoggerFactory.getLogger(HttpHelper.class);

	@Autowired
	private HttpConf conf;

	@Autowired
	private AbstractHttpClientFactory<CloseableHttpClient> synFactory;

	@Autowired
	private AbstractHttpClientFactory<CloseableHttpAsyncClient> asynFactory;

	private String callUrlGet(HttpGet httpMethod, List<BasicNameValuePair> urlParams,boolean isAsyn) {
		String resJson = null;
		URI uri = null;
		HttpResponse response = null;
		HttpUtil.setUrl2HttpMethod(httpMethod, urlParams);
		try {
			if (isAsyn) {
				CloseableHttpAsyncClient client = asynFactory.getClient();
				client.start();
				final CountDownLatch latch = new CountDownLatch(1);
				Future<HttpResponse> future = client.execute(httpMethod, new FutureCallbackImpl(latch));
				latch.await();
				response = future.get();
				resJson = HttpUtil.parseRes2Json(response);
			} else {
				CloseableHttpClient client = synFactory.getClient();
				response = client.execute(httpMethod);
				resJson = HttpUtil.parseRes2Json(response);
				resJson = processRetryLogic(resJson, uri, conf.getRetryTimes());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} finally {
			httpMethod.releaseConnection();
		}
		return resJson;
	}

	// TODO 提出参数
	private String callUrlPost(HttpPost httpMethod, List<BasicNameValuePair> postBodyParams,
			List<BasicNameValuePair> urlParams, boolean isAsyn) {
		String resJson = null;
		CloseableHttpAsyncClient httpAsynclient = isAsyn ? asynFactory.getClient() : null;
		CloseableHttpClient httpSynclient = isAsyn ? null :synFactory.getClient();

		if (null != postBodyParams) {
			logger.debug("exeAsyncReq post postBody={}", postBodyParams);
			UrlEncodedFormEntity entity;
			try {
				entity = new UrlEncodedFormEntity(postBodyParams, Constants.UTF_8);
				httpMethod.setEntity(entity);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (isAsyn) {
			HttpUtil.setUrl2HttpMethod(httpMethod, urlParams);
			httpAsynclient.start();
			try {
				final CountDownLatch latch = new CountDownLatch(1);
				Future<HttpResponse> future = httpAsynclient.execute(httpMethod, new FutureCallbackImpl(latch));
				latch.await();
				HttpResponse response = future.get();
				resJson = HttpUtil.parseRes2Json(response);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}

		} else {
			try {
				if (null != postBodyParams) {
					logger.debug("exeAsyncReq post postBody={}", postBodyParams);
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postBodyParams, Constants.UTF_8);
					httpMethod.setEntity(entity);
				}
				HttpUtil.setUrl2HttpMethod(httpMethod, urlParams);
				HttpResponse response;
				response = httpSynclient.execute(httpMethod);
				resJson = HttpUtil.parseRes2Json(response);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resJson;
	}


	public String getConfCall(String url, List<BasicNameValuePair> urlParams, boolean isPost, boolean isPostBody,
			boolean isAsyn) throws Exception {
		if (ParamValidator.validateIsBlank(url)) {
			throw new Exception("Url is empty or null , please check if correctly!");
		}
		return isPost? processPost(url, urlParams, isAsyn, isPostBody): processGet(url, urlParams, isAsyn);

	}

	private String processGet(String url, List<BasicNameValuePair> urlParams, boolean isAsyn) {
		HttpGet httpMethod = new HttpGet(url);
		return callUrlGet(httpMethod, urlParams, isAsyn);
	}

	private String processPost(String url, List<BasicNameValuePair> urlParams, boolean isAsyn, boolean isPostBody) {
		HttpPost httpMethod = new HttpPost(url);
		List<BasicNameValuePair> postBodyParams = new ArrayList<BasicNameValuePair>();
		if (isPostBody) {
			postBodyParams = urlParams;
			urlParams = null;
		}
		return callUrlPost(httpMethod, postBodyParams, urlParams, isAsyn);

	}

	private String processRetryLogic(String resJson, URI uri, Integer retryTimes) throws InterruptedException {
		for (int i = 1; i <= retryTimes; i++) {
			if (resJson.contains("\"status\":0") || !resJson.contains("status")) {
				break;
			}
			if (resJson.contains(Constants.ILLEAGLE_AGUMENT) || resJson.contains(Constants.OUT_OF_CALL)
					|| resJson.contains(Constants.OUT_TIME_RANGE) || resJson.contains(Constants.ILLEAGLE_TIMESTAM)) {
				break;
			}
			if (i == retryTimes) {
				resJson = JsonUtil.buildSimpleJson(Constants.ERROR_CODE_TWO, Constants.OVER_RETRY_TIMES);
			}
			Integer retryInterval = conf.getRetryInterval();
			Thread.sleep(retryInterval);
			logger.warn(
					"Havn't received response correctly. sleep {} seconds and retry {} times , response json as below {}",
					retryInterval / 1000, i, resJson);
		}
		logger.info("callUrlGetAsyn {} 请求结束. 结果：{}", uri, resJson);
		return resJson;
	}
	

}
