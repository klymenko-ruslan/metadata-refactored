package com.turbointernational.metadata.domain.part;

import com.turbointernational.metadata.domain.GenericDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jrodriguez
 */
@Repository
public class ProductImageDao extends GenericDao<ProductImage> {

    public static String getResizedFilename(Long partId, Long imageId, int size) {
        return size + "/" + partId + "_" + imageId + ".jpg";
    }

    public ProductImageDao() {
        super(ProductImage.class);
    }
}
