package com.ywcx.price.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ywcx.price.common.Constants;

public class HttpUtil {
	private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	public static String parseRes2Json(HttpResponse response) {
		HttpEntity entity = response.getEntity();
		String body = null;
		if (entity == null) {
			return null;
		}
		try {
			body = EntityUtils.toString(entity, Constants.UTF_8);
			EntityUtils.consume(entity);
			return body;
		} catch (ParseException e) {
			logger.warn("the response's content inputstream is corrupt", e);
		} catch (IOException e) {
			logger.warn("the response's content inputstream is corrupt", e);
		}
		return body;
	}

	public static void setUrl2HttpMethod(HttpRequestBase httpMethod, List<BasicNameValuePair> urlParams) {
		if (null != urlParams) {
			try {
				String getUrl = EntityUtils.toString(new UrlEncodedFormEntity(urlParams, Constants.UTF_8));
				httpMethod.setURI(new URI(httpMethod.getURI().toString() + "?" + getUrl));
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

	public static List<BasicNameValuePair> map2BasicNVPair(Map<String, String> KVParams) {
		List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
		for (String key : KVParams.keySet()) {
			pairList.add(new BasicNameValuePair(key, KVParams.get(key)));
		}
		return pairList;
	}
}
