package ch.get.util;

import java.util.UUID;

public class UuidUtil {
	
	private UuidUtil() {}
	
	public static String getUuid() {
		return UUID.randomUUID().toString();
	}
}
