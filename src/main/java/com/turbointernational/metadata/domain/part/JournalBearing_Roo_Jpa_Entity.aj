// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.part;

import com.turbointernational.metadata.domain.part.JournalBearing;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

privileged aspect JournalBearing_Roo_Jpa_Entity {
    
    declare @type: JournalBearing: @Entity;
    
    declare @type: JournalBearing: @Table(name = "JOURNAL_BEARING");
    
    declare @type: JournalBearing: @Inheritance(strategy = InheritanceType.JOINED);
    
}
