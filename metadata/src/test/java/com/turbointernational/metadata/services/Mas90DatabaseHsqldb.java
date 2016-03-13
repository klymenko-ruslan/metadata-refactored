package com.turbointernational.metadata.services;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Implementation of the interface {@link Mas90Database} with the HSQL database as backend.
 * This implementation is used in integration tests only.
 *
 * Created by dmytro.trunykov@zorallabs.com on 3/8/16.
 */
@Service
@Profile("integration")
public class Mas90DatabaseHsqldb implements Mas90Database {

    @Override
    public String getCountQuery() {
        // @formatter:off
         return "select count(*) " +
                "from ci_item as im join productLine_to_parttype_value as t2 " +
                "   on im.productline = t2.productLineCode " +
                "where " +
                "   im.itemcode like '" + MANUFACTURER_NUMBER_STR_REGEX_0 + "' or " +
                "   im.itemcode like '" + MANUFACTURER_NUMBER_STR_REGEX_1 + "'";
        // @formatter:on
    }

    @Override
    public String getItemsQuery() {
        return "select itemcode, itemcodedesc, productline, producttype  " +
               "from ci_item as im join productLine_to_parttype_value as t2 " +
               "   on im.productline = t2.productLineCode " +
               "where " +
               "   regexp_matches(itemcode, '" + MANUFACTURER_NUMBER_STR_REGEX_0 + "') or " +
               "   regexp_matches(itemcode, '" + MANUFACTURER_NUMBER_STR_REGEX_1 + "')";
    }

    @Override
    public String getBomsQuery() {
        return BOMS_QUERY;
    }

}
