package de.acetous.dependencycompliance;


import com.google.common.collect.Comparators;
import de.acetous.dependencycompliance.export.DependencyExport;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ExportSortedTest extends AbstractTest {

    private BuildResult result;

    @Before
    public void setup() throws IOException {
        copyFile("export-sorted/testcase.gradle", "build.gradle");

        result = createGradleRunner()
                .withArguments("dependencyComplianceExport", "--stacktrace")
                .build();
    }

    @Test
    public void taskShouldSucceed() {
        assertThat(result.task(":dependencyComplianceExport").getOutcome()).isEqualTo(TaskOutcome.SUCCESS);
    }

    @Test
    public void dependenciesShouldBeResolved() {
        DependencyExport dependencyExport = parseDependencyExport();
        assertThat(dependencyExport.getDependencies()).hasSize(16);
        assertThat(dependencyExport.getBuildDependencies()).hasSize(2);
    }

    @Test
    public void dependenciesShouldBeSorted() {
        DependencyExport dependencyExport = parseDependencyExport();
        assertThat(Comparators.isInOrder(dependencyExport.getDependencies(), new DependencyIdentifierComparator())).isTrue();
    }

    @Test
    public void buildDependenciesShouldBeSorted() {
        DependencyExport dependencyExport = parseDependencyExport();
        assertThat(Comparators.isInOrder(dependencyExport.getBuildDependencies(), new DependencyIdentifierComparator())).isTrue();
    }
}
