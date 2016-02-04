package com.turbointernational.metadata.services;

import com.turbointernational.metadata.Application;
import com.turbointernational.metadata.domain.AbstractDao;
import com.turbointernational.metadata.domain.SearchableEntity;
import com.turbointernational.metadata.domain.car.*;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.PartDao;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Created by dmytro.trunykov@zorallabs.com on 1/31/16.
 */
@Service
public class SearchService {

    private final static Logger log = LoggerFactory.getLogger(SearchService.class);

    private final static Pattern REGEX_TOSHORTFIELD = Pattern.compile("\\W");

    @Value("${elasticsearch.index}")
    protected String elasticSearchIndex = "metadata";

    @Value("${elasticsearch.host}")
    private String elasticSearchHost;

    @Value("${elasticsearch.port}")
    private int elasticSearchPort = 9300;

    @Value("${elasticsearch.timeout}")
    protected int timeout = 10000;

    @Value("${elasticsearch.cluster.name}")
    private String clusterName = "elasticsearch";

    @Value("${elasticsearch.type.part}")
    private String elasticSearchTypePart = "part";

    @Autowired
    private PartDao partDao;

    @Value("${elasticsearch.type.carmodelengineyear}")
    private String elasticSearchTypeCarModelEngineYear = "carmodelengineyear";

    @Autowired
    private CarModelEngineYearDao carModelEngineYearDao;

    @Value("${elasticsearch.type.carengine}")
    private String elasticSearchTypeCarEngine = "carengine";

    @Autowired
    private CarEngineDao carEngineDao;

    @Value("${elasticsearch.type.carfueltype}")
    private String elasticSearchTypeCarFuelType = "carfueltype";

    @Autowired
    private CarFuelTypeDao carFuelTypeDao;

    @Value("${elasticsearch.type.carmake}")
    private String elasticSearchTypeCarMake = "carmake";

    @Autowired
    private CarMakeDao carMakeDao;

    @Value("${elasticsearch.type.carmodel}")
    private String elasticSearchTypeCarModel = "carmodel";

    @Autowired
    private CarModelDao carModelDao;

    private Client elasticSearch; // connection with ElasticSearch

    private final static int DEF_AGGR_RESULT_SIZE = 100;

    private final static AggregationBuilder[] PART_AGGREGATIONS = new AggregationBuilder[]{
            AggregationBuilders.terms("Part Type").field("partType.name.full").size(DEF_AGGR_RESULT_SIZE),
            AggregationBuilders.terms("Manufacturer").field("manufacturer.name.full").size(DEF_AGGR_RESULT_SIZE),
            AggregationBuilders.terms("Kit Type").field("kitType.name.full").size(DEF_AGGR_RESULT_SIZE),
            AggregationBuilders.terms("Gasket Type").field("gasketType.name.full").size(DEF_AGGR_RESULT_SIZE),
            AggregationBuilders.terms("Seal Type").field("sealType.name.full").size(DEF_AGGR_RESULT_SIZE),
            AggregationBuilders.terms("Coolant Type").field("coolType.name.full").size(DEF_AGGR_RESULT_SIZE),
            AggregationBuilders.terms("Turbo Type").field("turboModel.turboType.name.full").size(DEF_AGGR_RESULT_SIZE),
            AggregationBuilders.terms("Turbo Model").field("turboModel.name.full").size(DEF_AGGR_RESULT_SIZE)
    };

    private final static AggregationBuilder[] CMEY_AGGREGATIONS = new AggregationBuilder[]{
            AggregationBuilders.terms("Year").field("year.name.full").size(DEF_AGGR_RESULT_SIZE),
            AggregationBuilders.terms("Make").field("model.make.name.full").size(DEF_AGGR_RESULT_SIZE),
            AggregationBuilders.terms("Model").field("model.name.full").size(DEF_AGGR_RESULT_SIZE),
            AggregationBuilders.terms("Engine").field("engine.engineSize").size(DEF_AGGR_RESULT_SIZE),
            AggregationBuilders.terms("Fuel Type").field("engine.fuelType.name.full").size(DEF_AGGR_RESULT_SIZE)
    };

