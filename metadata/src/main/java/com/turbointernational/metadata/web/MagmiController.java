package com.turbointernational.metadata.web;

import au.com.bytecode.opencsv.CSVWriter;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.other.ManufacturerDao;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.PartDao;
import com.turbointernational.metadata.domain.type.*;
import com.turbointernational.metadata.magmi.MagmiDataFinder;
import com.turbointernational.metadata.magmi.dto.MagmiProduct;
import com.turbointernational.metadata.services.Mas90Service;
import com.turbointernational.metadata.services.mas90.pricing.CalculatedPrice;
import com.turbointernational.metadata.services.mas90.pricing.ItemPricing;
import com.turbointernational.metadata.services.mas90.pricing.UnknownDiscountCodeException;
import com.turbointernational.metadata.util.ImageResizer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.Map.Entry;

/**
 *
 * @author jrodriguez
 */
@Controller
@RequestMapping(value={"/magmi", "/metadata/magmi"})
public class MagmiController {

    private static final Logger logger = LoggerFactory.getLogger(MagmiController.class);

    @Value("${magmi.batch.size}")
    Integer magmiBatchSize;

    @Value("${magmi.finderId.application}")
    String finderIdApplication;
    
    @Value("${magmi.finderId.turbo}")
    String finderIdTurbo;
    
    @Autowired
    ImageResizer imageResizer;
    
    @Autowired
    MagmiDataFinder magmiDataFinder;
    
    @Autowired
    EntityManager entityManager;
    
    @Autowired
    ManufacturerDao manufacturerDao;
    
    @Autowired
    ManufacturerTypeDao manufacturerTypeDao;
    
    @Autowired
    PartTypeDao partTypeDao;
    
    @Autowired
    CoolTypeDao coolTypeDao;
    
    @Autowired
    GasketTypeDao gasketTypeDao;
    
    @Autowired
    KitTypeDao kitTypeDao;
    
    @Autowired
    SealTypeDao sealTypeDao;
    
    @Autowired
    PartDao partDao;
    
    @Autowired
    JdbcTemplate db;

    @Autowired
    Mas90Service mas90Service;

    public String[] getCsvHeaders(SortedSet<String> priceLevels) {
        List<String> headers = new ArrayList<String>();

        //<editor-fold defaultstate="collapsed" desc="Basic">
        headers.addAll(Arrays.asList(
            "sku",
            "magmi:delete",
            "part_type",
            "attribute_set",
            "type",
            "visibility",
            "status",
            "name",
            "description",
            "manufacturer",
            "part_number",
            "part_number_short",
            "categories",
            "ti_part_number",    // First interchangeable TI part number
            "ti_part_sku",       // Interchangeable parts by TI
            "interchanges",      // Interchangeable parts
            "bill_of_materials", // BOM
            "where_used",        // Usages
            "has_ti_chra",       // Has TI CHRA descendant
            "has_ti_interchange",
            "has_foreign_interchange",
            "price",
            "qty",
            "turbo_model",      // Turbo Models
            "turbo_type",       // Turbo Types

            // Product images
            "image",
            "small_image",
            "thumbnail",
            "media_gallery",
            "standard_oversize_part"
        ));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Types">
        headers.addAll(Arrays.asList(
            "kit_type",
            "gasket_type",
            "seal_type",
            "cool_type"
        ));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Part Type Specifics">
        headers.addAll(Arrays.asList(
             // Cartridge
            "service_kits",

            // Other
            "bearing_type",
            "bore_oe",
            "compressor_housing_diameter",
            "compressor_wheel_diameter",
            "design_features",
            "exducer_oa",
            "exducer_oc",
            "hub_length_d",
            "i_gap_max",
            "i_gap_min",
            "inducer_diameter",
            "inducer_oa",
            "inducer_oc",
            "inside_diameter",
            "inside_diameter_max",
            "inside_diameter_min",
            "journal_od",
            "number_of_blades",
            "oil",
            "oil_inlet",
            "oil_outlet",
            "outlet_flange_holes",
            "outside_diameter_max",
            "outside_diameter_min",
            "outside_dim_max",
            "outside_dim_min",
            "overall_diameter",
            "overall_height",
            "piston_ring_diameter",
            "secondary_diameter",
            "shaft_thread_f",
            "stem_oe",
            "style_compressor_wheel",
            "tip_height_b",
            "water_ports",
            "width_jb",
            "width_max",
            "width_min"
        ));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="MAS90 Prices">
        headers.add("customerprice");

        // Group Prices
        for (String priceLevel: priceLevels) {
            headers.add("group_price:ERP_PL_" + priceLevel);
        }

        // Tier Prices
        for (String priceLevel: priceLevels) {
            headers.add("tier_price:ERP_PL_" + priceLevel);
        }
        //</editor-fold>


        headers.addAll(Arrays.asList(

            // Make,Year,Model
            "finder:" + finderIdApplication,

            // Manufacturer,TurboType,TurboModel
            "finder:" + finderIdTurbo,

            // Make!!Model!!Year!!Displacement!!Fuel||...
            "application_detail",

            // OEM SKU (custom option, used to show OEM part in cart)
            "OEMSKU:field:0"
        ));

        return headers.toArray(new String[0]);
    }

