package com.turbointernational.metadata.service.search.parser;

import static com.turbointernational.metadata.service.search.parser.SearchTermEnum.DECIMAL_RANGE;

/**
 * Created by dmytro.trunykov@zorallabs.com on 08.08.16.
 */
public class RangeSearchTerm extends AbstractSearchTerm  {

    private final Number from;
    private final Number to;

    RangeSearchTerm(String fieldName, Number from, Number to) {
        super(DECIMAL_RANGE, fieldName);
        this.from = from;
        this.to = to;
    }

    public Number getFrom() {
        return from;
    }

    public Number getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "RangeSearchTerm{" +
                "from=" + from +
                ", to=" + to +
                "} " + super.toString();
    }

}
