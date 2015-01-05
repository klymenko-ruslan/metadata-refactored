
package com.turbointernational.metadata.magmi.dto;

import com.google.common.collect.Sets;
import com.turbointernational.metadata.domain.type.PartType;
import flexjson.JSON;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.Set;

/**
 *
 * @author jrodriguez
 */
public class MagmiBomItem {
    
    /**
     * Descendant part ID
     */
    private final long sku;
    
    private final int quantity;
    
    private final int distance;
    
    private final boolean hasBom;
    
    /**
     * @see PartType#getParent()
     * @see PartType#getValue()
     */
    private final String partTypeParent;
    
    private final Set<Long> altSku = Sets.newTreeSet();
    
    private final Set<Long> tiPartSku = Sets.newTreeSet();

    public MagmiBomItem(long sku, int quantity, int distance, boolean hasBom, String partTypeParent) {
        this.sku = sku;
        this.quantity = quantity;
        this.distance = distance;
        this.hasBom = hasBom;
        this.partTypeParent = partTypeParent;
    }

    public long getSku() {
        return sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getDistance() {
        return distance;
    }
    
    @JSON(name = "has_bom")
    public boolean isHasBom() {
        return hasBom;
    }

    @JSON(name = "part_type_parent")
    public String getPartTypeParent() {
        return partTypeParent;
    }
    
    @JSON(name = "alt_sku")
    public Set<Long> getAltSku() {
        return altSku;
    }

    @JSON(name = "ti_part_sku")
    public Set<Long> getTiPartSku() {
        return tiPartSku;
    }
    
    
    public static String toJsonArray(Collection<MagmiBomItem> collection) {
        return new JSONSerializer()
                .exclude("*.class")
                .serialize(collection);
    }
    
}
