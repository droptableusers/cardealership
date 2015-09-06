package end2end;

import static io.dropwizard.testing.FixtureHelpers.fixture;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;

import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.interact.cardealership.DealershipApplication;
import io.interact.cardealership.DealershipConfiguration;
import io.interact.cardealership.model.Car;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;

public class DealershipEndToEndTest {

	private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
	@Rule
	public DropwizardAppRule<DealershipConfiguration> rule = 
			new DropwizardAppRule<DealershipConfiguration>(DealershipApplication.class, 
					ResourceHelpers.resourceFilePath("config.yml"));
	private Client client;
	private Car testCar;
	
	@Before
	public void setUp() {
		client = new JerseyClientBuilder(rule.getEnvironment()).build("test client");
	}
	
	@Test
	public void itSavesCarData() throws Exception {
		URI location = postCar();
		getCar(location);
		dataIsPresentInElasticsearch(getIdFrom(location));
	}

	private void dataIsPresentInElasticsearch(String id) {
		
	}

	private String getIdFrom(URI location) {
		String path = location.getPath();
		return path.substring(path.lastIndexOf("/"));
	}

	private void getCar(URI location) {
		Car carFromAPI = client.target(location)
                .request()
                .get(Car.class);
		assertThat(testCar).isEqualTo(carFromAPI);
	}

	private URI postCar() throws IOException, JsonParseException, JsonMappingException {
		testCar = MAPPER.readValue(fixture("fixtures/car.json"), Car.class);
		Response response = client.target(
				String.format("http://localhost:%d/cars", rule.getLocalPort()))
                .request()
                .post(Entity.json(testCar));
		assertThat(response.getStatus()).isEqualTo(201);
		return response.getLocation();
	}
}
