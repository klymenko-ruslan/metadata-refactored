package com.turbointernational.metadata.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.turbointernational.metadata.entity.CarModelEngineYear;
import com.turbointernational.metadata.entity.part.TurboCarModelEngineYear;
import com.turbointernational.metadata.entity.part.types.Turbo;
import com.turbointernational.metadata.service.GraphDbService;
import com.turbointernational.metadata.service.GraphDbService.GetAncestorsResponse;
import com.turbointernational.metadata.service.GraphDbService.GetAncestorsResponse.Row;

/**
 * Created by trunikov on 12/4/15.
 */
@Repository
public class TurboCarModelEngineYearDao extends AbstractDao<TurboCarModelEngineYear> {

    public static class PLARrec {

        private final String year;

        private final String model;

        private final String make;

        private final String engine;

        private final String fuel;

        public PLARrec(String year, String model, String make, String engine, String fuel) {
            this.year = year;
            this.model = model;
            this.make = make;
            this.engine = engine;
            this.fuel = fuel;
        }

        public String getYear() {
            return year;
        }

        public String getModel() {
            return model;
        }

        public String getMake() {
            return make;
        }

        public String getEngine() {
            return engine;
        }

        public String getFuel() {
            return fuel;
        }

    }

    @Autowired
    private GraphDbService graphDbService;

    @Autowired
    private DataSource dataSource = null;

    private NamedParameterJdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public TurboCarModelEngineYearDao() {
        super(TurboCarModelEngineYear.class);
    }

    public List<TurboCarModelEngineYear> getPartLinkedApplications(Long partId) {
        return em.createNamedQuery("partLinkedApplications", TurboCarModelEngineYear.class)
                .setParameter("partId", partId).getResultList();
    }

    //@formatter:off
    public List<PLARrec> getPartLinkedApplicationsRecursion(Long partId) {
        GetAncestorsResponse ancestors = graphDbService.getAncestors(partId);
        Row[] rows = ancestors.getRows();
        if (rows.length == 0) {
            return new ArrayList<>(0);
        }
        Set<Long> ancestorsIds = Arrays.stream(rows).map(row -> row.getPartId())
                .collect(Collectors.toSet());
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("partId", partId);
        parameters.addValue("ancestorsIds", ancestorsIds);
        List<PLARrec> retVal = jdbcTemplate.query(
            "SELECT DISTINCT " +
            "   cy.name AS year, cmd.name AS model, cmk.name AS make, " +
            "   ce.engine_size AS engine, cft.name AS fuel " +
            "FROM " +
            "   turbo_car_model_engine_year AS tcmey" +
            "   JOIN car_model_engine_year AS cmey ON tcmey.car_model_engine_year_id = cmey.id " +
            "   LEFT JOIN car_year AS cy ON cmey.car_year_id = cy.id " +
            "   LEFT JOIN car_model AS cmd ON cmd.id = cmey.car_model_id " +
            "       LEFT JOIN car_make AS cmk ON cmd.car_make_id = cmk.id " +
            "   LEFT JOIN car_engine AS ce ON ce.id = cmey.car_engine_id " +
            "       LEFT JOIN car_fuel_type AS cft ON ce.car_fuel_type_id = cft.id " +
            "WHERE tcmey.part_id = :partId OR tcmey.part_id IN(:ancestorsIds)",
            parameters,
            (rs, idx) -> {
                String year = rs.getString(1);
                String model = rs.getString(2);
                String make = rs.getString(3);
                String engine = rs.getString(4);
                String fuel = rs.getString(5);
                return new PLARrec(year, model, make, engine, fuel);
            });
        return retVal;
    }
    //@formatter:on

    public TurboCarModelEngineYear find(Long partId, Long applicationId) {
        Turbo turbo = em.getReference(Turbo.class, partId);
        CarModelEngineYear application = em.getReference(CarModelEngineYear.class, applicationId);
        TurboCarModelEngineYear partApplication = new TurboCarModelEngineYear();
        partApplication.setCarModelEngineYear(application);
        partApplication.setTurbo(turbo);
        return em.find(TurboCarModelEngineYear.class, partApplication);
    }

    public void add(Long partId, Long applicationId) {
        Turbo turbo = em.getReference(Turbo.class, partId);
        CarModelEngineYear application = em.getReference(CarModelEngineYear.class, applicationId);
        TurboCarModelEngineYear partApplication = new TurboCarModelEngineYear();
        partApplication.setTurbo(turbo);
        partApplication.setCarModelEngineYear(application);
        em.persist(partApplication);
    }

    public int delete(Long partId, Long applicationId) {
        final Query delQuery = em.createNamedQuery("delelePartApplication");
        delQuery.setParameter("partId", partId);
        delQuery.setParameter("applicationId", applicationId);
        int deleted = delQuery.executeUpdate();
        return deleted;
    }

}
