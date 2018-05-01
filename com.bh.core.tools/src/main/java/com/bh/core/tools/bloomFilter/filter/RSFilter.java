package com.bh.core.tools.bloomFilter.filter;

import com.bh.proprietor.core.tools.util.HashUtil;

public class RSFilter extends AbstractFilter {

	public RSFilter(long maxValue, int machineNum) {
		super(maxValue, machineNum);
	}

	public RSFilter(long maxValue) {
		super(maxValue);
	}

	@Override
	public long hash(String str) {
		return HashUtil.rsHash(str) % size;
	}

}
