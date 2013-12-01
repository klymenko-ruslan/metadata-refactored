package com.turbointernational.metadata.util;

import com.turbointernational.metadata.domain.part.Part;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.ClientConfig;
import io.searchbox.core.Bulk;
import io.searchbox.core.Delete;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import java.util.List;
import net.sf.jsog.JSOG;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jrodriguez
 */
@Service
public class ElasticSearch {

    private String metadataIndex = "metadata";
    
    public  String partType = "part";

    @Value("${elasticsearch.serverUrl}")
    public String searchboxUrl;

    public JestClient client() {
        ClientConfig clientConfig = new ClientConfig.Builder(searchboxUrl).multiThreaded(false).build();
        JestClientFactory factory = new JestClientFactory();
        factory.setClientConfig(clientConfig);
        return factory.getObject();
        
    }

    public String partSearch(String queryString, int from, int size) throws Exception {
        JSOG query = JSOG.object();
        query.put("from", from)
             .put("size", size);

        query.get("query")
                .get("query_string")
                    .put("query", queryString)
                    .get("fields")
                        .add("manufacturer_part_number.autocomplete")
                        .add("manufacturer_part_number.text")

                        .add("manufacturer_name.autocomplete")
                        .add("manufacturer_name.text");

//        // Disjunction
//        JSOG queries = query.get("query")
//             .get("dis_max")
//                 .put("boost", 1.5)
//                 .get("queries");
//
//        // Query String
//        JSOG queryStringQuery = JSOG.object();
//        queries.add(queryStringQuery);
//        queryStringQuery.get("query_string")
//            .put("query", queryString)
//            .put("phrase_slop", 5);
//
//        // Manufacturer Name
//        JSOG manufacturerNameQuery = JSOG.object();
//        queries.add(manufacturerNameQuery);
//        manufacturerNameQuery.get("prefix")
//            .put("manufacturer_name", queryString);
//
//        // Manufacturer Part Number
//        JSOG manufacturerPartNumberQuery = JSOG.object();
//        queries.add(manufacturerPartNumberQuery);
//        manufacturerPartNumberQuery.get("prefix")
//                .put("manufacturer_part_number", queryString);

        return search(new Search.Builder(query.toString())
                                .addIndex(metadataIndex)
                                .addType(partType)
                                .build());
    }

    public String search(Search search) throws Exception {
        String searchResultString = client().execute(search).getJsonString();
        JSOG searchResult = JSOG.parse(searchResultString);

        JSOG result = JSOG.object("total", searchResult.get("hits").get("total"));

        for (JSOG resultItem : searchResult.get("hits").get("hits").arrayIterable()) {
            result.get("items").add(resultItem.get("_source"));
        }

        return result.toString();
    }
    
    @Transactional
    public void indexPart(Part part) throws Exception {
        Index index = new Index
                .Builder(part.toJson())
                .index(metadataIndex)
                .type(partType)
                .id(part.getId().toString())
                .build();
        
        JestResult result = client().execute(index);
        
        if (!result.isSucceeded()) {
            throw new Error(result.getJsonString());
        }
    }

    @Transactional(readOnly = true)
    public int indexParts(int firstResult, int maxResults, String type) throws Exception {
        
        
        Bulk.Builder bulkBuilder = new Bulk.Builder();
        bulkBuilder.defaultIndex(metadataIndex);
        bulkBuilder.defaultType(partType);
        
        List<Part> parts = Part.findPartEntries(firstResult, maxResults, type);
        
        for (Part part : parts) {

            // TODO: ??? partObject.put("_id", partObject.remove("id")); // Rename id to _id
//            Index.Builder indexBuilder = new Index.Builder(JSOG.object("properties", part.toJson()).toString()).id(part.getId().toString());
            Index.Builder indexBuilder = new Index.Builder(part.toJson()).id(part.getId().toString());

            bulkBuilder.addAction(indexBuilder.build());
        }

        JestResult result = client().execute(bulkBuilder.build());

        if (!result.isSucceeded()) {
//            StringBuilder error = new StringBuilder();
//
//            Iterator<BulkItemResponse> it = result.getJsonString();
//            while (it.hasNext()) {
//                BulkItemResponse itemResponse = it.next();
//                error.append(itemResponse.getFailureMessage());
//                error.append("\n");
//            }

            throw new Error(result.getJsonString());
        }
        
        return parts.size();
    }

    @Async
    public void deletePart(Part part) throws Exception {
        Delete delete = new Delete
                .Builder(metadataIndex, partType, part.getId().toString())
                .build();
        client().execute(delete);
    }

}
