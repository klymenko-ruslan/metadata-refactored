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

    public interface ChangelogSourceDetailed extends Summary {}

    public interface CommonComponentKit extends Summary {}

    public interface CommonComponentPart extends Summary {}

    public interface CommonComponentKitAndPart extends CommonComponentKit, CommonComponentPart {}

}
