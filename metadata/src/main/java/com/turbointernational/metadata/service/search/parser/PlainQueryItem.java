package com.turbointernational.metadata.service.search.parser;

import static com.turbointernational.metadata.service.search.parser.AbstractQueryItem.TypeEnum.PLAIN;

import com.turbointernational.metadata.service.search.parser.terms.AbstractSearchTerm;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
public class PlainQueryItem extends AbstractQueryItem {

    private final AbstractSearchTerm term;

    public PlainQueryItem(AbstractSearchTerm term) {
        super(PLAIN);
        this.term = term;
    }

    public AbstractSearchTerm getTerm() {
        return term;
    }

}
