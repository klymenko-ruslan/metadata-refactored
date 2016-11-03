package com.turbointernational.metadata.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.dao.CarModelEngineYearDao;
import com.turbointernational.metadata.entity.*;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.entity.part.types.Turbo;
import com.turbointernational.metadata.entity.part.types.TurboCarModelEngineYear;
import com.turbointernational.metadata.dao.TurboCarModelEngineYearDao;
import com.turbointernational.metadata.service.OtherService.GenerateApplicationsResponse.Failure;
import com.turbointernational.metadata.util.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-09-27.
 */
@Service
public class OtherService {

    private final static Long PART_TYPE_ID_TURBO = 1L;

    @Autowired
    private PartDao partDao;

    @Autowired
    private CarModelEngineYearDao cmeyDao;

    @Autowired
    private TurboCarModelEngineYearDao tcmeyDao;


    public static class GenerateApplicationsRequest {

        @JsonView({View.Summary.class})
        private Long[] partIds;

        @JsonView({View.Summary.class})
        private Long[] appIds;


        public Long[] getPartIds() {
            return partIds;
        }

        public void setPartIds(Long[] partIds) {
            this.partIds = partIds;
        }

        public Long[] getAppIds() {
            return appIds;
        }

        public void setAppIds(Long[] appIds) {
            this.appIds = appIds;
        }
    }

    @JsonInclude(ALWAYS)
    public static class GenerateApplicationsResponse {

        @JsonInclude(ALWAYS)
        public static class Failure {

            @JsonView({View.Summary.class})
            private final Long partId;

            @JsonView({View.Summary.class})
            private final Long appId;

            @JsonView({View.Summary.class})
            private final String partType;

            @JsonView({View.Summary.class})
            private final String manufacturer;

            @JsonView({View.Summary.class})
            private final String manufacturerPartNumber;

            @JsonView({View.Summary.class})
            private final String year;

            @JsonView({View.Summary.class})
            private final String make;

            @JsonView({View.Summary.class})
            private final String model;

            @JsonView({View.Summary.class})
            private final String engine;

            @JsonView({View.Summary.class})
            private final String fuelType;

            @JsonView({View.Summary.class})
            private final String errorMessage;

            public Failure(Long partId, Long appId, String partType, String manufacturer,
                           String manufacturerPartNumber, String year, String make, String model,
                           String engine, String fuelType, String errorMessage) {
                this.partId = partId;
                this.appId = appId;
                this.partType = partType;
                this.manufacturer = manufacturer;
                this.manufacturerPartNumber = manufacturerPartNumber;
                this.year = year;
                this.make = make;
                this.model = model;
                this.engine = engine;
                this.fuelType = fuelType;
                this.errorMessage = errorMessage;
            }

            public Long getPartId() {
                return partId;
            }

            public Long getAppId() {
                return appId;
            }

            public String getPartType() {
                return partType;
            }

            public String getManufacturer() {
                return manufacturer;
            }

            public String getManufacturerPartNumber() {
                return manufacturerPartNumber;
            }

            public String getYear() {
                return year;
            }

            public String getMake() {
                return make;
            }

            public String getModel() {
                return model;
            }

            public String getEngine() {
                return engine;
            }

            public String getFuelType() {
                return fuelType;
            }

            public String getErrorMessage() {
                return errorMessage;
            }

        }

        @JsonView({View.Summary.class})
        private int generated;

        @JsonView({View.Summary.class})
        private List<Failure> failures;

        public GenerateApplicationsResponse(int generated, List<Failure> failures) {
            this.generated = generated;
            this.failures = failures;
        }

        public int getGenerated() {
            return generated;
        }

        public void setGenerated(int generated) {
            this.generated = generated;
        }

        public List<Failure> getFailures() {
            return failures;
        }

        public void setFailures(List<Failure> failures) {
            this.failures = failures;
        }

    }

    @Transactional
    public GenerateApplicationsResponse generateTurboApplications(GenerateApplicationsRequest request) {
        int generated = 0;
        List<Failure> failures = new ArrayList<>();
        for(Long partId: request.getPartIds()) {
            Part part = partDao.findOne(partId);
            Turbo turbo = null;
            TurboCarModelEngineYear tcmey = null;
            boolean partTypeMismatch = false;
            if (part.getPartType().getId() == PART_TYPE_ID_TURBO) {
                turbo = (Turbo) part;
            } else {
                partTypeMismatch = true;
            }
            for(Long appId: request.getAppIds()) {
                CarModelEngineYear cmey = cmeyDao.findOne(appId);
                String errorMessage = null;
                if (partTypeMismatch) {
                    errorMessage = "Expected part type 'Turbo'.";
                } else {
                    tcmey = tcmeyDao.find(partId, appId);
                    if (tcmey == null) { // Not found.
                        tcmey = new TurboCarModelEngineYear();
                        tcmey.setTurbo(turbo);
                        tcmey.setCarModelEngineYear(cmey);
                        tcmeyDao.persist(tcmey);
                        generated++;
                    } else {
                        errorMessage = "This association already exists.";
                    }
                }
                if (errorMessage != null) {
                    String partTypeName = part.getPartType().getName();
                    String manufacturerName = part.getManufacturer().getName();
                    String manufacturerPartNumber = part.getManufacturerPartNumber();
                    String yearName = null;
                    CarYear carYear = cmey.getYear();
                    if (carYear != null) {
                        yearName = carYear.getName();
                    }
                    String makeName = null;
                    String modelName = null;
                    CarModel carModel = cmey.getModel();
                    if (carModel != null) {
                        modelName = carModel.getName();
                        CarMake carMake = carModel.getMake();
                        if (carMake != null) {
                            makeName = carMake.getName();
                        }
                    }
                    String engineName = null;
                    String fuelTypeName = null;
                    CarEngine carEngine = cmey.getEngine();
                    if (carEngine != null) {
                        engineName = carEngine.getEngineSize();
                        CarFuelType carFuelType = carEngine.getFuelType();
                        if (carFuelType != null) {
                            fuelTypeName = carFuelType.getName();
                        }
                    }
                    Failure failure = new Failure(partId, appId, partTypeName, manufacturerName,
                            manufacturerPartNumber, yearName, makeName, modelName, engineName,
                            fuelTypeName, errorMessage);
                    failures.add(failure);
                }
            }
        }
        return new GenerateApplicationsResponse(generated, failures);
    }


}
