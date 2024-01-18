package com.simplesoft.util;

import java.util.HashMap;

import dev.mccue.guava.base.CaseFormat;

/*
 * 카멜표기변환 Util 
 */
public class CamelHashMap extends HashMap<Object, Object>{
	private static final long serialVersionUID = 1l;
	
	public Object put(Object key, Object value) {
		return super.put(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL,(String) key), value);
	}
}

