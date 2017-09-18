package com.turbointernational.metadata.util;

import static org.apache.commons.lang.StringUtils.abbreviate;

import com.turbointernational.metadata.entity.BOMItem;
import com.turbointernational.metadata.entity.CarModelEngineYear;
import com.turbointernational.metadata.entity.CarYear;
import com.turbointernational.metadata.entity.SalesNote;
import com.turbointernational.metadata.entity.part.Part;
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
            return formatApplication(cmey.getId());
        }
    }

    public static String formatApplication(Long cmeyId) {
        return "[" + cmeyId + "]";
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
