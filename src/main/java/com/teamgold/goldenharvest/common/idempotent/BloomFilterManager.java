package com.teamgold.goldenharvest.common.idempotent;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

@Component
public class BloomFilterManager {

	private static final int EXPECTED_INSERTIONS = 100_000; // 입력될것으로 예상되는 id들의 수
	private static final double FPP = 0.01; // 목표로 하는 false positive percentage

	// event 단위 별도로 관리되는 bloom filter들의 hashmap
	private final Map<String, BloomFilter<String>> filters = new ConcurrentHashMap<>();

	// 수신한 eventType에 대해 이미 존재하는 bloom filter가 없을 경우 새로 생성 후 반환
	// 이미 존재하는 경우 해당 bloom filter 반환
	private BloomFilter<String> getOrCreateFilter(String eventType) {
		return filters.computeIfAbsent(eventType, f ->
			BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), EXPECTED_INSERTIONS, FPP)
		);
	}

	public boolean isFirstRequest(String eventType, String id) {
		BloomFilter<String> filter = getOrCreateFilter(eventType);

		if (!filter.mightContain(id)) { // 해당 id가 이전에 없었음(첫 request임)을 의미
			filter.put(id);
			return true;
		}
		else return false; // id가 이전에 있었을 수도 있음을 의미 (2차 validation 필요)
	}
}
