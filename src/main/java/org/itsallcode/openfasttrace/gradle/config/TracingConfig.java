/**
 * openfasttrace-gradle - Gradle plugin for tracing requirements using OpenFastTrace
 * Copyright (C) 2017 It's all code <christoph at users.sourceforge.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.itsallcode.openfasttrace.gradle.config;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Project;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;
import org.itsallcode.openfasttrace.importer.legacytag.config.PathConfig;
import org.itsallcode.openfasttrace.report.ReportVerbosity;

public class TracingConfig
{
    private static final String DEFAULT_REPORT_FILE = "reports/tracing.txt";
    private static final List<String> DEFAULT_DIRECTORIES = asList("src", "doc");

    private final Project project;
    public final Property<ReportVerbosity> reportVerbosity;
    public final ConfigurableFileCollection inputDirectories;
    public final RegularFileProperty reportFile;
    private final NamedDomainObjectContainer<PathPatternConfig> pathConfig;

    public TracingConfig(Project project, NamedDomainObjectContainer<PathPatternConfig> pathConfig)
    {
        this.project = project;
        this.pathConfig = pathConfig;
        this.inputDirectories = project.files(getDefaultInputDirectories(project));
        this.reportFile = project.getLayout().fileProperty();
        this.reportFile.set(new File(project.getBuildDir(), DEFAULT_REPORT_FILE));
        this.reportVerbosity = project.getObjects().property(ReportVerbosity.class);
        this.reportVerbosity.set(ReportVerbosity.FAILURE_DETAILS);
    }

    public void setInputDirectories(FileCollection inputDirs)
    {
        this.inputDirectories.setFrom(inputDirs);
    }

    public void setReportFile(File reportFile)
    {
        this.reportFile.set(reportFile);
    }

    public RegularFileProperty getReportFile()
    {
        return reportFile;
    }

    private Set<File> getDefaultInputDirectories(Project project)
    {
        return DEFAULT_DIRECTORIES.stream() //
                .map(dir -> new File(project.getRootDir(), dir)) //
                .collect(toSet());
    }

    public void setReportVerbosity(ReportVerbosity reportVerbosity)
    {
        this.reportVerbosity.set(reportVerbosity);
    }

    public Stream<PathConfig> getPathConfig()
    {
        return pathConfig.stream().map(c -> c.convert(project));
    }

    @Override
    public String toString()
    {
        return "TracingConfig [project=" + project + ", reportVerbosity=" + reportVerbosity
                + ", inputDirectories=" + inputDirectories + ", reportFile=" + reportFile
                + ", pathConfig=" + pathConfig + "]";
    }
}
