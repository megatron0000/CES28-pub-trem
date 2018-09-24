package Q1.pubV0;

import java.util.Map;

public class Pub {

	private final Map<Drink, Integer> limits = new Parameters().getLimits();

	public int computeCost(String drink, boolean student, int amount) {
		
		if (!Drink.existsNamed(drink))
			throw new RuntimeException("No such drink exists");
		
		Drink drinkObj = Drink.byName(drink);
		
		Order order = new Order(this.limits, student, drinkObj, amount);
		
		if (!order.isValid()) {
			throw new RuntimeException(order.getValidationMessage());
		}

		return order.computeCost();
	}
}
