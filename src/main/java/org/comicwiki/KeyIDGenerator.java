package org.comicwiki;

import java.util.concurrent.atomic.AtomicLong;

public final class KeyIDGenerator {

	private final AtomicLong count;

	public KeyIDGenerator(long start){
		count = new AtomicLong(start);
	}
	
	public long createInstanceId() {
		return count.incrementAndGet();
	}
	
	public long createID() {
		return count.incrementAndGet();
	}
}
