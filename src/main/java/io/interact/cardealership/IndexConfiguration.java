package io.interact.cardealership;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexConfiguration {
	private String index;
	private String type;
	private int numberOfShards;
	private int numberOfReplicas;
}
