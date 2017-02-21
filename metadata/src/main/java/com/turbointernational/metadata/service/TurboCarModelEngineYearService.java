package com.turbointernational.metadata.service;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.dao.TurboCarModelEngineYearDao;
import com.turbointernational.metadata.entity.Changelog;
import com.turbointernational.metadata.entity.part.TurboCarModelEngineYear;
import com.turbointernational.metadata.service.ChangelogService.RelatedPart;
import com.turbointernational.metadata.util.View;
import flexjson.JSONSerializer;
import flexjson.transformer.HibernateTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.APPLICATIONS;
import static com.turbointernational.metadata.entity.ChangelogPart.Role.PART0;

/**
 * Created by dmytro.trunykov@zorallabs.com on 11/4/16.
 */
@Service
public class TurboCarModelEngineYearService {

    @Autowired
    private TurboCarModelEngineYearDao turboCarModelEngineYearDao;

    @Autowired
    private ChangelogService changelogService;

    @Autowired
    private ChangelogSourceService changelogSourceService;

    public static class AddRequest {

        @JsonView(View.Summary.class)
        private Long[] cmeyIds;

        /**
         * Changelog source IDs which should be linked to the changelog.
         * See ticket #891 for details.
         */
        @JsonView(View.Summary.class)
        private Long[] sourcesIds;

        @JsonView(View.Summary.class)
        private Integer[] chlogSrcRatings;

        @JsonView(View.Summary.class)
        private String chlogSrcLnkDescription;

        public Long[] getCmeyIds() {
            return cmeyIds;
        }

        public void setCmeyIds(Long[] cmeyIds) {
            this.cmeyIds = cmeyIds;
        }

        public Long[] getSourcesIds() {
            return sourcesIds;
        }

        public void setSourcesIds(Long[] sourcesIds) {
            this.sourcesIds = sourcesIds;
        }

        public Integer[] getChlogSrcRatings() {
            return chlogSrcRatings;
        }

        public void setChlogSrcRatings(Integer[] chlogSrcRatings) {
            this.chlogSrcRatings = chlogSrcRatings;
        }

        public String getChlogSrcLnkDescription() {
            return chlogSrcLnkDescription;
        }

        public void setChlogSrcLnkDescription(String chlogSrcLnkDescription) {
            this.chlogSrcLnkDescription = chlogSrcLnkDescription;
        }

    }

    @Transactional
    public String getApplications(Long partId) {
        List<TurboCarModelEngineYear> partLinkedApplications =
                turboCarModelEngineYearDao.getPartLinkedApplications(partId);
        String json = new JSONSerializer()
                .transform(new HibernateTransformer(), TurboCarModelEngineYear.class)
                .include("carModelEngineYear.id")
                .include("carModelEngineYear.year.name")
                .include("carModelEngineYear.model.name")
                .include("carModelEngineYear.model.make.name")
                .include("carModelEngineYear.engine.engineSize")
                .include("carModelEngineYear.engine.fuelType.name")
                .exclude("turbo")
                .exclude("*.class")
                .serialize(partLinkedApplications);
        return json;
    }

    @Transactional
    public void add(HttpServletRequest httpRequest, Long partId, AddRequest request) throws Exception {
        Long[] cmeyIds = request.getCmeyIds();
        if (cmeyIds != null) {
            for (Long cmeyId : cmeyIds) {
                turboCarModelEngineYearDao.add(partId, cmeyId);
                List<RelatedPart> relatedParts = new ArrayList<>(1);
                relatedParts.add(new RelatedPart(partId, PART0));
                Changelog changelog = changelogService.log(APPLICATIONS, "Part [" + partId
                        + "] has been associated with the application [" + cmeyId + "].", relatedParts);
                Long[] sourcesIds = request.getSourcesIds();
                Integer[] ratings = request.getChlogSrcRatings();
                String description = request.getChlogSrcLnkDescription();
                changelogSourceService.link(httpRequest, changelog, sourcesIds, ratings, description);
            }
        }
    }

    @Transactional
    public void delete(Long partId, Long applicationId) throws Exception {
        turboCarModelEngineYearDao.delete(partId, applicationId);
        List<RelatedPart> relatedParts = new ArrayList<>(1);
        relatedParts.add(new RelatedPart(partId, PART0));
        changelogService.log(APPLICATIONS, "Deleted association between part [" + partId + "] and an application ["
                + applicationId + "].", relatedParts);
    }

}
