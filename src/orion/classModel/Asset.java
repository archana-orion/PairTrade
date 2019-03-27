package orion.classModel;

import java.util.Date;

import orion.classModel.Category;
import orion.classModel.Asset;

public class Asset implements Comparable<Asset>{

	private String symbol;
	private Category category;
	private Date expiryDate;
	private double multipier;
	private String sector;
	private String exchange;
	private String currency;
	private double minTick;
	
	public Asset(String symbol, Category category, Date expiryDate, double multipier, String sector, String exchange,
			String currency) {
		
		this.symbol = symbol;
		this.category = category == null ? Category.STK : category;
		this.expiryDate = expiryDate == null ? new Date(Long.MAX_VALUE) : expiryDate;
		this.multipier = multipier == 0 ? 1.0 : multipier;
		this.sector = sector;
		this.exchange = exchange == null ? "SMART" : exchange;
		this.currency = currency == null ? "USD" : currency;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public double getMultipier() {
		return multipier;
	}

	public void setMultipier(double multipier) {
		this.multipier = multipier;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getMinTick() {
		return minTick;
	}

	public void setMinTick(double minTick) {
		this.minTick = minTick;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((expiryDate == null) ? 0 : expiryDate.hashCode());
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
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
		Asset other = (Asset) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (expiryDate == null) {
			if (other.expiryDate != null)
				return false;
		} else if (!expiryDate.equals(other.expiryDate))
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}
	
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Asset [symbol=");
		builder.append(symbol);
		builder.append(", category=");
		builder.append(category);
		builder.append(", expiryDate=");
		builder.append(expiryDate);
		builder.append(", multipier=");
		builder.append(multipier);
		builder.append(", sector=");
		builder.append(sector);
		builder.append(", exchange=");
		builder.append(exchange);
		builder.append(", currency=");
		builder.append(currency);
		builder.append("]");
		return builder.toString();
	}
	
	public int compareTo(Asset o) {
		return this.getSymbol().compareTo(o.getSymbol());
	}


}

