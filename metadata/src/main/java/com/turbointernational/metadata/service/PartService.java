package com.turbointernational.metadata.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.util.View;
import flexjson.JSONSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.PART;
import static com.turbointernational.metadata.util.FormatUtils.formatPart;

/**
 * Created by dmytro.trunykov@zorallabs.com on 12/15/16.
 */
@Service
public class PartService {

    @Autowired
    private PartDao partDao;

    @Autowired
    private ChangelogService changelogService;

    @JsonInclude(ALWAYS)
    public static class PartCreateResponse {

        @JsonInclude(ALWAYS)
        static class Row {

            @JsonView({View.Summary.class})
            private final Long partId;

            @JsonView({View.Summary.class})
            private final String manufacturerPartNumber;

            @JsonView({View.Summary.class})
            private final boolean success;

            @JsonView({View.Summary.class})
            private final String errorMessage;

            Row(Long partId, String manufacturerPartNumber, boolean success, String errorMessage) {
                this.partId = partId;
                this.manufacturerPartNumber = manufacturerPartNumber;
                this.success = success;
                this.errorMessage = errorMessage;
            }

            public Long getPartId() {
                return partId;
            }

            public String getManufacturerPartNumber() {
                return manufacturerPartNumber;
            }

            public boolean isSuccess() {
                return success;
            }

            public String getErrorMessage() {
                return errorMessage;
            }

        }

        PartCreateResponse(List<Row> results) {
            this.results = results;
        }

        @JsonView({View.Summary.class})
        private List<Row> results;


        public List<Row> getResults() {
            return results;
        }

        public void setResults(List<Row> results) {
            this.results = results;
        }

    }

    @Transactional
    public PartCreateResponse createPart(Part origin, List<String> partNumbers) throws Exception {
        JSONSerializer jsonSerializer = new JSONSerializer()
                .include("id")
                .include("name")
                .include("manufacturerPartNumber")
                .include("description")
                .include("inactive")
                .include("partType.id")
                .include("partType.name")
                .exclude("partType.*")
                .include("manufacturer.id")
                .include("manufacturer.name")
                .exclude("manufacturer.*")
                .exclude("bomParentParts")
                .exclude("bom")
                .exclude("interchange")
                .exclude("turbos")
                .exclude("productImages")
                .exclude("*.class");
        Set<String> added = new HashSet<>(partNumbers.size());
        List<PartCreateResponse.Row> results = new ArrayList<>(partNumbers.size());
        for(Iterator<String> iter = partNumbers.iterator(); iter.hasNext();) {
            String mpn = iter.next();
            if (added.contains(mpn)) {
                continue; // skip duplicate
            }
            partDao.getEntityManager().detach(origin);
            origin.setId(null);
            origin.setManufacturerPartNumber(mpn);
            partDao.persist(origin);
            // Update the changelog.
            String json = jsonSerializer.serialize(origin);
            changelogService.log(PART, "Created part " + formatPart(origin) + ".", json);
            results.add(new PartCreateResponse.Row(origin.getId(), mpn, true, null));
            added.add(mpn);
        }
        return new PartCreateResponse(results);
    }

}
