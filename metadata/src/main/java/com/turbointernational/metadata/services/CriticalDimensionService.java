package com.turbointernational.metadata.services;

import com.turbointernational.metadata.domain.criticaldimension.CriticalDimension;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimensionDao;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimensionEnum;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimensionEnumVal;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.PartDao;
import com.turbointernational.metadata.domain.type.PartType;
import flexjson.JSONContext;
import flexjson.TransformerUtil;
import flexjson.TypeContext;
import flexjson.transformer.AbstractTransformer;
import flexjson.transformer.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.ValidationErrors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.turbointernational.metadata.domain.criticaldimension.CriticalDimension.DataTypeEnum.DECIMAL;
import static com.turbointernational.metadata.domain.criticaldimension.CriticalDimension.DataTypeEnum.TEXT;

/**
 * Created by dmytro.trunykov@zorallabs.com on 06.04.16.
 */
@Service
public class CriticalDimensionService {

    @Autowired
    private CriticalDimensionDao criticalDimensionDao;

    @Autowired
    private PartDao partDao;

    private Map<Long, List<CriticalDimension>> criticalDimensionsCache = null;

    static class JsonIdxNameTransformer extends AbstractTransformer {

        private String fieldName;

        JsonIdxNameTransformer(String fieldName) {
            this.fieldName = fieldName;
        }

        @Override
        public void transform(Object object) {
            JSONContext jsonContext = getContext();
            TypeContext typeContext = jsonContext.peekTypeContext();
            if (typeContext.isFirst()) {
                typeContext.increment();
            } else {
                jsonContext.writeComma();
            }
            jsonContext.writeName(fieldName);
            Transformer defTransformer = TransformerUtil.getDefaultTypeTransformers().getTransformer(object);
            defTransformer.transform(object);
        }

        @Override
        public Boolean isInline() {
            return Boolean.TRUE;
        }

    }

    @Transactional
    public synchronized List<CriticalDimension> getCriticalDimensionForPartType(Long partTypeId) {
        if (criticalDimensionsCache == null) {
            Map<Long, List<CriticalDimension>> cache = new HashMap<>(); // part type ID => List<CriticalDimension>
            // Load current values to the cache.
            for (CriticalDimension cd : criticalDimensionDao.findAll()) {
                criticalDimensionDao.getEntityManager().detach(cd);
                // Initialize {@link CriticalDimension#jsonIdxNameTransformer}.
                if (!cd.getJsonName().equals(cd.getIdxName())) {
                    cd.setJsonIdxNameTransformer(new JsonIdxNameTransformer(cd.getIdxName()));
                }
                // Put to the cache.
                Long ptid = cd.getPartType().getId();
                List<CriticalDimension> cdlst = cache.get(ptid);
                if (cdlst == null) {
                    cdlst = new ArrayList<>(10);
                    cache.put(ptid, cdlst);
                }
                cdlst.add(cd);
            }
            criticalDimensionsCache = cache;
        }
        return criticalDimensionsCache.get(partTypeId);
    }

    private synchronized void resetCriticalDimensionsCache() {
        this.criticalDimensionsCache = null;
    }

    public List<CriticalDimension> findForThePart(long partId) {
        Part part = partDao.findOne(partId);
        Long partTypeId = part.getPartType().getId();
        return getCriticalDimensionForPartType(partTypeId);
    }

    public List<CriticalDimensionEnum> getAllCritDimEnums() {
        return criticalDimensionDao.getAllCritDimEnums();
    }

    public List<CriticalDimensionEnumVal> getCritDimEnumVals(Integer enumId) {
        return criticalDimensionDao.getCritDimEnumVals(enumId);
    }

    public CriticalDimensionEnum addCritDimEnum(CriticalDimensionEnum cde) {
        CriticalDimensionEnum retVal = criticalDimensionDao.addCritDimEnum(cde);
        resetCriticalDimensionsCache();
        return retVal;
    }

    public CriticalDimensionEnumVal addCritDimEnumVal(CriticalDimensionEnumVal cdev) {
        CriticalDimensionEnumVal retVal = criticalDimensionDao.addCritDimEnumVal(cdev);
        resetCriticalDimensionsCache();
        return retVal;
    }

