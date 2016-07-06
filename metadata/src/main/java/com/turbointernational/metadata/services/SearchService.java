package com.turbointernational.metadata.services;

import com.turbointernational.metadata.Application;
import com.turbointernational.metadata.domain.car.*;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.salesnote.SalesNotePart;
import com.turbointernational.metadata.domain.part.salesnote.SalesNoteState;

import java.util.Map;
import java.util.Set;

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

    class IndexingStatus {

    }

    SearchService.IndexingStatus startIndexing(boolean indexParts, boolean indexApplications,
                                               boolean indexSalesNotes) throws Exception;

    SearchService.IndexingStatus getIndexingState() throws Exception;

    void indexPart(long id);

    void indexPart(Part part);

    void deletePart(Part part) throws Exception;

    void indexAllParts() throws Exception;

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

    void indexAllApplications() throws Exception;

    void indexSalesNotePart(SalesNotePart salesNotePart);

    void deleteSalesNotePart(SalesNotePart salesNotePart) throws Exception;

    void indexAllSalesNotes() throws Exception;

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
