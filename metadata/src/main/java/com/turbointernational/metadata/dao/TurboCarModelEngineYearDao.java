package com.turbointernational.metadata.dao;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.turbointernational.metadata.entity.CarModelEngineYear;
import com.turbointernational.metadata.entity.part.TurboCarModelEngineYear;
import com.turbointernational.metadata.entity.part.types.Turbo;

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
    private DataSource dataSource = null;

    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public TurboCarModelEngineYearDao() {
        super(TurboCarModelEngineYear.class);
    }

    public List<TurboCarModelEngineYear> getPartLinkedApplications(Long partId) {
        return em.createNamedQuery("partLinkedApplications", TurboCarModelEngineYear.class)
                .setParameter("partId", partId).getResultList();
    }

    public List<PLARrec> getPartLinkedApplicationsRecursion(Long partId) {
        List<PLARrec> retVal = jdbcTemplate.query("SELECT DISTINCT "
                + "   cy.name AS year, cmd.name AS model, cmk.name AS make, "
                + "   ce.engine_size AS engine, cft.name AS fuel " + "FROM " + "   turbo_car_model_engine_year AS tcmey"
                + "   JOIN car_model_engine_year AS cmey ON tcmey.car_model_engine_year_id = cmey.id "
                + "   LEFT JOIN car_year AS cy ON cmey.car_year_id = cy.id "
                + "   LEFT JOIN car_model AS cmd ON cmd.id = cmey.car_model_id "
                + "       LEFT JOIN car_make AS cmk ON cmd.car_make_id = cmk.id "
                + "   LEFT JOIN car_engine AS ce ON ce.id = cmey.car_engine_id "
                + "       LEFT JOIN car_fuel_type AS cft ON ce.car_fuel_type_id = cft.id "
                + "WHERE tcmey.part_id = ? OR tcmey.part_id IN( " + "   SELECT DISTINCT b2.child_part_id "
                + "   FROM bom as b " + "       JOIN bom_descendant AS bd ON b.id = bd.part_bom_id "
                + "       JOIN bom AS b2 ON bd.descendant_bom_id = b2.id " + "   WHERE b.parent_part_id=?" + ")",
                ps -> {
                    ps.setLong(1, partId);
                    ps.setLong(2, partId);
                }, (rs, rowNum) -> {
                    String year = rs.getString(1);
                    String model = rs.getString(2);
                    String make = rs.getString(3);
                    String engine = rs.getString(4);
                    String fuel = rs.getString(5);
                    return new PLARrec(year, model, make, engine, fuel);
                });
        return retVal;
    }

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
