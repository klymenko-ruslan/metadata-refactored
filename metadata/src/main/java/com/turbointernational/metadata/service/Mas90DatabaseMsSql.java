package com.turbointernational.metadata.service;

import com.turbointernational.metadata.util.RegExpUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import static com.turbointernational.metadata.util.RegExpUtils.*;

/**
 * Created by dmytro.trunykov@zorallabs.com on 3/8/16.
 */
@Service
@Profile("!integration")
public class Mas90DatabaseMsSql implements Mas90Database {

    @Override
    public String getCountQuery() {
        // @formatter:off
         return "select count(*) " +
                "from ci_item as im join productLine_to_parttype_value as t2 " +
                "   on im.productline = t2.productLineCode " +
                "where " +
                "   im.itemcode like '" + MANUFACTURER_NUMBER_STR_REGEX_0 + "' or " +
                "   im.itemcode like '" + MANUFACTURER_NUMBER_STR_REGEX_1 + "' or " +
                "   im.itemcode like '" + MANUFACTURER_NUMBER_STR_REGEX_2 + "' or " +
                "   im.itemcode like '" + MANUFACTURER_NUMBER_STR_REGEX_3 + "'";
        // @formatter:on
    }

    @Override
    public String getItemsQuery() {
        return "select itemcode, itemcodedesc, productline, producttype  " +
               "from ci_item as im join productLine_to_parttype_value as t2 " +
               "   on im.productline = t2.productLineCode " +
               "where " +
               "   itemcode like '" + MANUFACTURER_NUMBER_STR_REGEX_0 + "' or " +
               "   itemcode like '" + MANUFACTURER_NUMBER_STR_REGEX_1 + "' or " +
               "   itemcode like '" + MANUFACTURER_NUMBER_STR_REGEX_2 + "' or " +
               "   itemcode like '" + MANUFACTURER_NUMBER_STR_REGEX_3 + "'";
    }

    @Override
    public String getBomsQuery() {
        return BOMS_QUERY;
    }

}
