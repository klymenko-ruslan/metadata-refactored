package com.turbointernational.metadata.services.mas90;

import com.turbointernational.metadata.services.mas90.pricing.CalculatedPrice;
import com.turbointernational.metadata.services.mas90.pricing.ItemPricing;
import com.turbointernational.metadata.services.mas90.pricing.Pricing;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

/**
 * Interface of the service to export data from MAS90 to Magento.
 *
 * MAS90 is the name of the external ERP software used by Company to maintain
 * the master price list and customer list.
 *
 * Created by dmytro.trunykov@zorallabs.com on 1/1/16.
 */
public interface Mas90 {

        SortedSet<String> getPriceLevels();

        Map<String, Pricing> getDefaultPriceLevelPricing();

        BigDecimal getStandardPrice(String itemNumber);

        ItemPricing getItemPricing(String itemNumber);

        List<CalculatedPrice> calculatePriceLevelPrices(final String priceLevel, final ItemPricing item);
}
