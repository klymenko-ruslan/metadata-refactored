package com.turbointernational.metadata.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-09-26.
 */
@Controller
@RequestMapping(value = {"/other", "/metadata/other"})
public class OtherController {

    static class GenerateApplicationsRequest {

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
    static class GenerateApplicationsResponse {

        @JsonInclude(ALWAYS)
        static class Failure {

            @JsonView({View.Summary.class})
            private String partType;

            @JsonView({View.Summary.class})
            private String manufacturer;

            @JsonView({View.Summary.class})
            private String manufacturerPartNumber;

            @JsonView({View.Summary.class})
            private Integer year;

            @JsonView({View.Summary.class})
            private String make;

            @JsonView({View.Summary.class})
            private String model;

            @JsonView({View.Summary.class})
            private String engine;

            @JsonView({View.Summary.class})
            private String fuelType;

            @JsonView({View.Summary.class})
            private String errorMessage;

            public Failure() {
            }

            public Failure(String partType, String manufacturer, String manufacturerPartNumber,
                           Integer year, String make, String model, String engine, String fuelType,
                           String errorMessage) {
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

            public String getPartType() {
                return partType;
            }

            public void setPartType(String partType) {
                this.partType = partType;
            }

            public String getManufacturer() {
                return manufacturer;
            }

            public void setManufacturer(String manufacturer) {
                this.manufacturer = manufacturer;
            }

            public String getManufacturerPartNumber() {
                return manufacturerPartNumber;
            }

            public void setManufacturerPartNumber(String manufacturerPartNumber) {
                this.manufacturerPartNumber = manufacturerPartNumber;
            }

            public Integer getYear() {
                return year;
            }

            public void setYear(Integer year) {
                this.year = year;
            }

            public String getMake() {
                return make;
            }

            public void setMake(String make) {
                this.make = make;
            }

            public String getModel() {
                return model;
            }

            public void setModel(String model) {
                this.model = model;
            }

            public String getEngine() {
                return engine;
            }

            public void setEngine(String engine) {
                this.engine = engine;
            }

            public String getFuelType() {
                return fuelType;
            }

            public void setFuelType(String fuelType) {
                this.fuelType = fuelType;
            }

            public String getErrorMessage() {
                return errorMessage;
            }

            public void setErrorMessage(String errorMessage) {
                this.errorMessage = errorMessage;
            }
        }

        @JsonView({View.Summary.class})
        private int generated;

        @JsonView({View.Summary.class})
        private List<Failure> failures;

        GenerateApplicationsResponse(int generated, List<Failure> failures) {
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
    @RequestMapping(value = "/appsturbos/generate", method = POST,
            consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_APPLICATION_CRUD")
    public GenerateApplicationsResponse generateApplications(@RequestBody GenerateApplicationsRequest request) {
        int generated = 10;
        List<GenerateApplicationsResponse.Failure> failures = new ArrayList<>();
        // TODO
        return new GenerateApplicationsResponse(generated, failures);
    }

}
