package com.turbointernational.metadata.domain.part.types;

import com.turbointernational.metadata.domain.car.CarModelEngineYear;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Cacheable
@Configurable
@Entity
@Table(name="turbo_car_model_engine_year")
@NamedQueries(
        @NamedQuery(
                name = "partLinkedApplications",
                query = "SELECT tcmey FROM TurboCarModelEngineYear AS tcmey WHERE tcmey.turbo.id=:partId"
        )
)
public class TurboCarModelEngineYear implements Serializable {

    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="part_id", nullable = false)
    private Turbo turbo;

    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="car_model_engine_year_id", nullable = false)
    private CarModelEngineYear carModelEngineYear;

    @Column(name = "import_pk", nullable = true)
    private Long importPk;

    @PersistenceContext
    @Transient
    private EntityManager entityManager;

    public static final EntityManager entityManager() {
        EntityManager em = new TurboCarModelEngineYear().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected " +
                "(is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

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

    public static void delete(Long partId, Long applicationId) {
        TurboCarModelEngineYear partApplication = new TurboCarModelEngineYear();
        Turbo turbo = new Turbo();
        turbo.setId(partId);
        CarModelEngineYear application = new CarModelEngineYear();
        application.setId(applicationId);
        partApplication.setTurbo(turbo);
        partApplication.setCarModelEngineYear(application);
        entityManager().remove(partApplication);
    }

    public static List<TurboCarModelEngineYear> getPartLinkedApplications(Long partId) {
        return TurboCarModelEngineYear.entityManager().
                createNamedQuery("partLinkedApplications", TurboCarModelEngineYear.class).
                setParameter("partId", partId).
                getResultList();
    }

}
