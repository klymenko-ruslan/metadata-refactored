package com.turbointernational.metadata.domain.part.types;

import com.turbointernational.metadata.domain.AbstractDao;
import com.turbointernational.metadata.domain.car.CarModelEngineYear;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.sql.DataSource;
import java.util.List;

/**
 * Created by trunikov on 12/4/15.
 */
@Repository
public class TurboCarModelEngineYearDao extends AbstractDao<TurboCarModelEngineYear> {

    private static final Logger log = LoggerFactory.getLogger(TurboCarModelEngineYearDao.class);

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

    public TurboCarModelEngineYearDao() {
        super(TurboCarModelEngineYear.class);
    }

    public List<TurboCarModelEngineYear> getPartLinkedApplications(Long partId) {
        return em.createNamedQuery("partLinkedApplications", TurboCarModelEngineYear.class).
                setParameter("partId", partId).
                getResultList();
    }

    public List<PLARrec> getPartLinkedApplicationsRecursion(Long partId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<PLARrec> retVal = jdbcTemplate.query(
            "SELECT DISTINCT " +
            "   cy.name AS year, cmd.name AS model, cmk.name AS make, " +
            "   ce.engine_size AS engine, cft.name AS fuel " +
            "FROM " +
            "   turbo_car_model_engine_year AS tcmey" +
            "   JOIN car_model_engine_year AS cmey ON tcmey.car_model_engine_year_id = cmey.id " +
            "   RIGHT JOIN car_year AS cy ON cmey.car_year_id = cy.id " +
            "   RIGHT JOIN car_model AS cmd ON cmd.id = cmey.car_model_id " +
            "       JOIN car_make AS cmk ON cmd.car_make_id = cmk.id " +
            "   RIGHT JOIN car_engine AS ce ON ce.id = cmey.car_engine_id " +
            "       JOIN car_fuel_type AS cft ON ce.car_fuel_type_id = cft.id " +
            "WHERE tcmey.part_id = ? OR tcmey.part_id IN( " +
            "   SELECT DISTINCT b2.child_part_id " +
            "   FROM bom as b " +
            "       JOIN bom_descendant AS bd ON b.id = bd.part_bom_id " +
            "       JOIN bom AS b2 ON bd.descendant_bom_id = b2.id " +
            "   WHERE b.parent_part_id=?" +
            ")",
            ps -> {
                ps.setLong(1, partId);
                ps.setLong(2, partId);
            },
            (rs, rowNum) -> {
                String year = rs.getString(1);
                String model = rs.getString(2);
                String make = rs.getString(3);
                String engine = rs.getString(4);
                String fuel = rs.getString(5);
                return new PLARrec(year, model, make, engine, fuel);
            }
        );
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