    @RequestMapping("/products")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasRole('ROLE_MAGMI_EXPORT') or hasIpAddress('127.0.0.1/32')")
    public void products(HttpServletResponse response, OutputStream out,
                         @RequestParam(name = "impl", required = false, defaultValue = "MS_SQL")
                         Mas90Service.Implementation implementation,
                         @RequestParam(defaultValue="30", required=false) int days) throws Exception {
        logger.info("Magmi export started.");
        
        response.setHeader("Content-Type", "text/csv");
        response.setHeader("Content-Disposition: attachment; filename=products.csv", null);
        
        long startTime = System.currentTimeMillis();
        
        Mas90Service.Mas90 mas90 = mas90Service.getService(implementation);
        CSVWriter writer = new CSVWriter(new OutputStreamWriter(out), ',', '\'', '\\');
        
        // Write the header row
        writer.writeNext(getCsvHeaders(mas90.getPriceLevels()));
        
        // Get the product IDs to retrieve
        int position = 0;
        Long lastSuccessfulSku = null;
        List<Part> parts = Collections.emptyList();
        do {
            try {
                
                // Clear Hibernate
                entityManager.clear();
                
                // Pre-cache our frequently-used entities
                manufacturerTypeDao.findAll();
                manufacturerDao.findAll();
                partTypeDao.findAll();
                coolTypeDao.findAll();
                gasketTypeDao.findAll();
                kitTypeDao.findAll();
                sealTypeDao.findAll();

                // Get the next batch of part IDs
                parts = partDao.findAll(position, magmiBatchSize);

                // Process each product
                for (MagmiProduct product : magmiDataFinder.findMagmiProducts(parts).values()) {
                    try {
                        writer.writeNext(magmiProductToCsvRow(mas90, product));
                        
                        // Debugging variable
                        lastSuccessfulSku = product.getSku();
                    } catch (Exception e) {
                        logger.warn("Could not write magmi part " + product.getSku(), e);
                    }
                }

                // Update the position
                position += parts.size();
            } catch (Exception e) {
                logger.error("Batch failed! position: " + position
                            + ", batch size: " + magmiBatchSize
                            + ", lastSuccessfulSku: " + lastSuccessfulSku,
                    e);
            }
        } while (parts.size() >= magmiBatchSize);
        
        
        // Deleted products
        List<String> deletedIds = db.queryForList(
                  "SELECT"
                + "  `id`"
                + "  FROM `deleted_parts`"
                + "  WHERE dt > DATE_SUB(NOW(), INTERVAL ? DAY)",
                String.class, days);
        
        for (String id : deletedIds) {
            writer.writeNext(new String[] {id, "1"});
        }
        
        
        writer.flush();
        writer.close();
        
        logger.info("Exported {} products in {}ms", position, System.currentTimeMillis() - startTime);
    }
    
    @RequestMapping("/product/{partId}")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasRole('ROLE_MAGMI_EXPORT') or hasIpAddress('127.0.0.1/32')")
    public void product(HttpServletResponse response, OutputStream out, @PathVariable Long partId,
                         @RequestParam(name = "impl", required = false, defaultValue = "MS_SQL")
                         Mas90Service.Implementation implementation) throws Exception {
        
        response.setHeader("Content-Type", "text/csv");
        response.setHeader("Content-Disposition: attachment; filename=products.csv", null);

        Mas90Service.Mas90 mas90 = mas90Service.getService(implementation);
        CSVWriter writer = new CSVWriter(new OutputStreamWriter(out), ',', '\'', '\\');
        
        // Write the header row
        writer.writeNext(getCsvHeaders(mas90.getPriceLevels()));
        
        Part part = partDao.findOne(partId);
        List<Part> parts = new ArrayList<Part>();
        parts.add(part);
        
        Iterator<MagmiProduct> it = magmiDataFinder.findMagmiProducts(parts).values().iterator();
        while (it.hasNext()) {
            writer.writeNext(magmiProductToCsvRow(mas90, it.next()));
        }
        
        writer.flush();
        writer.close();
    }
    
