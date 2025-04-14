package gov.epa.ccte.api.rapidtox.hazard.service;

import gov.epa.ccte.api.rapidtox.hazard.model.Hazard;
import static gov.epa.ccte.api.rapidtox.sessionreport.SuperCategory.SUPER_CATEGORY_ORDER;
import java.util.Comparator;

public class HazardComparator implements Comparator<Hazard> {

    @Override
    public int compare(Hazard o1, Hazard o2) {
        int retVal = compareBySuperCategory(o1, o2);
        if (retVal == 0) {
            retVal = compareByToxvalNumeric(o1, o2);
            if (retVal == 0) {
                retVal = compareByToxvalType(o1, o2);
            }
        }
        return retVal;
    }

    private int compareByToxvalType(Hazard o1, Hazard o2) {
        return compare(o1.getToxvalType(), o2.getToxvalType());
    }

    // todo: really need to change toxval-numeric to a double type :(
    private int compareByToxvalNumeric(Hazard o1, Hazard o2) throws NumberFormatException {
        return compareDoubles(o1.getToxvalNumeric(), o2.getToxvalNumeric());
    }

    // this should push null values to the right
    public static int compareDoubles(Double a, Double b) {
        if (a == null && b == null) {
            return 0;
        }
        if (a != null && b != null) {
            return Double.compare(a, b);
        }
        if (a != null) {
            return -1;
        }
        return 1;
    }

    public static int compareBySuperCategory(Hazard o1, Hazard o2) {
        int idx1 = SUPER_CATEGORY_ORDER.indexOf(o1.getSuperCategory());
        int idx2 = SUPER_CATEGORY_ORDER.indexOf(o2.getSuperCategory());

        if (idx1 == -1 && idx2 == -1) {
            return o1.getSuperCategory().compareTo(o2.getSuperCategory());
        } else if (idx1 == -1) {
            return -1;
        } else if (idx2 == -1) {
            return 1;
        } else {
            return idx1 - idx2;
        }
    }

    public int compare(String s1, String s2) {
        if (s1 == null && s2 == null) {
            return 0;
        } else if (s1 == null) {
            return -1;
        } else if (s2 == null) {
            return 1;
        } else {
            return s1.compareTo(s2);
        }
    }
}
