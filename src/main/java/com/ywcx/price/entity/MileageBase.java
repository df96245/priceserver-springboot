package com.ywcx.price.entity;

//各种里程
public class MileageBase {
	private Double estimateMileage;
	
	private Double realTimeMileage;
	
	private Double midNightMileage;
	
	private Double outOfRangeMileage;

	public Double getEstimateMileage() {
		return estimateMileage;
	}

	public void setEstimateMileage(Double estimateMileage) {
		this.estimateMileage = estimateMileage;
	}

	public Double getRealTimeMileage() {
		return realTimeMileage;
	}

	public void setRealTimeMileage(Double realTimeMileage) {
		this.realTimeMileage = realTimeMileage;
	}

	public Double getMidNightMileage() {
		return midNightMileage;
	}

	public void setMidNightMileage(Double midNightMileage) {
		this.midNightMileage = midNightMileage;
	}

	public Double getOutOfRangeMileage() {
		return outOfRangeMileage;
	}

	public void setOutOfRangeMileage(Double outOfRangeMileage) {
		this.outOfRangeMileage = outOfRangeMileage;
	}
}
