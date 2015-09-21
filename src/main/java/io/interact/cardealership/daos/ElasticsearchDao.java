package io.interact.cardealership.daos;

import io.interact.cardealership.model.SearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;

@AllArgsConstructor
@Builder
@Slf4j
public class ElasticsearchDao {

	private final Client esClient;

	public String create(String indexName, String type, String id, byte[] jsonData) {
		try {
			IndexRequest request = esClient
					.prepareIndex(indexName, type, id)
					.setSource(jsonData)
					.request();
			return esClient.index(request).get().getId();
		} catch (ExecutionException | InterruptedException e) {
			log.error("", e);
		}
		return null;
	}

	public SearchResult<String> search(String indexName, String type, int from, int pageSize, QueryBuilder queryBuilder) {
		SearchResponse response = esClient.prepareSearch()
				.setIndices(indexName)
				.setTypes(type)
				.setQuery(queryBuilder)
				.setFrom(from)
				.setSize(pageSize)
				.addField("_source")
				.get();
		List<String> result = new ArrayList<>(pageSize);
		for (SearchHit hit : response.getHits()) {
			result.add(hit.getSourceAsString());
		}
		long totalHits = response.getHits().getTotalHits();
		return new SearchResult<String>(totalHits, result);
	}

	public String fetchById(String indexName, String type, String id) {
		GetResponse response = esClient.prepareGet(indexName, type, id).execute().actionGet();
		return response.getSourceAsString();
	}

	public boolean delete(String indexName, String type, String id) {
		DeleteResponse response = esClient.prepareDelete(indexName, type, id).execute().actionGet();
		return response.isFound();
	}

	public void update(String index, String type, String id, byte[] json) {
		try {
			esClient.prepareUpdate(index, type, id).setDoc(json).get();
		} catch (Exception e) {
			log.error("error while updating " + id, e);
		}
	}

}
