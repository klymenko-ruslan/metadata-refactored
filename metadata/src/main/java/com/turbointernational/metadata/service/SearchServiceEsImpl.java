package com.turbointernational.metadata.service;

import static com.turbointernational.metadata.service.SearchService.IndexingStatus.PHASE_ESTIMATION;
import static com.turbointernational.metadata.service.SearchService.IndexingStatus.PHASE_FINISHED;
import static com.turbointernational.metadata.service.SearchService.IndexingStatus.PHASE_INDEXING;
import static com.turbointernational.metadata.service.search.parser.SearchTermCmpOperatorEnum.EQ;
import static com.turbointernational.metadata.service.search.parser.SearchTermFactory.newBooleanSearchTerm;
import static com.turbointernational.metadata.service.search.parser.SearchTermFactory.newIntegerSearchTerm;
import static com.turbointernational.metadata.service.search.parser.SearchTermFactory.newSearchTerm;
import static com.turbointernational.metadata.service.search.parser.SearchTermFactory.newTextSearchTerm;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.elasticsearch.action.search.SearchType.DFS_QUERY_THEN_FETCH;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.hibernate.ScrollableResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.turbointernational.metadata.dao.CarEngineDao;
import com.turbointernational.metadata.dao.CarFuelTypeDao;
import com.turbointernational.metadata.dao.CarMakeDao;
import com.turbointernational.metadata.dao.CarModelDao;
import com.turbointernational.metadata.dao.CarModelEngineYearDao;
import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.dao.SalesNotePartDao;
import com.turbointernational.metadata.dao.SourceDao;
import com.turbointernational.metadata.entity.CarEngine;
import com.turbointernational.metadata.entity.CarFuelType;
import com.turbointernational.metadata.entity.CarMake;
import com.turbointernational.metadata.entity.CarModel;
import com.turbointernational.metadata.entity.CarModelEngineYear;
import com.turbointernational.metadata.entity.CarYear;
import com.turbointernational.metadata.entity.CriticalDimension;
import com.turbointernational.metadata.entity.SalesNotePart;
import com.turbointernational.metadata.entity.SalesNoteState;
import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.entity.chlogsrc.Source;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.service.search.index.IndexBuilder;
import com.turbointernational.metadata.service.search.parser.AbstractSearchTerm;
import com.turbointernational.metadata.service.search.parser.BooleanSearchTerm;
import com.turbointernational.metadata.service.search.parser.NumberSearchTerm;
import com.turbointernational.metadata.service.search.parser.RangeSearchTerm;
import com.turbointernational.metadata.service.search.parser.SearchTermEnum;
import com.turbointernational.metadata.service.search.parser.TextSearchTerm;

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

    private int DEF_FETCH_SIZE = 250;

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

    @Value("${elasticsearch.type.caryear}")
    private String elasticSearchTypeCarYear = "caryear";

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

    @Value("${elasticsearch.type.source}")
    private String elasticSearchTypeSource = "source";

    @Autowired
    private SourceDao sourceDao;

    @Autowired
    private CriticalDimensionService criticalDimensionService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private InterchangeService interchangeService;

    @Value("${elasticsearch.index.number_of_shards}")
    private int numberOfShards = 1;

    @Value("${elasticsearch.index.number_of_replicas}")
    private int numberOfReplicas = 0;

    @Value("${elasticsearch.index.max_result_window}")
    private int maxResultWindow = 100000;

    private Client elasticSearch; // connection with ElasticSearch

    private final static Pattern REGEX_TOSHORTFIELD = Pattern.compile("\\W");

    private final static int DEF_AGGR_RESULT_SIZE = 300;

    private final static AggregationBuilder[] CMEY_AGGREGATIONS = new AggregationBuilder[] {
            AggregationBuilders.terms("Year").field("year.name.full").size(DEF_AGGR_RESULT_SIZE),
            AggregationBuilders.terms("Make").field("model.make.name.full").size(DEF_AGGR_RESULT_SIZE),
            AggregationBuilders.terms("Model").field("model.name.full").size(DEF_AGGR_RESULT_SIZE),
            AggregationBuilders.terms("Engine").field("engine.engineSize.full").size(DEF_AGGR_RESULT_SIZE),
            AggregationBuilders.terms("Fuel Type").field("engine.fuelType.name.full").size(DEF_AGGR_RESULT_SIZE) };

    private final static AggregationBuilder[] CARMODEL_AGGREGATIONS = new AggregationBuilder[] {
            AggregationBuilders.terms("Make").field("make.name.full").size(DEF_AGGR_RESULT_SIZE) };

    private final static AggregationBuilder[] CARENGINE_AGGREGATIONS = new AggregationBuilder[] {
            AggregationBuilders.terms("Fuel Type").field("fuelType.name.full").size(DEF_AGGR_RESULT_SIZE) };

    private final IndexingStatus indexingStatus = new IndexingStatus();

    @SuppressWarnings("resource")
    @PostConstruct
    private void init() throws UnknownHostException {
        // Establish connection to ElasticSearch cluster.
        InetAddress inetAddrElasticSearchHost = InetAddress.getByName(elasticSearchHost);
        TransportAddress taddr = new InetSocketTransportAddress(inetAddrElasticSearchHost, elasticSearchPort);
        Settings settings = Settings.builder().put("cluster.name", clusterName).build();
        this.elasticSearch = new PreBuiltTransportClient(settings).addTransportAddress(taddr);
    }

    @PreDestroy
    private void done() {
        if (this.elasticSearch != null) {
            this.elasticSearch.close();
        }
    }

    /**
     * Transform a string to a string for search in the "name.short" field.
     * <p>
     * Some types in the ElasticSearch index has property "name" that is mapped
     * on two versions:
     * <dd>
     * <dt>full</dt>
     * <dd>the field indexed as is</dd>
     * <dt>short</dt>
     * <dd>the field transformed to a lower-case string and removed all non-word
     * characters</dd></dd>
     *
     * @param s
     *            string to transform. null not allowed.
     * @return transformed string
     */
    private final static Function<String, String> str2shotfield = s -> REGEX_TOSHORTFIELD.matcher(s).replaceAll("")
            .toLowerCase();

    /**
     * Convert string to SortOrder.
     */
    private final static Function<String, SortOrder> convertSortOrder = sortOrder -> SortOrder
            .valueOf(sortOrder.toUpperCase());

    private class IndexingJob extends Thread {

        private final boolean indexParts;
        private final boolean indexApplications;
        private final boolean indexSalesNotes;
        private final boolean indexChangelogSources;
        private final boolean recreateIndex;
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
                    ScrollableResults scrollableCarEngines, ScrollableResults scrollableCarFuelTypes,
                    ScrollableResults scrollableCarMakes, ScrollableResults scrollableCarModels) {
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
                    indexAllDocs(scrollableCarModelEngineYears, fetchSize, elasticSearchTypeCarModelEngineYear, this);
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

        class ChangelogSourcesIndexer extends Thread implements Observer {

            private final ScrollableResults scrollableChangelogSources;

            private ChangelogSourcesIndexer(ScrollableResults scrollableChangelogSources) {
                this.scrollableChangelogSources = scrollableChangelogSources;
            }

            @Override
            public void run() {
                super.run();
                try {
                    indexAllDocs(scrollableChangelogSources, fetchSize, elasticSearchTypeSource, this);
                } catch (Exception e) {
                    synchronized (indexingStatus) {
                        if (indexingStatus.getErrorMessage() != null) {
                            indexingStatus.setErrorMessage(e.getMessage());
                            indexingStatus.setChangelogSourcesIndexingFailures(1);
                        }
                    }
                }
            }

            @Override
            public void update(Observable o, Object arg) {
                Integer indexed = (Integer) arg;
                synchronized (indexingStatus) {
                    int current = indexingStatus.getChangelogSourcesIndexingCurrentStep();
                    int sum = current + indexed;
                    indexingStatus.setChangelogSourcesIndexed(sum);
                    indexingStatus.setChangelogSourcesIndexingCurrentStep(sum);
                }
            }

        }

        IndexingJob(boolean indexParts, boolean indexApplications, boolean indexSalesNotes,
                boolean indexChangelogSources, boolean recreateIndex, int fetchSize) {
            super();
            this.indexParts = indexParts;
            this.indexApplications = indexApplications;
            this.indexSalesNotes = indexSalesNotes;
            this.indexChangelogSources = indexChangelogSources;
            this.recreateIndex = recreateIndex;
            this.fetchSize = fetchSize;
        }

        @Override
        public void run() {
            super.run();
            TransactionTemplate tt = new TransactionTemplate(txManager);
            tt.setPropagationBehavior(PROPAGATION_REQUIRES_NEW); // new
                                                                 // transaction
            tt.execute((TransactionCallback<Void>) ts -> {
                try {
                    int partsTotal = 0;
                    int carModelEngineYearTotal = 0;
                    int carEngineTotal = 0;
                    int carFuelTypeTotal = 0;
                    int carMakeTotal = 0;
                    int carModelTotal = 0;
                    int salesNotesTotal = 0;
                    int changelogSourcesTotal = 0;

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
                    int applicationsTotal = carModelEngineYearTotal + carEngineTotal + carFuelTypeTotal + carMakeTotal
                            + carModelTotal;
                    if (indexSalesNotes) {
                        salesNotesTotal = salesNotePartDao.getTotal();
                    }

                    if (indexChangelogSources) {
                        changelogSourcesTotal = sourceDao.getTotal();
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

                    Thread changelogSourcesIndexer = null;
                    ScrollableResults scrollableChangelogSources = null;

                    try {
                        if (recreateIndex) {
                            createIndex();
                        }
                        if (indexParts) {
                            scrollableParts = partDao.getScrollableResults(fetchSize, true, "id");
                            partsIndexer = new PartsIndexer(scrollableParts);
                        }
                        if (indexApplications) {
                            scrollableCarModelEngineYears = carModelEngineYearDao.getScrollableResults(fetchSize, true,
                                    "id");
                            scrollableCarEngines = carEngineDao.getScrollableResults(fetchSize, true, "id");
                            scrollableCarFuelTypes = carFuelTypeDao.getScrollableResults(fetchSize, true, "id");
                            scrollableCarMakes = carMakeDao.getScrollableResults(fetchSize, true, "id");
                            scrollableCarModels = carModelDao.getScrollableResults(fetchSize, true, "id");

                            applicationsIndexer = new ApplicationsIndexer(scrollableCarModelEngineYears,
                                    scrollableCarEngines, scrollableCarFuelTypes, scrollableCarMakes,
                                    scrollableCarModels);
                        }
                        if (indexSalesNotes) {
                            scrollableSalesNotes = salesNotePartDao.getScrollableResults(fetchSize, true,
                                    "pk.salesNote.id");
                            salesNotesIndexer = new SalesNotesIndexer(scrollableSalesNotes);
                        }
                        if (indexChangelogSources) {
                            scrollableChangelogSources = sourceDao.getScrollableResults(fetchSize, true, "id");
                            changelogSourcesIndexer = new ChangelogSourcesIndexer(scrollableChangelogSources);
                        }
                        synchronized (indexingStatus) {
                            indexingStatus.setPartsIndexingTotalSteps(partsTotal);
                            indexingStatus.setApplicationsIndexingTotalSteps(applicationsTotal);
                            indexingStatus.setSalesNotesIndexingTotalSteps(salesNotesTotal);
                            indexingStatus.setChangelogSourcesIndexingTotalSteps(changelogSourcesTotal);
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
                        if (changelogSourcesIndexer != null) {
                            changelogSourcesIndexer.start();
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
                        if (changelogSourcesIndexer != null) {
                            changelogSourcesIndexer.join();
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
                        if (scrollableChangelogSources != null) {
                            scrollableChangelogSources.close();
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
            boolean indexSalesNotes, boolean indexChangelogSources, boolean recreateIndex) throws Exception {
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
            indexingStatus.setRecreateIndex(recreateIndex);
            retVal = getIndexingStatus();
        }
        Thread job = new IndexingJob(indexParts, indexApplications, indexSalesNotes, indexChangelogSources,
                recreateIndex, DEF_FETCH_SIZE);
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
    public void createIndex() throws IOException, AssertionError {
        IndicesAdminClient indices = elasticSearch.admin().indices();
        // Delete index.
        boolean indexExists = indices.prepareExists(elasticSearchIndex).execute().actionGet().isExists();
        if (indexExists) {
            indices.prepareDelete(elasticSearchIndex).toString();
            DeleteIndexResponse delIdxResponse = indices.prepareDelete(elasticSearchIndex).get();
            if (!delIdxResponse.isAcknowledged()) {
                throw new AssertionError(
                        String.format("Deletion of the ElasticSearch index '%1$s' failed.", elasticSearchIndex));
            }
        }
        CreateIndexRequestBuilder indexRequestBuilder = indices.prepareCreate(elasticSearchIndex);

        IndexBuilder.build(criticalDimensionService, resourceService, indexRequestBuilder, numberOfShards,
                numberOfReplicas, maxResultWindow);
        CreateIndexResponse createIndexResponse = indexRequestBuilder.get();
        if (!createIndexResponse.isAcknowledged()) {
            throw new AssertionError(
                    String.format("Creation of the ElasticSearch index '%1$s' failed.", elasticSearchIndex));
        }
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
    public void indexCarYear(CarYear carYear) {
        indexDoc(carYear, elasticSearchTypeCarYear);
    }

    @Override
    @Transactional(readOnly = true)
    public void deleteCarYear(CarYear carYear) throws Exception {
        deleteDoc(elasticSearchTypeCarYear, carYear.getSearchId());
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
    @Transactional(readOnly = true)
    public void indexChangelogSource(Source source) {
        indexDoc(source, elasticSearchTypeSource);
    }

    @Override
    @Transactional(readOnly = true)
    public void deleteChangelogSource(Source source) throws Exception {
        deleteDoc(elasticSearchTypeSource, source.getSearchId());
    }

    @Override
    public String filterParts(String partNumber, Long partTypeId, String manufacturerName,
            String name, String interchangeParts, String description, Boolean inactive, String turboTypeName,
            String turboModelName, String cmeyYear, String cmeyMake, String cmeyModel, String cmeyEngine,
            String cmeyFuelType, Map<String, String[]> queriedCriticalDimensions, String sortProperty,
            String sortOrder, Integer offset, Integer limit) {
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
            sterms.add(newBooleanSearchTerm("inactive", inactive));
        }

        if (isNotBlank(cmeyYear)) {
            String normalizedCmeyYear = str2shotfield.apply(cmeyYear);
            sterms.add(newTextSearchTerm("cmeyYear.short", normalizedCmeyYear));
        }
        if (isNotBlank(cmeyMake)) {
            String normalizedCmeyMake = str2shotfield.apply(cmeyMake);
            sterms.add(newTextSearchTerm("cmeyMake.short", normalizedCmeyMake));
        }
        if (isNotBlank(cmeyModel)) {
            String normalizedCmeyModel = str2shotfield.apply(cmeyModel);
            sterms.add(newTextSearchTerm("cmeyModel.short", normalizedCmeyModel));
        }
        if (isNotBlank(cmeyEngine)) {
            String normalizedCmeyEngine = str2shotfield.apply(cmeyEngine);
            sterms.add(newTextSearchTerm("cmeyEngine.short", normalizedCmeyEngine));
        }
        if (isNotBlank(cmeyFuelType)) {
            String normalizedCmeyFuelType = str2shotfield.apply(cmeyFuelType);
            sterms.add(newTextSearchTerm("cmeyFuelType.short", normalizedCmeyFuelType));
        }

        if (isNotBlank(turboTypeName)) {
            String normalizedTurboTypeName = str2shotfield.apply(turboTypeName);
            sterms.add(newTextSearchTerm("turboModel.turboType.name.short", normalizedTurboTypeName));
        }
        if (isNotBlank(turboModelName)) {
            String normalizedTurboModelName = str2shotfield.apply(turboModelName);
            sterms.add(newTextSearchTerm("turboModel.name.short", normalizedTurboModelName));
        }
        if (partTypeId != null) {
            List<CriticalDimension> criticalDimensions = criticalDimensionService
                    .getCriticalDimensionForPartType(partTypeId);
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
                            AbstractSearchTerm asterm = newSearchTerm(cd, val);
                            log.debug("Critical dimension [{}]  search: {}", idxName, asterm);
                            sterms.add(asterm);
                        }
                    } catch (IllegalArgumentException e) {
                        // ignore
                    }
                }
            }
        }
        SearchRequestBuilder srb = elasticSearch.prepareSearch(elasticSearchIndex).setTypes(elasticSearchTypePart)
                .setSearchType(DFS_QUERY_THEN_FETCH);
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
            SortBuilder<?> sort = SortBuilders.fieldSort(sortProperty).order(convertSortOrder.apply(sortOrder))
                    .missing("_last");
            srb.addSort(sort);
        }

        srb.addAggregation(
                AggregationBuilders.terms("Part Type").field("partType.name.full").size(DEF_AGGR_RESULT_SIZE));
        srb.addAggregation(
                AggregationBuilders.terms("Manufacturer").field("manufacturer.name.full").size(DEF_AGGR_RESULT_SIZE));
        srb.addAggregation(AggregationBuilders.terms("Turbo Type").field("turboModel.turboType.name.full")
                .size(DEF_AGGR_RESULT_SIZE));
        srb.addAggregation(
                AggregationBuilders.terms("Turbo Model").field("turboModel.name.full").size(DEF_AGGR_RESULT_SIZE));
        srb.addAggregation(AggregationBuilders.terms("State").field("inactive").size(DEF_AGGR_RESULT_SIZE));

        srb.addAggregation(AggregationBuilders.terms("Car Year").field("cmeyYear.full").size(DEF_AGGR_RESULT_SIZE));
        srb.addAggregation(AggregationBuilders.terms("Car Make").field("cmeyMake.full").size(DEF_AGGR_RESULT_SIZE));
        srb.addAggregation(AggregationBuilders.terms("Car Model").field("cmeyModel.full").size(DEF_AGGR_RESULT_SIZE));
        srb.addAggregation(AggregationBuilders.terms("Car Engine").field("cmeyEngine.full").size(DEF_AGGR_RESULT_SIZE));
        srb.addAggregation(
                AggregationBuilders.terms("Car Fuel Type").field("cmeyFuelType.full").size(DEF_AGGR_RESULT_SIZE));

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
            String engine, String fuel, String sortProperty, String sortOrder, Integer offset, Integer limit) {
        carModelEngineYear = StringUtils.defaultIfEmpty(carModelEngineYear, null);
        SearchRequestBuilder srb = elasticSearch.prepareSearch(elasticSearchIndex)
                .setTypes(elasticSearchTypeCarModelEngineYear).setSearchType(DFS_QUERY_THEN_FETCH);
        QueryBuilder query;
        if (carModelEngineYear == null && year == null && make == null && model == null && engine == null
                && fuel == null) {
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
                subBoolQuery
                        .should(QueryBuilders.termQuery("engine.fuelType.name.short", normalizedCarModelEngineYear));
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
                boolQuery.must(QueryBuilders.termQuery("engine.engineSize.full", engine));
            }
            if (fuel != null) {
                boolQuery.must(QueryBuilders.termQuery("engine.fuelType.name.full", fuel));
            }
            query = boolQuery;
        }
        srb.setQuery(query);
        if (sortProperty != null) {
            SortBuilder<?> sort = SortBuilders.fieldSort(sortProperty).order(convertSortOrder.apply(sortOrder))
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
    public String filterCarMakes(String carMake, String sortProperty, String sortOrder, Integer offset, Integer limit) {
        carMake = StringUtils.defaultIfEmpty(carMake, null);
        SearchRequestBuilder srb = elasticSearch.prepareSearch(elasticSearchIndex).setTypes(elasticSearchTypeCarMake)
                .setSearchType(DFS_QUERY_THEN_FETCH);
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
            SortBuilder<?> sort = SortBuilders.fieldSort(sortProperty).order(convertSortOrder.apply(sortOrder))
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
    public String filterCarModels(String carModel, String make, String sortProperty, String sortOrder, Integer offset,
            Integer limit) {
        carModel = StringUtils.defaultIfEmpty(carModel, null);
        SearchRequestBuilder srb = elasticSearch.prepareSearch(elasticSearchIndex).setTypes(elasticSearchTypeCarModel)
                .setSearchType(DFS_QUERY_THEN_FETCH);
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
            SortBuilder<?> sort = SortBuilders.fieldSort(sortProperty).order(convertSortOrder.apply(sortOrder))
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
        SearchRequestBuilder srb = elasticSearch.prepareSearch(elasticSearchIndex).setTypes(elasticSearchTypeCarEngine)
                .setSearchType(DFS_QUERY_THEN_FETCH);
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
            SortBuilder<?> sort = SortBuilders.fieldSort(sortProperty).order(convertSortOrder.apply(sortOrder))
                    .missing("_last");
            srb.addSort(sort);
        }
        for (AggregationBuilder agg : CARENGINE_AGGREGATIONS) {
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
    public String filterCarFuelTypes(String fuelType, String sortProperty, String sortOrder, Integer offset,
            Integer limit) {
        fuelType = StringUtils.defaultIfEmpty(fuelType, null);
        SearchRequestBuilder srb = elasticSearch.prepareSearch(elasticSearchIndex)
                .setTypes(elasticSearchTypeCarFuelType).setSearchType(DFS_QUERY_THEN_FETCH);
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
            SortBuilder<?> sort = SortBuilders.fieldSort(sortProperty).order(convertSortOrder.apply(sortOrder))
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
            boolean includePrimary, boolean includeRelated, String sortProperty, String sortOrder, Integer offset,
            Integer limit) {
        SearchRequestBuilder srb = elasticSearch.prepareSearch(elasticSearchIndex)
                .setTypes(elasticSearchTypeSalesNotePart).setSearchType(DFS_QUERY_THEN_FETCH);
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
            SortBuilder<?> sort = SortBuilders.fieldSort(sortProperty).order(convertSortOrder.apply(sortOrder))
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

    @Override
    public String filterChanglelogSources(String name, String descritpion, String url, Long sourceNameId,
            String sortProperty, String sortOrder, Integer offset, Integer limit) {
        SearchRequestBuilder srb = elasticSearch.prepareSearch(elasticSearchIndex).setTypes(elasticSearchTypeSource)
                .setSearchType(DFS_QUERY_THEN_FETCH);
        QueryBuilder query;
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (isNotBlank(name)) {
            String normalizedName = str2shotfield.apply(name);
            boolQuery.must(QueryBuilders.termQuery("name.short", normalizedName));
        }
        if (isNotBlank(descritpion)) {
            String normalizedDescription = str2shotfield.apply(descritpion);
            boolQuery.must(QueryBuilders.termQuery("description.short", normalizedDescription));
        }
        if (isNotBlank(url)) {
            String normalizedUrl = str2shotfield.apply(url);
            boolQuery.must(QueryBuilders.termQuery("url.short", normalizedUrl));
        }
        if (sourceNameId != null) {
            boolQuery.must(QueryBuilders.termQuery("sourceName.id", sourceNameId));
        }
        query = boolQuery;
        srb.setQuery(query);
        if (sortProperty != null) {
            SortBuilder<?> sort = SortBuilders.fieldSort(sortProperty).order(convertSortOrder.apply(sortOrder))
                    .missing("_last");
            srb.addSort(sort);
        }
        if (offset != null) {
            srb.setFrom(offset);
        }
        if (limit != null) {
            srb.setSize(limit);
        }
        log.debug("Search request (changlelog sources) to search engine:\n{}", srb);
        return srb.execute().actionGet(timeout).toString();
    }

    @Override
    public void indexAll() throws Exception {
        log.info("All documents are being indexed.");
        createIndex();
        ScrollableResults scrollableParts = partDao.getScrollableResults(DEF_FETCH_SIZE, true, "id");
        indexAllDocs(scrollableParts, DEF_FETCH_SIZE, elasticSearchTypePart, null);
        ScrollableResults scrollableCarModelEngineYears = carModelEngineYearDao.getScrollableResults(DEF_FETCH_SIZE,
                true, "id");
        indexAllDocs(scrollableCarModelEngineYears, DEF_FETCH_SIZE, elasticSearchTypeCarModelEngineYear, null);
        ScrollableResults scrollableCarEngines = carEngineDao.getScrollableResults(DEF_FETCH_SIZE, true, "id");
        indexAllDocs(scrollableCarEngines, DEF_FETCH_SIZE, elasticSearchTypeCarEngine, null);
        ScrollableResults scrollableCarFuelTypes = carFuelTypeDao.getScrollableResults(DEF_FETCH_SIZE, true, "id");
        indexAllDocs(scrollableCarFuelTypes, DEF_FETCH_SIZE, elasticSearchTypeCarFuelType, null);
        ScrollableResults scrollableCarMakes = carMakeDao.getScrollableResults(DEF_FETCH_SIZE, true, "id");
        indexAllDocs(scrollableCarMakes, DEF_FETCH_SIZE, elasticSearchTypeCarMake, null);
        ScrollableResults scrollableCarModels = carModelDao.getScrollableResults(DEF_FETCH_SIZE, true, "id");
        indexAllDocs(scrollableCarModels, DEF_FETCH_SIZE, elasticSearchTypeCarModel, null);
        ScrollableResults scrollableSalesNotes = salesNotePartDao.getScrollableResults(DEF_FETCH_SIZE, true,
                "pk.salesNote.id");
        indexAllDocs(scrollableSalesNotes, DEF_FETCH_SIZE, elasticSearchTypeSalesNotePart, null);
        ScrollableResults scrollableChangelogSources = sourceDao.getScrollableResults(DEF_FETCH_SIZE, true, "id");
        indexAllDocs(scrollableChangelogSources, DEF_FETCH_SIZE, elasticSearchTypeSource, null);
        log.info("The indexing has been finished.");
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
        TransactionTemplate tt = new TransactionTemplate(txManager);
        tt.setPropagationBehavior(PROPAGATION_REQUIRES_NEW); // new transaction
        tt.execute((TransactionCallback<Void>) ts -> {
            String searchId = doc.getSearchId();
            doc.beforeIndexing(interchangeService);
            List<CriticalDimension> criticalDimensions = getCriticalDimensions(doc);
            String asJson = doc.toSearchJson(criticalDimensions);
            log.debug("elasticSearchIndex: {}, elasticSearchType: {}, searchId: {}, asJson: {}", elasticSearchIndex,
                    elasticSearchType, searchId, asJson);
            IndexRequest index = new IndexRequest(elasticSearchIndex, elasticSearchType, searchId);
            index.source(asJson);
            elasticSearch.index(index).actionGet(timeout);
            return null;
        });
    }

    private void indexAllDocs(ScrollableResults scrollableResults, int batchSize, String elasticSearchType,
            Observer observer) throws Exception {
        TransactionTemplate tt = new TransactionTemplate(txManager);
        tt.setPropagationBehavior(PROPAGATION_REQUIRES_NEW); // new transaction
        tt.execute((TransactionCallback<Void>) ts -> {
            String searchId = null;
            try {
                BulkRequest bulk = null;
                while (true) {
                    IndexRequest index;
                    String asJson;
                    // The synchronization below is needed because scrollable
                    // results share the same
                    // EntityManager that is not multithreaded.
                    synchronized (this) {
                        if (!scrollableResults.next()) {
                            break; // stop cycle
                        }
                        Object entity = scrollableResults.get(0);
                        SearchableEntity doc = (SearchableEntity) entity;
                        searchId = doc.getSearchId();
                        index = new IndexRequest(elasticSearchIndex, elasticSearchType, searchId);
                        doc.beforeIndexing(interchangeService);
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
                log.error("Reindexing of '" + elasticSearchType + "' failed. Search ID: {}", searchId, e);
                throw e;
            } finally {
                scrollableResults.close();
            }
            return null;
        });
    }

    private int batchIndex(BulkRequest bulk, String elasticSearchType, Observer observer) {
        elasticSearch.bulk(bulk).actionGet();
        int n = bulk.numberOfActions();
        log.debug("Indexed '{}': {} docs.", elasticSearchType, n);
        if (observer != null) {
            log.debug("Publishing indexing event: {}", n);
            observer.update(null, new Integer(n));
        }
        return n;
    }

}
