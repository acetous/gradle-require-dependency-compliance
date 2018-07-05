package de.acetous.dependencycompliance;

import org.gradle.api.Project;
import org.gradle.api.file.RegularFileProperty;

/**
 * Extension for the {@link DependencyCompliancePlugin}.
 */
public class DependencyComplianceExtension {

    /**
     * The file the report will be written to.
     */
    private final RegularFileProperty outputFile;

    public DependencyComplianceExtension(Project project) {
        outputFile = project.getLayout().fileProperty(project.provider(() -> project.getLayout().getProjectDirectory().file("dependency-compliance-report.json")));
    }

    public void setOutputFile(RegularFileProperty outputFile) {
        this.outputFile.set(outputFile);
    }

    public RegularFileProperty getOutputFile() {
        return outputFile;
    }

}
