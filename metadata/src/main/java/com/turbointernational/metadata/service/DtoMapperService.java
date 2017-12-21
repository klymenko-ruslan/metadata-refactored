package com.turbointernational.metadata.service;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.service.GraphDbService.GetBomsResponse;
import com.turbointernational.metadata.service.GraphDbService.GetInterchangeResponse;
import com.turbointernational.metadata.web.dto.Bom;
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

    protected ModelMapper modelMapper;

    protected Converter<Long, Part> partId2Part = new AbstractConverter<Long, Part>() {

        @Override
        protected Part convert(Long partId) {
            Part retVal = null;
            com.turbointernational.metadata.entity.part.Part p = partDao.findOne(partId);
            if (p == null) {
                log.error("Conversion of a part ID [{}] to an entity Part failed.", partId);
            }
            retVal = modelMapper.map(p, Part.class);
            return retVal;
        }

    };

    protected Converter<Long[], Part[]> partIds2Parts = new AbstractConverter<Long[], Part[]>() {

        @Override
        protected Part[] convert(Long[] partIds) {
            Part[] retVal = null;
            if (partIds != null) {
                List<com.turbointernational.metadata.entity.part.Part> partsEntities = partDao.findPartsByIds(Arrays.asList(partIds));
                retVal = (Part[]) partsEntities.stream().map(entity -> modelMapper.map(entity, Part.class)).toArray();
            }
            return retVal;
        }

    };

    @PostConstruct
    public void init() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setImplicitMappingEnabled(false);
        modelMapper.addConverter(partId2Part);
        // TODO: modelMapper.addConverter(partIds2Parts);
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
                 .addMapping(com.turbointernational.metadata.entity.part.Part::getManufacturer, Part::setManufacturer)
                 .include(com.turbointernational.metadata.entity.part.types.Actuator.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.Backplate.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.BackplateSealplate.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.BearingHousing.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.BoltScrew.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.CarbonSeal.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.Cartridge.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.Clamp.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.CompressorCover.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.CompressorWheel.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.FastWearingComponent.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.Fitting.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.Gasket.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.GasketKit.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.HeatshieldShroud.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.JournalBearing.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.JournalBearingSpacer.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.Kit.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.MajorComponent.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.MinorComponent.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.Misc.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.MiscMinorComponent.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.NozzleRing.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.Nut.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.OilDeflector.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.ORing.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.P.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.Pin.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.PistonRing.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.Plug.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.RetainingRing.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.SealPlate.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.Shroud.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.Spring.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.ThrustBearing.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.ThrustCollar.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.ThrustPart.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.ThrustSpacer.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.ThrustWasher.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.TurbineHousing.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.TurbineWheel.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.Turbo.class, Part.class)
                 .include(com.turbointernational.metadata.entity.part.types.Washer.class, Part.class);
        // DTO: Ancestor
        /*
        modelMapper.createTypeMap(GetAncestorsResponse.Row.class, Ancestor.class)
                .addMapping(GetAncestorsResponse.Row::getPartId, Ancestor::setPart)
                .addMapping(GetAncestorsResponse.Row::isRelationType, Ancestor::setRelationType)
                .addMapping(GetAncestorsResponse.Row::getRelationDistance, Ancestor::setRelationDistance);
        */
        // DTO: Interchange
        modelMapper.createTypeMap(GetInterchangeResponse.class, Interchange.class)
                .addMapping(GetInterchangeResponse::getHeaderId, Interchange::setId)
                .addMapping(GetInterchangeResponse::getParts, Interchange::setParts);
        // DTO: BOM
        modelMapper.createTypeMap(GetBomsResponse.Row.class, Bom.class)
                .addMapping(GetBomsResponse.Row::getPartId, Bom::setPart)
                .addMapping(GetBomsResponse.Row::getQty, Bom::setQty)
                .addMapping(GetBomsResponse.Row::getInterchanges, Bom::setInterchanges);
    }

    public <D> D map(Object source, Class<D> destinationType) {
        return modelMapper.map(source, destinationType);
    }

}
