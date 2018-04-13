package com.asgard.maven.plugins.generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "xsd2wsdl", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class Xsd2WsdlMojo extends AbstractMojo {
	@Parameter(defaultValue = "${project}", readonly = true)
	private MavenProject project;
	
	@Parameter(defaultValue = "${session}", readonly = true)
	private MavenSession mavenSession;
	
	@Parameter
	private boolean skip;

	@Parameter
	private boolean verbose;

	@Parameter
	private boolean quiet;

	@Parameter(defaultValue = "${basedir}/src/main/resources/wsdl")
	private File wsdlRoot;

	@Parameter(defaultValue = "${basedir}/src/test/resources/wsdl")
	private File testWsdlRoot;
	// A list of xsd files to include. Can contain ant-style wildcards and
	// double wildcards.
	@Parameter
	private String includes[];
	// A list of xsd files to exclude. Can contain ant-style wildcards and
	// double wildcards.
	@Parameter
	private String excludes[];

	/**
	 * 
	 * @param arr
	 * @return
	 */
	private String getIncludeExcludeString(String[] arr) {
		if (arr == null) {
			return "";
		}
		StringBuilder str = new StringBuilder();

		for (String s : arr) {
			if (str.length() > 0) {
				str.append(',');
			}
			str.append(s);
		}
		return str.toString();
	}

	/**
	 * 
	 * @param dir
	 * @return
	 * @throws MojoExecutionException
	 */
	private List<File> getXsdFiles(File dir) throws MojoExecutionException {
		List<String> exList = new ArrayList<String>();
		if (excludes != null) {
			exList.addAll(Arrays.asList(excludes));
		}
		exList.addAll(Arrays.asList(org.codehaus.plexus.util.FileUtils.getDefaultExcludes()));

		String inc = getIncludeExcludeString(includes);
		String ex = getIncludeExcludeString(exList.toArray(new String[exList.size()]));

		try {
			List<File> newfiles = org.codehaus.plexus.util.FileUtils.getFiles(dir, inc, ex);
			return newfiles;
		} catch (IOException exc) {
			throw new MojoExecutionException(exc.getMessage(), exc);
		}
	}

	/**
	 * 
	 * @param file
	 * @throws MojoExecutionException
	 */
	private void processXsd(File file) throws MojoExecutionException {
		List<String> list = new ArrayList<String>();

		// verbose arg
		if (verbose != Boolean.FALSE) {
			list.add("-verbose");
		}

		// quiet arg
		if (quiet != Boolean.FALSE) {
			list.add("-quiet");
		}

		getLog().error("Calling wsdlvalidator with args: " + list);
		try {
			list.add(file.getCanonicalPath());
			String[] pargs = list.toArray(new String[list.size()]);

			/*
			 * ToolSpec spec = null; try (InputStream toolspecStream =
			 * WSDLValidator.class.getResourceAsStream("wsdlvalidator.xml")) {
			 * spec = new ToolSpec(toolspecStream, false); } WSDLValidator
			 * validator = new WSDLValidator(spec);
			 * validator.setArguments(pargs); boolean ok =
			 * validator.executeForMaven(); if (!ok) { throw new
			 * MojoExecutionException("WSDL failed validation: " +
			 * file.getName()); }
			 */
		} catch (Throwable e) {
			throw new MojoExecutionException(file.getName() + ": " + e.getMessage(), e);
		}
	}

	/**
	 * 
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {
		// TODO Auto-generated method stub
		getLog().debug("execute xsd2wsdl...");
		if (includes == null) {
			includes = new String[] { "*.wsdl" };
		}

		List<File> wsdls = new ArrayList<File>();
		if (wsdlRoot != null && wsdlRoot.exists()) {
			wsdls.addAll(getXsdFiles(wsdlRoot));
		}
		if (testWsdlRoot != null && testWsdlRoot.exists()) {
			wsdls.addAll(getXsdFiles(testWsdlRoot));
		}

		for (File wsdl : wsdls) {
			processXsd(wsdl);
		}
	}

}
