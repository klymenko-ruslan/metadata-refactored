package com.turbointernational.metadata.domain.part.types;

import com.turbointernational.metadata.domain.part.Part;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="cartridge")
@PrimaryKeyJoinColumn(name = "part_id")
public class Cartridge extends Part {
    
}
