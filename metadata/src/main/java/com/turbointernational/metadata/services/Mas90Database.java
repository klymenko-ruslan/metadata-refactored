package com.turbointernational.metadata.services;

/**
 * This interface define methods to obtain SQL statements to work with MAS90 database servers.
 *
 * We use different MAS90 databases servers:
 * <dl>
 *     <dt>MS SQL</dt>
 *     <dd>In development and production environments.</dd>
 *     <dt>HSQLDB</dt>
 *     <dd>In integration tests.</dd>
 * </dl>
 *
 * Different MAS90 database servers have different SQL dialects.
 * So different implementations of this interface allow provide correct SQL statements depending on
 * the database server.
 *
 * @see Mas90DatabaseMsSql
 * @see Mas90DatabaseHsqldb
 *
 * Created by dmytro.trunykov@zorallabs.com on 3/8/16.
 */
public interface Mas90Database {

    String MANUFACTURER_NUMBER_STR_REGEX_0 = "[0-9]-[a-z|A-Z]-[0-6][0-9][0-9][0-9]";
    String MANUFACTURER_NUMBER_STR_REGEX_1 = "[0-9][0-9]-[a-z|A-Z]-[0-6][0-9][0-9][0-9]";

    // @formatter:off
    String BOMS_QUERY =
            "select bd.componentitemcode, bd.quantityperbill " +
            "from " +
            "   bm_billdetail as bd join ( " +
            "       select billno, max(revision) as last_revision from bm_billheader group by billno " +
            "   ) as br on bd.billno = br.billno and bd.revision = br.last_revision " +
            "where bd.billno = ?";
    // @formatter:on

    String getCountQuery();

    String getItemsQuery();

    String getBomsQuery();

}
