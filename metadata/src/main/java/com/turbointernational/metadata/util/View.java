package com.turbointernational.metadata.util;

/**
 *
 * @author jrodriguez
 */
public class View {

    public interface Summary {}

    public interface SummaryWithGroups extends Summary {}

    public interface SummaryWithInterchangeParts extends Summary {}

    public interface SummaryWithBOMDetail extends Summary {}

    public interface SummaryWithTurbos extends Summary {}

    public interface Detail extends Summary {}
    
    public interface DetailWithGroups extends Detail {}

    public interface DetailWithUsers extends Detail {}
    
    public interface DetailWithPartsAndAttachments extends Detail {}

    public interface DetailWithParts extends Detail {}

    public interface CarEngineDetailed extends Summary {}

    public interface CarModelDetailed extends Summary {}

}
