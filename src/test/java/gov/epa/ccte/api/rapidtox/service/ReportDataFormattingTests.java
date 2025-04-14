package gov.epa.ccte.api.rapidtox.service;

import gov.epa.ccte.api.rapidtox.util.JasperHelper;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;

public class ReportDataFormattingTests {

	record StringResultCase(
			double value,
			int precision,
			String expectation) {

	}

	List<StringResultCase> cases = List.of(
			new StringResultCase(5.123456, 5, "5.1235"),
			new StringResultCase(5.123456, 2, "5.1"),
			new StringResultCase(5.123456, 1, "5"),
			new StringResultCase(0.000123, 5, "0.00012300"),
			new StringResultCase(0.000123, 2, "0.00012"),
			new StringResultCase(0.000123, 1, "0.0001")
	);

	@Test
	public void testNumericSolution() throws Exception {
		cases.stream().forEach(c -> {
			assertThat(JasperHelper.toPrecision(c.value, c.precision)).isEqualTo(c.expectation);
		});
	}

	@Test
	public void testSingle() throws Exception {
		StringResultCase c = new StringResultCase(5.123456, 5, "5.1235");
		assertThat(JasperHelper.formatDoubleValue(c.value)).isEqualTo("5.12");
	}

}
