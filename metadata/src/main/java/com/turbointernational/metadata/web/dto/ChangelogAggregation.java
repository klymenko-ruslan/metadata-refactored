package com.turbointernational.metadata.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class ChangelogAggregation {

    @JsonView(View.Summary.class)
    private User user;

    @JsonView(View.Summary.class)
    private Integer bom;

    @JsonView(View.Summary.class)
    private Integer interchange;

    @JsonView(View.Summary.class)
    private Integer mas90sync;

    @JsonView(View.Summary.class)
    private Integer salesnotes;

    @JsonView(View.Summary.class)
    private Integer applications;

    @JsonView(View.Summary.class)
    private Integer kit;

    @JsonView(View.Summary.class)
    private Integer part;

    @JsonView(View.Summary.class)
    private Integer turbomodel;

    @JsonView(View.Summary.class)
    private Integer turbotype;

    @JsonView(View.Summary.class)
    private Integer criticaldim;

    @JsonView(View.Summary.class)
    private Integer image;

    public ChangelogAggregation() {
        super();
    }

    public ChangelogAggregation(User user, Integer bom, Integer interchange, Integer mas90sync, Integer salesnotes,
            Integer applications, Integer kit, Integer part, Integer turbomodel, Integer turbotype, Integer criticaldim,
            Integer image) {
        this.setUser(user);
        this.setBom(bom);
        this.setInterchange(interchange);
        this.setMas90sync(mas90sync);
        this.setSalesnotes(salesnotes);
        this.setApplications(applications);
        this.setKit(kit);
        this.setPart(part);
        this.setTurbomodel(turbomodel);
        this.setTurbotype(turbotype);
        this.setCriticaldim(criticaldim);
        this.setImage(image);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getBom() {
        return bom;
    }

    public void setBom(Integer bom) {
        this.bom = bom;
    }

    public Integer getInterchange() {
        return interchange;
    }

    public void setInterchange(Integer interchange) {
        this.interchange = interchange;
    }

    public Integer getMas90sync() {
        return mas90sync;
    }

    public void setMas90sync(Integer mas90sync) {
        this.mas90sync = mas90sync;
    }

    public Integer getSalesnotes() {
        return salesnotes;
    }

    public void setSalesnotes(Integer salesnotes) {
        this.salesnotes = salesnotes;
    }

    public Integer getApplications() {
        return applications;
    }

    public void setApplications(Integer applications) {
        this.applications = applications;
    }

    public Integer getKit() {
        return kit;
    }

    public void setKit(Integer kit) {
        this.kit = kit;
    }

    public Integer getPart() {
        return part;
    }

    public void setPart(Integer part) {
        this.part = part;
    }

    public Integer getTurbomodel() {
        return turbomodel;
    }

    public void setTurbomodel(Integer turbomodel) {
        this.turbomodel = turbomodel;
    }

    public Integer getTurbotype() {
        return turbotype;
    }

    public void setTurbotype(Integer turbotype) {
        this.turbotype = turbotype;
    }

    public Integer getCriticaldim() {
        return criticaldim;
    }

    public void setCriticaldim(Integer criticaldim) {
        this.criticaldim = criticaldim;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

}
