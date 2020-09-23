package com.afterwave.core.util;
 
@SuppressWarnings("rawtypes")
public class EnumUtil {

	public static boolean isDefined(Enum[] enums, String value) {
		for (Enum e : enums) {
			if (e.name().equals(value)) {
				return true;
			}
		}
		return false;
	}

}
