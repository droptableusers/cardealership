package io.interact.cardealership;

import io.dropwizard.Application;
import io.dropwizard.elasticsearch.health.EsClusterHealthCheck;
import io.dropwizard.elasticsearch.managed.ManagedEsClient;
import io.dropwizard.setup.Environment;
import io.interact.cardealership.daos.CarElasticsearchDao;
import io.interact.cardealership.daos.ElasticsearchDao;
import io.interact.cardealership.resources.CarsResource;

public class DealershipApplication extends Application<DealershipConfiguration> {

	public static void main(String[] args) throws Exception {
		new DealershipApplication().run(args);
	}

	@Override
	public void run(DealershipConfiguration configuration, Environment environment) throws Exception {
		final ManagedEsClient managedClient = new ManagedEsClient(configuration.getElasticsearch());
		environment.lifecycle().manage(managedClient);
		environment.healthChecks().register("Es cluster health", new EsClusterHealthCheck(managedClient.getClient()));
		final ElasticsearchDao elasticsearchDao = new ElasticsearchDao(managedClient.getClient());
		final CarElasticsearchDao carEsDao = new CarElasticsearchDao(
				elasticsearchDao, 
				configuration.getIndex(),
				configuration.getType());
		final CarsResource carResource = CarsResource.builder().dao(carEsDao).build();
		environment.jersey().register(carResource);
	}

}
