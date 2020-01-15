# de.acetous.dependency-compliance

Builds on CircleCI: 
[![CircleCI](https://circleci.com/gh/acetous/gradle-require-dependency-compliance/tree/master.svg?style=svg)](https://circleci.com/gh/acetous/gradle-require-dependency-compliance/tree/master)

Checked on Sonarcloud: [![SonarCloud](https://sonarcloud.io/api/project_badges/measure?project=de.acetous%3Agradle-dependency-compliance-plugin&metric=alert_status
)](https://sonarcloud.io/dashboard?id=de.acetous%3Agradle-dependency-compliance-plugin) ![Code Coverage](https://sonarcloud.io/api/project_badges/measure?project=de.acetous%3Agradle-dependency-compliance-plugin&metric=coverage)


Release 2.x is tested and runs with Gradle 5.0 and 5.1.

| Gradle Version | Latest successfully tested Release |         
|---|---|
| 4.8 to 4.10.2 | 1.3.0 |
| 5.0 to 5.6.4 | 2.0.0 |



Release 1.3.0 is tested and runs with Gradle 4.8 to 4.10.2.

## Usage

Include this plugin in your root project. Visit this 
[plugin's page on plugins.gradle.org](https://plugins.gradle.org/plugin/de.acetous.dependency-compliance) 
for more information. 

```
plugins {
  id "de.acetous.dependency-compliance" version "1.3.0"
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

### Ignoring dependencies

You may want to ignore some dependencies, maybe in case of own libraries you use. Add ignored libraries with the `ignore` option
to your configuration. Your can ignore specific versions (e.g. `junit:junit:4.12`), an artifact of any version (e.g. `junit:junit:*`)
or whole groups (e.g. `junit:*:*`).


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
    ignore = [
            'org.assertj:*:*',
            'com.google.code.gson:gson:*',
            'junit:junit:4.12',
    ]
}
```

| Parameter    | Default                             | Description               |
|--------------|-------------------------------------|---------------------------|
| `outputFile` | `dependency-compliance-report.json` | Filename / location of the export file. Used by the export- and check-task. |
| `ignore`     | `[]`                                | Ignored dependencies as list. Entries should be strings in format `group:artifact:version`. The `artifact` and `version` can be a wildcard (`*`).|
| `ignoreMavenLocal` | `false`                       | Ignore the local Maven repository. This is useful if you use this repo and run the check task in your CI environment. 


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

## Changelog

### Version 2.0.0

* compatible with Gradle 5.0

### Version 1.3.0

* add info about existing versions to task `dependencyComlianceCheck`
  *  `commons-io:commons-io:2.4 - existing versions: 2.3`
* dependencies are now sorted for better readability

### Version 1.2.0

* add `ignoreMavenLocal` option

### Version 1.1.0

* add `ignore` option

### Version 1.0.0

* Initial Release
