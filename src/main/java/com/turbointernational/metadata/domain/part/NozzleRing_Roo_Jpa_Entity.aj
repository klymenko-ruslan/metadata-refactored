// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.part;

import com.turbointernational.metadata.domain.part.NozzleRing;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

privileged aspect NozzleRing_Roo_Jpa_Entity {
    
    declare @type: NozzleRing: @Entity;
    
    declare @type: NozzleRing: @Table(name = "NOZZLE_RING");
    
    declare @type: NozzleRing: @Inheritance(strategy = InheritanceType.JOINED);
    
}
