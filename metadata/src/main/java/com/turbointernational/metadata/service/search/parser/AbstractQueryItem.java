package com.turbointernational.metadata.service.search.parser;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
public abstract class AbstractQueryItem {

    public static enum TypeEnum {
        PLAIN, NESTED
    }

    private final TypeEnum type;

    public AbstractQueryItem(TypeEnum type) {
        this.type = type;
    }

    public TypeEnum getType() {
        return type;
    }

}
