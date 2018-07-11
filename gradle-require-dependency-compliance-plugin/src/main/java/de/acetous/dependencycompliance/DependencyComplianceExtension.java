package de.acetous.dependencycompliance;

import org.gradle.api.Project;
import org.gradle.api.file.RegularFileProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Extension for the {@link DependencyCompliancePlugin}.
 */
public class DependencyComplianceExtension {

    /**
     * The file the report will be written to.
     */
    private final RegularFileProperty outputFile;

    private List<String> dependencyFilter;

    public DependencyComplianceExtension(Project project) {
        outputFile = project.getLayout().fileProperty(project.provider(() -> project.getLayout().getProjectDirectory().file("dependency-compliance-report.json")));
        dependencyFilter = new ArrayList<>();
    }

    public void setOutputFile(RegularFileProperty outputFile) {
        this.outputFile.set(outputFile);
    }

    public void setDependencyFilter(List<String> dependencyFilter) {
        this.dependencyFilter = dependencyFilter;
    }

    public RegularFileProperty getOutputFile() {
        return outputFile;
    }

    public List<String> getDependencyFilter() {
        return dependencyFilter;
    }
}
