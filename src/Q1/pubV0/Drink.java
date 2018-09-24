package Q1.pubV0;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Drink {
	BEER("hansa", 74, new ArrayList<>(), 10),

	CIDER("grans", 103, new ArrayList<>(), 10), PROPER_CIDER("strongbow", 110, new ArrayList<>(), 10),

	GT("gt", 0, new ArrayList<Ingredient>() {
		private static final long serialVersionUID = 1L;
		{
			add(Ingredient.TONIC_WATER);
			add(Ingredient.GREEN_STUFF);
			add(Ingredient.GIN);
		}
	}, 0),

	BACARDI_SPECIAL("bacardi_special", 0, new ArrayList<Ingredient>() {
		private static final long serialVersionUID = 1L;
		{
			add(Ingredient.RUM);
			add(Ingredient.GRENADINE);
			add(Ingredient.LIME_JUICE);
			add(Ingredient.GIN);
		}
	}, 0);

	private final String name;
	private final int basePrice;
	private final List<Ingredient> ingredients;
	private final int discountPercent;

	private static Map<String, Drink> drinkMap = new HashMap<>();

	static {
		for (Drink drink : Drink.values())
			drinkMap.put(drink.name, drink);
	}

	Drink(String name, int basePrice, List<Ingredient> ingredients, int discountPercent) {
		this.name = name;
		this.basePrice = basePrice;
		this.ingredients = ingredients;
		this.discountPercent = discountPercent;
	}

	public int getPrice(boolean applyDiscount) {
		int price = this.basePrice;
		for (int i = 0; i < this.ingredients.size(); i++)
			price += this.ingredients.get(i).getPrice();
		if (applyDiscount && this.discountPercent != 0)
			price = price - price / this.discountPercent;
		return price;
	}

	public static boolean existsNamed(String name) {
		return drinkMap.containsKey(name);
	}

	public static Drink byName(String name) {
		return drinkMap.get(name);
	}

}
