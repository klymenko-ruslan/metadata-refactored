package com.turbointernational.metadata.web.dto;

import javax.validation.constraints.NotNull;

/**
 *
 * @author jrodriguez
 */
public class CreateBomItemRequest {
    
    @NotNull
    private Long childPartId;
    
    @NotNull
    private Long parentPartId;
    
    @NotNull
    private Integer quantity;

    public Long getChildPartId() {
        return childPartId;
    }

    public void setChildPartId(Long childPartId) {
        this.childPartId = childPartId;
    }

    public Long getParentPartId() {
        return parentPartId;
    }

    public void setParentPartId(Long parentPartId) {
        this.parentPartId = parentPartId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
