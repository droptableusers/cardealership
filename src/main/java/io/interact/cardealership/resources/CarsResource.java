package io.interact.cardealership.resources;

import io.interact.cardealership.daos.CarElasticsearchDao;
import io.interact.cardealership.model.Car;

import java.net.URISyntaxException;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.google.common.base.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Path("/cars")
@Produces(MediaType.APPLICATION_JSON)
@AllArgsConstructor
@Builder
public class CarsResource {

	private final CarElasticsearchDao dao;

	@POST
	public Response createCar(Car car) throws URISyntaxException {
		String id = dao.create(car);
		return Response.created(
				UriBuilder.fromResource(CarsResource.class)
				.path(CarsResource.class, "fetchCarById")
				.build(id)).build();
	}
	
	@POST
	@Path("/batch")
	public Response createCars(List<Car> cars) {
		for (Car car: cars) {
			dao.create(car);
		}
		return Response.ok().build();
	}

	@GET
	@Path("/{id}")
	public Car fetchCarById(@PathParam("id") @NotNull String id) {
		return dao.getById(id);
	}
	
	@GET
	@Path("/search")
	public List<Car> search(
			@NotNull @QueryParam("q") Optional<String> q, 
			@DefaultValue("0") @QueryParam("from") int from, 
			@DefaultValue("10") @QueryParam("pageSize") int pageSize) {
		return dao.search(buildQuery(q.get()), from, pageSize);
	}

	private String buildQuery(String query) {
		return query;
	}

}
