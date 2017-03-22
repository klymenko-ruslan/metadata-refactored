package com.turbointernational.metadata.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turbointernational.metadata.dao.ManufacturerTypeDao;
import com.turbointernational.metadata.entity.ManufacturerType;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@Service
public class ManufacturerTypeService {

    @Autowired
    private ManufacturerTypeDao manufacturerTypeDao;

    public List<ManufacturerType> getAll() {
        return manufacturerTypeDao.findAll();
    }

}
