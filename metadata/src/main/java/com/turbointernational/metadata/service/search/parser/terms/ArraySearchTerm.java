package com.turbointernational.metadata.service.search.parser.terms;

import static com.turbointernational.metadata.service.search.parser.terms.SearchTermEnum.ARRAY;

/**
 * @author dmytro.trunykov@zorallabs.com
 *
 */
public class ArraySearchTerm extends AbstractSearchTerm {

    private final Object[] terms;

    ArraySearchTerm(String fieldName, Object[] terms) {
        super(ARRAY, fieldName);
        this.terms = terms;
    }

    public Object[] getTerms() {
        return terms;
    }

}
