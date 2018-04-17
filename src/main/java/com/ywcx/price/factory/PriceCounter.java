package com.ywcx.price.factory;

import com.ywcx.price.entity.MileageBase;

public abstract class PriceCounter {
	
	public abstract Double callPrice(Double distance,String city,Double lowSpeedMin);
	
	protected MileageBase initMileage(Double distance) {
		MileageBase mileageBase= new MileageBase();
		mileageBase.setEstimateMileage(distance);
		mileageBase.setRealTimeMileage(distance);
		return mileageBase;
	}
	
}
