package de.acetous.dependencycompliance;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class FilteredCheckTest extends AbstractTest {

    @Before
    public void setup() throws IOException {
        copyFile("check-filtered/testcase.gradle", "build.gradle");
    }

    @Test
    public void shouldSucceedWithGeneratedReport() {
        createGradleRunner().withArguments("dependencyComplianceExport").build();
        BuildResult result = createGradleRunner().withArguments("dependencyComplianceCheck").build();
        assertThat(result.task(":dependencyComplianceCheck").getOutcome()).isEqualTo(TaskOutcome.SUCCESS);
    }

    @Test
    public void shouldSucceedWithCorrectReport() throws IOException {
        copyFile("check-filtered/report-correct.json", "dependency-compliance-report.json");
        BuildResult result = createGradleRunner().withArguments("dependencyComplianceCheck").build();
        assertThat(result.task(":dependencyComplianceCheck").getOutcome()).isEqualTo(TaskOutcome.SUCCESS);
    }

    @Test
    public void shouldSucceedWithReportContainingIgnored() throws IOException {
        copyFile("check-filtered/report-full.json", "dependency-compliance-report.json");
        BuildResult result = createGradleRunner().withArguments("dependencyComplianceCheck").build();
        assertThat(result.task(":dependencyComplianceCheck").getOutcome()).isEqualTo(TaskOutcome.SUCCESS);
    }

    @Test
    public void shouldFailWithMissingDependency() throws IOException {
        copyFile("check-filtered/report-missing.json", "dependency-compliance-report.json");
        BuildResult result = createGradleRunner().withArguments("dependencyComplianceCheck").buildAndFail();
        assertThat(result.task(":dependencyComplianceCheck").getOutcome()).isEqualTo(TaskOutcome.FAILED);
        assertThat(result.getOutput()).contains("Dependencies are not listed in dependency compliance export.");
        assertThat(result.getOutput()).contains("org.hamcrest:hamcrest-core:1.3");
        assertThat(result.getOutput()).contains("Buildfile dependencies are not listed in dependency compliance export.");
        assertThat(result.getOutput()).contains("commons-io:commons-io:2.4");

        assertTaskFailSummary(result);
    }

    private void assertTaskFailSummary(BuildResult result) {
        assertThat(result.getOutput()).contains("Build contains violating dependencies or repositories.");
    }
}