    public CriticalDimensionEnum updateCritDimEnum(CriticalDimensionEnum cde) {
        CriticalDimensionEnum original = criticalDimensionDao.getCriticalDimensionEnumById(cde.getId());
        original.setName(cde.getName());
        CriticalDimensionEnum retVal = criticalDimensionDao.updateCritDimEnum(original);
        resetCriticalDimensionsCache();
        return retVal;
    }

    public CriticalDimensionEnumVal updateCritDimEnumVal(CriticalDimensionEnumVal cdev) {
        CriticalDimensionEnumVal retVal = criticalDimensionDao.updateCritDimEnumVal(cdev);
        resetCriticalDimensionsCache();
        return retVal;
    }

    public void removeCritDimEnum(Integer cdeId) {
        criticalDimensionDao.removeCritDimEnum(cdeId);
        resetCriticalDimensionsCache();
    }

    public void removeCritDimEnumVal(Integer cdevId) {
        resetCriticalDimensionsCache();
        criticalDimensionDao.removeCritDimEnumVal(cdevId);
    }

    public CriticalDimensionEnum findCritDimEnumByName(String name) {
        return criticalDimensionDao.findCritDimEnumByName(name);
    }

    public CriticalDimensionEnumVal findCritDimEnumValByName(Integer enumId, String name) {
        return criticalDimensionDao.findCritDimEnumValByName(enumId, name);
    }

    public Errors validateCriticalDimensions(Part part) {
        PartType partType = part.getPartType();
        Errors errors = new ValidationErrors(partType.getName(), part, null);
        Long partTypeId = partType.getId();
        List<CriticalDimension> criticalDimensions = criticalDimensionDao.findForPartType(partTypeId);
        if (criticalDimensions.isEmpty()) {
            return errors;
        }
        Validator criticalDimensionsValidator = new CriticalDimensionsValidator(criticalDimensions);
        ValidationUtils.invokeValidator(criticalDimensionsValidator, part, errors);
        return errors;
    }

}

/**
 * Server-side validation for critical dimensions.
 */
class CriticalDimensionsValidator implements Validator {

    private final static Logger log = LoggerFactory.getLogger(CriticalDimensionsValidator.class);

    private final List<CriticalDimension> criticalDimensions;

    CriticalDimensionsValidator(List<CriticalDimension> criticalDimensions) {
        this.criticalDimensions = criticalDimensions;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Part.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Part part = (Part) target;
        for (CriticalDimension cd : criticalDimensions) {
            String fieldName = cd.getJsonName();
            Class partClass = part.getClass();
            try {
                Field field = partClass.getDeclaredField(fieldName);
                boolean accessible = field.isAccessible();
                try {
                    field.setAccessible(true);
                    Object value = field.get(part);
                    // Check: not null.
                    if (!cd.isNullAllowed() && value == null) {
                        errors.rejectValue(fieldName, null, "The value is required.");
                    } else if (cd.getDataType() == DECIMAL) {
                        Double decimal = (Double) value;
                        Double minVal = cd.getMinVal();
                        if (minVal != null && decimal < minVal) {
                            errors.rejectValue(fieldName, null, "The value lower than allowed: " + minVal);
                        }
                        Double maxVal = cd.getMaxVal();
                        if (maxVal != null && decimal > maxVal) {
                            errors.rejectValue(fieldName, null, "The value greather than allowed: " + maxVal);
                        }
                    } else if (cd.getDataType() == TEXT) {
                        String text = (String) value;
                        String regex = cd.getRegex();
                        if (text != null && regex != null && !text.matches(regex)) {
                            errors.rejectValue(fieldName, null, "The string '" + text
                                    + "' is not matched for regex '" + regex + "'.");
                        }
                    }
                } finally {
                    field.setAccessible(accessible);
                }
            } catch (Exception e) {
                String message = "Internal error. Validation of the field '" + fieldName
                        + "' failed for the part with ID="
                        + part.getId() + ". Does JPA entity declares this field? Details: " + e.getMessage();
                log.warn(message);
                //errors.rejectValue(fieldName, null, message);
            }
        }
    }
}
