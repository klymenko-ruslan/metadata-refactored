package com.turbointernational.metadata.entity.part.types;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.Application;
import com.turbointernational.metadata.dao.TurboCarModelEngineYearDao;
import com.turbointernational.metadata.dao.TurboCarModelEngineYearDao.PLARrec;
import com.turbointernational.metadata.entity.CoolType;
import com.turbointernational.metadata.entity.CriticalDimension;
import com.turbointernational.metadata.entity.TurboModel;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.util.View;
import flexjson.JSONSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static javax.persistence.FetchType.LAZY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;


/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-08-01 17:06:08.756706.
 */
@Entity
@Table(name = "turbo")
@PrimaryKeyJoinColumn(name = "part_id")
@JsonInclude(ALWAYS)
public class Turbo extends Part {

    private final static Logger log = LoggerFactory.getLogger(Turbo.class);

    //<editor-fold defaultstate="collapsed" desc="Properties: members">

    @JsonView(View.Detail.class)
    @OneToOne(fetch = LAZY)
    @JoinColumn(name="turbo_model_id")
    private TurboModel turboModel;

    @JsonView(View.Detail.class)
    @OneToOne(fetch = LAZY)
    @JoinColumn(name="cool_type_id")
    private CoolType coolType;

    /**
     * A Gasket Kit linked with this Turbo.
     *
     * This field is declared as type of Part (not GasketKit) for workaround of an issue
     * described in the ticket #878.
     */
    @JsonView(View.Detail.class)
    @OneToOne(fetch = LAZY)
    @JoinColumn(name="gasket_kit_id")
    private Part gasketKit;

    @Transient
    @JsonView(View.Summary.class)
    private Set<String> cmeyYear = new HashSet<>();

    @Transient
    @JsonView(View.Summary.class)
    private Set<String> cmeyMake = new HashSet<>();

    @Transient
    @JsonView(View.Summary.class)
    private Set<String> cmeyModel = new HashSet<>();


    @Transient
    @JsonView(View.Summary.class)
    private Set<String> cmeyEngine = new HashSet<>();

    @Transient
    @JsonView(View.Summary.class)
    private Set<String> cmeyFuelType = new HashSet<>();

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and setters: members">

    public TurboModel getTurboModel() {
        return turboModel;
    }

    public void setTurboModel(TurboModel turboModel) {
        this.turboModel = turboModel;
    }

    public CoolType getCoolType() {
        return coolType;
    }

    public void setCoolType(CoolType coolType) {
        this.coolType = coolType;
    }

    public Set<String> getCmeyYear() {
        return cmeyYear;
    }

    public void setCmeyYear(Set<String> cmeyYear) {
        this.cmeyYear = cmeyYear;
    }

    public Set<String> getCmeyMake() {
        return cmeyMake;
    }

    public void setCmeyMake(Set<String> cmeyMake) {
        this.cmeyMake = cmeyMake;
    }

    public Set<String> getCmeyModel() {
        return cmeyModel;
    }

    public void setCmeyModel(Set<String> cmeyModel) {
        this.cmeyModel = cmeyModel;
    }

    public Set<String> getCmeyEngine() {
        return cmeyEngine;
    }

    public void setCmeyEngine(Set<String> cmeyEngine) {
        this.cmeyEngine = cmeyEngine;
    }

    public Set<String> getCmeyFuelType() {
        return cmeyFuelType;
    }

    public void setCmeyFuelType(Set<String> cmeyFuelType) {
        this.cmeyFuelType = cmeyFuelType;
    }

    public Part getGasketKit() {
        return gasketKit;
    }

    public void setGasketKit(Part gasketKit) {
        this.gasketKit = gasketKit;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Serialization: members">

    /**
     * The entity <code>Turbo</code> contains transient fields
     * <code>cmeyYear</code>, <code>cmeyModel</code>, <code>cmeyMake</code> and <code>cmeyEngine</code>.
     * These fields contain arrays of part IDs which are obtained from the table <code>bom_descendant</code>.
     *
     * This method initialize these fields in order to be indexed in the ElasticSearch index.
     * See more details in a ticket #807.
     */
    @Override
    public void beforeIndexing() {
        super.beforeIndexing();
        TurboCarModelEngineYearDao tcmeyDao = Application.getContext().getBean(TurboCarModelEngineYearDao.class);
        long t0 = System.currentTimeMillis();
        Long id = getId();
        List<PLARrec> recs = tcmeyDao.getPartLinkedApplicationsRecursion(id);
        long t1 = System.currentTimeMillis();
        //log.info("Prepare Turbo [{}] for indexing: {} records for {} millis.", id, recs.size(), t1 - t0);
        if (log.isDebugEnabled()) {
            log.debug("Prepare Turbo [{}] for indexing: {} records for {} millis.", id, recs.size(), t1 - t0);
        }
        for(PLARrec r : recs) {
            String engine = r.getEngine();
            if (isNotBlank(engine)) {
                cmeyEngine.add(engine);
            }
            String fuel = r.getFuel();
            if (isNotBlank(fuel)) {
                cmeyFuelType.add(fuel);
            }
            String make = r.getMake();
            if (isNotBlank(make)) {
                cmeyMake.add(make);
            }
            String model = r.getModel();
            if (isNotBlank(model)) {
                cmeyModel.add(model);
            }
            String year = r.getYear();
            if (isNotBlank(year)) {
                cmeyYear.add(year);
            }
        }
    }

    @Override
    protected JSONSerializer buildJsonSerializerSearch(List<CriticalDimension> criticalDimensions) {
        JSONSerializer jsonSerializer = super.buildJsonSerializerSearch(criticalDimensions);
        jsonSerializer.include("turboModel.id", "turboModel.name",
                "turboModel.turboType.id", "turboModel.turboType.name",
                "cmeyYear", "cmeyMake", "cmeyModel", "cmeyEngine", "cmeyFuelType");
        return jsonSerializer;
    }

    //</editor-fold>

}
