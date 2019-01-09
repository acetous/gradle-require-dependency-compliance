package de.acetous.dependencycompliance;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckTest extends AbstractTest {

    @Before
    public void setup() throws IOException {
        copyFile("check/testcase.gradle", "build.gradle");
    }

    @Test
    public void failsWithoutExport() {
        BuildResult result = createGradleRunner().withArguments("dependencyComplianceCheck").buildAndFail();
        assertThat(result.task(":dependencyComplianceCheck").getOutcome()).isEqualTo(TaskOutcome.FAILED);
        assertThat(result.getOutput()).matches(Pattern.compile(".*Dependency compliance export expectet at '.*' is not present\\..*", Pattern.DOTALL));
    }

    @Test
    public void shouldSucceedWithGeneratedReport() {
        createGradleRunner().withArguments("dependencyComplianceExport").build();
        BuildResult result = createGradleRunner().withArguments("dependencyComplianceCheck").build();
        assertThat(result.task(":dependencyComplianceCheck").getOutcome()).isEqualTo(TaskOutcome.SUCCESS);
    }

    @Test
    public void shouldSucceedWithCorrectReport() throws IOException {
        copyFile("check/report-correct.json", "dependency-compliance-report.json");
        BuildResult result = createGradleRunner().withArguments("dependencyComplianceCheck").build();
        assertThat(result.task(":dependencyComplianceCheck").getOutcome()).isEqualTo(TaskOutcome.SUCCESS);
    }

    @Test
    public void shouldFailWithMissingDependency() throws IOException {
        copyFile("check/report-missing-dependency.json", "dependency-compliance-report.json");
        BuildResult result = createGradleRunner().withArguments("dependencyComplianceCheck").buildAndFail();
        assertThat(result.task(":dependencyComplianceCheck").getOutcome()).isEqualTo(TaskOutcome.FAILED);
        assertThat(result.getOutput()).contains("Dependencies are not listed in dependency compliance export.");
        assertThat(result.getOutput()).contains("com.google.code.gson:gson:2.8.5");
        assertTaskFailSummary(result);
    }

    @Test
    public void shouldFailWithMissingBuildDependency() throws IOException {
        copyFile("check/report-missing-build-dependency.json", "dependency-compliance-report.json");
        BuildResult result = createGradleRunner().withArguments("dependencyComplianceCheck").buildAndFail();
        assertThat(result.task(":dependencyComplianceCheck").getOutcome()).isEqualTo(TaskOutcome.FAILED);
        assertThat(result.getOutput()).contains("Buildfile dependencies are not listed in dependency compliance export.");
        assertThat(result.getOutput()).contains("commons-io:commons-io:2.4");
        assertTaskFailSummary(result);
    }

    @Test
    public void shouldFailWithMissingRepository() throws IOException {
        copyFile("check/report-missing-repository.json", "dependency-compliance-report.json");
        BuildResult result = createGradleRunner().withArguments("dependencyComplianceCheck").buildAndFail();
        assertThat(result.task(":dependencyComplianceCheck").getOutcome()).isEqualTo(TaskOutcome.FAILED);
        assertThat(result.getOutput()).contains("Repository is not listed in dependency compliance export: 'BintrayJCenter (https://jcenter.bintray.com/)'");
        assertTaskFailSummary(result);
    }

    @Test
    public void shouldFailWithMissingBuildRepository() throws IOException {
        copyFile("check/report-missing-build-repository.json", "dependency-compliance-report.json");
        BuildResult result = createGradleRunner().withArguments("dependencyComplianceCheck").buildAndFail();
        assertThat(result.task(":dependencyComplianceCheck").getOutcome()).isEqualTo(TaskOutcome.FAILED);
        assertThat(result.getOutput()).contains("Buildfile repository is not listed in dependency compliance export: '__plugin_repository__Gradle Central Plugin Repository (https://plugins.gradle.org/)'");
        assertTaskFailSummary(result);
    }

    @Test
    public void shouldSucceedWithReportContainingAdditionalDependencies() throws IOException {
        copyFile("check/report-correct-other-entries.json", "dependency-compliance-report.json");
        BuildResult result = createGradleRunner().withArguments("dependencyComplianceCheck", "--stacktrace").build();
        assertThat(result.task(":dependencyComplianceCheck").getOutcome()).isEqualTo(TaskOutcome.SUCCESS);
    }

    private void assertTaskFailSummary(BuildResult result) {
        assertThat(result.getOutput()).contains("Build contains violating dependencies or repositories.");
    }
}