    private final static AggregationBuilder[] CARMODEL_AGGREGATIONS = new AggregationBuilder[]{
            AggregationBuilders.terms("Make").field("make.name.full").size(DEF_AGGR_RESULT_SIZE)
    };

    @PostConstruct
    private void init() throws UnknownHostException {
        // Establish connection to ElasticSearch cluster.
        InetAddress inetAddrElasticSearchHost = InetAddress.getByName(elasticSearchHost);
        TransportAddress taddr = new InetSocketTransportAddress(inetAddrElasticSearchHost, elasticSearchPort);
        Settings settings = Settings.settingsBuilder().put("cluster.name", clusterName).build();
        this.elasticSearch = TransportClient.builder().settings(settings).build().addTransportAddress(taddr);
    }

    /**
     * Transform a string to a string for search in the "name.short" field.
     * <p/>
     * Some types in the ElasticSearch index has property "name" that is mapped on two versions:
     * <dd>
     * <dt>full</dt>
     * <dd>the field indexed as is</dd>
     * <dt>short</dt>
     * <dd>the field transformed to a lower-case string and removed all non-word characters</dd>
     * </dd>
     *
     * @param s string to transform. null not allowed.
     * @return transformed string
     */
    private final static Function<String, String> str2shotfield = s -> REGEX_TOSHORTFIELD.matcher(s).replaceAll("").toLowerCase();

    /**
     * Convert string to SortOrder.
     */
    private final static Function<String, SortOrder> convertSortOrder = sortOrder -> SortOrder.valueOf(sortOrder.toUpperCase());

    private final static Function<String, String> queryString = s -> "*" + s + "*";

    public static SearchService instance() {
        return Application.getContext().getBean(SearchService.class);
    }

    @Transactional(readOnly = true)
    public void indexPart(long id) {
        Part part = partDao.findOne(id);
        indexPart(part);
    }

    @Transactional(readOnly = true)
    public void indexPart(Part part) {
        indexDoc(part, elasticSearchTypePart);
    }

    @Transactional(readOnly = true)
    public void deletePart(Part part) throws Exception {
        deleteDoc(elasticSearchTypePart, part.getSearchId());
    }

    @Transactional(readOnly = true)
    public void indexAllParts() throws Exception {
        indexAllDocs(partDao, elasticSearchTypePart);
    }

    @Transactional(readOnly = true)
    public void indexCarEngine(CarEngine carEngine) {
        indexDoc(carEngine, elasticSearchTypeCarEngine);
    }

    @Transactional(readOnly = true)
    public void deleteCarEngine(CarEngine carEngine) throws Exception {
        deleteDoc(elasticSearchTypeCarEngine, carEngine.getSearchId());
    }

    @Transactional(readOnly = true)
    public void indexCarFuelType(CarFuelType carFuelType) {
        indexDoc(carFuelType, elasticSearchTypeCarFuelType);
    }

    @Transactional(readOnly = true)
    public void deleteCarFuelType(CarFuelType carFuelType) throws Exception {
        deleteDoc(elasticSearchTypeCarFuelType, carFuelType.getSearchId());
    }

    @Transactional(readOnly = true)
    public void indexCarMake(CarMake carMake) {
        indexDoc(carMake, elasticSearchTypeCarMake);
    }

    @Transactional(readOnly = true)
    public void deleteCarMake(CarMake carMake) throws Exception {
        deleteDoc(elasticSearchTypeCarMake, carMake.getSearchId());
    }

    @Transactional(readOnly = true)
    public void indexCarModel(CarModel carModel) {
        indexDoc(carModel, elasticSearchTypeCarModel);
    }

    @Transactional(readOnly = true)
    public void deleteCarModel(CarModel carModel) throws Exception {
        deleteDoc(elasticSearchTypeCarModel, carModel.getSearchId());
    }

    @Transactional(readOnly = true)
    public void indexCarModelEngineYear(CarModelEngineYear carModelEngineYear) {
        indexDoc(carModelEngineYear, elasticSearchTypeCarModelEngineYear);
    }

    @Transactional(readOnly = true)
    public void deleteCarModelEngineYear(CarModelEngineYear carModelEngineYear) throws Exception {
        deleteDoc(elasticSearchTypeCarModel, carModelEngineYear.getSearchId());
    }

