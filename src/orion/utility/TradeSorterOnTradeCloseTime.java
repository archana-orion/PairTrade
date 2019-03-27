package orion.utility;

import java.util.Comparator;

import orion.classModel.PairTrade;

public class TradeSorterOnTradeCloseTime implements Comparator<PairTrade> {

	@Override
	public int compare(PairTrade t1, PairTrade t2) {
		return t1.getCloseTime().compareTo(t2.getCloseTime());
	}

}
