package orion.classModel;

import java.util.Date;

public class Trade implements Comparable<Trade> {
	
	private final long id;
	
	private final Asset asset;
	
	private final Date openTime;
	private Date fillTime;
	private Date closeTime;
	private Date cancelTime;

	private double slippage;	
	
	private double liveAsk;
	private double liveBid;
	private double liveLtp;
	private double liveVolume;
	private double quantity ; 
	
	private final double orderPrice;
	private double executionPrice;
	private double closingPrice;
	
	private double fut_open;
	private double fut_close;
	
	//private TradeType trendWhileEntering;
	
	private double returns;
	//private double ADXClosePrice = 0.0;
	//private double ADXReturns = 0.0;
	//private double adxVAlue;
	//private TradeType tradeType;
	
	private double daysTradingRange;
	
	private double daysOpen;
	private double daysClosing;
	private double yesterDaysClose;
	private double yesterDaysOpen;

	
	
	//private double sdOf5DaysReturns;
	//private double meanOf5DaysTradingRange;
	
	//private double vwapPrice;

	public Trade(long id, Asset asset, Date openTime, double orderPrice) {
		this.id = id;
		this.asset = asset;
		this.openTime = openTime;
		this.orderPrice = orderPrice;
	
	}


	public double getQuantity() {
		return quantity;
	}


	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((asset == null) ? 0 : asset.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((openTime == null) ? 0 : openTime.hashCode());
		long temp;
		temp = Double.doubleToLongBits(orderPrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Trade other = (Trade) obj;
		if (asset == null) {
			if (other.asset != null)
				return false;
		} else if (!asset.equals(other.asset))
			return false;
		if (id != other.id)
			return false;
		if (openTime == null) {
			if (other.openTime != null)
				return false;
		} else if (!openTime.equals(other.openTime))
			return false;
		if (Double.doubleToLongBits(orderPrice) != Double.doubleToLongBits(other.orderPrice))
			return false;
		return true;
	}


	public long getId() {
		return id;
	}

	public Asset getAsset() {
		return asset;
	}

	public Date getOpenTime() {
		return openTime;
	}


	public Date getFillTime() {
		return fillTime;
	}

	public void setFillTime(Date fillTime) {
		this.fillTime = fillTime;
	}

	public Date getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}

	public Date getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}

	public double getSlippage() {
		return slippage;
	}

	public void setSlippage(double slippage) {
		this.slippage = slippage;
	}

	public double getLiveAsk() {
		return liveAsk;
	}

	public void setLiveAsk(double liveAsk) {
		this.liveAsk = liveAsk;
	}

	public double getLiveBid() {
		return liveBid;
	}

	public void setLiveBid(double liveBid) {
		this.liveBid = liveBid;
	}

	public double getLiveLtp() {
		return liveLtp;
	}

	public void setLiveLtp(double liveLtp) {
		this.liveLtp = liveLtp;
	}

	public double getLiveVolume() {
		return liveVolume;
	}

	public void setLiveVolume(double liveVolume) {
		this.liveVolume = liveVolume;
	}

	public double getOrderPrice() {
		return orderPrice;
	}

	public double getExecutionPrice() {
		return executionPrice;
	}

	public void setExecutionPrice(double executionPrice) {
		this.executionPrice = executionPrice;
	}

	public double getClosingPrice() {
		return closingPrice;
	}

	public void setClosingPrice(double closingPrice) {
		this.closingPrice = closingPrice;
	}

	public double getReturns() {
		return returns;
	}

	public void setReturns(double returns) {
		this.returns = returns;
	}

	/*
	  public TradeType getTradeType() { return tradeType; }
	 */


	/*
	  public void setTradeType(TradeType tradeType) { this.tradeType = tradeType; }
	 */


	public double getFut_open() {
		return fut_open;
	}


	public void setFut_open(double fut_open) {
		this.fut_open = fut_open;
	}


	public double getFut_close() {
		return fut_close;
	}


	public void setFut_close(double fut_close) {
		this.fut_close = fut_close;
	}


	/*public double getADXClosePrice() {
		return ADXClosePrice;
	}


	public void setADXClosePrice(double aDXClosePrice) {
		ADXClosePrice = aDXClosePrice;
	}


	public double getADXReturns() {
		return ADXReturns;
	}


	public void setADXReturns(double aDXReturns) {
		ADXReturns = aDXReturns;
	}


	
	 public TradeType getTrendWhileEntering() { return trendWhileEntering; }
	  
	  
	 this.trendWhileEntering = trendWhileEntering; }
	 


	public double getAdxVAlue() {
		return adxVAlue;
	}


	public void setAdxVAlue(double adxVAlue) {
		this.adxVAlue = adxVAlue;
	} */


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Trade [id=");
		builder.append(id);
		builder.append(", asset=");
		builder.append(asset);
		builder.append(", openTime=");
		builder.append(openTime);
		builder.append(", fillTime=");
		builder.append(fillTime);
		builder.append(", closeTime=");
		builder.append(closeTime);
		builder.append(", cancelTime=");
		builder.append(cancelTime);
		builder.append(", quantity=");
		builder.append(quantity);
		builder.append(", slippage=");
		builder.append(slippage);
		builder.append(", liveAsk=");
		builder.append(liveAsk);
		builder.append(", liveBid=");
		builder.append(liveBid);
		builder.append(", liveLtp=");
		builder.append(liveLtp);
		builder.append(", liveVolume=");
		builder.append(liveVolume);
		builder.append(", orderPrice=");
		builder.append(orderPrice);
		builder.append(", executionPrice=");
		builder.append(executionPrice);
		builder.append(", closingPrice=");
		builder.append(closingPrice);
		builder.append(", returns=");
		builder.append(returns);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int compareTo(Trade o) {
		return (getOpenTime()).compareTo(o.getOpenTime());
	}


	public double getDaysTradingRange() {
		return daysTradingRange;
	}


	public void setDaysTradingRange(double daysTradingRange) {
		this.daysTradingRange = daysTradingRange;
	}


	public double getDaysOpen() {
		return daysOpen;
	}


	public void setDaysOpen(double daysOpen) {
		this.daysOpen = daysOpen;
	}


	public double getDaysClosing() {
		return daysClosing;
	}


	public void setDaysClosing(double daysClosing) {
		this.daysClosing = daysClosing;
	}


	/*public double getSdOf5DaysReturns() {
		return sdOf5DaysReturns;
	}


	public void setSdOf5DaysReturns(double sdOf5DaysReturns) {
		this.sdOf5DaysReturns = sdOf5DaysReturns;
	}


	public double getMeanOf5DaysTradingRange() {
		return meanOf5DaysTradingRange;
	}


	public void setMeanOf5DaysTradingRange(double meanOf5DaysTradingRange) {
		this.meanOf5DaysTradingRange = meanOf5DaysTradingRange;
	}*/


	public double getYesterDaysClose() {
		return yesterDaysClose;
	}


	public void setYesterDaysClose(double yesterDaysClose) {
		this.yesterDaysClose = yesterDaysClose;
	}


	/*public double getVwapPrice() {
		return vwapPrice;
	}


	public void setVwapPrice(double vwapPrice) {
		this.vwapPrice = vwapPrice;
	}*/


	public double getYesterDaysOpen() {
		return yesterDaysOpen;
	}


	public void setYesterDaysOpen(double yesterDaysOpen) {
		this.yesterDaysOpen = yesterDaysOpen;
	}
	

	
}