    @Transactional(readOnly = true)
    public void indexAllApplications() throws Exception {
        indexAllDocs(carModelEngineYearDao, elasticSearchTypeCarModelEngineYear);
        indexAllDocs(carEngineDao, elasticSearchTypeCarEngine);
        indexAllDocs(carFuelTypeDao, elasticSearchTypeCarFuelType);
        indexAllDocs(carMakeDao, elasticSearchTypeCarMake);
        indexAllDocs(carModelDao, elasticSearchTypeCarModel);
    }

    public String filterParts(String partNumber, String partTypeName, String manufacturerName, String kitType,
                              String gasketType, String sealType, String coolType, String turboType,
                              String turboModel, String sortProperty, String sortOrder,
                              Integer offset, Integer limit) {
        partNumber = StringUtils.defaultIfEmpty(partNumber, null);
        SearchRequestBuilder srb = elasticSearch.prepareSearch(elasticSearchIndex).setTypes(elasticSearchTypePart)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        QueryBuilder query;
        if (partNumber == null && partTypeName == null && manufacturerName == null && kitType == null
                && gasketType == null && sealType == null && coolType == null && turboType == null
                && turboModel == null) {
            query = QueryBuilders.matchAllQuery();
        } else {
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            if (partNumber != null) {
                String normalizedPartNumber = str2shotfield.apply(partNumber);
                boolQuery.must(QueryBuilders.termQuery("manufacturerPartNumber.short", normalizedPartNumber));
            }
            if (partTypeName != null) {
                boolQuery.must(QueryBuilders.termQuery("partType.name.full", partTypeName));
            }
            if (manufacturerName != null) {
                boolQuery.must(QueryBuilders.termQuery("manufacturer.name.full", manufacturerName));
            }
            if (kitType != null) {
                boolQuery.must(QueryBuilders.termQuery("kitType.name.full", kitType));
            }
            if (gasketType != null) {
                boolQuery.must(QueryBuilders.termQuery("gasketType.name.full", gasketType));
            }
            if (sealType != null) {
                boolQuery.must(QueryBuilders.termQuery("sealType.name.full", sealType));
            }
            if (coolType != null) {
                boolQuery.must(QueryBuilders.termQuery("coolType.name.full", coolType));
            }
            if (turboType != null) {
                boolQuery.must(QueryBuilders.termQuery("turboModel.turboType.name.full", turboModel));
            }
            if (turboModel != null) {
                boolQuery.must(QueryBuilders.termQuery("turboModel.name.full", turboModel));
            }
            query = boolQuery;
        }
        srb.setQuery(query);
        if (sortProperty != null) {
            SortBuilder sort = SortBuilders.fieldSort(sortProperty)
                    .order(convertSortOrder.apply(sortOrder))
                    .missing("_last");
            srb.addSort(sort);
        }
        for (AggregationBuilder agg : PART_AGGREGATIONS) {
            srb.addAggregation(agg);
        }
        if (offset != null) {
            srb.setFrom(offset);
        }
        if (limit != null) {
            srb.setSize(limit);
        }
        log.debug("Search request (parts) to search engine:\n{}", srb);
        return srb.execute().actionGet(timeout).toString();
    }

