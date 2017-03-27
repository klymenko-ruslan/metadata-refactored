package com.turbointernational.metadata.util;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
public class JpaUtils {

    /**
     * Build predicate for SQL expression: lower(colName) like('%?%'). A value of the '?' is converted to a lower case.
     *
     * @param cb JPA criteria builder
     * @param colName column
     * @param s string to search
     * @return
     */
    public static Predicate broadLike(CriteriaBuilder cb, Path<String> colName, String s) {
        return cb.equal(cb.lower(colName), "%" + s.toLowerCase() + "%");
    };

}
