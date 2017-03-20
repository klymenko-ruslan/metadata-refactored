package com.turbointernational.metadata;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
public class JpaConfigProps {

    private String platform;

    private String namingImplicitStrategy;

    private String namingPhysicalStrategy;

    private Boolean tempUseJdbcMetadataDefaults;

    private Boolean idNewGeneratorMapping;

    private Boolean showSql;

    private Boolean formatSql;

    private Boolean useSqlComments;

    private String ddlAuto;

    public Map<String, ?> toJpaPropersies() {
        Map<String, Object> props = new HashMap<>();
        if (platform != null) {
            props.put("hibernate.dialect", platform);
        }
        if (namingImplicitStrategy != null) {
            props.put("hibernate.naming.implicit-strategy", namingImplicitStrategy);
        }
        if (namingPhysicalStrategy != null) {
            props.put("hibernate.naming.physical-strategy", namingPhysicalStrategy);
        }
        if (tempUseJdbcMetadataDefaults != null) {
            props.put("hibernate.temp.use_jdbc_metadata_defaults", tempUseJdbcMetadataDefaults);
        }
        if (idNewGeneratorMapping != null) {
            props.put("hibernate.id.new_generator_mappings", idNewGeneratorMapping);
        }
        if (showSql != null) {
            props.put("show_sql", showSql);
        }
        if (formatSql != null) {
            props.put("format_sql", formatSql);
        }
        if (useSqlComments != null) {
            props.put("use_sql_comments", useSqlComments);
        }
        if (ddlAuto != null) {
            props.put("hibernate.hbm2ddl.auto", ddlAuto);
        }
        return props;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getNamingImplicitStrategy() {
        return namingImplicitStrategy;
    }

    public void setNamingImplicitStrategy(String namingImplicitStrategy) {
        this.namingImplicitStrategy = namingImplicitStrategy;
    }

    public String getNamingPhysicalStrategy() {
        return namingPhysicalStrategy;
    }

    public void setNamingPhysicalStrategy(String namingPhysicalStrategy) {
        this.namingPhysicalStrategy = namingPhysicalStrategy;
    }

    public Boolean getTempUseJdbcMetadataDefaults() {
        return tempUseJdbcMetadataDefaults;
    }

    public void setTempUseJdbcMetadataDefaults(Boolean tempUseJdbcMetadataDefaults) {
        this.tempUseJdbcMetadataDefaults = tempUseJdbcMetadataDefaults;
    }

    public Boolean getIdNewGeneratorMapping() {
        return idNewGeneratorMapping;
    }

    public void setIdNewGeneratorMapping(Boolean idNewGeneratorMapping) {
        this.idNewGeneratorMapping = idNewGeneratorMapping;
    }

    public String getDdlAuto() {
        return ddlAuto;
    }

    public void setDdlAuto(String ddlAuto) {
        this.ddlAuto = ddlAuto;
    }

    public Boolean getShowSql() {
        return showSql;
    }

    public void setShowSql(Boolean showSql) {
        this.showSql = showSql;
    }

    public Boolean getFormatSql() {
        return formatSql;
    }

    public void setFormatSql(Boolean formatSql) {
        this.formatSql = formatSql;
    }

    public Boolean getUseSqlComments() {
        return useSqlComments;
    }

    public void setUseSqlComments(Boolean useSqlComments) {
        this.useSqlComments = useSqlComments;
    }

}
