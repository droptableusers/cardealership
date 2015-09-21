package io.interact.cardealership.daos;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import io.dropwizard.jackson.Jackson;
import io.interact.cardealership.model.Car;
import io.interact.cardealership.model.SearchResult;

import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;

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

	public SearchResult<Car> search(String q, int from, int pageSize) {
		QueryBuilder queryBuilder = q == null || q.isEmpty() ? matchAllQuery() : buildQuery(q);
		SearchResult<String> searchResults = elasticsearchDao.search(
				index, type, from, pageSize, queryBuilder);
		List<Car> carResults = searchResults.getHits()
			.stream()
			.map(this::mapToCar)
			.collect(Collectors.toList());
		return new SearchResult<Car>(searchResults.getTotalHits(), carResults);
	}
	
	private QueryStringQueryBuilder buildQuery(String query) {
		QueryStringQueryBuilder builder =  queryStringQuery(query);
		builder.field("brand", 3);
		builder.field("model", 2);
		builder.field("_all", 1);
		return builder;
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
