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
        query.get("query").get("match").put("_all", queryString);
        query.put("fields", "_all");

        return search(new Search.Builder(query.toString())
                                .addIndex(metadataIndex)
                                .addType(StringUtils.defaultIfBlank(partType, this.partType))
                                .build());
    }

    public String search(Search search) throws Exception {
        String result = client.execute(search).getJsonString();

        System.out.println(result);
        return result;
    }

    public void indexPart(Part part) throws Exception {
        List<Part> parts = new ArrayList<Part>();
        parts.add(part);
        indexParts(parts);
    }

    @Async
    public void indexParts(Collection<Part> parts) throws Exception {
        Bulk.Builder bulkBuilder = new Bulk.Builder();
        for (Part part : parts) {
            if (part == null) {
                continue;
            }

            JSOG partObject = JSOG.object()
                .put("id", part.getId())
                .put("part_type", part.getClass().getName())
                .put("name", part.getName())
                .put("description", part.getDescription())
                .put("manufacturer_name", part.getManufacturer().getName())
                .put("manufacturer_type_name", part.getManufacturer().getType())
                .put("manufacturer_part_number", part.getManufacturerPartNumber());

            Index.Builder indexBuilder = new Index.Builder(part.toString()).index(metadataIndex).type(part.getClass().getName());

            part.addIndexFields(partObject);

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
