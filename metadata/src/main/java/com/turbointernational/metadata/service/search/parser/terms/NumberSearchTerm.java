package com.turbointernational.metadata.service.search.parser.terms;

/**
 * Created by dmytro.trunykov@zorallabs.com on 08.08.16.
 */
public class NumberSearchTerm extends AbstractNumberSearchTerm {

    private final Number term;

    NumberSearchTerm(SearchTermEnum type, String fieldName, SearchTermCmpOperatorEnum cmpOperator, Number term) {
        super(type, fieldName, cmpOperator);
        this.term = term;
    }

    public Number getTerm() {
        return term;
    }

    @Override
    public String toString() {
        return "NumberSearchTerm{" +
                "term=" + term +
                "} " + super.toString();
    }
}
