package fusion.distance;

public class DistanceFactory extends AbstractDistanceFactory {

	@Override
	public Distance makeDistance(String distance) {
		switch (distance) {
		case "HM":
			return new DistanceHamming();

		default:
			throw new IllegalArgumentException("The distance " + distance + " does not exist.");
		}
	}

}
