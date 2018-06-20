package de.acetous.dependencycompliance;

import org.gradle.api.Project;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.RegularFileProperty;

public class DependencyComplianceExtension {

    private final RegularFileProperty outputFile;

    public DependencyComplianceExtension(Project project) {
        outputFile = project.getLayout().fileProperty();
    }

    public void setOutputFile(RegularFileProperty outputFile) {
        this.outputFile.set(outputFile);
    }

    public RegularFileProperty getOutputFile() {
        return outputFile;
    }
}
