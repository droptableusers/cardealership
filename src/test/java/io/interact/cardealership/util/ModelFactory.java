package io.interact.cardealership.util;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import io.dropwizard.jackson.Jackson;
import io.interact.cardealership.model.Car;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ModelFactory {
	private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

	public static Car createCar() throws Exception {
		return MAPPER.readValue(fixture("fixtures/car.json"), Car.class);
	}
}
