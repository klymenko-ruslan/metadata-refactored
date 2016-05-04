package com.turbointernational.metadata.domain.part.types;

import com.turbointernational.metadata.domain.part.Part;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by dmytro.trunykov@zorallabs.com on 04.05.16.
 */
@Entity
@Table(name="actuator")
@PrimaryKeyJoinColumn(name = "part_id")
public class TurbineHousing extends Part {
}
