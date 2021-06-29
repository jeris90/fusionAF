package fusion.distance;

public class DistanceFactory extends AbstractDistanceFactory {

	@Override
	public Distance makeDistance(String distance) {
		switch (distance) {
		case "HM":
			return new DistanceHamming();
		case "JC":
			return new DistanceJaccard();
		case "SD":
			return new DistanceSorensenDice();
		default:
			throw new IllegalArgumentException("The distance " + distance + " does not exist.");
		}
	}

}
