package io.interact.cardealership.resources;

import io.interact.cardealership.model.Car;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/cars")
@Produces(MediaType.APPLICATION_JSON)
public class CarsResource {

	Map<Integer, Car> cars = new HashMap<>();

	@POST
	public Response createCar(Car car) throws URISyntaxException {
		int hash = car.hashCode();
		cars.put(hash, car);
		return Response.created(new URI("/cars/" + hash)).build();
	}

	@GET
	@Path("/{id}")
	public Car fetchCarById(@PathParam("id") String id) {
		return cars.get(Integer.valueOf(id));
	}

}
