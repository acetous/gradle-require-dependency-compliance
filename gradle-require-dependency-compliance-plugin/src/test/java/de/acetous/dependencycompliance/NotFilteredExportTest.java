package de.acetous.dependencycompliance;


import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class NotFilteredExportTest extends AbstractTest {

    private BuildResult result;

    @Before
    public void setup() throws IOException {
        copyFile("export-filtered/testcase-no-ignores.gradle", "build.gradle");

        result = createGradleRunner()
                .withArguments("dependencyComplianceExport", "--stacktrace")
                .build();
    }

    @Test
    public void taskShouldSucceed() {
        assertThat(result.task(":dependencyComplianceExport").getOutcome()).isEqualTo(TaskOutcome.SUCCESS);
    }

    @Test
    public void taskShouldNotLogIgnoredDependencies() {
        assertThat(result.getOutput()).doesNotContain("(DependencyCompliance) Ignoring these dependencies:");
    }
}