    public String filterCarModelEngineYears(String carModelEngineYear, String year, String make, String model,
                                            String engine, String fuel, String sortProperty, String sortOrder,
                                            Integer offset, Integer limit) {
        carModelEngineYear = StringUtils.defaultIfEmpty(carModelEngineYear, null);
        SearchRequestBuilder srb = elasticSearch.prepareSearch(elasticSearchIndex)
                .setTypes(elasticSearchTypeCarModelEngineYear)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        QueryBuilder query;
        if (carModelEngineYear == null && year == null && make == null && model == null
                && engine == null && fuel == null) {
            query = QueryBuilders.matchAllQuery();
        } else {
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            if (carModelEngineYear != null) {
                String normalizedCarModelEngineYear = str2shotfield.apply(carModelEngineYear);
                boolQuery.must(QueryBuilders.queryStringQuery(queryString.apply(normalizedCarModelEngineYear)));
            }
            if (year != null) {
                boolQuery.must(QueryBuilders.termQuery("year.name.full", year));
            }
            if (make != null) {
                boolQuery.must(QueryBuilders.termQuery("model.make.name.full", make));
            }
            if (model != null) {
                boolQuery.must(QueryBuilders.termQuery("model.name.full", model));
            }
            if (engine != null) {
                boolQuery.must(QueryBuilders.termQuery("engine.engineSize", engine));
            }
            if (fuel != null) {
                boolQuery.must(QueryBuilders.termQuery("engine.fuelType.name.full", fuel));
            }
            query = boolQuery;
        }
        srb.setQuery(query);
        if (sortProperty != null) {
            SortBuilder sort = SortBuilders.fieldSort(sortProperty)
                    .order(convertSortOrder.apply(sortOrder))
                    .missing("_last");
            srb.addSort(sort);
        }
        for (AggregationBuilder agg : CMEY_AGGREGATIONS) {
            srb.addAggregation(agg);
        }
        if (offset != null) {
            srb.setFrom(offset);
        }
        if (limit != null) {
            srb.setSize(limit);
        }
        log.debug("Search request (car model engine years) to search engine:\n{}", srb);
        return srb.execute().actionGet(timeout).toString();
    }

    public String filterCarMakes(String carMake , String sortProperty, String sortOrder,
                                 Integer offset, Integer limit) {
        carMake = StringUtils.defaultIfEmpty(carMake, null);
        SearchRequestBuilder srb = elasticSearch.prepareSearch(elasticSearchIndex)
                .setTypes(elasticSearchTypeCarMake)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        QueryBuilder query;
        if (carMake == null) {
            query = QueryBuilders.matchAllQuery();
        } else {
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            if (carMake != null) {
                String normalizedCarMake = str2shotfield.apply(carMake);
                boolQuery.must(QueryBuilders.termQuery("name.short", normalizedCarMake));
            }
            query = boolQuery;
        }
        srb.setQuery(query);
        if (sortProperty != null) {
            SortBuilder sort = SortBuilders.fieldSort(sortProperty)
                    .order(convertSortOrder.apply(sortOrder))
                    .missing("_last");
            srb.addSort(sort);
        }
        if (offset != null) {
            srb.setFrom(offset);
        }
        if (limit != null) {
            srb.setSize(limit);
        }
        log.debug("Search request (car makes) to search engine:\n{}", srb);
        return srb.execute().actionGet(timeout).toString();
    }

    public String filterCarModels(String carModel, String make, String sortProperty, String sortOrder,
                                 Integer offset, Integer limit) {
        carModel = StringUtils.defaultIfEmpty(carModel, null);
        SearchRequestBuilder srb = elasticSearch.prepareSearch(elasticSearchIndex)
                .setTypes(elasticSearchTypeCarModel)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        QueryBuilder query;
        if (carModel == null && make == null) {
            query = QueryBuilders.matchAllQuery();
        } else {
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            if (carModel != null) {
                String normalizedCarModel = str2shotfield.apply(carModel);
                boolQuery.must(QueryBuilders.termQuery("name.short", normalizedCarModel));
            }
            if (make != null) {
                boolQuery.must(QueryBuilders.termQuery("make.name.full", make));
            }
            query = boolQuery;
        }
        srb.setQuery(query);
        if (sortProperty != null) {
            SortBuilder sort = SortBuilders.fieldSort(sortProperty)
                    .order(convertSortOrder.apply(sortOrder))
                    .missing("_last");
            srb.addSort(sort);
        }
        for (AggregationBuilder agg : CARMODEL_AGGREGATIONS) {
            srb.addAggregation(agg);
        }
        if (offset != null) {
            srb.setFrom(offset);
        }
        if (limit != null) {
            srb.setSize(limit);
        }
        log.debug("Search request (car models) to search engine:\n{}", srb);
        return srb.execute().actionGet(timeout).toString();
    }

