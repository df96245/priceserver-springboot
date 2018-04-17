package com.ywcx.price.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
public class BaiduConf {

    @Value("${baidu.ak}")
    private String ak;
    
    @Value("${baidu.coord.type.input}")
    private String coordTypeInput;
    
    @Value("${baidu.service.id}")
    private String serviceId;
    
    @Value("${baidu.output}")
    private String output;

	public String getAk() {
		return ak;
	}

	public void setAk(String ak) {
		this.ak = ak;
	}

	public String getCoordTypeInput() {
		return coordTypeInput;
	}

	public void setCoordTypeInput(String coordTypeInput) {
		this.coordTypeInput = coordTypeInput;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}
	
	

}