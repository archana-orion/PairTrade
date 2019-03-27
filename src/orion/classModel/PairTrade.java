package orion.classModel;

import java.util.Date;

public class PairTrade implements Comparable<PairTrade> {
	
	private long id; // will be same as longTrade
	private StockPair stockPair;
	private Trade longTrade;
	private Trade shortTrade;
	private double openZScore;
	
	private boolean isOpenTrade;
	private double closingZScore;
	
	private Date openTime;
	private Date closeTime;
	private Date maxHoldingTime;
	
	private int pairTradeNumber;
	
	private double netPnl;
	private double returns;
	private double estimatedReturns;
	private double dayFourReturn;
	private double dayThreeReturn;
	private double dayTwoReturns;
	private double dayOneReturns;	
	private double oneDayBeforeReturns;
	private double twoDayBeforeReturns;
	private String reasonOfClosingTrade;
	
	/*private double fft16Value;
	private double fft64Value;
	private double fft128Value;
	private double lmCoeff;
	private double lJungQValue ;
	private double lJungPvalue;
	private double oUValue;
	private double wmaValue;
	private double vwmaValue;
	private double hv60Value;
	private double trailFFTValue;
	private double trailFFT16SdValue;
	private double trailOuMean;
	private double trailOuSd10; */
	

	private TradeClosingReason closingReason;
	
	public PairTrade(StockPair stockPair, Trade longTrade, Trade shortTrade, double openZScore) {
		
		this.id = longTrade.getId();
		this.stockPair = stockPair;
		this.longTrade = longTrade;
		this.shortTrade = shortTrade;
		this.openZScore = openZScore;
		
	}
	
	public PairTrade() {
		
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((openTime == null) ? 0 : openTime.hashCode());
		result = prime * result + pairTradeNumber;
		result = prime * result + ((stockPair == null) ? 0 : stockPair.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PairTrade other = (PairTrade) obj;
		if (openTime == null) {
			if (other.openTime != null)
				return false;
		} else if (!openTime.equals(other.openTime))
			return false;
		if (pairTradeNumber != other.pairTradeNumber)
			return false;
		if (stockPair == null) {
			if (other.stockPair != null)
				return false;
		} else if (!stockPair.equals(other.stockPair))
			return false;
		return true;
	}


	public double getClosingZScore() {
		return closingZScore;
	}

	public void setClosingZScore(double closingZScore) {
		this.closingZScore = closingZScore;
	}

	public double getNetPnl() {
		return netPnl;
	}

	public void setNetPnl(double netPnl) {
		this.netPnl = netPnl;
	}

	public double getReturns() {
		return returns;
	}

	public void setReturns(double returns) {
		this.returns = returns;
	}
	
	public double getDayFourReturn() {
		return dayFourReturn;
	}


	public void setDayFourReturn(double dayFourReturn) {
		this.dayFourReturn = dayFourReturn;
	}


	
	public double getDayThreeReturn() {
		return dayThreeReturn;
	}

	public void setDayThreeReturn(double dayThreeReturn) {
		this.dayThreeReturn = dayThreeReturn;
	}

	public double getDayTwoReturns() {
		return dayTwoReturns;
	}

	public void setDayTwoReturns(double dayTwoReturns) {
		this.dayTwoReturns = dayTwoReturns;
	}
	
	public double getDayOneReturns() {
		return dayOneReturns;
	}

	public void setDayOneReturns(double dayOneReturns) {
		this.dayOneReturns = dayOneReturns;
	}
	
	public double getOneDayBeforeReturns() {
		return oneDayBeforeReturns;
	}

	public void setOneDayBeforeReturns(double oneDayBeforeReturns) {
		this.oneDayBeforeReturns = oneDayBeforeReturns;
	}

	public double getTwoDayBeforeReturns() {
		return twoDayBeforeReturns;
	}

	public void setTwoDayBeforeReturns(double twoDayBeforeReturns) {
		this.twoDayBeforeReturns = twoDayBeforeReturns;
	}

	public long getId() {
		return id;
	}

	public StockPair getStockPair() {
		return stockPair;
	}



	public Trade getLongTrade() {
		return longTrade;
	}

	public Trade getShortTrade() {
		return shortTrade;
	}

	public double getOpenZScore() {
		return openZScore;
	}


	public Date getOpenTime() {
		return openTime;
	}


	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}


