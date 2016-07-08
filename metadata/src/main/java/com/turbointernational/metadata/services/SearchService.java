package com.turbointernational.metadata.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.Application;
import com.turbointernational.metadata.domain.car.*;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.salesnote.SalesNotePart;
import com.turbointernational.metadata.domain.part.salesnote.SalesNoteState;
import com.turbointernational.metadata.domain.security.User;
import com.turbointernational.metadata.web.View;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import java.util.Map;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;

/**
 * Interface for indexing and search metadata items.
 *
 * @see SearchServiceEsImpl
 * @see SearchServiceMockImpl
 * <p>
 * Created by dmytro.trunykov@zorallabs.com on 1/31/16.
 */
public interface SearchService {

    static SearchService instance() {
        return Application.getContext().getBean(SearchService.class);
    }

    class IndexingStatus implements Cloneable {

        public final static int PHASE_NONE = 0;
        public final static int PHASE_ESTIMATION = 1;
        public final static int PHASE_INDEXING = 2;
        public final static int PHASE_FINISHED = 3;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int phase = PHASE_NONE;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private String errorMessage = null;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int partsIndexed = 0;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int partsIndexingFailures = 0;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int partsIndexingTotalSteps = 0;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int partsIndexingCurrentStep = 0;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int applicationsIndexed = 0;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int applicationsIndexingFailures = 0;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int applicationsIndexingTotalSteps = 0;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int applicationsIndexingCurrentStep = 0;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int salesNotesIndexed = 0;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int salesNotesIndexingFailures = 0;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int salesNotesIndexingTotalSteps = 0;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int salesNotesIndexingCurrentStep = 0;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private boolean indexParts = true;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private boolean indexApplications = true;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private boolean indexSalesNotes = true;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private Long startedOn = null;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private Long userId = null;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private String userName;

        public IndexingStatus() {
        }

        public int getPhase() {
            return phase;
        }

        public void setPhase(int phase) {
            this.phase = phase;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public int getPartsIndexed() {
            return partsIndexed;
        }

        public void setPartsIndexed(int partsIndexed) {
            this.partsIndexed = partsIndexed;
        }

        public int getPartsIndexingFailures() {
            return partsIndexingFailures;
        }

        public void setPartsIndexingFailures(int partsIndexingFailures) {
            this.partsIndexingFailures = partsIndexingFailures;
        }

        public int getPartsIndexingTotalSteps() {
            return partsIndexingTotalSteps;
        }

        public void setPartsIndexingTotalSteps(int partsIndexingTotalSteps) {
            this.partsIndexingTotalSteps = partsIndexingTotalSteps;
        }

        public int getPartsIndexingCurrentStep() {
            return partsIndexingCurrentStep;
        }

        public void setPartsIndexingCurrentStep(int partsIndexingCurrentStep) {
            this.partsIndexingCurrentStep = partsIndexingCurrentStep;
        }

        public int getApplicationsIndexed() {
            return applicationsIndexed;
        }

        public void setApplicationsIndexed(int applicationsIndexed) {
            this.applicationsIndexed = applicationsIndexed;
        }

        public int getApplicationsIndexingFailures() {
            return applicationsIndexingFailures;
        }

        public void setApplicationsIndexingFailures(int applicationsIndexingFailures) {
            this.applicationsIndexingFailures = applicationsIndexingFailures;
        }

        public int getApplicationsIndexingTotalSteps() {
            return applicationsIndexingTotalSteps;
        }

        public void setApplicationsIndexingTotalSteps(int applicationsIndexingTotalSteps) {
            this.applicationsIndexingTotalSteps = applicationsIndexingTotalSteps;
        }

        public int getApplicationsIndexingCurrentStep() {
            return applicationsIndexingCurrentStep;
        }

        public void setApplicationsIndexingCurrentStep(int applicationsIndexingCurrentStep) {
            this.applicationsIndexingCurrentStep = applicationsIndexingCurrentStep;
        }

        public int getSalesNotesIndexed() {
            return salesNotesIndexed;
        }

        public void setSalesNotesIndexed(int salesNotesIndexed) {
            this.salesNotesIndexed = salesNotesIndexed;
        }

        public int getSalesNotesIndexingFailures() {
            return salesNotesIndexingFailures;
        }

        public void setSalesNotesIndexingFailures(int salesNotesIndexingFailures) {
            this.salesNotesIndexingFailures = salesNotesIndexingFailures;
        }

        public int getSalesNotesIndexingTotalSteps() {
            return salesNotesIndexingTotalSteps;
        }

        public void setSalesNotesIndexingTotalSteps(int salesNotesIndexingTotalSteps) {
            this.salesNotesIndexingTotalSteps = salesNotesIndexingTotalSteps;
        }

        public int getSalesNotesIndexingCurrentStep() {
            return salesNotesIndexingCurrentStep;
        }

        public void setSalesNotesIndexingCurrentStep(int salesNotesIndexingCurrentStep) {
            this.salesNotesIndexingCurrentStep = salesNotesIndexingCurrentStep;
        }

        public boolean isIndexParts() {
            return indexParts;
        }

        public void setIndexParts(boolean indexParts) {
            this.indexParts = indexParts;
        }

        public boolean isIndexApplications() {
            return indexApplications;
        }

        public void setIndexApplications(boolean indexApplications) {
            this.indexApplications = indexApplications;
        }

        public boolean isIndexSalesNotes() {
            return indexSalesNotes;
        }

        public void setIndexSalesNotes(boolean indexSalesNotes) {
            this.indexSalesNotes = indexSalesNotes;
        }

        public Long getStartedOn() {
            return startedOn;
        }

        public void setStartedOn(Long startedOn) {
            this.startedOn = startedOn;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            IndexingStatus retVal = new IndexingStatus();
            retVal.phase = phase;
            retVal.errorMessage = errorMessage;
            retVal.partsIndexed = partsIndexed;
            retVal.partsIndexingFailures = partsIndexingFailures;
            retVal.partsIndexingTotalSteps = partsIndexingTotalSteps;
            retVal.partsIndexingCurrentStep = partsIndexingCurrentStep;
            retVal.applicationsIndexed = applicationsIndexed;
            retVal.applicationsIndexingFailures = applicationsIndexingFailures;
            retVal.applicationsIndexingTotalSteps = applicationsIndexingTotalSteps;
            retVal.applicationsIndexingCurrentStep = applicationsIndexingCurrentStep;
            retVal.salesNotesIndexed = salesNotesIndexed;
            retVal.salesNotesIndexingFailures = salesNotesIndexingFailures;
            retVal.salesNotesIndexingTotalSteps = salesNotesIndexingTotalSteps;
            retVal.salesNotesIndexingCurrentStep = salesNotesIndexingCurrentStep;
            retVal.indexParts = indexParts;
            retVal.indexApplications = indexApplications;
            retVal.indexSalesNotes = indexSalesNotes;
            retVal.startedOn = startedOn;
            retVal.userName = userName;
            return retVal;
        }

        @Override
        public String toString() {
            return "IndexingStatus{" +
                    "phase=" + phase +
                    ", errorMessage='" + errorMessage + '\'' +
                    ", partsIndexed=" + partsIndexed +
                    ", partsIndexingFailures=" + partsIndexingFailures +
                    ", partsIndexingTotalSteps=" + partsIndexingTotalSteps +
                    ", partsIndexingCurrentStep=" + partsIndexingCurrentStep +
                    ", applicationsIndexed=" + applicationsIndexed +
                    ", applicationsIndexingFailures=" + applicationsIndexingFailures +
                    ", applicationsIndexingTotalSteps=" + applicationsIndexingTotalSteps +
                    ", applicationsIndexingCurrentStep=" + applicationsIndexingCurrentStep +
                    ", salesNotesIndexed=" + salesNotesIndexed +
                    ", salesNotesIndexingFailures=" + salesNotesIndexingFailures +
                    ", salesNotesIndexingTotalSteps=" + salesNotesIndexingTotalSteps +
                    ", salesNotesIndexingCurrentStep=" + salesNotesIndexingCurrentStep +
                    ", indexParts=" + indexParts +
                    ", indexApplications=" + indexApplications +
                    ", indexSalesNotes=" + indexSalesNotes +
                    ", startedOn=" + startedOn +
                    ", userId=" + userId +
                    ", userName='" + userName + '\'' +
                    '}';
        }
    }

