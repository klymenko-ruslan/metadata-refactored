package com.turbointernational.metadata.services.search.parser;

import static com.turbointernational.metadata.services.search.parser.SearchTermEnum.TEXT;

/**
 * Created by dmytro.trunykov@zorallabs.com on 08.08.16.
 */
public class TextSearchTerm extends AbstractSearchTerm {

    private final String term;

    TextSearchTerm(String fieldName, String term) {
        super(TEXT, fieldName);
        this.term = term;
    }

    public String getTerm() {
        return term;
    }

}