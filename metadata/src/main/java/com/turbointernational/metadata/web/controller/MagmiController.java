package com.turbointernational.metadata.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turbointernational.metadata.exception.PartNotFound;
import com.turbointernational.metadata.service.PriceService;
import com.turbointernational.metadata.web.dto.ProductPricesDto;
import com.turbointernational.metadata.web.dto.mas90.ArInvoiceHistoryDetailDto;

/**
 *
 * @author jrodriguez
 */
@Controller
@RequestMapping(value = { "/magmi", "/metadata/magmi" })
public class MagmiController {

    private enum InputTypeEnum {
        id, pn
    }

    @Autowired
    private PriceService priceService;

    @RequestMapping(value = "/prices", method = GET)
    @ResponseBody
    @Transactional(noRollbackFor = PartNotFound.class)
    @PreAuthorize("hasRole('ROLE_MAGMI_EXPORT') or hasIpAddress('127.0.0.1/32')")
    public List<ProductPricesDto> getProductPricesByIdsAsGet(@RequestParam(name = "id") List<Long> partIds)
            throws IOException {
        List<ProductPricesDto> retVal = priceService.getProductsPricesByIds(partIds);
        return retVal;
    }

    @RequestMapping(value = "/prices/bypn", method = GET)
    @ResponseBody
    @Transactional(noRollbackFor = PartNotFound.class)
    @PreAuthorize("hasRole('ROLE_MAGMI_EXPORT') or hasIpAddress('127.0.0.1/32')")
    public List<ProductPricesDto> getProductPricesByNumsAsGet(@RequestParam(name = "id") List<String> partNums)
            throws IOException {
        List<ProductPricesDto> retVal = priceService.getProductsPricesByNums(partNums);
        return retVal;
    }

    @RequestMapping(value = "/prices", method = POST)
    @ResponseBody
    @Transactional(noRollbackFor = PartNotFound.class)
    @PreAuthorize("hasRole('ROLE_MAGMI_EXPORT') or hasIpAddress('127.0.0.1/32')")
    public List<ProductPricesDto> getProductPricesByIdsAsPost(
            @RequestParam(name = "inputtype", defaultValue = "id") InputTypeEnum inputType, @RequestBody String strJson)
            throws IOException {
        List<ProductPricesDto> retVal;
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser jp = factory.createParser(strJson);
        JsonNode json = mapper.readTree(jp);
        switch (inputType) {
        case id:
            retVal = getPricesForPartIds(json);
            break;
        case pn:
            retVal = getPricesForPartNums(json);
            break;
        default:
            throw new AssertionError("Unsupported value for parameter 'inputtype': " + inputType);
        }
        return retVal;
    }

    @RequestMapping(value = "/invoice/history/detail", method = GET)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_MAGMI_EXPORT') or hasIpAddress('127.0.0.1/32')")
    public List<ArInvoiceHistoryDetailDto> getInvoiceHistoryDetail() {
        return null;
    }

    private List<ProductPricesDto> getPricesForPartIds(JsonNode json) {
        List<Long> partIds = new ArrayList<>(json.size());
        for (Iterator<JsonNode> iter = json.iterator(); iter.hasNext();) {
            JsonNode jn = iter.next();
            Long partId = jn.asLong();
            partIds.add(partId);
        }
        List<ProductPricesDto> retVal = priceService.getProductsPricesByIds(partIds);
        return retVal;
    }

    private List<ProductPricesDto> getPricesForPartNums(JsonNode json) {
        List<String> partNums = new ArrayList<>(json.size());
        for (Iterator<JsonNode> iter = json.iterator(); iter.hasNext();) {
            JsonNode jn = iter.next();
            String pn = jn.asText();
            partNums.add(pn);
        }
        List<ProductPricesDto> retVal = priceService.getProductsPricesByNums(partNums);
        return retVal;
    }

}
