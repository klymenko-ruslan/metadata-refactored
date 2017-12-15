package com.turbointernational.metadata.service.search.parser.terms;

import static com.turbointernational.metadata.service.search.parser.terms.SearchTermEnum.TEXT;

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

    @Override
    public String toString() {
        return "TextSearchTerm{" +
                "term='" + term + '\'' +
                "} " + super.toString();
    }

}