    class IndexingEvent extends ApplicationEvent {

        private int indexed;

        IndexingEvent(Object source, int indexed) {
            super(source);
            this.indexed = indexed;
        }

        public int getIndexed() {
            return indexed;
        }

        public void setIndexed(int indexed) {
            this.indexed = indexed;
        }

    }

    SearchService.IndexingStatus startIndexing(User user, boolean indexParts, boolean indexApplications,
                                               boolean indexSalesNotes) throws Exception;

    SearchService.IndexingStatus getIndexingStatus() throws Exception;

    void indexPart(long id);

    void indexPart(Part part);

    void deletePart(Part part) throws Exception;

    void indexAllParts(ApplicationListener<IndexingEvent> listener) throws Exception;

    void indexCarEngine(CarEngine carEngine);

    void deleteCarEngine(CarEngine carEngine) throws Exception;

    void indexCarFuelType(CarFuelType carFuelType);

    void deleteCarFuelType(CarFuelType carFuelType) throws Exception;

    void indexCarMake(CarMake carMake);

    void deleteCarMake(CarMake carMake) throws Exception;

    void indexCarModel(CarModel carModel);

    void deleteCarModel(CarModel carModel) throws Exception;

    void indexCarModelEngineYear(CarModelEngineYear carModelEngineYear);

    void deleteCarModelEngineYear(CarModelEngineYear carModelEngineYear) throws Exception;

    void indexAllApplications(ApplicationListener<IndexingEvent> listener) throws Exception;

    void indexSalesNotePart(SalesNotePart salesNotePart);

    void deleteSalesNotePart(SalesNotePart salesNotePart) throws Exception;

    void indexAllSalesNotes(ApplicationListener<IndexingEvent> listener) throws Exception;

    String filterParts(String partNumber, Long partTypeId, Long manufacturerId,
                       String name, String description, Boolean inactive,
                       Map<String, String[]> queriedCriticalDimensions,
                       String sortProperty, String sortOrder,
                       Integer offset, Integer limit);

    String filterCarModelEngineYears(String carModelEngineYear, String year, String make, String model,
                                     String engine, String fuel, String sortProperty, String sortOrder,
                                     Integer offset, Integer limit);

    String filterCarMakes(String carMake, String sortProperty, String sortOrder, Integer offset, Integer limit);

    String filterCarModels(String carModel, String make, String sortProperty, String sortOrder,
                           Integer offset, Integer limit);

    String filterCarEngines(String carEngine, String fuelType, String sortProperty, String sortOrder,
                            Integer offset, Integer limit);

    String filterCarFuelTypes(String fuelType, String sortProperty, String sortOrder,
                              Integer offset, Integer limit);

    String filterSalesNotes(String partNumber, String comment, Long primaryPartId, Set<SalesNoteState> states,
                            boolean includePrimary, boolean includeRelated,
                            String sortProperty, String sortOrder, Integer offset, Integer limit);
}
