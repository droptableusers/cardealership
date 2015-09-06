package io.interact.cardealership;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import io.interact.cardealership.resources.CarsResource;

public class DealershipApplication extends Application<DealershipConfiguration> {

	public static void main(String[] args) throws Exception {
		new DealershipApplication().run(args);
	}

	@Override
	public void run(DealershipConfiguration configuration, Environment environment) throws Exception {
		final CarsResource carResource = new CarsResource();
		environment.jersey().register(carResource);
	}

}
