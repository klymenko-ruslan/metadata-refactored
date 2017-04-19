package com.turbointernational.metadata.service;

import static com.turbointernational.metadata.util.RegExpUtils.PTRN_MANUFACTURER_NUMBER;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.dao.PartTypeDao;
import com.turbointernational.metadata.entity.PartType;
import com.turbointernational.metadata.entity.part.Part;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@Service
public class Mas90Service {

    private final static Logger log = LoggerFactory.getLogger(Mas90Service.class);

    public final static long TURBO_INTERNATIONAL_MANUFACTURER_ID = 11L;

    @Autowired
    private PartDao partDao;

    @Autowired
    private PartTypeDao partTypeDao;

    @Qualifier("dataSourceMas90")
    @Autowired
    private DataSource dataSourceMas90; // connections to MS-SQL (MAS90)

    private JdbcTemplate mas90db;

    @PostConstruct
    public void init() {
        mas90db = new JdbcTemplate(dataSourceMas90);
    }

    public Part findTurboInternationalPart(String manfrPartNum) {
        return partDao.findByPartNumberAndManufacturer(TURBO_INTERNATIONAL_MANUFACTURER_ID, manfrPartNum);
    }

    public boolean isManfrNum(String s) {
        return PTRN_MANUFACTURER_NUMBER.matcher(s).matches();
    }

    /**
     * MAS90 uses different part type codes than in the local database --
     * 'metadata'.
     *
     * This method makes mapping between product type codes in MAS90 and
     * 'metadata'.
     *
     * @return map ProductLineCode => PartType
     */
    public Map<String, PartType> loadPartTypesMap() throws SQLException {
        Map<String, PartType> retVal = new HashMap<>(50);
        mas90db.query("select ProductLineCode, part_type_value from productLine_to_parttype_value", rs -> {
            String productLineCode = rs.getString(1);
            String partTypeValue = rs.getString(2);
            PartType pt = partTypeDao.findPartTypeByValue(partTypeValue);
            if (pt != null) {
                retVal.put(productLineCode, pt);
                log.debug("Mapping: {} => {}", productLineCode, pt.getId());
            } else {
                String msg = String.format("Part type not found for productLineCode: '%1$s'.", productLineCode);
                throw new SQLException(msg);
            }
        });
        return retVal;
    }

}
