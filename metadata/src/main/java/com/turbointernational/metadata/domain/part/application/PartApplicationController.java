package com.turbointernational.metadata.domain.part.application;

import com.turbointernational.metadata.domain.car.CarModelEngineYear;
import flexjson.JSONSerializer;
import flexjson.transformer.HibernateTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@RequestMapping("/metadata/part")
@Controller
public class PartApplicationController {

    private static final Logger log = Logger.getLogger(PartApplicationController.class.toString());

    @Autowired(required=true)
    JdbcTemplate db;

    @Transactional
    @RequestMapping(value = "{partId}/application", method = RequestMethod.GET)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public ResponseEntity<String> getApplications(@PathVariable("partId") int partId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Long> ids = db.query(
                "select cmey.*" +
                "from car_model_engine_year as cmey " +
                "join turbo_car_model_engine_year as tcmey on cmey.id=tcmey.car_model_engine_year_id " +
                "join part as p on tcmey.part_id=p.id " +
                "where p.id=?",
            new RowMapper<Long>() {
                @Override
                public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Long id = rs.getLong(1);
                    return rs.wasNull() ? null : id;
                }
            }, partId);
        List<CarModelEngineYear> applications = null;
        if (ids != null && !ids.isEmpty()) {
            applications = CarModelEngineYear.findApplications(ids);
        }
        if (applications == null) {
            applications = Collections.<CarModelEngineYear>emptyList();
        }
        String json = new JSONSerializer()
                .transform(new HibernateTransformer(), CarModelEngineYear.class)
                .include("id")
                .include("year.name")
                .include("model.name")
                .include("model.make.name")
                .include("engine.engineSize")
                .include("engine.fuelType.name")
                .exclude("*.class")
                .serialize(applications);
        return new ResponseEntity<String>(json, headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/{partId}/application/{cmeyId}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public ResponseEntity<String> delete(@PathVariable("partId") Long partId, @PathVariable("cmeyId") Long cmeyId)
            throws Exception {
        log.info("Deleted application: " + partId + ", " + cmeyId);
        return new ResponseEntity<String>("", HttpStatus.OK);
    }
}
