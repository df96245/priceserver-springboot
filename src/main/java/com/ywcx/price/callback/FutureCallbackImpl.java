package com.ywcx.price.callback;

import java.util.concurrent.CountDownLatch;

import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FutureCallbackImpl implements FutureCallback<HttpResponse> {
	private static Logger logger = LoggerFactory.getLogger(FutureCallbackImpl.class);
	private CountDownLatch latch;

	public FutureCallbackImpl(CountDownLatch latch) {
		this.latch=latch;
	}
	
	public void completed(HttpResponse response) {
		latch.countDown();
	}

	public void cancelled() {
		latch.countDown();
	}
	public void failed(Exception e) {
		latch.countDown();
	}


}