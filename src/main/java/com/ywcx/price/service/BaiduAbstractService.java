package com.ywcx.price.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.ywcx.price.config.BaiduConf;
import com.ywcx.price.http.HttpHelper;

public abstract class BaiduAbstractService {
	@Autowired
	protected HttpHelper instance;

	@Autowired
	protected BaiduConf bdConfig;
}
