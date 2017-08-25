package com.turbointernational.metadata;

/**
 * Objects which  are subjects for the Audit Log should implement this interface.
 * @author dmytro.trunykov@zorallabs.com
 */
public interface Auditable {

    /**
     * Get representation of the object in a text form.
     *
     * @return
     */
    String toAuditLog();

}
