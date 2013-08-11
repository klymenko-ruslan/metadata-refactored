// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain;

import com.turbointernational.metadata.domain.ApplicationConversionServiceFactoryBean;
import com.turbointernational.metadata.domain.other.Interchange;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.other.TurboModel;
import com.turbointernational.metadata.domain.part.Backplate;
import com.turbointernational.metadata.domain.part.BearingHousing;
import com.turbointernational.metadata.domain.part.BearingSpacer;
import com.turbointernational.metadata.domain.part.Cartridge;
import com.turbointernational.metadata.domain.part.CompressorWheel;
import com.turbointernational.metadata.domain.part.Gasket;
import com.turbointernational.metadata.domain.part.Heatshield;
import com.turbointernational.metadata.domain.part.JournalBearing;
import com.turbointernational.metadata.domain.part.Kit;
import com.turbointernational.metadata.domain.part.NozzleRing;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.PistonRing;
import com.turbointernational.metadata.domain.part.TurbineWheel;
import com.turbointernational.metadata.domain.part.Turbo;
import com.turbointernational.metadata.domain.security.Group;
import com.turbointernational.metadata.domain.security.User;
import com.turbointernational.metadata.domain.type.TurboType;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;

privileged aspect ApplicationConversionServiceFactoryBean_Roo_ConversionService {
    
    declare @type: ApplicationConversionServiceFactoryBean: @Configurable;
    
    public Converter<Interchange, String> ApplicationConversionServiceFactoryBean.getInterchangeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.turbointernational.metadata.domain.other.Interchange, java.lang.String>() {
            public String convert(Interchange interchange) {
                return new StringBuilder().append(interchange.getName()).append(' ').append(interchange.getDescription()).toString();
            }
        };
    }
    
    public Converter<Long, Interchange> ApplicationConversionServiceFactoryBean.getIdToInterchangeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.turbointernational.metadata.domain.other.Interchange>() {
            public com.turbointernational.metadata.domain.other.Interchange convert(java.lang.Long id) {
                return Interchange.findInterchange(id);
            }
        };
    }
    
    public Converter<String, Interchange> ApplicationConversionServiceFactoryBean.getStringToInterchangeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.turbointernational.metadata.domain.other.Interchange>() {
            public com.turbointernational.metadata.domain.other.Interchange convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Interchange.class);
            }
        };
    }
    
    public Converter<Manufacturer, String> ApplicationConversionServiceFactoryBean.getManufacturerToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.turbointernational.metadata.domain.other.Manufacturer, java.lang.String>() {
            public String convert(Manufacturer manufacturer) {
                return new StringBuilder().append(manufacturer.getName()).toString();
            }
        };
    }
    
    public Converter<Long, Manufacturer> ApplicationConversionServiceFactoryBean.getIdToManufacturerConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.turbointernational.metadata.domain.other.Manufacturer>() {
            public com.turbointernational.metadata.domain.other.Manufacturer convert(java.lang.Long id) {
                return Manufacturer.findManufacturer(id);
            }
        };
    }
    
    public Converter<String, Manufacturer> ApplicationConversionServiceFactoryBean.getStringToManufacturerConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.turbointernational.metadata.domain.other.Manufacturer>() {
            public com.turbointernational.metadata.domain.other.Manufacturer convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Manufacturer.class);
            }
        };
    }
    
    public Converter<TurboModel, String> ApplicationConversionServiceFactoryBean.getTurboModelToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.turbointernational.metadata.domain.other.TurboModel, java.lang.String>() {
            public String convert(TurboModel turboModel) {
                return new StringBuilder().append(turboModel.getName()).toString();
            }
        };
    }
    
    public Converter<Long, TurboModel> ApplicationConversionServiceFactoryBean.getIdToTurboModelConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.turbointernational.metadata.domain.other.TurboModel>() {
            public com.turbointernational.metadata.domain.other.TurboModel convert(java.lang.Long id) {
                return TurboModel.findTurboModel(id);
            }
        };
    }
    
    public Converter<String, TurboModel> ApplicationConversionServiceFactoryBean.getStringToTurboModelConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.turbointernational.metadata.domain.other.TurboModel>() {
            public com.turbointernational.metadata.domain.other.TurboModel convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), TurboModel.class);
            }
        };
    }
    
    public Converter<Backplate, String> ApplicationConversionServiceFactoryBean.getBackplateToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.turbointernational.metadata.domain.part.Backplate, java.lang.String>() {
            public String convert(Backplate backplate) {
                return new StringBuilder().append(backplate.getManufacturerPartNumber()).append(' ').append(backplate.getName()).append(' ').append(backplate.getDescription()).append(' ').append(backplate.getStyleCompressorWheel()).toString();
            }
        };
    }
    
    public Converter<Long, Backplate> ApplicationConversionServiceFactoryBean.getIdToBackplateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.turbointernational.metadata.domain.part.Backplate>() {
            public com.turbointernational.metadata.domain.part.Backplate convert(java.lang.Long id) {
                return Backplate.findBackplate(id);
            }
        };
    }
    
    public Converter<String, Backplate> ApplicationConversionServiceFactoryBean.getStringToBackplateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.turbointernational.metadata.domain.part.Backplate>() {
            public com.turbointernational.metadata.domain.part.Backplate convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Backplate.class);
            }
        };
    }
    
    public Converter<BearingHousing, String> ApplicationConversionServiceFactoryBean.getBearingHousingToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.turbointernational.metadata.domain.part.BearingHousing, java.lang.String>() {
            public String convert(BearingHousing bearingHousing) {
                return new StringBuilder().append(bearingHousing.getManufacturerPartNumber()).append(' ').append(bearingHousing.getName()).append(' ').append(bearingHousing.getDescription()).append(' ').append(bearingHousing.getOilInlet()).toString();
            }
        };
    }
    
    public Converter<Long, BearingHousing> ApplicationConversionServiceFactoryBean.getIdToBearingHousingConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.turbointernational.metadata.domain.part.BearingHousing>() {
            public com.turbointernational.metadata.domain.part.BearingHousing convert(java.lang.Long id) {
                return BearingHousing.findBearingHousing(id);
            }
        };
    }
    
    public Converter<String, BearingHousing> ApplicationConversionServiceFactoryBean.getStringToBearingHousingConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.turbointernational.metadata.domain.part.BearingHousing>() {
            public com.turbointernational.metadata.domain.part.BearingHousing convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), BearingHousing.class);
            }
        };
    }
    
    public Converter<BearingSpacer, String> ApplicationConversionServiceFactoryBean.getBearingSpacerToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.turbointernational.metadata.domain.part.BearingSpacer, java.lang.String>() {
            public String convert(BearingSpacer bearingSpacer) {
                return new StringBuilder().append(bearingSpacer.getManufacturerPartNumber()).append(' ').append(bearingSpacer.getName()).append(' ').append(bearingSpacer.getDescription()).append(' ').append(bearingSpacer.getOutsideDiameterMin()).toString();
            }
        };
    }
    
    public Converter<Long, BearingSpacer> ApplicationConversionServiceFactoryBean.getIdToBearingSpacerConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.turbointernational.metadata.domain.part.BearingSpacer>() {
            public com.turbointernational.metadata.domain.part.BearingSpacer convert(java.lang.Long id) {
                return BearingSpacer.findBearingSpacer(id);
            }
        };
    }
    
    public Converter<String, BearingSpacer> ApplicationConversionServiceFactoryBean.getStringToBearingSpacerConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.turbointernational.metadata.domain.part.BearingSpacer>() {
            public com.turbointernational.metadata.domain.part.BearingSpacer convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), BearingSpacer.class);
            }
        };
    }
    
    public Converter<Cartridge, String> ApplicationConversionServiceFactoryBean.getCartridgeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.turbointernational.metadata.domain.part.Cartridge, java.lang.String>() {
            public String convert(Cartridge cartridge) {
                return new StringBuilder().append(cartridge.getManufacturerPartNumber()).append(' ').append(cartridge.getName()).append(' ').append(cartridge.getDescription()).toString();
            }
        };
    }
    
    public Converter<Long, Cartridge> ApplicationConversionServiceFactoryBean.getIdToCartridgeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.turbointernational.metadata.domain.part.Cartridge>() {
            public com.turbointernational.metadata.domain.part.Cartridge convert(java.lang.Long id) {
                return Cartridge.findCartridge(id);
            }
        };
    }
    
    public Converter<String, Cartridge> ApplicationConversionServiceFactoryBean.getStringToCartridgeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.turbointernational.metadata.domain.part.Cartridge>() {
            public com.turbointernational.metadata.domain.part.Cartridge convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Cartridge.class);
            }
        };
    }
    
    public Converter<CompressorWheel, String> ApplicationConversionServiceFactoryBean.getCompressorWheelToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.turbointernational.metadata.domain.part.CompressorWheel, java.lang.String>() {
            public String convert(CompressorWheel compressorWheel) {
                return new StringBuilder().append(compressorWheel.getManufacturerPartNumber()).append(' ').append(compressorWheel.getName()).append(' ').append(compressorWheel.getDescription()).append(' ').append(compressorWheel.getInducerOa()).toString();
            }
        };
    }
    
    public Converter<Long, CompressorWheel> ApplicationConversionServiceFactoryBean.getIdToCompressorWheelConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.turbointernational.metadata.domain.part.CompressorWheel>() {
            public com.turbointernational.metadata.domain.part.CompressorWheel convert(java.lang.Long id) {
                return CompressorWheel.findCompressorWheel(id);
            }
        };
    }
    
    public Converter<String, CompressorWheel> ApplicationConversionServiceFactoryBean.getStringToCompressorWheelConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.turbointernational.metadata.domain.part.CompressorWheel>() {
            public com.turbointernational.metadata.domain.part.CompressorWheel convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), CompressorWheel.class);
            }
        };
    }
    
    public Converter<Gasket, String> ApplicationConversionServiceFactoryBean.getGasketToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.turbointernational.metadata.domain.part.Gasket, java.lang.String>() {
            public String convert(Gasket gasket) {
                return new StringBuilder().append(gasket.getManufacturerPartNumber()).append(' ').append(gasket.getName()).append(' ').append(gasket.getDescription()).toString();
            }
        };
    }
    
    public Converter<Long, Gasket> ApplicationConversionServiceFactoryBean.getIdToGasketConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.turbointernational.metadata.domain.part.Gasket>() {
            public com.turbointernational.metadata.domain.part.Gasket convert(java.lang.Long id) {
                return Gasket.findGasket(id);
            }
        };
    }
    
    public Converter<String, Gasket> ApplicationConversionServiceFactoryBean.getStringToGasketConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.turbointernational.metadata.domain.part.Gasket>() {
            public com.turbointernational.metadata.domain.part.Gasket convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Gasket.class);
            }
        };
    }
    
    public Converter<Heatshield, String> ApplicationConversionServiceFactoryBean.getHeatshieldToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.turbointernational.metadata.domain.part.Heatshield, java.lang.String>() {
            public String convert(Heatshield heatshield) {
                return new StringBuilder().append(heatshield.getManufacturerPartNumber()).append(' ').append(heatshield.getName()).append(' ').append(heatshield.getDescription()).append(' ').append(heatshield.getOverallDiameter()).toString();
            }
        };
    }
    
    public Converter<Long, Heatshield> ApplicationConversionServiceFactoryBean.getIdToHeatshieldConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.turbointernational.metadata.domain.part.Heatshield>() {
            public com.turbointernational.metadata.domain.part.Heatshield convert(java.lang.Long id) {
                return Heatshield.findHeatshield(id);
            }
        };
    }
    
    public Converter<String, Heatshield> ApplicationConversionServiceFactoryBean.getStringToHeatshieldConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.turbointernational.metadata.domain.part.Heatshield>() {
            public com.turbointernational.metadata.domain.part.Heatshield convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Heatshield.class);
            }
        };
    }
    
    public Converter<JournalBearing, String> ApplicationConversionServiceFactoryBean.getJournalBearingToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.turbointernational.metadata.domain.part.JournalBearing, java.lang.String>() {
            public String convert(JournalBearing journalBearing) {
                return new StringBuilder().append(journalBearing.getManufacturerPartNumber()).append(' ').append(journalBearing.getName()).append(' ').append(journalBearing.getDescription()).append(' ').append(journalBearing.getOutsideDiameterMin()).toString();
            }
        };
    }
    
    public Converter<Long, JournalBearing> ApplicationConversionServiceFactoryBean.getIdToJournalBearingConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.turbointernational.metadata.domain.part.JournalBearing>() {
            public com.turbointernational.metadata.domain.part.JournalBearing convert(java.lang.Long id) {
                return JournalBearing.findJournalBearing(id);
            }
        };
    }
    
    public Converter<String, JournalBearing> ApplicationConversionServiceFactoryBean.getStringToJournalBearingConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.turbointernational.metadata.domain.part.JournalBearing>() {
            public com.turbointernational.metadata.domain.part.JournalBearing convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), JournalBearing.class);
            }
        };
    }
    
    public Converter<Kit, String> ApplicationConversionServiceFactoryBean.getKitToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.turbointernational.metadata.domain.part.Kit, java.lang.String>() {
            public String convert(Kit kit) {
                return new StringBuilder().append(kit.getManufacturerPartNumber()).append(' ').append(kit.getName()).append(' ').append(kit.getDescription()).toString();
            }
        };
    }
    
    public Converter<Long, Kit> ApplicationConversionServiceFactoryBean.getIdToKitConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.turbointernational.metadata.domain.part.Kit>() {
            public com.turbointernational.metadata.domain.part.Kit convert(java.lang.Long id) {
                return Kit.findKit(id);
            }
        };
    }
    
    public Converter<String, Kit> ApplicationConversionServiceFactoryBean.getStringToKitConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.turbointernational.metadata.domain.part.Kit>() {
            public com.turbointernational.metadata.domain.part.Kit convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Kit.class);
            }
        };
    }
    
    public Converter<NozzleRing, String> ApplicationConversionServiceFactoryBean.getNozzleRingToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.turbointernational.metadata.domain.part.NozzleRing, java.lang.String>() {
            public String convert(NozzleRing nozzleRing) {
                return new StringBuilder().append(nozzleRing.getManufacturerPartNumber()).append(' ').append(nozzleRing.getName()).append(' ').append(nozzleRing.getDescription()).toString();
            }
        };
    }
    
    public Converter<Long, NozzleRing> ApplicationConversionServiceFactoryBean.getIdToNozzleRingConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.turbointernational.metadata.domain.part.NozzleRing>() {
            public com.turbointernational.metadata.domain.part.NozzleRing convert(java.lang.Long id) {
                return NozzleRing.findNozzleRing(id);
            }
        };
    }
    
    public Converter<String, NozzleRing> ApplicationConversionServiceFactoryBean.getStringToNozzleRingConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.turbointernational.metadata.domain.part.NozzleRing>() {
            public com.turbointernational.metadata.domain.part.NozzleRing convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), NozzleRing.class);
            }
        };
    }
    
    public Converter<Part, String> ApplicationConversionServiceFactoryBean.getPartToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.turbointernational.metadata.domain.part.Part, java.lang.String>() {
            public String convert(Part part) {
                return new StringBuilder().append(part.getManufacturerPartNumber()).append(' ').append(part.getName()).append(' ').append(part.getDescription()).toString();
            }
        };
    }
    
    public Converter<Long, Part> ApplicationConversionServiceFactoryBean.getIdToPartConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.turbointernational.metadata.domain.part.Part>() {
            public com.turbointernational.metadata.domain.part.Part convert(java.lang.Long id) {
                return Part.findPart(id);
            }
        };
    }
    
    public Converter<String, Part> ApplicationConversionServiceFactoryBean.getStringToPartConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.turbointernational.metadata.domain.part.Part>() {
            public com.turbointernational.metadata.domain.part.Part convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Part.class);
            }
        };
    }
    
    public Converter<PistonRing, String> ApplicationConversionServiceFactoryBean.getPistonRingToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.turbointernational.metadata.domain.part.PistonRing, java.lang.String>() {
            public String convert(PistonRing pistonRing) {
                return new StringBuilder().append(pistonRing.getManufacturerPartNumber()).append(' ').append(pistonRing.getName()).append(' ').append(pistonRing.getDescription()).append(' ').append(pistonRing.getOutsideDiameterMin()).toString();
            }
        };
    }
    
    public Converter<Long, PistonRing> ApplicationConversionServiceFactoryBean.getIdToPistonRingConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.turbointernational.metadata.domain.part.PistonRing>() {
            public com.turbointernational.metadata.domain.part.PistonRing convert(java.lang.Long id) {
                return PistonRing.findPistonRing(id);
            }
        };
    }
    
    public Converter<String, PistonRing> ApplicationConversionServiceFactoryBean.getStringToPistonRingConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.turbointernational.metadata.domain.part.PistonRing>() {
            public com.turbointernational.metadata.domain.part.PistonRing convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), PistonRing.class);
            }
        };
    }
    
    public Converter<TurbineWheel, String> ApplicationConversionServiceFactoryBean.getTurbineWheelToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.turbointernational.metadata.domain.part.TurbineWheel, java.lang.String>() {
            public String convert(TurbineWheel turbineWheel) {
                return new StringBuilder().append(turbineWheel.getManufacturerPartNumber()).append(' ').append(turbineWheel.getName()).append(' ').append(turbineWheel.getDescription()).append(' ').append(turbineWheel.getExducerDiameterA()).toString();
            }
        };
    }
    
    public Converter<Long, TurbineWheel> ApplicationConversionServiceFactoryBean.getIdToTurbineWheelConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.turbointernational.metadata.domain.part.TurbineWheel>() {
            public com.turbointernational.metadata.domain.part.TurbineWheel convert(java.lang.Long id) {
                return TurbineWheel.findTurbineWheel(id);
            }
        };
    }
    
    public Converter<String, TurbineWheel> ApplicationConversionServiceFactoryBean.getStringToTurbineWheelConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.turbointernational.metadata.domain.part.TurbineWheel>() {
            public com.turbointernational.metadata.domain.part.TurbineWheel convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), TurbineWheel.class);
            }
        };
    }
    
    public Converter<Turbo, String> ApplicationConversionServiceFactoryBean.getTurboToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.turbointernational.metadata.domain.part.Turbo, java.lang.String>() {
            public String convert(Turbo turbo) {
                return new StringBuilder().append(turbo.getManufacturerPartNumber()).append(' ').append(turbo.getName()).append(' ').append(turbo.getDescription()).toString();
            }
        };
    }
    
    public Converter<Long, Turbo> ApplicationConversionServiceFactoryBean.getIdToTurboConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.turbointernational.metadata.domain.part.Turbo>() {
            public com.turbointernational.metadata.domain.part.Turbo convert(java.lang.Long id) {
                return Turbo.findTurbo(id);
            }
        };
    }
    
    public Converter<String, Turbo> ApplicationConversionServiceFactoryBean.getStringToTurboConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.turbointernational.metadata.domain.part.Turbo>() {
            public com.turbointernational.metadata.domain.part.Turbo convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Turbo.class);
            }
        };
    }
    
    public Converter<Group, String> ApplicationConversionServiceFactoryBean.getGroupToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.turbointernational.metadata.domain.security.Group, java.lang.String>() {
            public String convert(Group group) {
                return new StringBuilder().append(group.getName()).toString();
            }
        };
    }
    
    public Converter<Long, Group> ApplicationConversionServiceFactoryBean.getIdToGroupConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.turbointernational.metadata.domain.security.Group>() {
            public com.turbointernational.metadata.domain.security.Group convert(java.lang.Long id) {
                return Group.findGroup(id);
            }
        };
    }
    
    public Converter<String, Group> ApplicationConversionServiceFactoryBean.getStringToGroupConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.turbointernational.metadata.domain.security.Group>() {
            public com.turbointernational.metadata.domain.security.Group convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Group.class);
            }
        };
    }
    
    public Converter<User, String> ApplicationConversionServiceFactoryBean.getUserToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.turbointernational.metadata.domain.security.User, java.lang.String>() {
            public String convert(User user) {
                return new StringBuilder().append(user.getName()).append(' ').append(user.getEmail()).append(' ').append(user.getPassword()).toString();
            }
        };
    }
    
    public Converter<Long, User> ApplicationConversionServiceFactoryBean.getIdToUserConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.turbointernational.metadata.domain.security.User>() {
            public com.turbointernational.metadata.domain.security.User convert(java.lang.Long id) {
                return User.findUser(id);
            }
        };
    }
    
    public Converter<String, User> ApplicationConversionServiceFactoryBean.getStringToUserConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.turbointernational.metadata.domain.security.User>() {
            public com.turbointernational.metadata.domain.security.User convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), User.class);
            }
        };
    }
    
    public Converter<TurboType, String> ApplicationConversionServiceFactoryBean.getTurboTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.turbointernational.metadata.domain.type.TurboType, java.lang.String>() {
            public String convert(TurboType turboType) {
                return new StringBuilder().append(turboType.getName()).toString();
            }
        };
    }
    
    public Converter<Long, TurboType> ApplicationConversionServiceFactoryBean.getIdToTurboTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.turbointernational.metadata.domain.type.TurboType>() {
            public com.turbointernational.metadata.domain.type.TurboType convert(java.lang.Long id) {
                return TurboType.findTurboType(id);
            }
        };
    }
    
    public Converter<String, TurboType> ApplicationConversionServiceFactoryBean.getStringToTurboTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.turbointernational.metadata.domain.type.TurboType>() {
            public com.turbointernational.metadata.domain.type.TurboType convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), TurboType.class);
            }
        };
    }
    
    public void ApplicationConversionServiceFactoryBean.installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getInterchangeToStringConverter());
        registry.addConverter(getIdToInterchangeConverter());
        registry.addConverter(getStringToInterchangeConverter());
        registry.addConverter(getManufacturerToStringConverter());
        registry.addConverter(getIdToManufacturerConverter());
        registry.addConverter(getStringToManufacturerConverter());
        registry.addConverter(getTurboModelToStringConverter());
        registry.addConverter(getIdToTurboModelConverter());
        registry.addConverter(getStringToTurboModelConverter());
        registry.addConverter(getBackplateToStringConverter());
        registry.addConverter(getIdToBackplateConverter());
        registry.addConverter(getStringToBackplateConverter());
        registry.addConverter(getBearingHousingToStringConverter());
        registry.addConverter(getIdToBearingHousingConverter());
        registry.addConverter(getStringToBearingHousingConverter());
        registry.addConverter(getBearingSpacerToStringConverter());
        registry.addConverter(getIdToBearingSpacerConverter());
        registry.addConverter(getStringToBearingSpacerConverter());
        registry.addConverter(getCartridgeToStringConverter());
        registry.addConverter(getIdToCartridgeConverter());
        registry.addConverter(getStringToCartridgeConverter());
        registry.addConverter(getCompressorWheelToStringConverter());
        registry.addConverter(getIdToCompressorWheelConverter());
        registry.addConverter(getStringToCompressorWheelConverter());
        registry.addConverter(getGasketToStringConverter());
        registry.addConverter(getIdToGasketConverter());
        registry.addConverter(getStringToGasketConverter());
        registry.addConverter(getHeatshieldToStringConverter());
        registry.addConverter(getIdToHeatshieldConverter());
        registry.addConverter(getStringToHeatshieldConverter());
        registry.addConverter(getJournalBearingToStringConverter());
        registry.addConverter(getIdToJournalBearingConverter());
        registry.addConverter(getStringToJournalBearingConverter());
        registry.addConverter(getKitToStringConverter());
        registry.addConverter(getIdToKitConverter());
        registry.addConverter(getStringToKitConverter());
        registry.addConverter(getNozzleRingToStringConverter());
        registry.addConverter(getIdToNozzleRingConverter());
        registry.addConverter(getStringToNozzleRingConverter());
        registry.addConverter(getPartToStringConverter());
        registry.addConverter(getIdToPartConverter());
        registry.addConverter(getStringToPartConverter());
        registry.addConverter(getPistonRingToStringConverter());
        registry.addConverter(getIdToPistonRingConverter());
        registry.addConverter(getStringToPistonRingConverter());
        registry.addConverter(getTurbineWheelToStringConverter());
        registry.addConverter(getIdToTurbineWheelConverter());
        registry.addConverter(getStringToTurbineWheelConverter());
        registry.addConverter(getTurboToStringConverter());
        registry.addConverter(getIdToTurboConverter());
        registry.addConverter(getStringToTurboConverter());
        registry.addConverter(getGroupToStringConverter());
        registry.addConverter(getIdToGroupConverter());
        registry.addConverter(getStringToGroupConverter());
        registry.addConverter(getUserToStringConverter());
        registry.addConverter(getIdToUserConverter());
        registry.addConverter(getStringToUserConverter());
        registry.addConverter(getTurboTypeToStringConverter());
        registry.addConverter(getIdToTurboTypeConverter());
        registry.addConverter(getStringToTurboTypeConverter());
    }
    
    public void ApplicationConversionServiceFactoryBean.afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
    
}
