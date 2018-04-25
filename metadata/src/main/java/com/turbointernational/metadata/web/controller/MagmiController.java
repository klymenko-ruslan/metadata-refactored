package com.turbointernational.metadata.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turbointernational.metadata.exception.PartNotFound;
import com.turbointernational.metadata.service.MagmiService;
import com.turbointernational.metadata.service.PriceService;
import com.turbointernational.metadata.util.View;
import com.turbointernational.metadata.web.dto.ProductPrices;
import com.turbointernational.metadata.web.dto.mas90.ArInvoiceHistoryDetailDto;
import com.turbointernational.metadata.web.dto.mas90.ArInvoiceHistoryHeaderDto;
import com.turbointernational.metadata.web.dto.mas90.InvoicesChunk;

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

    @Autowired
    private MagmiService magmiService;

    @RequestMapping(value = "/prices", method = GET)
    @ResponseBody
    @Transactional(noRollbackFor = PartNotFound.class)
    public List<ProductPrices> getProductPricesByIdsAsGet(@RequestParam(name = "id") List<Long> partIds)
            throws IOException {
        List<ProductPrices> retVal = priceService.getProductsPricesByIds(partIds);
        return retVal;
    }

    @RequestMapping(value = "/prices/bypn", method = GET)
    @ResponseBody
    @Transactional(noRollbackFor = PartNotFound.class)
    public List<ProductPrices> getProductPricesByNumsAsGet(@RequestParam(name = "id") List<String> partNums)
            throws IOException {
        List<ProductPrices> retVal = priceService.getProductsPricesByNums(partNums);
        return retVal;
    }

    @RequestMapping(value = "/prices", method = POST)
    @ResponseBody
    @Transactional(noRollbackFor = PartNotFound.class)
    public List<ProductPrices> getProductPricesByIdsAsPost(
            @RequestParam(name = "inputtype", defaultValue = "id") InputTypeEnum inputType, @RequestBody String strJson)
            throws IOException {
        List<ProductPrices> retVal;
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

    @RequestMapping(value = "/invoice/history", method = GET)
    @ResponseBody
    @JsonView(View.Summary.class)
    public InvoicesChunk getInvoiceHistory(@RequestParam(name = "startDate", required = false) Long startDate,
            @RequestParam(name = "limitDays", defaultValue = "0", required = false) int limitDays) throws SQLException {
        return magmiService.getInvoiceHistory(startDate, limitDays);
    }

    @RequestMapping(value = "/invoice/history/header", method = POST)
    @ResponseBody
    @JsonView(View.Summary.class)
    public List<ArInvoiceHistoryHeaderDto> getInvoiceHistoryHeader(
            @RequestBody List<ArInvoiceHistoryHeaderDto.Key> request) {
        List<ArInvoiceHistoryHeaderDto> retVal = magmiService.getInvoiceHistoryHeader(request);
        return retVal;
    }

    @RequestMapping(value = "/invoice/history/detail", method = POST)
    @ResponseBody
    @JsonView(View.Summary.class)
    public List<ArInvoiceHistoryDetailDto> getInvoiceHistoryDetail(
            @RequestBody List<ArInvoiceHistoryDetailDto.Key> request) {
        List<ArInvoiceHistoryDetailDto> retVal = magmiService.getInvoiceHistoryDetail(request);
        return retVal;
    }

    private List<ProductPrices> getPricesForPartIds(JsonNode json) {
        List<Long> partIds = new ArrayList<>(json.size());
        for (Iterator<JsonNode> iter = json.iterator(); iter.hasNext();) {
            JsonNode jn = iter.next();
            Long partId = jn.asLong();
            partIds.add(partId);
        }
        List<ProductPrices> retVal = priceService.getProductsPricesByIds(partIds);
        return retVal;
    }

    private List<ProductPrices> getPricesForPartNums(JsonNode json) {
        List<String> partNums = new ArrayList<>(json.size());
        for (Iterator<JsonNode> iter = json.iterator(); iter.hasNext();) {
            JsonNode jn = iter.next();
            String pn = jn.asText();
            partNums.add(pn);
        }
        List<ProductPrices> retVal = priceService.getProductsPricesByNums(partNums);
        return retVal;
    }

}