    private String[] magmiProductToCsvRow(Mas90Service.Mas90 mas90, MagmiProduct product) {
        Map<String, String> columns = product.getCsvColumns();
        product.csvFinderColumns(columns, finderIdApplication, finderIdTurbo);
        product.csvImageColumns(columns, imageResizer);
        
        // Only TI parts get this info
        if (product.hasTiPart()) {

            // Default to 1
            columns.put("qty", "1");
            
            // ERP Prices
            if (product.getManufacturerId() == Manufacturer.TI_ID) {
                addErpPrices(mas90, columns, product);
            }
        } else {
            columns.put("qty", "0");
        }

        // Map the column into a value array for the CSV writer
        String[] csvHeaders = getCsvHeaders(mas90.getPriceLevels());
        String[] valueArray = new String[csvHeaders.length];
        for (int i = 0; i < csvHeaders.length; i++) {
            
            String header = csvHeaders[i];
            
            valueArray[i] = StringUtils.defaultIfEmpty(columns.get(header), "");
        }
        
        return valueArray;
    }
    
    private void addErpPrices(Mas90Service.Mas90 mas90, Map<String, String> columns, MagmiProduct product) {
        try {
            
            // Stop now if there's no part number
            if (StringUtils.isBlank(product.getPartNumber())) {
                return;
            }
            
            // Get the pricing info
            ItemPricing itemPricing = mas90.getItemPricing(product.getPartNumber());

            // Stop if there's no standard price
            if (itemPricing.getStandardPrice() == null) {
                logger.info("Missing standard price product: {}", product.getPartNumber());
                return;
            }
            
            columns.put("price", itemPricing.getStandardPrice().toString());

            addErpCustomerPrices(mas90, itemPricing, columns);
            addErpGroupPrices(mas90, itemPricing, columns);
        } catch (UnknownDiscountCodeException e) {
                logger.warn("Unknown discount code {} for product {}", e.getCode(), product.getPartNumber());
        } catch (EmptyResultDataAccessException e) {
            logger.warn("Missing prices for product: {}", product.getPartNumber());
        } catch (IOException e) {
            logger.error("Error getting prices from MAS90 db", e);
        }
    }
    
    // bob@example.com;0:$1.00;10:$0.95;20:$0.90|jim@example.com....
    private void addErpCustomerPrices(Mas90Service.Mas90 mas90, ItemPricing itemPricing, Map<String, String> columns) throws IOException {
        
        // Build the customer price string
        StringBuilder priceString = new StringBuilder();
        for (Entry<String, List<CalculatedPrice>> entry : itemPricing.calculateCustomerSpecificPrices().entrySet()) {
            
            // Get the entry info
            String customerEmail = entry.getKey();
            List<CalculatedPrice> prices = entry.getValue();
            
            if (StringUtils.isBlank(customerEmail)) {
                continue;
            }
            
            // Add a separator is this isn't the first customer
            if (priceString.length() > 0) {
                priceString.append('|');
            }
            
            priceString.append(customerEmail);
            
            // MAS90's uses "up to this quantity", Magento is "this quantity and up"
            // Keep track of the previous quantity so we can use the proper quantity in Magento
            int previousQuantity = 0;
            
            // Build the value string "quantity1:price1;quantityN:priceN"
            for (CalculatedPrice price : prices) {
                priceString.append(";");
                
                priceString.append(previousQuantity + 1);
                priceString.append(":");
                priceString.append(price.getPrice());
                
                // Update the previous quantity
                previousQuantity = price.getQuantity();
            }
        }
        
        columns.put("customerprice", priceString.toString());
    }
    
    private void addErpGroupPrices(Mas90Service.Mas90 mas90, ItemPricing itemPricing, Map<String, String> columns) throws IOException {
        
        // Add column data for each price level, group and tier prices
        for (String priceLevel : mas90.getPriceLevels()) {
            
            // Get the price level pricing
            StringBuilder priceString = new StringBuilder();
            List<CalculatedPrice> prices = mas90.calculatePriceLevelPrices(priceLevel, itemPricing);
            
            // MAS90's uses "up to this quantity", Magento is "this quantity and up"
            // Keep track of the previous quantity so we can use the proper quantity in Magento
            int previousQuantity = 0;
            
            // Build the value string "quantity1:price1;quantityN:priceN"
            for (CalculatedPrice price : prices) {
                if (price.getBreakLevel() > 0) {
                    priceString.append(";");
                }
                
                priceString.append(previousQuantity + 1);
                priceString.append(":");
                priceString.append(price.getPrice());
                
                // Update the previous quantity
                previousQuantity = price.getQuantity();
            }
            
            // Add the tier price column if we have more than a base price
            if (prices.size() > 1) {
                columns.put("tier_price:ERP_PL_" + priceLevel, priceString.toString());
            }
            
            // Add the group price column
            if (!prices.isEmpty()) {
                columns.put("group_price:ERP_PL_" + priceLevel, prices.get(0).getPrice().toString());
            }
        }
    }
    
}
