package com.turbointernational.metadata.web;

/**
 *
 * @author jrodriguez
 */
public class View {

    public static interface Summary {};

    public static interface SummaryWithGroups extends Summary {};

    public static interface SummaryWithInterchangeParts extends Summary {};

    public static interface SummaryWithBOMDetail extends Summary {};
    
    public static interface Detail extends Summary {};
    
    public static interface DetailWithGroups extends Detail {};

    public static interface DetailWithUsers extends Detail {};
    
    public static interface DetailWithPartsAndAttachments extends Detail {};

    public static interface DetailWithParts extends Detail {};

    public static interface CarModel extends Summary {};

    public static interface CarMake extends Summary {};

    public static interface CarEngine extends Summary {};

    public static interface CarFuelType extends Summary {};

    public static interface CarYear extends Summary {};

}
