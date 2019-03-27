package orion.classModel;



public class StockPair implements Comparable<StockPair> {
	
	private final Asset longAsset;
	private final Asset shortAsset;
	private final String pair;
	
	public StockPair(Asset longAsset, Asset shortAsset) {
		this.longAsset = longAsset;
		this.shortAsset = shortAsset;
		pair = longAsset.getSymbol()+"|"+shortAsset.getSymbol();
	}

	public Asset getLongAsset() {
		return longAsset;
	}

	public Asset getShortAsset() {
		return shortAsset;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pair == null) ? 0 : pair.hashCode());
		
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
		StockPair other = (StockPair) obj;
		if (pair == null) {
			if (other.pair != null)
				return false;
		} else if (!pair.equals(other.pair))
			return false;
		return true;
	}
	
	

	@Override
	public int compareTo(StockPair o) {
		return pair.compareTo(o.getPair());
	}


	public String getPair() {
		return pair;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("["+longAsset.getSymbol());
		builder.append("|"+shortAsset.getSymbol());
		builder.append("]");
		return builder.toString();
	}
	
	
}
