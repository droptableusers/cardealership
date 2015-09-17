package io.interact.cardealership.daos;

import java.util.HashMap;
import java.util.Map;

import lombok.Builder;
import io.interact.cardealership.model.Car;

public class MockCarEsDao extends CarElasticsearchDao {

	@Builder
	public MockCarEsDao() {
		super(null, "index", "type");
	}

	Map<String, Car> cars  = new HashMap<>();
	
	@Override
	public String create(Car car) {
		String carId = getId(car);
		cars.put(carId, car);
		return carId;
	}
	
	private String getId(Car car) {
		return "" + car.hashCode();
	}
}
