package io.interact.cardealership;

import static org.eclipse.jetty.servlets.CrossOriginFilter.*;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.basic.BasicAuthFactory;
import io.dropwizard.elasticsearch.health.EsClusterHealthCheck;
import io.dropwizard.elasticsearch.managed.ManagedEsClient;
import io.dropwizard.setup.Environment;
import io.interact.cardealership.daos.CarElasticsearchDao;
import io.interact.cardealership.daos.ElasticsearchDao;
import io.interact.cardealership.resources.CarsResource;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;

public class DealershipApplication extends Application<DealershipConfiguration> {

	public static void main(String[] args) throws Exception {
		new DealershipApplication().run(args);
	}

	@Override
	public void run(DealershipConfiguration configuration, Environment environment) throws Exception {
		enableCors(environment);
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
		environment.jersey().register(AuthFactory.binder(
				new BasicAuthFactory<String>(new SimpleAuthenticator(), "SECRET STUFF", String.class)));
	}

	private void enableCors(Environment environment) {
		FilterRegistration.Dynamic corsFilter = environment.servlets().addFilter("CorsFilter", CrossOriginFilter.class);
		corsFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, environment.getApplicationContext().getContextPath() + "*");
		corsFilter.setInitParameter(ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS,HEAD");
		corsFilter.setInitParameter(ALLOWED_ORIGINS_PARAM, "*");
		corsFilter.setInitParameter(ALLOWED_HEADERS_PARAM, "*");
		corsFilter.setInitParameter(ALLOW_CREDENTIALS_PARAM, "true");
	}

}
