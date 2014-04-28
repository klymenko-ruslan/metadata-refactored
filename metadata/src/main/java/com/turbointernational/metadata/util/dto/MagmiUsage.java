package com.turbointernational.metadata.util.dto;

/**
 *
 * @author jrodriguez
 */
public class MagmiUsage {

    public final Long principalId;

    public final Long sku;

    public final String manufacturer;

    public final String partNumber;

    public final Long tiSku;

    public final String tiPartNumber;

    public final String partType;

    public final String turboType;

    public final String turboPartNumber;

    public MagmiUsage(
        Long principalId,
        Long sku,
        String manufacturer,
        String partNumber,
        Long tiSku,
        String tiPartNumber,
        String partType,
        String turboType,
        String turboPartNumber) {
        
        this.principalId = principalId;
        this.sku = sku;
        this.manufacturer = manufacturer;
        this.partNumber = partNumber;
        this.tiSku = tiSku;
        this.tiPartNumber = tiPartNumber;
        this.partType = partType;
        this.turboType = turboType;
        this.turboPartNumber = turboPartNumber;
    }

}
