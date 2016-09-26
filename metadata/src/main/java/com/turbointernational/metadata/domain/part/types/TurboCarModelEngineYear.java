package com.turbointernational.metadata.domain.part.types;

import com.turbointernational.metadata.domain.car.CarModelEngineYear;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.FetchType.LAZY;

@Cacheable
@Entity
@Table(name="turbo_car_model_engine_year")
@NamedQueries({
        @NamedQuery(
                name = "partLinkedApplications",
                query = "SELECT tcmey FROM TurboCarModelEngineYear AS tcmey WHERE tcmey.turbo.id=:partId"
        ),
        @NamedQuery(
                name = "delelePartApplication",
                query = "DELETE FROM TurboCarModelEngineYear AS tcmey " +
                        "WHERE tcmey.turbo.id=:partId " +
                        "AND tcmey.carModelEngineYear.id=:applicationId"
        )
})
public class TurboCarModelEngineYear implements Serializable {

    @Id
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="part_id", nullable = false)
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="part_id", nullable = false)
    private Turbo turbo;

    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="car_model_engine_year_id", nullable = false)
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
        if (this == o) return true;
        if (!(o instanceof TurboCarModelEngineYear)) return false;

        TurboCarModelEngineYear that = (TurboCarModelEngineYear) o;

        if (!turbo.equals(that.turbo)) return false;
        if (!carModelEngineYear.equals(that.carModelEngineYear)) return false;
        if (importPk != null ? !importPk.equals(that.importPk) : that.importPk != null) return false;
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
