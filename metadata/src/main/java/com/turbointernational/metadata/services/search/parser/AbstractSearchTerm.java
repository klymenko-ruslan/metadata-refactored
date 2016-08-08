package com.turbointernational.metadata.services.search.parser;

/**
 * Created by dmytro.trunykov@zorallabs.com on 08.08.16.
 */
public abstract class AbstractSearchTerm {

    private final SearchTermEnum type;

    private final String fieldName;

    AbstractSearchTerm(SearchTermEnum type, String fieldName) {
        this.type = type;
        this.fieldName = fieldName;
    }

    public SearchTermEnum getType() {
        return type;
    }

    public String getFieldName() {
        return fieldName;
    }

}
