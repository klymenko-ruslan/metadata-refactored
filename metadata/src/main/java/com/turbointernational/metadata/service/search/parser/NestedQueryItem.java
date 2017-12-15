package com.turbointernational.metadata.service.search.parser;

import static com.turbointernational.metadata.service.search.parser.AbstractQueryItem.TypeEnum.NESTED;

import java.util.ArrayList;
import java.util.List;

import com.turbointernational.metadata.service.search.parser.terms.AbstractSearchTerm;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
public class NestedQueryItem extends AbstractQueryItem {

    private final String path;

    private final List<AbstractSearchTerm> terms;

    public NestedQueryItem(String path) {
        super(NESTED);
        this.path = path;
        this.terms = new ArrayList<>(30);
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @return the terms
     */
    public List<AbstractSearchTerm> getTerms() {
        return terms;
    }

    public void addTerm(AbstractSearchTerm term) {
        terms.add(term);
    }

}
