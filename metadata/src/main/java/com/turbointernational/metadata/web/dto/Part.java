package com.turbointernational.metadata.web.dto;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.util.View;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
public class Part {

    @JsonView({ View.Summary.class })
    private Long partId;

    @JsonView({ View.Summary.class })
    private String name;

    @JsonView({ View.Summary.class })
    private String description;

    @JsonView({ View.Summary.class })
    private String partNumber;

    @JsonView({ View.Summary.class })
    private PartType partType;

    @JsonView({ View.Summary.class })
    private Manufacturer manufacturer;

    public Part() {
    }

    public Part(Long partId, String name, String description, String partNumber, PartType partType,
            Manufacturer manufacturer) {
        this.partId = partId;
        this.name = name;
        this.description = description;
        this.partNumber = partNumber;
        this.partType = partType;
        this.manufacturer = manufacturer;
    }

    public static Part from(PartDao dao, Long partID) {
        com.turbointernational.metadata.entity.part.Part p = dao.findOne(partID);
        return entity2dto(p);
    }

    public static Part[] from(PartDao dao, Long[] partIDs) {
        int n = partIDs.length;
        Part[] retVal = new Part[n];
        if (retVal.length > 0) {
            List<com.turbointernational.metadata.entity.part.Part> parts = dao.findPartsByIds(Arrays.asList(partIDs));
            for (ListIterator<com.turbointernational.metadata.entity.part.Part> it = parts.listIterator(); it
                    .hasNext();) {
                int i = it.nextIndex();
                com.turbointernational.metadata.entity.part.Part p = it.next();
                retVal[i] = entity2dto(p);
            }
        }
        return retVal;
    }

    private static Part entity2dto(com.turbointernational.metadata.entity.part.Part p) {
        PartType pt = PartType.from(p.getPartType());
        Manufacturer m = Manufacturer.from(p.getManufacturer());
        return new Part(p.getId(), p.getName(), p.getDescription(), p.getManufacturerPartNumber(), pt, m);
    }

    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public PartType getPartType() {
        return partType;
    }

    public void setPartType(PartType partType) {
        this.partType = partType;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String toString() {
        return "Part [partId=" + partId + ", name=" + name + ", description=" + description + ", partNumber="
                + partNumber + ", partType=" + partType + ", manufacturer=" + manufacturer + "]";
    }

}
