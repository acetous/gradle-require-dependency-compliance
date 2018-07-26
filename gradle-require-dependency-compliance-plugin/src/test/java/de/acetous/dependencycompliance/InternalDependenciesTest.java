package de.acetous.dependencycompliance;

import de.acetous.dependencycompliance.export.DependencyExport;
import de.acetous.dependencycompliance.export.DependencyIdentifier;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class InternalDependenciesTest extends AbstractTest {

    private BuildResult result;

    @Before
    public void setup() throws IOException {
        testProjectDir.newFolder("submodule");
        copyFile("internaldependencies/parent.gradle", "build.gradle");
        copyFile("internaldependencies/settings.gradle", "settings.gradle");
        copyFile("internaldependencies/submodule.gradle", "submodule/build.gradle");

        result = createGradleRunner()
                .withArguments("dependencyComplianceExport")
                .build();
    }

    @Test
    public void listDependencies() {
        createGradleRunner()
                .withArguments("dependencyComplianceList")
                .build();
    }

    @Test
    public void taskShouldSucceed() {
        assertThat(result.task(":dependencyComplianceExport").getOutcome()).isEqualTo(TaskOutcome.SUCCESS);
    }

    @Test
    public void shouldNotContainInternalDependencies() {
        DependencyExport dependencyExport = parseDependencyExport();
        Collection<DependencyIdentifier> dependencies = dependencyExport.getDependencies();
        assertThat(dependencies).containsOnly(DependencyIdentifier.create("commons-io", "commons-io", "2.4"));
    }
}
