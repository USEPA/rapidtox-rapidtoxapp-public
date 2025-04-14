
package gov.epa.ccte.api.rapidtox.util;

public interface Mapper<FROM, TO> {
	TO map(FROM fromItem);
}
