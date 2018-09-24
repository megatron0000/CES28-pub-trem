package Q1.pubV0;

import java.util.Map;

public class Order {

	private final Map<Drink, Integer> limits;
	private final boolean isStudent;
	private final Drink drink;
	private final int amount;

	public Order(Map<Drink, Integer> limits, boolean isStudent, Drink drink, int amount) {
		this.limits = limits;
		this.isStudent = isStudent;
		this.drink = drink;
		this.amount = amount;
	}

	public boolean isValid() {
		return this.limits.get(this.drink) == null || this.amount < this.limits.get(this.drink);
	}

	public String getValidationMessage() {
		if (this.isValid())
			return "";
		else
			return "Too many drinks, max " + this.limits.get(this.drink) + ".";
	}
	
	public int computeCost() {
		return amount * this.drink.getPrice(isStudent);
	}

}
