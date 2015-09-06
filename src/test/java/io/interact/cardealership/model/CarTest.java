package io.interact.cardealership.model;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;
import io.dropwizard.jackson.Jackson;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CarTest {

	private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void serializesToJson() throws IOException {
		final Car builtCar = createCar();
		Car fixtureCar = MAPPER.readValue(fixture("fixtures/car.json"), Car.class);

		final String expected = MAPPER.writeValueAsString(fixtureCar);
		assertThat(MAPPER.writeValueAsString(builtCar)).isEqualTo(expected);
	}

	@Test
	public void deserializesFromJson() throws Exception {
		final Car builtCar = createCar();
		assertThat(MAPPER.readValue(fixture("fixtures/car.json"), Car.class))
				.isEqualTo(builtCar);
	}

	private Car createCar() {
		return Car.builder()
				.engineSize(1598)
				.fuelType("Diesel")
				.registration("04/2011")
				.bodyStyle("Limousine")
				.fuelEconomy(4.2f)
				.seats(5)
				.interiorColour("Anthracite")
				.interior("Textile")
				.doors(5)
				.transmission("Manual, 5 Gears")
				.emissionClass(5)
				.price(14490)
				.exteriorColour("Black Myth, metallic")
				.model("A3 SB Ambiente 1,6 TDI DPF")
				.power(77)
				.brand("Audi")
				.emissions(112)
				.mileage(69207)
				.build();
	}

}
