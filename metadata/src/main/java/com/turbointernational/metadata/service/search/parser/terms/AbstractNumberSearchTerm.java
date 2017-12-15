package com.turbointernational.metadata.service.search.parser.terms;

/**
 * Created by dmytro.trunykov@zorallabs.com on 08.08.16.
 */
abstract class AbstractNumberSearchTerm extends AbstractSearchTerm {

    private final SearchTermCmpOperatorEnum cmpOperator;

    public AbstractNumberSearchTerm(SearchTermEnum type, String fieldName, SearchTermCmpOperatorEnum cmpOperator) {
        super(type, fieldName);
        this.cmpOperator = cmpOperator;
    }

    public SearchTermCmpOperatorEnum getCmpOperator() {
        return cmpOperator;
    }

    @Override
    public String toString() {
        return "AbstractNumberSearchTerm{" +
                "cmpOperator=" + cmpOperator +
                "} " + super.toString();
    }

}
