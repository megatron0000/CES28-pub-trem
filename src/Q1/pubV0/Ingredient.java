package Q1.pubV0;

public enum Ingredient {
	RUM(65), GRENADINE(10), LIME_JUICE(10), GREEN_STUFF(10), TONIC_WATER(20), GIN(85);
	
	private final int price;
	
	Ingredient(int price) {
		this.price = price;
	}
	
	public int getPrice() {
		return this.price;
	}
}
