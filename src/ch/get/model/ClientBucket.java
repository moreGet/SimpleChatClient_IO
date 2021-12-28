package ch.get.model;

import java.util.concurrent.ConcurrentHashMap;

public class ClientBucket {
	private static ConcurrentHashMap<String, Client> clientBucket = new ConcurrentHashMap<>();
	
	private ClientBucket() {}
	
	public static ConcurrentHashMap<String, Client> getClientBucket() {
		return clientBucket;
	}
}
