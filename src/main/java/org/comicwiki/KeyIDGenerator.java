package org.comicwiki;

import java.util.concurrent.atomic.AtomicInteger;

public final class KeyIDGenerator {

	private final AtomicInteger count;

	public KeyIDGenerator(int start){
		count = new AtomicInteger(start);
	}
	
	public String createID() {
		return "I" + count.incrementAndGet();
	}
}
