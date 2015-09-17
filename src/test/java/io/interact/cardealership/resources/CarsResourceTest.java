package io.interact.cardealership.resources;


import io.interact.cardealership.daos.MockCarEsDao;
import io.interact.cardealership.model.Car;
import io.interact.cardealership.util.ModelFactory;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;

public class CarsResourceTest {
	
	private CarsResource testObj;
	private MockCarEsDao dao;

	@Before
	public void setUp() throws Exception {
		dao = MockCarEsDao.builder().build();
		testObj = CarsResource.builder().dao(dao).build();
	}
	
	@Test
	public void itSavesDataToElasticsearch() throws Exception {
		Car car = ModelFactory.createCar();
//		String id = testObj.createCar(car).getEntity();
	}
	
	@Test
	public void itSearches() {
		
//		List<Car> cars = testObj.search(Optional.of("audi"), 0, 10);
		
	}

}
