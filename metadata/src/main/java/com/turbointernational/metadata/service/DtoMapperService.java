package com.turbointernational.metadata.service;

import javax.annotation.PostConstruct;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.service.GraphDbService.GetInterchangeResponse;
import com.turbointernational.metadata.web.dto.Ancestor;
import com.turbointernational.metadata.web.dto.Interchange;
import com.turbointernational.metadata.web.dto.Manufacturer;
import com.turbointernational.metadata.web.dto.Part;
import com.turbointernational.metadata.web.dto.PartType;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@Service
public class DtoMapperService {

    private final static Logger log = LoggerFactory.getLogger(DtoMapperService.class);

    @Autowired
    private PartDao partDao;

    private ModelMapper modelMapper;
/*
    private Converter<Long, com.turbointernational.metadata.entity.part.Part> partId2Part = new AbstractConverter<Long, com.turbointernational.metadata.entity.part.Part>() {

        @Override
        protected com.turbointernational.metadata.entity.part.Part convert(Long partId) {
            com.turbointernational.metadata.entity.part.Part retVal = partDao.findOne(partId);
            if (retVal == null) {
                log.error("Conversion of a part ID [{}] to an entity Part failed.", partId);
            }
            return retVal;
        }

    };
*/
    private Converter<Long[], Part[]> partIds2Parts = new AbstractConverter<Long[], Part[]>() {

        @Override
        protected Part[] convert(Long[] partIds) {
            Part[] retVal = null;
            if (partIds != null) {
                retVal = new Part[partIds.length];
                for (int i = 0; i < partIds.length; i++) {
                    Long id = partIds[i];
                    com.turbointernational.metadata.entity.part.Part p = partDao.findOne(id);
                    if (retVal == null) {
                        log.error("Conversion of a part ID [{}] to an entity Part failed.", id);
                    }
                    retVal[i] = modelMapper.map(p, Part.class);;
                }
            }
            return retVal;
        }

    };

    @PostConstruct
    public void init() {
        modelMapper = new ModelMapper();
        // DTO: PartType
        modelMapper.createTypeMap(com.turbointernational.metadata.entity.PartType.class, PartType.class)
                .addMapping(com.turbointernational.metadata.entity.PartType::getId, PartType::setId)
                .addMapping(com.turbointernational.metadata.entity.PartType::getName, PartType::setName);
        // DTO: Manufacturer
        modelMapper.createTypeMap(com.turbointernational.metadata.entity.Manufacturer.class, Manufacturer.class)
                .addMapping(com.turbointernational.metadata.entity.Manufacturer::getId, Manufacturer::setId)
                .addMapping(com.turbointernational.metadata.entity.Manufacturer::getName, Manufacturer::setName);
        // DTO: Part
        modelMapper.createTypeMap(com.turbointernational.metadata.entity.part.Part.class, Part.class)
                .addMapping(com.turbointernational.metadata.entity.part.Part::getId, Part::setPartId)
                .addMapping(com.turbointernational.metadata.entity.part.Part::getName, Part::setName)
                .addMapping(com.turbointernational.metadata.entity.part.Part::getDescription, Part::setDescription)
                .addMapping(com.turbointernational.metadata.entity.part.Part::getManufacturerPartNumber,
                        Part::setPartNumber)
                .addMapping(com.turbointernational.metadata.entity.part.Part::getPartType, Part::setPartType)
                .addMapping(com.turbointernational.metadata.entity.part.Part::getManufacturer, Part::setManufacturer);
        // DTO: Ancestor
        modelMapper.createTypeMap(com.turbointernational.metadata.entity.part.Part.class, Ancestor.class)
                .addMapping(com.turbointernational.metadata.entity.part.Part::getId, Ancestor::setPartId)
                .addMapping(com.turbointernational.metadata.entity.part.Part::getName, Ancestor::setName)
                .addMapping(com.turbointernational.metadata.entity.part.Part::getDescription, Ancestor::setDescription)
                .addMapping(com.turbointernational.metadata.entity.part.Part::getManufacturerPartNumber,
                        Ancestor::setPartNumber)
                .addMapping(com.turbointernational.metadata.entity.part.Part::getManufacturer,
                        Ancestor::setManufacturer)
                .addMapping(com.turbointernational.metadata.entity.part.Part::getPartType, Ancestor::setPartType);
        // DTO: Interchange
        modelMapper.createTypeMap(GetInterchangeResponse.class, Interchange.class)
                .addMapping(GetInterchangeResponse::getHeaderId, Interchange::setId)
                .addMapping(GetInterchangeResponse::getParts, Interchange::setParts)
                .setPropertyConverter(partIds2Parts);
    }

    public <D> D map(Object source, Class<D> destinationType) {
        return modelMapper.map(source, destinationType);
    }

}
