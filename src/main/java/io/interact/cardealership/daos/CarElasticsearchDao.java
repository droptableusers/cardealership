package io.interact.cardealership.daos;

import io.dropwizard.jackson.Jackson;
import io.interact.cardealership.model.Car;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import com.fasterxml.jackson.databind.ObjectMapper;

@AllArgsConstructor
public class CarElasticsearchDao {

	private final ElasticsearchDao elasticsearchDao;
	private final String index;
	private final String type;
	private final ObjectMapper mapper = Jackson.newObjectMapper();
	
	@SneakyThrows
	public String create(Car car) {
		return elasticsearchDao.create(index, type, car.getId(), mapFromCar(car));
	}
	
	public Car getById(String id) {
		String json = elasticsearchDao.fetchById(index, type, id);
		return json == null ? null : mapToCar(json);
	}

	public List<Car> search(String q, int from, int pageSize) {
		List<String> searchResults = elasticsearchDao.search(index, type, from, pageSize, q);
		return searchResults
			.stream()
			.map(this::mapToCar)
			.collect(Collectors.toList());
	}
	
	@SneakyThrows
	private Car mapToCar(String json) {
		return mapper.readValue(json, Car.class);
	}

	@SneakyThrows
	private byte[] mapFromCar(Car car) {
		return mapper.writeValueAsBytes(car);
	}

	public boolean deleteById(String id) {
		return elasticsearchDao.delete(index, type, id);
	}

	public void update(Car car) {
		elasticsearchDao.update(index, type, car.getId(), mapFromCar(car));
		
	}
}
