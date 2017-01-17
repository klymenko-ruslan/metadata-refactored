package com.turbointernational.metadata.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.Application;
import com.turbointernational.metadata.entity.*;
import com.turbointernational.metadata.entity.chlogsrc.Source;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.entity.SalesNotePart;
import com.turbointernational.metadata.entity.SalesNoteState;
import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.util.View;

import java.io.IOException;
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
        private int phase;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private String errorMessage;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int partsIndexed;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int partsIndexingFailures;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int partsIndexingTotalSteps;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int partsIndexingCurrentStep;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int applicationsIndexed;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int applicationsIndexingFailures;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int applicationsIndexingTotalSteps;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int applicationsIndexingCurrentStep;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int salesNotesIndexed;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int salesNotesIndexingFailures;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int salesNotesIndexingTotalSteps;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int salesNotesIndexingCurrentStep;

        @JsonInclude(ALWAYS)
        private int changelogSourcesIndexed;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int changelogSourcesIndexingFailures;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int changelogSourcesIndexingTotalSteps;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int changelogSourcesIndexingCurrentStep;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private boolean indexParts;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private boolean indexApplications;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private boolean indexSalesNotes;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private boolean indexChangelogSources;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private boolean recreateIndex;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private Long startedOn;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private Long finishedOn;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private Long userId;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private String userName;

        public IndexingStatus() {
            reset();
        }

        public void reset() {
            this.phase = PHASE_NONE;
            this.errorMessage = null;
            this.partsIndexed = 0;
            this.partsIndexingFailures = 0;
            this.partsIndexingTotalSteps = 0;
            this.partsIndexingCurrentStep = 0;
            this.applicationsIndexed = 0;
            this.applicationsIndexingFailures = 0;
            this.applicationsIndexingTotalSteps = 0;
            this.applicationsIndexingCurrentStep = 0;
            this.salesNotesIndexed = 0;
            this.salesNotesIndexingFailures = 0;
            this.salesNotesIndexingTotalSteps = 0;
            this.salesNotesIndexingCurrentStep = 0;
            this.changelogSourcesIndexed = 0;
            this.changelogSourcesIndexingFailures = 0;
            this.changelogSourcesIndexingTotalSteps = 0;
            this.changelogSourcesIndexingCurrentStep = 0;
            this.indexParts = true;
            this.indexApplications = true;
            this.indexSalesNotes = true;
            this.indexChangelogSources = true;
            this.recreateIndex = true;
            this.startedOn = null;
            this.finishedOn = null;
            this.userId = null;
            this.userName = null;
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

        public boolean isIndexChangelogSources() {
            return indexChangelogSources;
        }

        public void setIndexChangelogSources(boolean indexChangelogSources) {
            this.indexChangelogSources = indexChangelogSources;
        }

        public int getChangelogSourcesIndexed() {
            return changelogSourcesIndexed;
        }

        public void setChangelogSourcesIndexed(int changelogSourcesIndexed) {
            this.changelogSourcesIndexed = changelogSourcesIndexed;
        }

        public int getChangelogSourcesIndexingFailures() {
            return changelogSourcesIndexingFailures;
        }

        public void setChangelogSourcesIndexingFailures(int changelogSourcesIndexingFailures) {
            this.changelogSourcesIndexingFailures = changelogSourcesIndexingFailures;
        }

        public int getChangelogSourcesIndexingTotalSteps() {
            return changelogSourcesIndexingTotalSteps;
        }

        public void setChangelogSourcesIndexingTotalSteps(int changelogSourcesIndexingTotalSteps) {
            this.changelogSourcesIndexingTotalSteps = changelogSourcesIndexingTotalSteps;
        }

        public int getChangelogSourcesIndexingCurrentStep() {
            return changelogSourcesIndexingCurrentStep;
        }

        public void setChangelogSourcesIndexingCurrentStep(int changelogSourcesIndexingCurrentStep) {
            this.changelogSourcesIndexingCurrentStep = changelogSourcesIndexingCurrentStep;
        }

        public Long getStartedOn() {
            return startedOn;
        }

        public void setStartedOn(Long startedOn) {
            this.startedOn = startedOn;
        }

        public Long getFinishedOn() {
            return finishedOn;
        }

        public void setFinishedOn(Long finishedOn) {
            this.finishedOn = finishedOn;
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

        public boolean isRecreateIndex() {
            return recreateIndex;
        }

        public void setRecreateIndex(boolean recreateIndex) {
            this.recreateIndex = recreateIndex;
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
            retVal.changelogSourcesIndexed = changelogSourcesIndexed;
            retVal.changelogSourcesIndexingFailures = changelogSourcesIndexingFailures;
            retVal.changelogSourcesIndexingTotalSteps = changelogSourcesIndexingTotalSteps;
            retVal.changelogSourcesIndexingCurrentStep = changelogSourcesIndexingCurrentStep;
            retVal.indexParts = indexParts;
            retVal.recreateIndex = recreateIndex;
            retVal.indexApplications = indexApplications;
            retVal.indexSalesNotes = indexSalesNotes;
            retVal.indexChangelogSources = indexChangelogSources;
            retVal.startedOn = startedOn;
            retVal.finishedOn = finishedOn;
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
                    ", changelogSourcesIndexed=" + changelogSourcesIndexed +
                    ", changelogSourcesIndexingFailures=" + changelogSourcesIndexingFailures +
                    ", changelogSourcesIndexingTotalSteps =" + changelogSourcesIndexingTotalSteps +
                    ", changelogSourcesIndexingCurrentStep=" + changelogSourcesIndexingCurrentStep +
                    ", indexParts=" + indexParts +
                    ", indexApplications=" + indexApplications +
                    ", indexSalesNotes=" + indexSalesNotes +
                    ", indexChangelogSources=" + indexChangelogSources +
                    ", recreateIndex=" + recreateIndex +
                    ", startedOn=" + startedOn +
                    ", finishedOn=" + finishedOn +
                    ", userId=" + userId +
                    ", userName='" + userName + '\'' +
                    '}';
        }

    }


    SearchService.IndexingStatus startIndexing(User user, boolean indexParts, boolean indexApplications,
                                               boolean indexSalesNotes, boolean indexChangelogSources,
                                               boolean recreateIndex) throws Exception;

    SearchService.IndexingStatus getIndexingStatus() throws Exception;

    void createIndex() throws IOException;

    void indexPart(long id);

    void indexPart(Part part);

    void deletePart(Part part) throws Exception;

    void indexCarEngine(CarEngine carEngine);

    void deleteCarEngine(CarEngine carEngine) throws Exception;

    void indexCarFuelType(CarFuelType carFuelType);

    void deleteCarFuelType(CarFuelType carFuelType) throws Exception;

    void indexCarYear(CarYear carYear);

    void deleteCarYear(CarYear carYear) throws Exception;

    void indexCarMake(CarMake carMake);

    void deleteCarMake(CarMake carMake) throws Exception;

    void indexCarModel(CarModel carModel);

    void deleteCarModel(CarModel carModel) throws Exception;

    void indexCarModelEngineYear(CarModelEngineYear carModelEngineYear);

    void deleteCarModelEngineYear(CarModelEngineYear carModelEngineYear) throws Exception;

    void indexSalesNotePart(SalesNotePart salesNotePart);

    void deleteSalesNotePart(SalesNotePart salesNotePart) throws Exception;

    void indexChangelogSource(Source source);

    void deleteChangelogSource(Source source) throws Exception;

    String filterParts(String partNumber, Long partTypeId, String manufacturerName,
                       String name, String description, Boolean inactive,
                       String turboTypeName, String turboModelName,
                       String year, String make, String model, String engine, String fuelType,
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

    String filterChanglelogSources(String name, String descritpion, String url, Long sourceNameId, String sortProperty,
                                   String sortOrder, Integer offset, Integer limit);
}
