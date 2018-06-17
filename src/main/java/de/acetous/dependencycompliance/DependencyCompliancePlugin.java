package de.acetous.dependencycompliance;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class DependencyCompliancePlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        DependencyComplianceExtension extension = project.getExtensions().create("dependencyCompliance", DependencyComplianceExtension.class, project);
        project.getTasks().create("dependencyWrite", DependencyWriterTask.class, (task) -> {
            task.setOutputFiles(extension.getOutputFiles());
        });
    }
}
