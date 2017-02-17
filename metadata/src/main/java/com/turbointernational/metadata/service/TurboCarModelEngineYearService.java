package com.turbointernational.metadata.service;

import com.turbointernational.metadata.dao.TurboCarModelEngineYearDao;
import com.turbointernational.metadata.entity.part.TurboCarModelEngineYear;
import com.turbointernational.metadata.service.ChangelogService.RelatedPart;
import flexjson.JSONSerializer;
import flexjson.transformer.HibernateTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void add(Long partId, Long[] cmeyIds) throws Exception {
        if (cmeyIds != null) {
            for (Long cmeyId : cmeyIds) {
                turboCarModelEngineYearDao.add(partId, cmeyId);
                List<RelatedPart> relatedParts = new ArrayList<>(1);
                relatedParts.add(new RelatedPart(partId, PART0));
                changelogService.log(APPLICATIONS, "Part [" + partId + "] has been associated with the application ["
                        + cmeyId + "].", relatedParts);
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
