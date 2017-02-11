package com.turbointernational.metadata.service;

import com.turbointernational.metadata.entity.*;
import com.turbointernational.metadata.entity.chlogsrc.Source;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.entity.SalesNotePart;
import com.turbointernational.metadata.entity.SalesNoteState;
import com.turbointernational.metadata.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

/**
 * Mocked implementation of the {@link SearchService} interface for integration testing.
 *
 * Created by dmytro.trunykov@zorallabs.com on 2016-03-08.
 */
@Service
@Profile("integration")
public class SearchServiceMockImpl implements SearchService {

    private final static Logger log = LoggerFactory.getLogger(SearchServiceMockImpl.class);

    @Override
    public void createIndex() {
        log.debug("Indexing: createIndex.");
    }

    @Override
    public IndexingStatus startIndexing(User user, boolean indexParts, boolean indexApplications,
                                        boolean indexSalesNotes, boolean indexChangelogSources,
                                        boolean recreateIndex) throws Exception {
        log.debug("Indexing: Started indexing.");
        return null;
    }

    @Override
    public IndexingStatus getIndexingStatus() throws Exception {
        log.debug("Indexing: Get indexing status.");
        return null;
    }

    @Override
    public void indexPart(long id) {
        log.debug("Indexing: indexPart(long id)");
    }

    @Override
    public void indexPart(Part part) {
        log.debug("Indexing: indexPart(Part part)");
    }

    @Override
    public void deletePart(Part part) throws Exception {
        log.debug("Indexing: deletePart(Part part)");
    }

    @Override
    public void indexCarEngine(CarEngine carEngine) {
        log.debug("Indexing: indexCarEngine(CarEngine carEngine)");
    }

    @Override
    public void deleteCarEngine(CarEngine carEngine) throws Exception {
        log.debug("Indexing: deleteCarEngine(CarEngine carEngine)");

    }

    @Override
    public void indexCarFuelType(CarFuelType carFuelType) {
        log.debug("Indexing: indexCarFuelType(CarFuelType carFuelType)");

    }

    @Override
    public void deleteCarFuelType(CarFuelType carFuelType) throws Exception {
        log.debug("Indexing: deleteCarFuelType(CarFuelType carFuelType)");

    }

    @Override
    public void indexCarYear(CarYear carYear) {
        log.debug("Indexing: indexCarYear(CarYear carYear)");
    }

    @Override
    public void deleteCarYear(CarYear carYear) throws Exception {
        log.debug("Indexing: deleteCarYear(CarYear carYear)");
    }

    @Override
    public void indexCarMake(CarMake carMake) {
        log.debug("Indexing: indexCarMake(CarMake carMake)");

    }

    @Override
    public void deleteCarMake(CarMake carMake) throws Exception {
        log.debug("Indexing: deleteCarMake(CarMake carMake)");

    }

    @Override
    public void indexCarModel(CarModel carModel) {
        log.debug("Indexing: indexCarModel(CarModel carModel)");

    }

    @Override
    public void deleteCarModel(CarModel carModel) throws Exception {
        log.debug("Indexing: deleteCarModel(CarModel carModel)");

    }

    @Override
    public void indexCarModelEngineYear(CarModelEngineYear carModelEngineYear) {
        log.debug("Indexing: indexCarModelEngineYear(CarModelEngineYear carModelEngineYear)");

    }

    @Override
    public void deleteCarModelEngineYear(CarModelEngineYear carModelEngineYear) throws Exception {
        log.debug("Indexing: deleteCarModelEngineYear(CarModelEngineYear carModelEngineYear)");

    }

    @Override
    public void indexSalesNotePart(SalesNotePart salesNotePart) {
        log.debug("Indexing: indexSalesNotePart(SalesNotePart salesNotePart)");

    }

    @Override
    public void deleteSalesNotePart(SalesNotePart salesNotePart) throws Exception {
        log.debug("Indexing: deleteSalesNotePart(SalesNotePart salesNotePart)");

    }

    @Override
    public void indexChangelogSource(Source source) {
        log.debug("Indexing: indexChangelogSource(Source source)");
    }

    @Override
    public void deleteChangelogSource(Source source) throws Exception {
        log.debug("Indexing: deleteChangelogSource(Source source)");
    }

    @Override
    public String filterParts(String partNumber, Long partTypeId, String manufacturerName,
                              String name, String description, Boolean inactive,
                              String turboTypeName, String turboModelName,
                              String year, String make, String model, String engine, String fuelType,
                              Map<String, String[]> queriedCriticalDimensions,
                              String sortProperty, String sortOrder,
                              Integer offset, Integer limit) {
        return null;
    }

    @Override
    public String filterCarModelEngineYears(String carModelEngineYear, String year, String make, String model, String engine, String fuel, String sortProperty, String sortOrder, Integer offset, Integer limit) {
        return null;
    }

    @Override
    public String filterCarMakes(String carMake, String sortProperty, String sortOrder, Integer offset, Integer limit) {
        return null;
    }

    @Override
    public String filterCarModels(String carModel, String make, String sortProperty, String sortOrder, Integer offset, Integer limit) {
        return null;
    }

    @Override
    public String filterCarEngines(String carEngine, String fuelType, String sortProperty, String sortOrder, Integer offset, Integer limit) {
        return null;
    }

    @Override
    public String filterCarFuelTypes(String fuelType, String sortProperty, String sortOrder, Integer offset, Integer limit) {
        return null;
    }

    @Override
    public String filterSalesNotes(String partNumber, String comment, Long primaryPartId, Set<SalesNoteState> states, boolean includePrimary, boolean includeRelated, String sortProperty, String sortOrder, Integer offset, Integer limit) {
        return null;
    }


    @Override
    public String filterChanglelogSources(String name, String descritpion, String url, Long sourceNameId,
                                          String sortProperty, String sortOrder, Integer offset, Integer limit) {
        return null;
    }

}
