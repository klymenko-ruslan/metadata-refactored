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
public class CompressorWheel extends Part {
    private Float inducerOa;

    private Float tipHeightB;

    private Float exducerOc;

    private Float hubLengthD;

    private Float boreOe;

    private String trimNoBlades;

    private String application;
}
