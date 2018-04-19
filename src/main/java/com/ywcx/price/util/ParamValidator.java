package com.ywcx.price.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ParamValidator {
	private static Logger logger= LoggerFactory.getLogger(ParamValidator.class);
	
	public static boolean validateIsBlank(String str) {
		boolean isBlank = StringUtils.isBlank(str);
		if (isBlank) {
			logger.debug("we have received empty or null str",str);
		}
		return isBlank;
	}
}
