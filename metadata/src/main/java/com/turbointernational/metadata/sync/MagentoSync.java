package com.turbointernational.metadata.sync;

import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.magento.rest.MagentoRest;
import com.turbointernational.metadata.magento.soap.MagentoSoap;
import java.util.Date;
import java.util.List;
import net.sf.jsog.JSOG;

public class MagentoSync {

    MagentoRest rest;
    MagentoSoap soap;

    public void synchronize(Date lastUpdated) {
        int pageSize = 1000;
        int page = 0;
        List<Part> parts;
        do {
            parts = Part.getPartsUpdatedAfter(lastUpdated, page * pageSize, pageSize);

            for (Part part : parts) {
                updatePart(part);
            }

            page++;

        } while (parts.size() >= pageSize);

    }

    private void updatePart(Part part) {

        // Get the part as JSOG, delete/transform the values for Magento
        JSOG partJsog = part.toJsog();
        partJsog.remove("id");

        // Get the current product
        JSOG originalProduct = getProduct(part);

        // Merge in the current part's properties
        JSOG updatedProduct = originalProduct.clone().merge(partJsog);

        // If they're different, update the part
        if (!originalProduct.equals(updatedProduct)) {
            rest.updateProduct(originalProduct.get("id").getIntegerValue(), partJsog);
        }

    }

    public JSOG getProduct(Part part) {
        return rest.getProductById(part.getMagentoProductId());
    }
}