package com.turbointernational.metadata.domain.part.types;

import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.*;
import java.io.Serializable;

@Cacheable
@Configurable
@Entity
@Table(name="turbo_car_model_engine_year")
public class TurboCarModelEngineYear implements Serializable {

    @Id
    @Column(name = "part_id", nullable = false)
    private Long partId;

    @Id
    @Column(name = "car_model_engine_year_id", nullable = false)
    private Long carModelEngineYearId;

    @Column(name = "import_pk", nullable = true)
    private Long importPk;

    public TurboCarModelEngineYear() {
    }

    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }

    public Long getImportPk() {
        return importPk;
    }

    public void setImportPk(Long importPk) {
        this.importPk = importPk;
    }

    public Long getCarModelEngineYearId() {
        return carModelEngineYearId;
    }

    public void setCarModelEngineYearId(Long carModelEngineYearId) {
        this.carModelEngineYearId = carModelEngineYearId;
    }

}
