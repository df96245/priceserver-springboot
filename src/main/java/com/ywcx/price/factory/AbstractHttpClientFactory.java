package com.ywcx.price.factory;

public abstract class AbstractHttpClientFactory<T> {
	public abstract T getClient();
}
