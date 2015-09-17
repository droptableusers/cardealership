package io.interact.cardealership;

import io.dropwizard.Configuration;
import io.dropwizard.elasticsearch.config.EsConfiguration;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DealershipConfiguration extends Configuration {
	private EsConfiguration elasticsearch;
	private String index;
	private String type;
}
