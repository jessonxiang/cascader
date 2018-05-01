package com.bh.proprietor.core.tools.bloomFilter.filter;

import com.bh.proprietor.core.tools.util.HashUtil;

public class ELFFilter extends AbstractFilter {

	public ELFFilter(long maxValue, int MACHINENUM) {
		super(maxValue, MACHINENUM);
	}

	public ELFFilter(long maxValue) {
		super(maxValue);
	}

	@Override
	public long hash(String str) {
		return HashUtil.elfHash(str) % size;
	}

}
