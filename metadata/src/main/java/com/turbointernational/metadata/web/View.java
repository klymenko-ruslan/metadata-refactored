package com.turbointernational.metadata.web;

/**
 *
 * @author jrodriguez
 */
public class View {
    public static interface Summary {};
    public static interface Detail extends Summary {};
    
    public static interface DetailWithGroups extends Detail {};
    public static interface DetailWithUsers extends Detail {};
    
    public static interface DetailWithPartsAndAttachments extends Detail {};
}
