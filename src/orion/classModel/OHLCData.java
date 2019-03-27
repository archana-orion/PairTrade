package orion.classModel;

import java.util.Date;


import orion.classModel.*;


public class OHLCData implements Comparable {
	private final Asset asset;
	private final Date time;
	private final double open;
	private final double high;
	private final double low;
	private final double close;
	private final double volume;
	
	//private ADX adx;

	public OHLCData( Asset asset,Date time,double open, double high, double low, double close, double volume) {
		
		this.asset = asset;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
		this.time = new Date(time.getTime());
	}


	public Asset getAsset() {
		return asset;
	}


	public Date getTime() {
		return time;
	}


	public double getOpen() {
		return open;
	}


	public double getHigh() {
		return high;
	}


	public double getLow() {
		return low;
	}


	public double getClose() {
		return close;
	}


	public double getVolume() {
		return volume;
	}



	/*
	 * public ADX getAdx() { return adx; }
	 * 
	 * 
	 * public void setAdx(ADX adx) { this.adx = adx; }
	 */


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((asset == null) ? 0 : asset.hashCode());
		long temp;
		temp = Double.doubleToLongBits(close);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(high);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(low);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(open);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		temp = Double.doubleToLongBits(volume);
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
		OHLCData other = (OHLCData) obj;
		if (asset == null) {
			if (other.asset != null)
				return false;
		} else if (!asset.equals(other.asset))
			return false;
		if (Double.doubleToLongBits(close) != Double.doubleToLongBits(other.close))
			return false;
		if (Double.doubleToLongBits(high) != Double.doubleToLongBits(other.high))
			return false;
		if (Double.doubleToLongBits(low) != Double.doubleToLongBits(other.low))
			return false;
		if (Double.doubleToLongBits(open) != Double.doubleToLongBits(other.open))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (Double.doubleToLongBits(volume) != Double.doubleToLongBits(other.volume))
			return false;
		return true;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\nOHLCData [asset=");
		builder.append(asset);
		builder.append(", time=");
		builder.append(time);
		builder.append(", open=");
		builder.append(open);
		builder.append(", high=");
		builder.append(high);
		builder.append(", low=");
		builder.append(low);
		builder.append(", close=");
		builder.append(close);
		builder.append(", volume=");
		builder.append(volume);
		builder.append(", ADX=");
		//builder.append(adx);
		builder.append("]");
		return builder.toString();
	}


	@Override
	public int compareTo(Object o) {
		OHLCData data = (OHLCData) o;
		
		return this.getTime().compareTo(data.getTime());
	}
	
	


}
