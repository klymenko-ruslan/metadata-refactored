package com.turbointernational.metadata.services.search.parser;

import static com.turbointernational.metadata.services.search.parser.SearchTermEnum.BOOLEAN;

/**
 * Created by dmytro.trunykov@zorallabs.com on 08.08.16.
 */
public class BooleanSearchTerm extends AbstractSearchTerm {

    private final Boolean term;

    BooleanSearchTerm(String fieldName, Boolean term) {
        super(BOOLEAN, fieldName);
        this.term = term;
    }

    public Boolean getTerm() {
        return term;
    }

}
