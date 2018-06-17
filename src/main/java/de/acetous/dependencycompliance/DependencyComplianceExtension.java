package de.acetous.dependencycompliance;

import org.gradle.api.Project;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.FileCollection;

public class DependencyComplianceExtension {

    private final ConfigurableFileCollection outputFiles;

    public DependencyComplianceExtension(Project project) {
        outputFiles = project.getLayout().configurableFiles();
    }

    public void setOutputFiles(FileCollection outputFiles) {
        this.outputFiles.setFrom(outputFiles);
    }

    public ConfigurableFileCollection getOutputFiles() {
        return outputFiles;
    }
}
