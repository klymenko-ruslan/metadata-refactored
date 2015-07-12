package com.turbointernational.metadata.web;

/**
 *
 * @author jrodriguez
 */
public class View {
    public static interface Summary {};
    public static interface Detail {};
    public static interface DetailWithGroups extends Detail {};
    public static interface DetailWithUsers extends Detail {};
}
