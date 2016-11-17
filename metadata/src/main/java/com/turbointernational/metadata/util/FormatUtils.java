package com.turbointernational.metadata.util;

import com.turbointernational.metadata.entity.*;
import com.turbointernational.metadata.entity.part.Interchange;
import com.turbointernational.metadata.entity.part.Part;

import static org.apache.commons.lang.StringUtils.abbreviate;

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

    public static String formatBOMItem(BOMItem bomItem) {
        if (bomItem == null) {
            return "null";
        } else {
            Long id = bomItem.getId();
            Part parent = bomItem.getParent();
            Part child = bomItem.getChild();
            Integer qty = bomItem.getQuantity();
            return String.format("(ID:%d, PRNT:%s, CHLD:%s, QTY:%d)", id, formatPart(parent), formatPart(child), qty);
        }
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

    public static String formatBOMAlternative(BOMAlternative bomAlternative) {
        if (bomAlternative == null) {
            return "null";
        } else {
            Long id = bomAlternative.getId();
            Part part = bomAlternative.getPart();
            return "[" + id + "] - PRT: " + formatPart(part);
        }
    }

}
