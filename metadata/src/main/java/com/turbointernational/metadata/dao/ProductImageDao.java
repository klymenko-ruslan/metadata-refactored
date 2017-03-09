package com.turbointernational.metadata.dao;

import org.springframework.stereotype.Repository;

import com.turbointernational.metadata.entity.part.ProductImage;

/**
 *
 * @author jrodriguez
 */
@Repository
public class ProductImageDao extends AbstractDao<ProductImage> {

    public static String getResizedFilename(Long partId, Long imageId, int size) {
        return size + "/" + partId + "_" + imageId + ".jpg";
    }

    public ProductImageDao() {
        super(ProductImage.class);
    }

}
