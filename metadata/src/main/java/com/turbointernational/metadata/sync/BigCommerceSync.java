package com.turbointernational.metadata.sync;

import com.turbointernational.metadata.domain.part.Part;

/**
 *
 * @author Akouvi
 */
public class BigCommerceSync {
    public void synchronize(Date lastUpdated) {
        int pageSize = 1000;
        int page = 0;
        List<Part> parts = null;
        do {
            parts = Part.getPartsUpdatedAfter(lastUpdated, page * pageSize, pageSize);
            
            for (Part part: parts) {
                updatePart(part);
            }
            
            page++;
            
        } while (parts.size() >= pageSize);
        
    }

    private void updatePart(Part part) {
        
        if (part instanceof PistonRing) {
            piston ring specific stuff
        }
        JSOG jsog = JSOG.object();
        jsog.put("name", part.getValue());
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
