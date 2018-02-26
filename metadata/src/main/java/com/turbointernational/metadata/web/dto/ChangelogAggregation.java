package com.turbointernational.metadata.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class ChangelogAggregation {

    @JsonView(View.Summary.class)
    private User user;

    @JsonView(View.Summary.class)
    private Long bom;

    @JsonView(View.Summary.class)
    private Long interchange;

    @JsonView(View.Summary.class)
    private Long mas90sync;

    @JsonView(View.Summary.class)
    private Long salesnotes;

    @JsonView(View.Summary.class)
    private Long applications;

    @JsonView(View.Summary.class)
    private Long kit;

    @JsonView(View.Summary.class)
    private Long part;

    @JsonView(View.Summary.class)
    private Long turbomodel;

    @JsonView(View.Summary.class)
    private Long turbotype;

    @JsonView(View.Summary.class)
    private Long criticaldim;

    @JsonView(View.Summary.class)
    private Long image;

    public ChangelogAggregation() {
        super();
    }

    public ChangelogAggregation(User user, Long bom, Long interchange, Long mas90sync, Long salesnotes,
            Long applications, Long kit, Long part, Long turbomodel, Long turbotype, Long criticaldim, Long image) {
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

    public Long getBom() {
        return bom;
    }

    public void setBom(Long bom) {
        this.bom = bom;
    }

    public Long getInterchange() {
        return interchange;
    }

    public void setInterchange(Long interchange) {
        this.interchange = interchange;
    }

    public Long getMas90sync() {
        return mas90sync;
    }

    public void setMas90sync(Long mas90sync) {
        this.mas90sync = mas90sync;
    }

    public Long getSalesnotes() {
        return salesnotes;
    }

    public void setSalesnotes(Long salesnotes) {
        this.salesnotes = salesnotes;
    }

    public Long getApplications() {
        return applications;
    }

    public void setApplications(Long applications) {
        this.applications = applications;
    }

    public Long getKit() {
        return kit;
    }

    public void setKit(Long kit) {
        this.kit = kit;
    }

    public Long getPart() {
        return part;
    }

    public void setPart(Long part) {
        this.part = part;
    }

    public Long getTurbomodel() {
        return turbomodel;
    }

    public void setTurbomodel(Long turbomodel) {
        this.turbomodel = turbomodel;
    }

    public Long getTurbotype() {
        return turbotype;
    }

    public void setTurbotype(Long turbotype) {
        this.turbotype = turbotype;
    }

    public Long getCriticaldim() {
        return criticaldim;
    }

    public void setCriticaldim(Long criticaldim) {
        this.criticaldim = criticaldim;
    }

    public Long getImage() {
        return image;
    }

    public void setImage(Long image) {
        this.image = image;
    }

}
