package Q1.pubV0;

import java.util.HashMap;
import java.util.Map;

public class Parameters {
	
	private static final Map<Drink, Integer> limits = new HashMap<Drink, Integer> () {
		private static final long serialVersionUID = 1L;

		{
			put(Drink.GT, 2);
			put(Drink.BACARDI_SPECIAL, 2);
		}
	};
	
	public Map<Drink, Integer> getLimits() {
		return this.limits;
	}
}
