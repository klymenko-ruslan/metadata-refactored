package com.turbointernational.metadata.domain.part;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(table="NOZZLE_RING", inheritanceType = "JOINED")
public class NozzleRing extends Part{
}
