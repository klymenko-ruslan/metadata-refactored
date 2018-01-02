package com.turbointernational.metadata.service.search.parser.terms;

import static com.turbointernational.metadata.service.search.parser.terms.SearchTermEnum.BOOLEAN;

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

    @Override
    public String toString() {
        return "BooleanSearchTerm{" +
                "term=" + term +
                "} " + super.toString();
    }

}