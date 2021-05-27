package fi.ari.bootweb.allin.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Value;

public class TestBase {
	@Value("${test.verbose:true}")
	protected boolean verbose = true;

	@BeforeEach
	public void logTestName(TestInfo info) {
		if (verbose) System.out.println("\n## " + getClass().getSimpleName() + "#" + info.getDisplayName());
	}
}
