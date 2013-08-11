package com.turbointernational.metadata.domain;

import com.turbointernational.metadata.domain.other.Interchange;
import com.turbointernational.metadata.domain.security.Group;
import com.turbointernational.metadata.domain.security.Role;
import com.turbointernational.metadata.domain.security.User;
import com.turbointernational.metadata.domain.type.CoolType;
import com.turbointernational.metadata.domain.type.GasketType;
import com.turbointernational.metadata.domain.type.KitType;
import com.turbointernational.metadata.domain.type.ManufacturerType;
import com.turbointernational.metadata.domain.type.PartType;
import com.turbointernational.metadata.domain.type.SealType;
import com.turbointernational.metadata.domain.type.TurboType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.roo.addon.web.mvc.controller.converter.RooConversionService;

/**
 * A central place to register application converters and formatters. 
 */
@RooConversionService
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);


        registry.addConverter(new Converter<CoolType, String>() {
            public String convert(CoolType source) {
                return source.getName();
            }
        });

        registry.addConverter(new Converter<GasketType, String>() {
            public String convert(GasketType source) {
                return source.getName();
            }
        });

        registry.addConverter(new Converter<Group, String>() {
            public String convert(Group source) {
                return source.getName();
            }
        });

        registry.addConverter(new Converter<KitType, String>() {
            public String convert(KitType source) {
                return source.getName();
            }
        });

        registry.addConverter(new Converter<ManufacturerType, String>() {
            public String convert(ManufacturerType source) {
                return source.getName();
            }
        });

        registry.addConverter(new Converter<TurboType, String>() {
            public String convert(TurboType source) {
                return source.getName();
            }
        });

        registry.addConverter(new Converter<PartType, String>() {
            public String convert(PartType source) {
                return source.getName();
            }
        });

        registry.addConverter(new Converter<Role, String>() {
            public String convert(Role source) {
                return source.getDisplay();
            }
        });

        registry.addConverter(new Converter<SealType, String>() {
            public String convert(SealType source) {
                return source.getName();
            }
        });

        registry.addConverter(new Converter<User, String>() {
            public String convert(User source) {
                return source.getName();
            }
        });

        registry.addConverter(new Converter<Interchange, String>() {
            public String convert(Interchange source) {
                return source.getName();
            }
        });
	}
}
