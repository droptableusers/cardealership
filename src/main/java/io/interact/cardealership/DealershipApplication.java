package io.interact.cardealership;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class DealershipApplication extends Application<DealershipConfiguration> {

	public static void main(String[] args) throws Exception {
		new DealershipApplication().run(args);
	}
	
	@Override
	public void run(DealershipConfiguration configuration, Environment environment) throws Exception {
		
	}

}
