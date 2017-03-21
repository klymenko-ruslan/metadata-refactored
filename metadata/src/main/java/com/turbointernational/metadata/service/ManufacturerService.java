package com.turbointernational.metadata.service;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.dao.ManufacturerDao;
import com.turbointernational.metadata.dao.ManufacturerTypeDao;
import com.turbointernational.metadata.entity.Manufacturer;
import com.turbointernational.metadata.entity.ManufacturerType;
import com.turbointernational.metadata.util.View;
import com.turbointernational.metadata.web.dto.Page;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@Service
public class ManufacturerService {

    @Autowired
    private ManufacturerDao manufacturerDao;
    
    @Autowired
    private ManufacturerTypeDao manufacturerTypeDao; 

    @JsonInclude(ALWAYS)
    public static class DeleteResponse {

        @JsonView(View.Summary.class)
        private boolean removed;

        @JsonView(View.Summary.class)
        private Long refParts;

        @JsonView(View.Summary.class)
        private Long refTurboTypes;

        public DeleteResponse() {
        }

        public DeleteResponse(boolean removed) {
            this.removed = removed;
        }

        public DeleteResponse(Long refParts, Long refTurboTypes) {
            this.removed = false;
            this.refParts = refParts;
            this.refTurboTypes = refTurboTypes;
        }

        public boolean isRemoved() {
            return removed;
        }

        public void setRemoved(boolean removed) {
            this.removed = removed;
        }

        public Long getRefParts() {
            return refParts;
        }

        public void setRefParts(Long refParts) {
            this.refParts = refParts;
        }

        public Long getRefTurboTypes() {
            return refTurboTypes;
        }

        public void setRefTurboTypes(Long refTurboTypes) {
            this.refTurboTypes = refTurboTypes;
        }

    }

    public List<Manufacturer> findAllManufacturers() {
        return manufacturerDao.findAll();
    }

    public Manufacturer findManufacturer(Long id) {
        return manufacturerDao.findManufacturer(id);
    }

    public Page<Manufacturer> filter(String fltrName, Long fltrManufacturerTypeId, Boolean notExternal,
            String sortProperty, String sortOrder, Integer offset, Integer limit) {
        return manufacturerDao.filter(fltrName, fltrManufacturerTypeId, notExternal, sortProperty, sortOrder, offset,
                limit);
    }

    public DeleteResponse delete(Long manufacturerId) {
        Long refParts = manufacturerDao.getRefCountFromParts(manufacturerId);
        Long refTurboTypes = manufacturerDao.getRefCountFromTurboTypes(manufacturerId);
        if (refParts > 0 || refTurboTypes > 0) {
            return new DeleteResponse(refParts, refTurboTypes);

        }
        manufacturerDao.delete(manufacturerId);
        return new DeleteResponse(true);
    }
    
    public Manufacturer findManufacurerByName(String name) {
        return manufacturerDao.findManufacurerByName(name);
    }
    
    public Manufacturer create(String name, Long typeId, boolean notExternal) {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName(name);
        manufacturer.setNotExternal(notExternal);
        ManufacturerType manufacturerType = manufacturerTypeDao.getReference(typeId);
        manufacturer.setType(manufacturerType);
        manufacturerDao.persist(manufacturer);
        return manufacturer;
    }

    public Manufacturer update(Long manufacturerId, String name, Long typeId, boolean notExternal) {
        Manufacturer manufacturer = manufacturerDao.findOne(manufacturerId);
        ManufacturerType manufacturerType = manufacturerTypeDao.getReference(typeId);
        manufacturer.setName(name);
        manufacturer.setNotExternal(notExternal);
        manufacturer.setType(manufacturerType);
        return manufacturer;
    }

}
