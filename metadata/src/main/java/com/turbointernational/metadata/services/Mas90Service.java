package com.turbointernational.metadata.services;

import com.turbointernational.metadata.services.mas90.MsAccessImpl;
import com.turbointernational.metadata.services.mas90.MsSqlImpl;
import com.turbointernational.metadata.services.mas90.pricing.CalculatedPrice;
import com.turbointernational.metadata.services.mas90.pricing.ItemPricing;
import com.turbointernational.metadata.services.mas90.pricing.Pricing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

/**
 * Created by dmytro.trunykov@zorallabs.com on 12/30/15.
 */
@Service
public class Mas90Service {

    public enum Implementation {MS_ACCESS, MS_SQL}

    public interface Mas90 {
        SortedSet<String> getPriceLevels();
        Map<String, Pricing> getDefaultPriceLevelPricing();
        BigDecimal getStandardPrice(String itemNumber);
        ItemPricing getItemPricing(String itemNumber);
        List<CalculatedPrice> calculatePriceLevelPrices(final String priceLevel, final ItemPricing item);
    }

    @Value("${mas90.db.path}")
    private String mas90DbPath;

    @Value("${mas90.db.mssql.url}")
    private String mas90mssqlUrl;

    @Value("${mas90.db.mssql.username}")
    private String mas90mssqlUsername;

    @Value("${mas90.db.mssql.password}")
    private String mas90mssqlPassword;

    public Mas90 getService(Implementation implementation) throws IOException {
        Mas90 retVal = null;
        switch (implementation) {
            case MS_ACCESS:
                retVal = new MsAccessImpl(new File(mas90DbPath));
                break;
            case MS_SQL:
                retVal = new MsSqlImpl(mas90mssqlUrl, mas90mssqlUsername, mas90mssqlPassword);
                break;
            default:
                throw new IllegalArgumentException("Unsupported service implementation: " + implementation);
        }
        return retVal;
    }

}
