package com.ywcx.price.pricecounter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ywcx.price.common.Constants;
import com.ywcx.price.entity.MileageBase;
import com.ywcx.price.entity.PriceBase;
import com.ywcx.price.factory.AbstractPriceCounter;
import com.ywcx.price.processor.PriceCalculator;
import com.ywcx.price.service.reporstory.PriceBaseService;
import com.ywcx.price.util.NumberUtil;

@Component
public class SelfPriceCounter extends AbstractPriceCounter {
	private Logger logger = LoggerFactory.getLogger(AbstractPriceCounter.class);
	
	public static Map<String, PriceBase> selfMap= new HashMap<String, PriceBase>();
	
	@Autowired
	private PriceBaseService pbService;
	
	
	@PostConstruct
	public void init(){
		List<PriceBase> selfList =pbService.getAllPriceBaseByType(Constants.SELF_CAR);
		for (PriceBase priceBase : selfList) {
			selfMap.put(priceBase.getCity(), priceBase);
		}
	}


	public Double callPrice(Double distance, String city,Double lowSpeedMin) {
		logger.debug("Begin calculate self car price ...");
		PriceBase priceBase = selfMap.get(city);
		
		// TODO
		MileageBase mileageBase = initMileage(NumberUtil.holdDecimalPlaces(distance / 1000, 2));
		if (null == priceBase) {
		}
		
		return PriceCalculator.calAllPrice(priceBase, mileageBase,lowSpeedMin);
	}

}
