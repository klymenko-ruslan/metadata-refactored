package com.turbointernational.mas90.entity;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@Entity
@Table(name = "productLine_to_parttype_value")
@Cacheable
// @formatter:off
@NamedQueries({
        @NamedQuery(
                name = "convertPartTypeValue2ProductLineCode",
                query = "select plpt.productLineCode from ProductLineToPartTypeValue plpt " +
                        "where plpt.partTypeValue = :partTypeValue"
        )
})
// @formatter:on
public class ProductLineToPartTypeValue implements Serializable {

    private static final long serialVersionUID = -5011506480626803083L;

    @Id
    @Column(name = "ProductLineCode", length = 50, nullable = false, unique = true, insertable = true, updatable = true)
    private String productLineCode;

    @Id
    @Column(name = "part_type_value", length = 50, nullable = false, unique = true, insertable = true, updatable = true)
    private String partTypeValue;

    public String getProductLineCode() {
        return productLineCode;
    }

    public void setProductLineCode(String productLineCode) {
        this.productLineCode = productLineCode;
    }

    public String getPartTypeValue() {
        return partTypeValue;
    }

    public void setPartTypeValue(String partTypeValue) {
        this.partTypeValue = partTypeValue;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((partTypeValue == null) ? 0 : partTypeValue.hashCode());
        result = prime * result + ((productLineCode == null) ? 0 : productLineCode.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProductLineToPartTypeValue other = (ProductLineToPartTypeValue) obj;
        if (partTypeValue == null) {
            if (other.partTypeValue != null)
                return false;
        } else if (!partTypeValue.equals(other.partTypeValue))
            return false;
        if (productLineCode == null) {
            if (other.productLineCode != null)
                return false;
        } else if (!productLineCode.equals(other.productLineCode))
            return false;
        return true;
    }

}