	public Date getCloseTime() {
		return closeTime;
	}


	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}



	public int getPairTradeNumber() {
		return pairTradeNumber;
	}



	public void setPairTradeNumber(int pairTradeNumber) {
		this.pairTradeNumber = pairTradeNumber;
	}
	
	public String getReasonOfClosingTrade() {
		return reasonOfClosingTrade;
	}

	public void setReasonOfClosingTrade(String reasonOfClosingTrade) {
		this.reasonOfClosingTrade = reasonOfClosingTrade;
	}



	/* public double getFft16Value() {
		return fft16Value;
	}



	public double getLmCoeff() {
		return lmCoeff;
	}



	public void setLmCoeff(double lmCoeff) {
		this.lmCoeff = lmCoeff;
	}



	public void setFft16Value(double fftValue) {
		this.fft16Value = fftValue;
	}



	public double getlJungQValue() {
		return lJungQValue;
	}



	public void setlJungQValue(double lJungQValue) {
		this.lJungQValue = lJungQValue;
	}



	public double getlJungPvalue() {
		return lJungPvalue;
	}



	public void setlJungPvalue(double lJungPvalue) {
		this.lJungPvalue = lJungPvalue;
	}


	public double getWmaValue() {
		return wmaValue;
	}



	public void setWmaValue(double wmaValue) {
		this.wmaValue = wmaValue;
	}



	public double getoUValue() {
		return oUValue;
	}



	public void setoUValue(double oUValue) {
		this.oUValue = oUValue;
	}



	public double getVwmaValue() {
		return vwmaValue;
	}


	public void setVwmaValue(double vwmaValue) {
		this.vwmaValue = vwmaValue;
	}


	public double getHv60Value() {
		return hv60Value;
	}


	public void setHv60Value(double hv60Value) {
		this.hv60Value = hv60Value;
	}


	public double getTrailFFTValue() {
		return trailFFTValue;
	}


	public void setTrailFFTValue(double trailFFTValue) {
		this.trailFFTValue = trailFFTValue;
	} */


	public Date getMaxHoldingTime() {
		return maxHoldingTime;
	}


	public void setMaxHoldingTime(Date maxHoldingTime) {
		this.maxHoldingTime = maxHoldingTime;
	}


	/* public double getFft64Value() {
		return fft64Value;
	}


	public void setFft64Value(double fft64Value) {
		this.fft64Value = fft64Value;
	}


	public double getFft128Value() {
		return fft128Value;
	}


	public void setFft128Value(double fft128Value) {
		this.fft128Value = fft128Value;
	}

	public double getTrailOuMean() {
		return trailOuMean;
	}


	public void setTrailOuMean(double trailOuMean) {
		this.trailOuMean = trailOuMean;
	} */


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PairTrade [id=");
		builder.append(id);
		builder.append(", stockPair=");
		builder.append(stockPair);
		builder.append(", longTrade=");
		builder.append(longTrade);
		builder.append(", shortTrade=");
		builder.append(shortTrade);
		builder.append(", openZScore=");
		builder.append(openZScore);
		builder.append(", closingZScore=");
		builder.append(closingZScore);
		builder.append(", openTime=");
		builder.append(openTime);
		builder.append(", closeTime=");
		builder.append(closeTime);
		builder.append(", pairTradeNumber=");
		builder.append(pairTradeNumber);
		builder.append(", netPnl=");
		builder.append(netPnl);
		builder.append(", returns=");
		builder.append(returns);
		builder.append("]");
		return builder.toString();
	}



	@Override
	public int compareTo(PairTrade o) {
		
		int comparingOpenTime = (getOpenTime()).compareTo(o.getOpenTime());
		
		if(comparingOpenTime == 0) {
			return getStockPair().toString().compareTo(o.getStockPair().toString());
		}
		
		return comparingOpenTime;
	}


	public TradeClosingReason getClosingReason() {
		return closingReason;
	}


	public void setClosingReason(TradeClosingReason closingReason) {
		this.closingReason = closingReason;
	}


	/*public double getTrailFFT16SdValue() {
		return trailFFT16SdValue;
	}


	public void setTrailFFT16SdValue(double trailFFT16SdValue) {
		this.trailFFT16SdValue = trailFFT16SdValue;
	} */


	public double getEstimatedReturns() {
		return estimatedReturns;
	}


	public void setEstimatedReturns(double estimatedReturns) {
		this.estimatedReturns = estimatedReturns;
	}


	/* public double getTrailOuSd10() {
		return trailOuSd10;
	}


	public void setTrailOuSd10(double trailOuSd10) {
		this.trailOuSd10 = trailOuSd10;
	} */


	public boolean isOpenTrade() {
		return isOpenTrade;
	}


	public void setOpenTrade(boolean isOpenTrade) {
		this.isOpenTrade = isOpenTrade;
	}


	public void setId(long id) {
		this.id = id;
	}


	public void setStockPair(StockPair stockPair) {
		this.stockPair = stockPair;
	}


	public void setLongTrade(Trade longTrade) {
		this.longTrade = longTrade;
	}


	public void setShortTrade(Trade shortTrade) {
		this.shortTrade = shortTrade;
	}


	public void setOpenZScore(double openZScore) {
		this.openZScore = openZScore;
	}
	
	
	
}
