package com.turbointernational.metadata.entity.part;

import static javax.persistence.FetchType.LAZY;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.turbointernational.metadata.entity.CarModelEngineYear;
import com.turbointernational.metadata.entity.part.types.Turbo;

@Cacheable
@Entity
@Table(name = "turbo_car_model_engine_year")
@NamedQueries({
        @NamedQuery(name = "partLinkedApplications", query = "SELECT tcmey FROM TurboCarModelEngineYear AS tcmey WHERE tcmey.turbo.id=:partId"),
        @NamedQuery(name = "delelePartApplication", query = "DELETE FROM TurboCarModelEngineYear AS tcmey "
                + "WHERE tcmey.turbo.id=:partId " + "AND tcmey.carModelEngineYear.id=:applicationId") })
@NamedNativeQueries({ @NamedNativeQuery(name = "partLinkedApplicationsRecursion", query = "SELECT DISTINCT "
        + "   cy.name AS year, cmd.name AS model, cmk.name AS make, " + "   ce.engine_size AS engine, cft.name AS fuel "
        + "FROM " + "   turbo_car_model_engine_year AS tcmey"
        + "   JOIN car_model_engine_year AS cmey ON tcmey.car_model_engine_year_id = cmey.id "
        + "   RIGHT JOIN car_year AS cy ON cmey.car_year_id = cy.id "
        + "   RIGHT JOIN car_model AS cmd ON cmd.id = cmey.car_model_id "
        + "       JOIN car_make AS cmk ON cmd.car_make_id = cmk.id "
        + "   RIGHT JOIN car_engine AS ce ON ce.id = cmey.car_engine_id "
        + "       JOIN car_fuel_type AS cft ON ce.car_fuel_type_id = cft.id "
        + "WHERE tcmey.part_id = :partId OR tcmey.part_id IN( " + "   SELECT DISTINCT b2.child_part_id "
        + "   FROM bom as b " + "       JOIN bom_descendant AS bd ON b.id = bd.part_bom_id "
        + "       JOIN bom AS b2 ON bd.descendant_bom_id = b2.id " + "   WHERE b.parent_part_id=:partId") })
public class TurboCarModelEngineYear implements Serializable {

    private static final long serialVersionUID = -8112317267665924429L;

    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "part_id", nullable = false)
    private Turbo turbo;

    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "car_model_engine_year_id", nullable = false)
    private CarModelEngineYear carModelEngineYear;

    @Column(name = "import_pk", nullable = true)
    private Long importPk;

    public TurboCarModelEngineYear() {
    }

    public Turbo getTurbo() {
        return turbo;
    }

    public void setTurbo(Turbo turbo) {
        this.turbo = turbo;
    }

    public CarModelEngineYear getCarModelEngineYear() {
        return carModelEngineYear;
    }

    public void setCarModelEngineYear(CarModelEngineYear carModelEngineYear) {
        this.carModelEngineYear = carModelEngineYear;
    }

    public Long getImportPk() {
        return importPk;
    }

    public void setImportPk(Long importPk) {
        this.importPk = importPk;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof TurboCarModelEngineYear))
            return false;

        TurboCarModelEngineYear that = (TurboCarModelEngineYear) o;

        if (!turbo.equals(that.turbo))
            return false;
        if (!carModelEngineYear.equals(that.carModelEngineYear))
            return false;
        if (importPk != null ? !importPk.equals(that.importPk) : that.importPk != null)
            return false;
        return true;

    }

    @Override
    public int hashCode() {
        int result = turbo.hashCode();
        result = 31 * result + carModelEngineYear.hashCode();
        result = 31 * result + (importPk != null ? importPk.hashCode() : 0);
        return result;
    }

}
