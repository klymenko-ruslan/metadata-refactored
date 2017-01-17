package com.turbointernational.metadata.service.search.index;

import com.turbointernational.metadata.entity.CriticalDimension;
import com.turbointernational.metadata.service.CriticalDimensionService;
import com.turbointernational.metadata.service.ResourceService;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.common.settings.loader.JsonSettingsLoader;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by dmytro.trunykov@zorallabs.com on 08.08.16.
 */
public class IndexBuilder {

    public static void build(CriticalDimensionService criticalDimensionService, ResourceService resourceService,
                             CreateIndexRequestBuilder indexRequestBuilder,
                             int numberOfShards, int numberOfReplicas, int maxResultWindow) throws IOException {
        for (String indexType : new String[]{"carengine", "carfueltype", "caryear", "carmake",
                "carmodel", "carmodelengineyear", "salesnotepart", "changelogsource"}) {
            String resourceName = "elasticsearch/" + indexType + ".json";
            String typeDef = resourceService.loadFromMeta(resourceName);
            indexRequestBuilder.addMapping(indexType, typeDef);
        }
        // Add Part fields to the index.
        String partDef = resourceService.loadFromMeta("elasticsearch/part.json");
        // Inject critical dimensions to the index definition of a Part type.
        String critDimsDef = critDimsIndexDef(criticalDimensionService);
        int n = partDef.lastIndexOf('}');
        n = partDef.lastIndexOf('}', n - 1);
        partDef = partDef.substring(0, n) + "," + critDimsDef + partDef.substring(n);
        indexRequestBuilder.addMapping("part", partDef);
        String settingsDefinition = resourceService.loadFromMeta("elasticsearch/settings.json");

        Map<String, String> settings = (new JsonSettingsLoader(true)).load(settingsDefinition);
        settings.put("index.number_of_shards", Integer.toString(numberOfShards));
        settings.put("index.number_of_replicas", Integer.toString(numberOfReplicas));
        settings.put("index.max_result_window", Integer.toString(maxResultWindow));
        indexRequestBuilder.setSettings(settings);
    }

    private static String critDimsIndexDef(CriticalDimensionService criticalDimensionService) throws IOException {
        XContentBuilder xcb = XContentFactory.jsonBuilder();
        Map<Long, List<CriticalDimension>> crtclDmnsns = criticalDimensionService.getCriticalDimensionsCacheById();
        xcb.startObject();
        try {
            crtclDmnsns.forEach((partTypeId, ptCrtclDmnsns) -> ptCrtclDmnsns.forEach(cd -> {
                try {
                    String idxName = cd.getIdxName();
                    CriticalDimension.DataTypeEnum dataType = cd.getDataType();
                    switch (dataType) {
                        case DECIMAL:
                            critDimsIndexDefPlainType(xcb, idxName, "double");
                            break;
                        case INTEGER:
                            critDimsIndexDefPlainType(xcb, idxName, "long");
                            break;
                        case TEXT:
                            critDimsIndexDefPlainType(xcb, idxName, "text");
                            break;
                        case ENUMERATION:
                            critDimsIndexDefEnumType(xcb, idxName);
                            break;
                        default:
                            throw new AssertionError("Unknown data type: " + dataType);
                    }

                } catch (IOException e) {
                    throw new AssertionError("Declaring of critical dimensions in the index failed: " + e.getMessage());
                }
            }));
        } catch (AssertionError e) {
            throw new IOException(e);
        }
        xcb.endObject();
        String s = xcb.string();
        int f = s.indexOf('{');
        int l = s.lastIndexOf('}');
        // Get part of the string without leading and trailing curve brackets.
        String retVal = s.substring(f + 1, l);
        return retVal;
    }

    private static void critDimsIndexDefPlainType(XContentBuilder xcb, String idxName, String idxType) throws IOException {
        xcb.startObject(idxName)
                .field("type", idxType)
                .field("store", "yes")
                .endObject();
    }

    private static void critDimsIndexDefEnumType(XContentBuilder xcb, String idxName) throws IOException {
        // Enumeration has a special index structure in oder
        // to store an enumeration item ID and its textual
        // representation. We need in the textual representation
        // in order to have a possibility to show enumeration value
        // in a WEB UI (e.g. in tables).
        xcb.startObject(idxName)
                .field("type", "long")
                .field("store", "yes")
                .endObject();
        // Index to store enumeration item LABEL.
        // Caveat. The suffix "Label" below is hardcoded in the
        // Java (see method CriticalDimensionService.JsonIdxNameTransformer#transform(Object)),
        // in the JavaSript code (see PartSearch.js)
        // and in the HTML (see PartSearch.html).
        // So if you changes this suffix you also must reflect the
        // rename in those files too.
        String idxNameLabel = idxName + "Label";
        xcb.startObject(idxNameLabel)
                //.field("type", "multi_field")
                .field("type", "text")
                .startObject("fields")
                .startObject("text")
                .field("type", "text")
                .field("fielddata", true)
                .field("analyzer", "keyword")
                .field("store", "yes")
                .endObject()
                .startObject("lower_case_sort")
                .field("type", "text")
                .field("fielddata", true)
                .field("analyzer", "case_insensitive_sort")
                .field("store", "yes")
                .endObject()
                .endObject()
                .endObject();
    }

}
