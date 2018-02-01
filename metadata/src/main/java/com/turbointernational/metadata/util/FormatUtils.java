package com.turbointernational.metadata.util;

import static org.apache.commons.lang.StringUtils.abbreviate;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.turbointernational.metadata.entity.CarEngine;
import com.turbointernational.metadata.entity.CarFuelType;
import com.turbointernational.metadata.entity.CarMake;
import com.turbointernational.metadata.entity.CarModel;
import com.turbointernational.metadata.entity.CarModelEngineYear;
import com.turbointernational.metadata.entity.CarYear;
import com.turbointernational.metadata.entity.SalesNote;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.entity.part.ProductImage;
import com.turbointernational.metadata.web.dto.Interchange;

/**
 * Static methods to represent various entities in a text form.
 *
 * Created by dmytro.trunykov@zorallabs.com on 11/17/16.
 */
public class FormatUtils {

    public static String formatPart(Part part) {
        if (part == null) {
            return "null";
        } else {
            Long partId = part.getId();
            String manufacturerNumber = part.getManufacturerPartNumber();
            return formatPart(partId, manufacturerNumber);

        }
    }

    public static String formatPart(com.turbointernational.metadata.web.dto.Part part) {
        if (part == null) {
            return "null";
        } else {
            Long partId = part.getPartId();
            String manufacturerNumber = part.getPartNumber();
            return formatPart(partId, manufacturerNumber);
        }
    }

    public static String formatPart(Long partId, String manufacturerNumber) {
        return "[" + partId + "] - " + manufacturerNumber;
    }

    public static String formatPart(Long partId) {
        return "[" + partId + "]";
    }

    public static String formatProductImage(ProductImage pi) {
        return "[" + pi.getId() + "] - " + pi.getFilename();
    }

    public static String formatSalesNote(SalesNote salesNote) {
        if (salesNote == null) {
            return "null";
        } else {
            return formatSalesNote(salesNote.getId());
        }
    }

    public static String formatSalesNote(Long salesNoteId) {
        return "[" + salesNoteId + "]";
    }

    public static String formatApplication(CarModelEngineYear cmey) {
        if (cmey == null) {
            return "null";
        } else {
            List<String> fields = new ArrayList<>(5);
            CarModel model = cmey.getModel();
            if (model != null) {
                CarMake make = model.getMake();
                if (make != null) {
                    String makeName = make.getName();
                    if (isNotBlank(makeName)) {
                        fields.add(makeName);
                    }
                }
                String modelName = model.getName();
                if (isNotBlank(modelName)) {
                    fields.add(modelName);
                }
            }
            CarEngine carEngine = cmey.getEngine();
            if (carEngine != null) {
                String engineSize = carEngine.getEngineSize();
                if (isNotBlank(engineSize)) {
                    fields.add(engineSize);
                }
                CarFuelType fuelType = carEngine.getFuelType();
                if (fuelType != null) {
                    String name = fuelType.getName();
                    if (isNotBlank(name)) {
                        fields.add(name);
                    }
                }
            }
            CarYear year = cmey.getYear();
            if (year != null) {
                String name = year.getName();
                fields.add(name);
            }
            StringBuilder sb = new StringBuilder();
            sb.append(formatApplication(cmey.getId()));
            String s = String.join(", ", fields);
            if (isNotBlank(s)) {
                sb.append(' ');
                sb.append(s);
            }
            return sb.toString();
        }
    }

    public static String formatApplication(Long cmeyId) {
        return "[" + cmeyId + "]";
    }

    public static String formatCarEngine(CarEngine carEngine) {
        if (carEngine == null) {
            return "null";
        } else {
            return formatCarEngine(carEngine.getId());
        }
    }

    public static String formatCarEngine(Long ceId) {
        return "[" + ceId + "]";
    }

    public static String formatCarFuelType(CarFuelType cft) {
        if (cft == null) {
            return "null";
        } else {
            return formatCarFuelType(cft.getId());
        }
    }

    public static String formatCarFuelType(Long cftId) {
        return "[" + cftId + "]";
    }

    public static String formatCarMake(CarMake cm) {
        if (cm == null) {
            return "null";
        } else {
            return formatCarMake(cm.getId());
        }
    }

    public static String formatCarMake(Long cmId) {
        return "[" + cmId + "]";
    }

    public static String formatCarModel(CarModel cm) {
        if (cm == null) {
            return "null";
        } else {
            return formatCarModel(cm.getId());
        }
    }

    public static String formatCarModel(Long cmId) {
        return "[" + cmId + "]";
    }

    public static String formatBom(Part parentPart, Part childPart) {
        return String.format("(PRNT:%s, CHLD:%s)", formatPart(parentPart), formatPart(childPart));
    }

    public static String formatBom(Part parentPart, Part childPart, Integer qty) {
        return String.format("(PRNT:%s, CHLD:%s, QTY:%d)", formatPart(parentPart), formatPart(childPart), qty);
    }

    public static String formatInterchange(Interchange interchange) {
        if (interchange == null) {
            return "null";
        } else {
            return "[" + interchange.getId() + "]";
        }
    }

    public static String formatCarYear(CarYear carYear) {
        if (carYear == null) {
            return "null";
        } else {
            String retVal = "[" + carYear.getId() + "] - ";
            String name = carYear.getName();
            if (name == null) {
                retVal += "null";
            } else {
                retVal += ("\"" + abbreviate(name, 32) + "\"");
            }
            return retVal;
        }
    }

    public static String formatBOMAlternative(Long parentPartId, Long childPartId, Long altPartId) {
        return "[" + parentPartId + ", " + childPartId + ", " + altPartId + "]";
    }

}
