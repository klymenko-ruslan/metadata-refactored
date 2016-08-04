package com.turbointernational.metadata.services;

import com.turbointernational.metadata.domain.SearchableEntity;
import com.turbointernational.metadata.domain.car.*;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimension;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.PartDao;
import com.turbointernational.metadata.domain.part.salesnote.SalesNotePart;
import com.turbointernational.metadata.domain.part.salesnote.SalesNotePartDao;
import com.turbointernational.metadata.domain.part.salesnote.SalesNoteState;
import com.turbointernational.metadata.domain.security.User;
import com.turbointernational.metadata.utils.RegExpUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.loader.JsonSettingsLoader;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.hibernate.ScrollableResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.turbointernational.metadata.services.SearchService.IndexingStatus.*;
import static com.turbointernational.metadata.services.SearchTermCmpOperatorEnum.*;
import static com.turbointernational.metadata.services.SearchTermEnum.*;
import static com.turbointernational.metadata.services.SearchTermFactory.newIntegerSearchTerm;
import static com.turbointernational.metadata.services.SearchTermFactory.newTextSearchTerm;
import static com.turbointernational.metadata.utils.RegExpUtils.PTRN_DOUBLE_LIMIT;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW;

/**
 * Implementation of the {@link SearchService} based on the ElasticSearch.
 * <p>
 * Created by dmytro.trunykov@zorallabs.com on 3/8/16.
 */
@Service
@Profile("!integration")
public class SearchServiceEsImpl implements SearchService {

    private final static Logger log = LoggerFactory.getLogger(SearchService.class);

    @Qualifier("transactionManager")
    @Autowired
    private PlatformTransactionManager txManager; // JPA

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

    @Value("${elasticsearch.type.salesnotepart}")
    private String elasticSearchTypeSalesNotePart = "salesnotepart";

    @Autowired
    private SalesNotePartDao salesNotePartDao;

    @Autowired
    private CriticalDimensionService criticalDimensionService;

    @Autowired
    private ResourceService resourceService;

    private Client elasticSearch; // connection with ElasticSearch

    private final static Pattern REGEX_TOSHORTFIELD = Pattern.compile("\\W");

    private final static int DEF_AGGR_RESULT_SIZE = 300;

