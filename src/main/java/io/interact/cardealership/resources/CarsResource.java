package io.interact.cardealership.resources;

import io.dropwizard.auth.Auth;
import io.interact.cardealership.daos.CarElasticsearchDao;
import io.interact.cardealership.model.Car;
import io.interact.cardealership.model.SearchResult;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;

@Path("/cars")
@Produces(MediaType.APPLICATION_JSON)
@AllArgsConstructor
@Builder
@Slf4j
public class CarsResource {

	private final CarElasticsearchDao dao;

	@POST
	public Response createCar(@Auth String user, Car car) throws URISyntaxException {
		car.setId(createId(car));
		String id = dao.create(car);
		return Response.created(
				UriBuilder.fromResource(CarsResource.class)
						.path(CarsResource.class, "fetchCarById")
						.build(id)).build();
	}

	@POST
	@Path("/batch")
	public Response createCars(@Auth String user, List<Car> cars) {
		log.info("createCars called with {} cars", cars.size());
		for (Car car : cars) {
			car.setId(createId(car));
			dao.create(car);
		}
		return Response.ok().build();
	}
	
	@POST
	@Path("/batch")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadJson(@Auth String user,
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {
		ObjectMapper mapper = new ObjectMapper();
		List<Car> cars = Collections.emptyList();
		try {
			cars = mapper.readValue(uploadedInputStream, new TypeReference<List<Car>>() {});
		} catch (IOException e) {
			log.error("Error while reading uploaded stream.", e);
			e.printStackTrace();
		}
		return createCars(user, cars);
	}

	@GET
	@Path("/{id}")
	public Response fetchCarById(@Auth String user, @PathParam("id") @NotNull String id) {
		Car car = dao.getById(id);
		if (car == null) {
			return Response.status(Status.NOT_FOUND).build();
		} else {
			return Response.ok(car).build();
		}
	}

	@GET
	public SearchResult<Car> search(
			@Auth String user,
			@QueryParam("q") Optional<String> q,
			@DefaultValue("0") @QueryParam("from") int from,
			@DefaultValue("10") @QueryParam("pageSize") int pageSize) {
		String query = q.isPresent() ? buildQuery(q.get()) : null; 
		return dao.search(query, from, pageSize);
	}

	private String buildQuery(String query) {
		return query;
	}

	@DELETE
	@Path("/{id}")
	public Response deleteById(@Auth String user, @PathParam("id") @NotNull String id) {
		boolean success = dao.deleteById(id);
		if (success) {
			return Response.ok().build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@PUT
	@Path("/{id}")
	public Response editById(@Auth String user, @PathParam("id") @NotNull String id, Car car) {
		car.setId(id);
		dao.update(car);
		return Response.ok().build();
	}

	private String createId(Car car) {
		return UUID.randomUUID().toString();
	}

}
