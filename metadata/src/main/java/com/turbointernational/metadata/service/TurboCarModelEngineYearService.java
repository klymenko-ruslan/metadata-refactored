package com.turbointernational.metadata.service;

import com.turbointernational.metadata.dao.TurboCarModelEngineYearDao;
import com.turbointernational.metadata.entity.part.types.TurboCarModelEngineYear;
import flexjson.JSONSerializer;
import flexjson.transformer.HibernateTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.APPLICATIONS;

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
                changelogService.log(APPLICATIONS, "Part [" + partId + "] has been associated with the application ["
                        + cmeyId + "].");
            }
        }
    }

    @Transactional
    public void delete(Long partId, Long applicationId) throws Exception {
        turboCarModelEngineYearDao.delete(partId, applicationId);
        changelogService.log(APPLICATIONS, "Deleted association between part [" + partId + "] and an application ["
                + applicationId + "].");
    }

}
