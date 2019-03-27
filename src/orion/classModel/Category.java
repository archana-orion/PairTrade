package orion.classModel;

import orion.classModel.Category;

public enum Category {
	STK("Equity"),
	FUT("Futures"),
	OPT("Options") ;
	
	private final String category;

    private Category(String orderStatus) {
        this.category = orderStatus;
    }

	public static Category getAssetCategory(String string) {
		for(Category catg : values()) {
			catg.category.equals(string);
			return catg;
		}
		return null;
	}
}