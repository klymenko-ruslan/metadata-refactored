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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.sf.jsog.JSOG;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;

/**
 *
 * @author jrodriguez
 */
@Scope(value = "singleton")
public class ElasticSearch {

    private String metadataIndex = "metadata";
    
    public  String partType = "part";

    private final ClientConfig clientConfig;

    private final JestClientFactory factory = new JestClientFactory();

    private final JestClient client;

    public ElasticSearch(String searchboxUrl) {
        clientConfig = new ClientConfig.Builder(searchboxUrl).multiThreaded(true).build();
        factory.setClientConfig(clientConfig);
        client = factory.getObject();
    }

    public String partSearch(String queryString, String partType) throws Exception {
        JSOG query = JSOG.object();
        query.get("query").get("query_string").put("query", queryString);

        return search(new Search.Builder(query.toString())
                                .addIndex(metadataIndex)
                                .addType(StringUtils.defaultIfBlank(partType, this.partType))
                                .build());
    }

    public String search(Search search) throws Exception {
        String searchResultString = client.execute(search).getJsonString();
        JSOG searchResult = JSOG.parse(searchResultString);

        JSOG result = JSOG.object("total", searchResult.get("hits").get("total"));

        for (JSOG resultItem : searchResult.get("hits").get("hits").arrayIterable()) {
            result.get("items").add(resultItem.get("_source"));
        }

        return result.toString();
    }

    public void indexPart(Part part) throws Exception {
        List<Part> parts = new ArrayList<Part>();
        parts.add(part);
        indexParts(parts);
    }

    @Async
    public void indexParts(Collection<Part> parts) throws Exception {
        Bulk.Builder bulkBuilder = new Bulk.Builder();
        bulkBuilder.defaultIndex(metadataIndex);
        bulkBuilder.defaultType(partType);
        for (Part part : parts) {
            if (part == null) {
                continue;
            }

            // Add the part fields
            JSOG partObject = JSOG.object()
                .put("part_type", part.getPartType() != null ? part.getPartType().getTypeName() : "Part")
                .put("name", part.getName())
                .put("description", part.getDescription())
                .put("manufacturer_name", part.getManufacturer().getName())
                .put("manufacturer_type_name", part.getManufacturer().getType().getName())
                .put("manufacturer_part_number", part.getManufacturerPartNumber());

            // Let part subclasses add their fields to the indexed data
            part.addIndexFields(partObject);

            Index.Builder indexBuilder = new Index.Builder(partObject.toString()).id(part.getId().toString());

            bulkBuilder.addAction(indexBuilder.build());
        }

        JestResult result = client.execute(bulkBuilder.build());

        if (!result.isSucceeded()) {
            StringBuilder error = new StringBuilder();

//            Iterator<BulkItemResponse> it = result.getJsonString();
//            while (it.hasNext()) {
//                BulkItemResponse itemResponse = it.next();
//                error.append(itemResponse.getFailureMessage());
//                error.append("\n");
//            }

            throw new Error(result.getJsonString());
        }
    }

    @Async
    public void deleteIndex(Part part) throws Exception {
        client.execute(new Delete.Builder().index(metadataIndex).type(partType).id(part.getId().toString()).build());
    }

}
