package com.ywcx.price.entity.json;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BaiduResponse<T> extends BaiduResponseBase{
	private List<T> result;
	
	public List<T> getResult() {
		return result;
	}
	public void setResult(List<T> result) {
		this.result = result;
	}
	@Override
	public String toString() {
		return "BaiduResponse [status=" + this.getStatus() + ", result=" + result + ", message=" + this.getMessage() + ", distance="
				+ this.getDistance() + "]";
	}
	
	
}