    public String filterCarEngines(String carEngine, String fuelType, String sortProperty, String sortOrder,
                                 Integer offset, Integer limit) {
        carEngine = StringUtils.defaultIfEmpty(carEngine, null);
        SearchRequestBuilder srb = elasticSearch.prepareSearch(elasticSearchIndex)
                .setTypes(elasticSearchTypeCarEngine)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        QueryBuilder query;
        if (carEngine == null && fuelType == null) {
            query = QueryBuilders.matchAllQuery();
        } else {
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            if (carEngine != null) {
                String normalizedCarEngine = str2shotfield.apply(carEngine);
                boolQuery.must(QueryBuilders.termQuery("name.short", normalizedCarEngine));
            }
            if (fuelType != null) {
                boolQuery.must(QueryBuilders.termQuery("fuelType.name.full", fuelType));
            }
            query = boolQuery;
        }
        srb.setQuery(query);
        if (sortProperty != null) {
            SortBuilder sort = SortBuilders.fieldSort(sortProperty)
                    .order(convertSortOrder.apply(sortOrder))
                    .missing("_last");
            srb.addSort(sort);
        }
        for (AggregationBuilder agg : CARMODEL_AGGREGATIONS) {
            srb.addAggregation(agg);
        }
        if (offset != null) {
            srb.setFrom(offset);
        }
        if (limit != null) {
            srb.setSize(limit);
        }
        log.debug("Search request (car engines) to search engine:\n{}", srb);
        return srb.execute().actionGet(timeout).toString();
    }

    public String filterCarFuelTypes(String fuelType , String sortProperty, String sortOrder,
                                 Integer offset, Integer limit) {
        fuelType = StringUtils.defaultIfEmpty(fuelType, null);
        SearchRequestBuilder srb = elasticSearch.prepareSearch(elasticSearchIndex)
                .setTypes(elasticSearchTypeCarFuelType)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        QueryBuilder query;
        if (fuelType == null) {
            query = QueryBuilders.matchAllQuery();
        } else {
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            if (fuelType != null) {
                String normalizedFuelType = str2shotfield.apply(fuelType);
                boolQuery.must(QueryBuilders.termQuery("name.short", normalizedFuelType));
            }
            query = boolQuery;
        }
        srb.setQuery(query);
        if (sortProperty != null) {
            SortBuilder sort = SortBuilders.fieldSort(sortProperty)
                    .order(convertSortOrder.apply(sortOrder))
                    .missing("_last");
            srb.addSort(sort);
        }
        if (offset != null) {
            srb.setFrom(offset);
        }
        if (limit != null) {
            srb.setSize(limit);
        }
        log.debug("Search request (car makes) to search engine:\n{}", srb);
        return srb.execute().actionGet(timeout).toString();
    }

    private void deleteDoc(String elasticSearchType, String searchId) throws Exception {
        DeleteRequest delete = new DeleteRequest(elasticSearchIndex, elasticSearchType, searchId);
        elasticSearch.delete(delete).actionGet(timeout);
    }

    private void indexDoc(SearchableEntity doc, String elasticSearchType) {
        String searchId = doc.getSearchId();
        String json = doc.toSearchJson();
        IndexRequest index = new IndexRequest(elasticSearchIndex, elasticSearchType, searchId);
        index.source(json);
        elasticSearch.index(index).actionGet(timeout);
    }

    private void indexAllDocs(AbstractDao<?> dao, String elasticSearchType) throws Exception {
        int maxPages = Integer.MAX_VALUE;
        int page = 0;
        int pageSize = 250;
        try {
            int result;
            do {
                // Clear Hibernate
                dao.clear();
                BulkRequest bulk = new BulkRequest();
                List<?> applications = dao.findAll(page * pageSize, pageSize);
                for (Object o : applications) {
                    SearchableEntity doc = (SearchableEntity) o;
                    String searchId = doc.getSearchId();
                    IndexRequest index = new IndexRequest(elasticSearchIndex, elasticSearchType, searchId);
                    String asJson = doc.toSearchJson();
                    index.source(asJson);
                    bulk.add(index);
                }
                result = applications.size();
                log.info("Indexed '{}' {}-{}: {}", elasticSearchType, page * pageSize,
                        (page * pageSize) + pageSize, result);
                page++;
                elasticSearch.bulk(bulk).actionGet();
            } while (result >= pageSize && page < maxPages);
        } catch (Exception e) {
            log.error("Reindexing of '" + elasticSearchType + "' failed.", e);
            throw e;
        } finally {
            log.info("Indexing of '{}' finished.", elasticSearchType);
        }
    }
}