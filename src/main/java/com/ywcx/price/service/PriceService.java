package com.ywcx.price.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ywcx.price.common.Constants;
import com.ywcx.price.common.ResponseCode;
import com.ywcx.price.common.ServerResponse;
import com.ywcx.price.entity.json.BaiduResponse;
import com.ywcx.price.entity.json.BaiduResponseBase;
import com.ywcx.price.entity.json.Result;
import com.ywcx.price.factory.PriceCounterFactory;
import com.ywcx.price.util.JsonUtil;

@Service
public class PriceService {
	private Logger logger = LoggerFactory.getLogger(PriceService.class);

	private ThreadLocal<String> ori = new ThreadLocal<String>();
	private ThreadLocal<String> dest = new ThreadLocal<String>();

	@Autowired
	private TrackService tkService;

	public ServerResponse getRealTimePrice(String type, String city, String entityName, String start_time,
			String end_time) throws Exception {
		String json = tkService.getDistance(entityName, start_time, end_time);
		BaiduResponseBase response = JsonUtil.parseJsonFromGetDistance(json);
		int status = response.getStatus();
		if (status!=0) {
			return ServerResponse.createByErrorCodeMessage(status,response.getMessage());
		}
		Double distance = response.getDistance();
		Double lowSpeedMin = 0.0;
		Double realTimePrice = PriceCounterFactory.getPriceCounter(type).callPrice(distance, city, lowSpeedMin);
		return ServerResponse.createBySuccess("获取实时价钱成功", realTimePrice);
	}

	public ServerResponse getEstPrice(String oriLatLng, String destLatLng, String city) throws Exception {
		ori.set(oriLatLng);
		dest.set(destLatLng);
		String json = tkService.routematrix(oriLatLng, destLatLng, city);
		BaiduResponse<Result> response=JsonUtil.parseJsonFromDriving(json);
		int status = response.getStatus();
		if (status!=0) {
			return ServerResponse.createByErrorCodeMessage(status,response.getMessage());
		}
		Double distance = response.getDistance();
		distance = null!=distance ? distance : response.getResult().get(0).getDistance().getValue();
		
		Map<String, Double> priceMap = new HashMap<>();
		Double lowSpeedMin = 0.0;
		priceMap.put("SELF", PriceCounterFactory.getPriceCounter("SELF").callPrice(distance, city, lowSpeedMin));
		priceMap.put("MANY", PriceCounterFactory.getPriceCounter("MANY").callPrice(distance, city, lowSpeedMin));

		return ServerResponse.createBySuccess("获取估算价格成功", priceMap);
	}

}
