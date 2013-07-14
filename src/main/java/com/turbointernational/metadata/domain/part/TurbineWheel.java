package com.turbointernational.metadata.domain.part;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@Inheritance(strategy=InheritanceType.JOINED)
public class TurbineWheel extends Part {
    private Float exduceOa;

    private Float tipHeightB;

    private Float inducerOc;

    private Float journalOd;

    private Float stemOe;

    private String trimNoBlades;
    
    private String shaftThreadF;
}