    private final static AggregationBuilder[] PART_AGGREGATIONS = new AggregationBuilder[]{
            AggregationBuilders.terms("Part Type").field("partType.name.full").size(DEF_AGGR_RESULT_SIZE),
            AggregationBuilders.terms("Manufacturer").field("manufacturer.name.full").size(DEF_AGGR_RESULT_SIZE),
//            AggregationBuilders.terms("Kit Type").field("kitType.name.full").size(DEF_AGGR_RESULT_SIZE),
//            AggregationBuilders.terms("Gasket Type").field("gasketType.name.full").size(DEF_AGGR_RESULT_SIZE),
//            AggregationBuilders.terms("Seal Type").field("sealType.name.full").size(DEF_AGGR_RESULT_SIZE),
//            AggregationBuilders.terms("Coolant Type").field("coolType.name.full").size(DEF_AGGR_RESULT_SIZE),
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

    private final IndexingStatus indexingStatus = new IndexingStatus();

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
     * <p>
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


    private class IndexingJob extends Thread {

        private final boolean indexParts;
        private final boolean indexApplications;
        private final boolean indexSalesNotes;
        private final int fetchSize;

        class PartsIndexer extends Thread implements Observer {

            private final ScrollableResults scrollableParts;

            private PartsIndexer(ScrollableResults scrollableParts) {
                this.scrollableParts = scrollableParts;
            }

            @Override
            public void run() {
                super.run();
                try {
                    indexAllDocs(scrollableParts, fetchSize, elasticSearchTypePart, this);
                } catch (Exception e) {
                    synchronized (indexingStatus) {
                        if (indexingStatus.getErrorMessage() != null) {
                            indexingStatus.setErrorMessage(e.getMessage());
                            indexingStatus.setPartsIndexingFailures(1);
                        }
                    }
                }
            }

            @Override
            public void update(Observable o, Object arg) {
                Integer indexed = (Integer) arg;
                synchronized (indexingStatus) {
                    int current = indexingStatus.getPartsIndexingCurrentStep();
                    int sum = current + indexed;
                    indexingStatus.setPartsIndexed(sum);
                    indexingStatus.setPartsIndexingCurrentStep(sum);
                }
            }

        }

        class ApplicationsIndexer extends Thread implements Observer {

            private final ScrollableResults scrollableCarModelEngineYears;
            private final ScrollableResults scrollableCarEngines;
            private final ScrollableResults scrollableCarFuelTypes;
            private final ScrollableResults scrollableCarMakes;
            private final ScrollableResults scrollableCarModels;

            private ApplicationsIndexer(ScrollableResults scrollableCarModelEngineYears,
                                        ScrollableResults scrollableCarEngines,
                                        ScrollableResults scrollableCarFuelTypes,
                                        ScrollableResults scrollableCarMakes,
                                        ScrollableResults scrollableCarModels) {
                this.scrollableCarModelEngineYears = scrollableCarModelEngineYears;
                this.scrollableCarEngines = scrollableCarEngines;
                this.scrollableCarFuelTypes = scrollableCarFuelTypes;
                this.scrollableCarMakes = scrollableCarMakes;
                this.scrollableCarModels = scrollableCarModels;
            }

            @Override
            public void run() {
                super.run();
                try {
                    indexAllDocs(scrollableCarModelEngineYears, fetchSize,
                            elasticSearchTypeCarModelEngineYear, this);
                    indexAllDocs(scrollableCarEngines, fetchSize, elasticSearchTypeCarEngine, this);
                    indexAllDocs(scrollableCarFuelTypes, fetchSize, elasticSearchTypeCarFuelType, this);
                    indexAllDocs(scrollableCarMakes, fetchSize, elasticSearchTypeCarMake, this);
                    indexAllDocs(scrollableCarModels, fetchSize, elasticSearchTypeCarModel, this);
                } catch (Exception e) {
                    synchronized (indexingStatus) {
                        if (indexingStatus.getErrorMessage() != null) {
                            indexingStatus.setErrorMessage(e.getMessage());
                            indexingStatus.setApplicationsIndexingFailures(1);
                        }
                    }
                }
            }

            @Override
            public void update(Observable o, Object arg) {
                Integer indexed = (Integer) arg;
                synchronized (indexingStatus) {
                    int current = indexingStatus.getApplicationsIndexingCurrentStep();
                    int sum = current + indexed;
                    indexingStatus.setApplicationsIndexed(sum);
                    indexingStatus.setApplicationsIndexingCurrentStep(sum);
                }
            }

        }

        class SalesNotesIndexer extends Thread implements Observer {

            private final ScrollableResults scrollableSalesNotes;

            private SalesNotesIndexer(ScrollableResults scrollableSalesNotes) {
                this.scrollableSalesNotes = scrollableSalesNotes;
            }

            @Override
            public void run() {
                super.run();
                try {
                    indexAllDocs(scrollableSalesNotes, fetchSize, elasticSearchTypeSalesNotePart, this);
                } catch (Exception e) {
                    synchronized (indexingStatus) {
                        if (indexingStatus.getErrorMessage() != null) {
                            indexingStatus.setErrorMessage(e.getMessage());
                            indexingStatus.setSalesNotesIndexingFailures(1);
                        }
                    }
                }
            }

            @Override
            public void update(Observable o, Object arg) {
                Integer indexed = (Integer) arg;
                synchronized (indexingStatus) {
                    int current = indexingStatus.getSalesNotesIndexingCurrentStep();
                    int sum = current + indexed;
                    indexingStatus.setSalesNotesIndexed(sum);
                    indexingStatus.setSalesNotesIndexingCurrentStep(sum);
                }
            }

        }

        IndexingJob(boolean indexParts, boolean indexApplications, boolean indexSalesNotes, int fetchSize) {
            super();
            this.indexParts = indexParts;
            this.indexApplications = indexApplications;
            this.indexSalesNotes = indexSalesNotes;
            this.fetchSize = fetchSize;
        }

        @Override
        public void run() {
            super.run();
            TransactionTemplate tt = new TransactionTemplate(txManager);
            tt.setPropagationBehavior(PROPAGATION_REQUIRES_NEW); // new transaction
            tt.execute((TransactionCallback<Void>) ts -> {
                try {
                    int partsTotal = 0;
                    int carModelEngineYearTotal = 0;
                    int carEngineTotal = 0;
                    int carFuelTypeTotal = 0;
                    int carMakeTotal = 0;
                    int carModelTotal = 0;
                    int salesNotesTotal = 0;


                    if (indexParts) {
                        partsTotal = partDao.getTotal();
                    }
                    if (indexApplications) {
                        carModelEngineYearTotal = carModelEngineYearDao.getTotal();
                        carEngineTotal = carEngineDao.getTotal();
                        carFuelTypeTotal = carFuelTypeDao.getTotal();
                        carMakeTotal = carMakeDao.getTotal();
                        carModelTotal = carModelDao.getTotal();
                    }
                    int applicationsTotal = carModelEngineYearTotal + carEngineTotal +
                            carFuelTypeTotal + carMakeTotal + carModelTotal;
                    if (indexSalesNotes) {
                        salesNotesTotal = salesNotePartDao.getTotal();
                    }

                    Thread partsIndexer = null;
                    ScrollableResults scrollableParts = null;

                    Thread applicationsIndexer = null;
                    ScrollableResults scrollableCarModelEngineYears = null;
                    ScrollableResults scrollableCarEngines = null;
                    ScrollableResults scrollableCarFuelTypes = null;
                    ScrollableResults scrollableCarMakes = null;
                    ScrollableResults scrollableCarModels = null;

                    Thread salesNotesIndexer = null;
                    ScrollableResults scrollableSalesNotes = null;

                    try {
                        if (indexParts) {
                            scrollableParts = partDao.getScrollableResults(fetchSize, true, "id");
                            partsIndexer = new PartsIndexer(scrollableParts);
                        }
                        if (indexApplications) {
                            scrollableCarModelEngineYears = carModelEngineYearDao.getScrollableResults(fetchSize, true, "id");
                            scrollableCarEngines = carEngineDao.getScrollableResults(fetchSize, true, "id");
                            scrollableCarFuelTypes = carFuelTypeDao.getScrollableResults(fetchSize, true, "id");
                            scrollableCarMakes = carMakeDao.getScrollableResults(fetchSize, true, "id");
                            scrollableCarModels = carModelDao.getScrollableResults(fetchSize, true, "id");

                            applicationsIndexer = new ApplicationsIndexer(
                                    scrollableCarModelEngineYears,
                                    scrollableCarEngines,
                                    scrollableCarFuelTypes,
                                    scrollableCarMakes,
                                    scrollableCarModels);
                        }
                        if (indexSalesNotes) {
                            scrollableSalesNotes = salesNotePartDao.getScrollableResults(fetchSize, true, "pk.salesNote.id");
                            salesNotesIndexer = new SalesNotesIndexer(scrollableSalesNotes);
                        }
                        synchronized (indexingStatus) {
                            indexingStatus.setPartsIndexingTotalSteps(partsTotal);
                            indexingStatus.setApplicationsIndexingTotalSteps(applicationsTotal);
                            indexingStatus.setSalesNotesIndexingTotalSteps(salesNotesTotal);
                            indexingStatus.setPhase(PHASE_INDEXING);
                        }

                        if (partsIndexer != null) {
                            partsIndexer.start();
                        }
                        if (applicationsIndexer != null) {
                            applicationsIndexer.start();
                        }
                        if (salesNotesIndexer != null) {
                            salesNotesIndexer.start();
                        }

                        if (partsIndexer != null) {
                            partsIndexer.join();
                        }
                        if (applicationsIndexer != null) {
                            applicationsIndexer.join();
                        }
                        if (salesNotesIndexer != null) {
                            salesNotesIndexer.join();
                        }

                    } finally {
                        if (scrollableParts != null) {
                            scrollableParts.close();
                        }
                        if (scrollableCarModelEngineYears != null) {
                            scrollableCarModelEngineYears.close();
                        }
                        if (scrollableCarEngines != null) {
                            scrollableCarEngines.close();
                        }
                        if (scrollableCarFuelTypes != null) {
                            scrollableCarFuelTypes.close();
                        }
                        if (scrollableCarMakes != null) {
                            scrollableCarMakes.close();
                        }
                        if (scrollableCarModels != null) {
                            scrollableCarModels.close();
                        }
                        if (scrollableSalesNotes != null) {
                            scrollableSalesNotes.close();
                        }
                    }
                } catch (Exception e) {
                    log.error("Indexing job failed.", e);
                    synchronized (indexingStatus) {
                        if (indexingStatus.getErrorMessage() == null) {
                            indexingStatus.setErrorMessage(e.getMessage());
                        }
                    }
                } finally {
                    synchronized (indexingStatus) {
                        indexingStatus.setPhase(PHASE_FINISHED);
                        indexingStatus.setFinishedOn(System.currentTimeMillis());
                    }
                }
                return null;
            });
        }

    }

    @Override
    public IndexingStatus startIndexing(User user, boolean indexParts, boolean indexApplications,
                                        boolean indexSalesNotes) throws Exception {
        IndexingStatus retVal;
        long now = System.currentTimeMillis();
        synchronized (indexingStatus) {
            int phase = indexingStatus.getPhase();
            if (phase == PHASE_ESTIMATION || phase == PHASE_INDEXING) {
                throw new IllegalStateException("Indexing is already in progress.");
            }
            indexingStatus.reset();
            indexingStatus.setPhase(PHASE_ESTIMATION);
            indexingStatus.setStartedOn(now);
            indexingStatus.setUserId(user.getId());
            indexingStatus.setUserName(user.getUsername());
            indexingStatus.setIndexParts(indexParts);
            indexingStatus.setIndexApplications(indexApplications);
            indexingStatus.setIndexSalesNotes(indexSalesNotes);
            retVal = getIndexingStatus();
        }
        Thread job = new IndexingJob(indexParts, indexApplications, indexSalesNotes, 250);
        job.start();
        return retVal;
    }

    @Override
    public IndexingStatus getIndexingStatus() throws Exception {
        synchronized (indexingStatus) {
            return (IndexingStatus) indexingStatus.clone();
        }
    }

    @Override
    public void createIndex() throws IOException {
        IndicesAdminClient indices = elasticSearch.admin().indices();
        // Delete index.
        boolean indexExists = indices.prepareExists(elasticSearchIndex).execute().actionGet().isExists();
        if (indexExists) {
            String s = indices.prepareDelete(elasticSearchIndex).toString();
            /*
            DeleteIndexResponse delIdxResponse = indices.prepareDelete(elasticSearchIndex).get();
            if (!delIdxResponse.isAcknowledged()) {
                throw new AssertionError("Deletion of the ElasticSearch index '%1$s' failed.".
                        format(elasticSearchIndex));
            }
            */
        }
        CreateIndexRequestBuilder indexRequestBuilder = indices.prepareCreate(elasticSearchIndex);
        String mappingsDefinition = resourceService.loadFromMeta("elasticsearch/mappings.json");
        indexRequestBuilder.addMapping("", "");
        String settingsDefinition = resourceService.loadFromMeta("elasticsearch/settings.json");
        Map<String, String> settings = (new JsonSettingsLoader()).load(settingsDefinition);
        indexRequestBuilder.setSettings(settings);
        // Add Part fields to the index.
        //indexRequestBuilder.addMapping()
        // Add critical dimensions to the index.
        Map<Long, List<CriticalDimension>> crtclDmnsns = criticalDimensionService.getCriticalDimensionsCacheById();
        crtclDmnsns.forEach((partTypeId, ptCrtclDmnsns) -> {
            ptCrtclDmnsns.forEach(cd -> {
                // TODO
            });
        });
        System.out.println(indexRequestBuilder.toString());
        /*
        CreateIndexResponse createIndexResponse = indexRequestBuilder.get();
        if (!createIndexResponse.isAcknowledged()) {
            throw new AssertionError("Creation of the ElasticSearch index '%1$s' failed.".
                    format(elasticSearchIndex));
        }
        */
    }

    @Override
    @Transactional(readOnly = true)
    public void indexPart(long id) {
        Part part = partDao.findOne(id);
        indexPart(part);
    }

    @Override
    @Transactional(readOnly = true)
    public void indexPart(Part part) {
        indexDoc(part, elasticSearchTypePart);
    }

    @Override
    @Transactional(readOnly = true)
    public void deletePart(Part part) throws Exception {
        deleteDoc(elasticSearchTypePart, part.getSearchId());
    }

    @Override
    @Transactional(readOnly = true)
    public void indexCarEngine(CarEngine carEngine) {
        indexDoc(carEngine, elasticSearchTypeCarEngine);
    }

    @Override
    @Transactional(readOnly = true)
    public void deleteCarEngine(CarEngine carEngine) throws Exception {
        deleteDoc(elasticSearchTypeCarEngine, carEngine.getSearchId());
    }

    @Override
    @Transactional(readOnly = true)
    public void indexCarFuelType(CarFuelType carFuelType) {
        indexDoc(carFuelType, elasticSearchTypeCarFuelType);
    }

    @Override
    @Transactional(readOnly = true)
    public void deleteCarFuelType(CarFuelType carFuelType) throws Exception {
        deleteDoc(elasticSearchTypeCarFuelType, carFuelType.getSearchId());
    }

    @Override
    @Transactional(readOnly = true)
    public void indexCarMake(CarMake carMake) {
        indexDoc(carMake, elasticSearchTypeCarMake);
    }

    @Override
    @Transactional(readOnly = true)
    public void deleteCarMake(CarMake carMake) throws Exception {
        deleteDoc(elasticSearchTypeCarMake, carMake.getSearchId());
    }

    @Override
    @Transactional(readOnly = true)
    public void indexCarModel(CarModel carModel) {
        indexDoc(carModel, elasticSearchTypeCarModel);
    }

    @Override
    @Transactional(readOnly = true)
    public void deleteCarModel(CarModel carModel) throws Exception {
        deleteDoc(elasticSearchTypeCarModel, carModel.getSearchId());
    }

    @Override
    @Transactional(readOnly = true)
    public void indexCarModelEngineYear(CarModelEngineYear carModelEngineYear) {
        indexDoc(carModelEngineYear, elasticSearchTypeCarModelEngineYear);
    }

    @Override
    @Transactional(readOnly = true)
    public void deleteCarModelEngineYear(CarModelEngineYear carModelEngineYear) throws Exception {
        deleteDoc(elasticSearchTypeCarModelEngineYear, carModelEngineYear.getSearchId());
    }

    @Override
    @Transactional(readOnly = true)
    public void indexSalesNotePart(SalesNotePart salesNotePart) {
        indexDoc(salesNotePart, elasticSearchTypeSalesNotePart);
    }

    @Override
    @Transactional(readOnly = true)
    public void deleteSalesNotePart(SalesNotePart salesNotePart) throws Exception {
        deleteDoc(elasticSearchTypeSalesNotePart, salesNotePart.getSearchId());
    }

    @Override
    public String filterParts(String partNumber, Long partTypeId, String manufacturerName,
                              String name, String description, Boolean inactive,
                              String turboTypeName, String turboModelName,
                              Map<String, String[]> queriedCriticalDimensions,
                              String sortProperty, String sortOrder,
                              Integer offset, Integer limit) {
        List<AbstractSearchTerm> sterms = new ArrayList<>(64);
        partNumber = StringUtils.defaultIfEmpty(partNumber, null);
        if (isNotBlank(partNumber)) {
            String normalizedPartNumber = str2shotfield.apply(partNumber);
            sterms.add(newTextSearchTerm("manufacturerPartNumber.short", normalizedPartNumber));
        }
        if (partTypeId != null) {
            sterms.add(newIntegerSearchTerm("partType.id", EQ, partTypeId));
        }
        if (manufacturerName != null) {
            sterms.add(newTextSearchTerm("manufacturer.name.full", manufacturerName));
        }
        if (isNotBlank(name)) {
            String normalizedName = str2shotfield.apply(name);
            sterms.add(newTextSearchTerm("name.short", normalizedName));
        }
        if (isNotBlank(description)) {
            String normalizedDescription = str2shotfield.apply(description);
            sterms.add(newTextSearchTerm("description.short", normalizedDescription));
        }
        if (inactive != null) {
            sterms.add(SearchTermFactory.newBooleanSearchTerm("inactive", inactive));
        }

        if (turboTypeName != null) {
            sterms.add(newTextSearchTerm("turboModel.turboType.name.full", turboTypeName));
        }
        if (turboModelName != null) {
            sterms.add(newTextSearchTerm("turboModel.name.full", turboModelName));
        }
        if (partTypeId != null) {
            List<CriticalDimension> criticalDimensions = criticalDimensionService.getCriticalDimensionForPartType(partTypeId);
            if (criticalDimensions != null && !criticalDimensions.isEmpty()) {
                for (CriticalDimension cd : criticalDimensions) {
                    try {
                        String val = null;
                        String idxName = cd.getIdxName();
                        String[] paramVals = queriedCriticalDimensions.get(idxName);
                        if (paramVals != null && paramVals.length > 0) {
                            val = paramVals[0];
                        }
                        if (isNotBlank(val)) {
                            AbstractSearchTerm asterm = SearchTermFactory.newSearchTerm(cd, val);
                            sterms.add(asterm);
                        }
                    } catch (IllegalArgumentException e) {
                        // ignore
                    }
                }
            }
        }
        SearchRequestBuilder srb = elasticSearch.prepareSearch(elasticSearchIndex).setTypes(elasticSearchTypePart)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        QueryBuilder query;
        if (sterms.isEmpty()) {
            query = QueryBuilders.matchAllQuery();
        } else {
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            for (AbstractSearchTerm ast : sterms) {
                SearchTermEnum astType = ast.getType();
                switch (astType) {
                    case BOOLEAN:
                        BooleanSearchTerm bst = (BooleanSearchTerm) ast;
                        boolQuery.must(QueryBuilders.termQuery(bst.getFieldName(), bst.getTerm()));
                        break;
                    case DECIMAL:
                        NumberSearchTerm dst = (NumberSearchTerm) ast;
                        QueryBuilder dqb;
                        switch (dst.getCmpOperator()) {
                            case EQ:
                                dqb = QueryBuilders.termQuery(dst.getFieldName(), dst.getTerm());
                                break;
                            case LT:
                                dqb = QueryBuilders.rangeQuery(dst.getFieldName()).lt(dst.getTerm());
                                break;
                            case LTE:
                                dqb = QueryBuilders.rangeQuery(dst.getFieldName()).lte(dst.getTerm());
                                break;
                            case GTE:
                                dqb = QueryBuilders.rangeQuery(dst.getFieldName()).gte(dst.getTerm());
                                break;
                            case GT:
                                dqb = QueryBuilders.rangeQuery(dst.getFieldName()).gt(dst.getTerm());
                                break;
                            default:
                                throw new IllegalArgumentException("Unsupported compare operator: " + dst.getCmpOperator());
                        }
                        boolQuery.must(dqb);
                        break;
                    case INTEGER:
                        NumberSearchTerm ist = (NumberSearchTerm) ast;
                        QueryBuilder iqb;
                        switch (ist.getCmpOperator()) {
                            case EQ:
                                iqb = QueryBuilders.termQuery(ist.getFieldName(), ist.getTerm());
                                break;
                            case LT:
                                iqb = QueryBuilders.rangeQuery(ist.getFieldName()).lt(ist.getTerm());
                                break;
                            case LTE:
                                iqb = QueryBuilders.rangeQuery(ist.getFieldName()).lte(ist.getTerm());
                                break;
                            case GTE:
                                iqb = QueryBuilders.rangeQuery(ist.getFieldName()).gte(ist.getTerm());
                                break;
                            case GT:
                                iqb = QueryBuilders.rangeQuery(ist.getFieldName()).gt(ist.getTerm());
                                break;
                            default:
                                throw new IllegalArgumentException("Unsupported compare operator: " + ist.getCmpOperator());
                        }
                        boolQuery.must(iqb);
                        break;
                    case TEXT:
                        TextSearchTerm tst = (TextSearchTerm) ast;
                        boolQuery.must(QueryBuilders.termQuery(tst.getFieldName(), tst.getTerm()));
                        break;
                    case DECIMAL_RANGE:
                        RangeSearchTerm drst = (RangeSearchTerm) ast;
                        RangeQueryBuilder drqb = QueryBuilders.rangeQuery(drst.getFieldName());
                        drqb.gte(drst.getFrom().doubleValue()).lte(drst.getTo().doubleValue());
                        boolQuery.must(drqb);
                        break;
                    case INTEGER_RANGE:
                        RangeSearchTerm irst = (RangeSearchTerm) ast;
                        RangeQueryBuilder irqb = QueryBuilders.rangeQuery(irst.getFieldName());
                        irqb.gte(irst.getFrom().longValue()).lte(irst.getTo().longValue());
                        boolQuery.must(irqb);
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported search term type: " + astType);
                }
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

    @Override
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
                BoolQueryBuilder subBoolQuery = QueryBuilders.boolQuery();
                subBoolQuery.should(QueryBuilders.termQuery("year.name.short", normalizedCarModelEngineYear));
                subBoolQuery.should(QueryBuilders.termQuery("model.name.short", normalizedCarModelEngineYear));
                subBoolQuery.should(QueryBuilders.termQuery("model.make.name.short", normalizedCarModelEngineYear));
                subBoolQuery.should(QueryBuilders.termQuery("engine.engineSize.short", normalizedCarModelEngineYear));
                subBoolQuery.should(QueryBuilders.termQuery("engine.fueltype.name.short", normalizedCarModelEngineYear));
                boolQuery.must(subBoolQuery);
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

    @Override
    public String filterCarMakes(String carMake, String sortProperty, String sortOrder,
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

    @Override
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

    @Override
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
                boolQuery.must(QueryBuilders.termQuery("engineSize.short", normalizedCarEngine));
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

    @Override
    public String filterCarFuelTypes(String fuelType, String sortProperty, String sortOrder,
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

    @Override
    public String filterSalesNotes(String partNumber, String comment, Long primaryPartId, Set<SalesNoteState> states,
                                   boolean includePrimary, boolean includeRelated,
                                   String sortProperty, String sortOrder, Integer offset, Integer limit) {
        SearchRequestBuilder srb = elasticSearch.prepareSearch(elasticSearchIndex)
                .setTypes(elasticSearchTypeSalesNotePart).setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        QueryBuilder query;
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (isNotBlank(partNumber)) {
            String normalizedPartNum = str2shotfield.apply(partNumber);
            boolQuery.must(QueryBuilders.termQuery("pk.part.manufacturerPartNumber.short", normalizedPartNum));
        }
        if (isNotBlank(comment)) {
            String normalizedComment = str2shotfield.apply(comment);
            boolQuery.must(QueryBuilders.termQuery("pk.salesNote.comment.short", normalizedComment));
        }
        if (primaryPartId != null) {
            boolQuery.must(QueryBuilders.termQuery("primaryPartId", primaryPartId));
        }
        if (states != null && !states.isEmpty()) {
            BoolQueryBuilder subBoolQuery = QueryBuilders.boolQuery();
            states.forEach(sns -> subBoolQuery.should(QueryBuilders.termQuery("pk.salesNote.state", sns.toString())));
            boolQuery.must(subBoolQuery);
        }
        if (includePrimary || includeRelated) {
            BoolQueryBuilder subBoolQuery = QueryBuilders.boolQuery();
            if (includePrimary) {
                subBoolQuery.should(QueryBuilders.termQuery("primary", true));
            }
            if (includeRelated) {
                subBoolQuery.should(QueryBuilders.termQuery("primary", false));
            }
            boolQuery.must(subBoolQuery);
        }
        query = boolQuery;
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
        log.debug("Search request (sales notes) to search engine:\n{}", srb);
        return srb.execute().actionGet(timeout).toString();
    }

    private void deleteDoc(String elasticSearchType, String searchId) throws Exception {
        DeleteRequest delete = new DeleteRequest(elasticSearchIndex, elasticSearchType, searchId);
        elasticSearch.delete(delete).actionGet(timeout);
    }

    private List<CriticalDimension> getCriticalDimensions(SearchableEntity doc) {
        List<CriticalDimension> retVal = null;
        if (Part.class.isAssignableFrom(doc.getClass())) {
            Part p = (Part) doc;
            Long partTypeId = p.getPartType().getId();
            retVal = criticalDimensionService.getCriticalDimensionForPartType(partTypeId);
        }
        return retVal;
    }

    private void indexDoc(SearchableEntity doc, String elasticSearchType) {
        String searchId = doc.getSearchId();
        List<CriticalDimension> criticalDimensions = getCriticalDimensions(doc);
        String asJson = doc.toSearchJson(criticalDimensions);
        log.debug("elasticSearchIndex: {}, elasticSearchType: {}, searchId: {}, asJson: {}",
                elasticSearchIndex, elasticSearchType, searchId, asJson);
        IndexRequest index = new IndexRequest(elasticSearchIndex, elasticSearchType, searchId);
        index.source(asJson);
        elasticSearch.index(index).actionGet(timeout);
    }

    private void indexAllDocs(ScrollableResults scrollableResults, int batchSize,
                                           String elasticSearchType, Observer observer) throws Exception {
        try {
            BulkRequest bulk = null;
            while (true) {
                String searchId;
                IndexRequest index;
                String asJson;
                // The synchronization below is needed because scrollable results share the same
                // EntityManager that is not multithreaded.
                synchronized (this) {
                    if (!scrollableResults.next()) {
                        break; // stop cycle
                    }
                    Object entity = scrollableResults.get(0);
                    SearchableEntity doc = (SearchableEntity) entity;
                    searchId = doc.getSearchId();
                    index = new IndexRequest(elasticSearchIndex, elasticSearchType, searchId);
                    List<CriticalDimension> criticalDimensions = getCriticalDimensions(doc);
                    asJson = doc.toSearchJson(criticalDimensions);
                }
                Thread.yield();
                log.debug("elasticSearchIndex: {}, elasticSearchType: {}, searchId: {}, asJson: {}",
                        elasticSearchIndex, elasticSearchType, searchId, asJson);
                index.source(asJson);
                if (bulk == null) {
                    bulk = new BulkRequest();
                }
                bulk.add(index);
                boolean flush = bulk.numberOfActions() == batchSize;
                if (flush) {
                    batchIndex(bulk, elasticSearchType, observer);
                    bulk = null;
                }
            }
            if (bulk != null && bulk.numberOfActions() > 0) {
                batchIndex(bulk, elasticSearchType, observer);
            }
        } catch (Exception e) {
            log.error("Reindexing of '" + elasticSearchType + "' failed.", e);
            throw e;
        } finally {
            scrollableResults.close();
        }
    }

    private int batchIndex(BulkRequest bulk, String elasticSearchType, Observer observer) {
        elasticSearch.bulk(bulk).actionGet();
        int n = bulk.numberOfActions();
        log.info("Indexed '{}': {} docs.", elasticSearchType, n);
        if (observer != null) {
            log.debug("Publishing indexing event: {}", n);
            observer.update(null, new Integer(n));
        }
        return n;
    }

}

enum SearchTermEnum {
    BOOLEAN, DECIMAL, INTEGER, DECIMAL_RANGE, INTEGER_RANGE, TEXT
}

enum SearchTermCmpOperatorEnum {

    LT("<"), LTE("<="), EQ("="), GTE(">="), GT(">");

    final String sign;

    SearchTermCmpOperatorEnum(String sign) {
        this.sign = sign;
    }

    static SearchTermCmpOperatorEnum fromSign(String s) throws IllegalArgumentException {
        if (s == null) {
            return null;
        }
        s = s.trim();
        if (s.equals("<")) {
            return LT;
        } else if (s.equals("<=")) {
            return LTE;
        } else if (s.equals("=")) {
            return EQ;
        } else if (s.equals(">=")) {
            return GTE;
        } else if (s.equals(">")) {
            return GT;
        }
        throw new IllegalArgumentException("Can't parse sing: " + s);
    }
}

abstract class AbstractSearchTerm {

    private final SearchTermEnum type;

    private final String fieldName;

    AbstractSearchTerm(SearchTermEnum type, String fieldName) {
        this.type = type;
        this.fieldName = fieldName;
    }

    SearchTermEnum getType() {
        return type;
    }

    String getFieldName() {
        return fieldName;
    }

}

class BooleanSearchTerm extends AbstractSearchTerm {

    private final Boolean term;

    BooleanSearchTerm(String fieldName, Boolean term) {
        super(BOOLEAN, fieldName);
        this.term = term;
    }

    Boolean getTerm() {
        return term;
    }

}

class AbstractNumberSearchTerm extends AbstractSearchTerm {

    private final SearchTermCmpOperatorEnum cmpOperator;

    public AbstractNumberSearchTerm(SearchTermEnum type, String fieldName, SearchTermCmpOperatorEnum cmpOperator) {
        super(type, fieldName);
        this.cmpOperator = cmpOperator;
    }

    SearchTermCmpOperatorEnum getCmpOperator() {
        return cmpOperator;
    }

}

class NumberSearchTerm extends AbstractNumberSearchTerm {

    private final Number term;

    NumberSearchTerm(SearchTermEnum type, String fieldName, SearchTermCmpOperatorEnum cmpOperator, Number term) {
        super(type, fieldName, cmpOperator);
        this.term = term;
    }

    Number getTerm() {
        return term;
    }

}

class RangeSearchTerm extends AbstractSearchTerm {

    private final Number from;
    private final Number to;

    RangeSearchTerm(String fieldName, Number from, Number to) {
        super(DECIMAL_RANGE, fieldName);
        this.from = from;
        this.to = to;
    }

    Number getFrom() {
        return from;
    }

    Number getTo() {
        return to;
    }

}

class TextSearchTerm extends AbstractSearchTerm {

    private final String term;

    TextSearchTerm(String fieldName, String term) {
        super(TEXT, fieldName);
        this.term = term;
    }

    String getTerm() {
        return term;
    }

}

class SearchTermFactory {

    static TextSearchTerm newTextSearchTerm(String fieldName, String term) {
        return new TextSearchTerm(fieldName, term);
    }

    static BooleanSearchTerm newBooleanSearchTerm(String fieldName, Boolean term) {
        return new BooleanSearchTerm(fieldName, term);
    }

    static NumberSearchTerm newIntegerSearchTerm(String fieldName, SearchTermCmpOperatorEnum cmpOperator, Long term) {
        return new NumberSearchTerm(INTEGER, fieldName, cmpOperator, term);
    }

    static AbstractSearchTerm newSearchTerm(CriticalDimension cd, String s) {
        AbstractSearchTerm retVal;
        Range pr;
        String idxName = cd.getIdxName();
        CriticalDimension.DataTypeEnum dataType = cd.getDataType();
        switch (dataType) {
            case DECIMAL:
                pr = parseSearchStr(s);
                if (pr.isBoundLimit()) {
                    retVal = new RangeSearchTerm(idxName, pr.limit1.val, pr.limit2.val);
                } else {
                    retVal = new NumberSearchTerm(DECIMAL, idxName, pr.limit1.operator, pr.limit1.val);
                }
                break;
            case ENUMERATION:
                Long n = Long.valueOf(s);
                retVal = new NumberSearchTerm(INTEGER, idxName, EQ, n);
                break;
            case INTEGER:
                pr = parseSearchStr(s);
                if (pr.isBoundLimit()) {
                    retVal = new RangeSearchTerm(idxName, pr.limit1.val, pr.limit2.val);
                } else {
                    retVal = new NumberSearchTerm(INTEGER, idxName, pr.limit1.operator, pr.limit1.val);
                }
                break;
            case TEXT:
                retVal = newTextSearchTerm(idxName, s);
                break;
            default:
                throw new AssertionError("Unsupported data type: " + dataType);
        }
        return retVal;
    }

    static Limit parseLimit(String s) throws IllegalArgumentException {
        if (s == null) {
            return null;
        }
        Matcher matcher = PTRN_DOUBLE_LIMIT.matcher(s);
        if (matcher.matches()) {
            SearchTermCmpOperatorEnum operator = SearchTermCmpOperatorEnum.fromSign(matcher.group(2));
            if (operator == null) {
                operator = EQ;
            }
            Double val = RegExpUtils.parseDouble(matcher.group(3));
            if (val == null) {
                throw new IllegalArgumentException("Invalid limit: " + s);
            }
            return new Limit(operator, val);
        } else {
            throw new NumberFormatException(s);
        }
    }

    static Range parseRange(String s) throws IllegalArgumentException {
        if (s == null) {
            return null;
        }
        int p0 = s.indexOf("..");
        if (p0 < 1) {
            throw new IllegalArgumentException("Invalid range: " + s);
        }
        if (s.length() - 2 == p0) {
            throw new IllegalArgumentException("Invalid range: " + s);
        }
        String s0 = s.substring(0, p0);
        String s1 = s.substring(p0 + 2);
        Double r0 = RegExpUtils.parseDouble(s0);
        Double r1 = RegExpUtils.parseDouble(s1);
        Limit limit0 = new Limit(GTE, r0);
        Limit limit1 = new Limit(LTE, r1);
        return new Range(limit0, limit1);
    }

    static Range parseSearchStr(String s) throws IllegalArgumentException {
        if (s == null) {
            return null;
        }
        try {
            Limit iLimit = parseLimit(s);
            return new Range(iLimit, null);
        } catch (IllegalArgumentException e) {
            // ignore
        }
        try {
            return parseRange(s);
        } catch (IllegalArgumentException e) {
            // ignore
        }
        throw new IllegalArgumentException("Parsing failed: " + s);
    }

    static class Limit {

        final SearchTermCmpOperatorEnum operator;
        final Number val;

        Limit(SearchTermCmpOperatorEnum operator, Number val) {
            this.operator = operator;
            this.val = val;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Limit limit = (Limit) o;

            if (operator != limit.operator) return false;
            return val != null ? val.equals(limit.val) : limit.val == null;

        }

        @Override
        public String toString() {
            return "Limit{" +
                    "operator=" + operator +
                    ", val=" + val +
                    '}';
        }

    }

    static class Range {

        final Limit limit1;
        final Limit limit2;

        public Range(Limit limit1, Limit limit2) {
            this.limit1 = limit1;
            this.limit2 = limit2;
        }

        boolean isBoundLimit() {
            return limit1 != null && limit2 != null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Range range = (Range) o;

            if (limit1 != null ? !limit1.equals(range.limit1) : range.limit1 != null) return false;
            return limit2 != null ? limit2.equals(range.limit2) : range.limit2 == null;

        }

        @Override
        public String toString() {
            return "Range{" +
                    "limit1=" + limit1 +
                    ", limit2=" + limit2 +
                    '}';
        }

    }

}