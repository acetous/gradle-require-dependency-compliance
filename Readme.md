# de.acetous.dependency-compliance

Builds on CircleCI: 
[![CircleCI](https://circleci.com/gh/acetous/gradle-require-dependency-compliance/tree/master.svg?style=svg)](https://circleci.com/gh/acetous/gradle-require-dependency-compliance/tree/master)

## Usage

Include this plugin in your root project. Visit this 
[plugin's page on plugins.gradle.org](https://plugins.gradle.org/plugin/de.acetous.dependency-compliance) 
for more information. 

```
plugins {
  id "de.acetous.dependency-compliance" version "1.0.0"
}
```

The plugin will always check all subprojects in your build.

### Listing all dependencies and repositories
```
> gradlew dependencyComplianceList
```

### Checking / monitoring your project for new dependencies or repositories.

You can generate a JSON-report of included libraries and repositories. The task will create or update the file 
`dependency-compliance-report.json`. 
```
> gradlew dependencyComplianceExport
```

You can check the current dependencies and repositories against a previous exported report.

```
> gradlew dependencyComplianceCheck
```

This will check for additional dependencies and repositories and fail the build. Commit the
`dependency-compliance-report.json` to your repository and run this task in your CI environment. If any developer 
introduces a new dependency or repository he will need to update the report file or your CI build will fail. You can
always check which dependencies were introduced by checking the history of that file. 

## Plugin Documentation

### Tasks

| Task                       | Description |         
|----------------------------|-------------|
| `dependencyComplianceList`   | Print the report to the build's log. |
| `dependencyComplianceExport` | Export the report. |
| `dependencyComplianceCheck`  | Check the project's current state against a given report. |

### Configuration

Example:
```
dependencyCompliance {
    outputFile = file('my-custom-report-file.json')
}
```

| Parameter    | Default                             | Description               |
|--------------|-------------------------------------|---------------------------|
| `outputFile` | `dependency-compliance-report.json` | Filename / location of the export file. Used by the export- and check-task. |


## Contributing

Feel free to open issues for new ideas and submit pull requests when adding features.

### Repository structure

This repository contains the plugin and a sandbox project for manual tests when editing the plugin.

#### ./gradle-require-dependency-compliance-plugin

This is the actual plugin. Run some integration tests via:
```
> cd gradle-require-dependency-compliance-plugin
> gradlew check
```

#### ./plugin-sandbox

This is a demo project which uses the plugin. The plugin is included in this build in order to test changes without the need to deploy the plugin first.
Run your manual tests here. You can change the sandbox project to demonstrate new features.

```
> cd plugin-sandbox
> gradlew dependencyComplicanceList
```
