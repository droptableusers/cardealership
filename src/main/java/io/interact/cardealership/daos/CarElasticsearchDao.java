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
		return elasticsearchDao.create(index, type, null, mapper.writeValueAsBytes(car));
	}
	
	public Car getById(String id) {
		return mapToCar(elasticsearchDao.fetchById(index, type, id));
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
}
