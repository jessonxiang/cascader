package com.bh.core.tools.bloomFilter.filter;

import com.bh.proprietor.core.tools.bloomFilter.bitMap.LongMap;
import com.bh.proprietor.core.tools.bloomFilter.BloomFilter;
import com.bh.proprietor.core.tools.bloomFilter.bitMap.BitMap;
import com.bh.proprietor.core.tools.bloomFilter.bitMap.IntMap;

/**
 * 抽象Bloom过滤器
 * 
 * @author loolly
 *
 */
public abstract class AbstractFilter implements BloomFilter {

	private BitMap bm = null;

	protected long size = 0;

	public AbstractFilter(long maxValue, int machineNum) {
		init(maxValue, machineNum);
	}

	public AbstractFilter(long maxValue) {
		this(maxValue, BitMap.MACHINE32);
	}

	public void init(long maxValue, int machineNum) {
		this.size = maxValue;
		switch (machineNum) {
		case BitMap.MACHINE32:
			bm = new IntMap((int) (size / machineNum));
			break;
		case BitMap.MACHINE64:
			bm = new LongMap((int) (size / machineNum));
			break;
		default:
			throw new RuntimeException("Error Machine number!");
		}
	}

	@Override
	public boolean contains(String str) {
		return bm.contains(hash(str));
	}

	@Override
	public boolean add(String str) {
		final long hash = this.hash(str);
		if (bm.contains(hash)) {
			return false;
		}

		bm.add(hash);
		return true;
	}

	/**
	 * 自定义Hash方法
	 * 
	 * @param str
	 *            字符串
	 * @return HashCode
	 */
	public abstract long hash(String str);
}