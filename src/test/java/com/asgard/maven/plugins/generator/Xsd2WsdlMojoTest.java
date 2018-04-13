package com.asgard.maven.plugins.generator;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;


public class Xsd2WsdlMojoTest extends AbstractMojoTestCase {
	/**
     * @see junit.framework.TestCase#setUp()
     */
	@Override
	protected void setUp() throws Exception {		
		super.setUp();
	}
	
	public void testMojoGoalxsd2wsdl() throws Exception {
		System.err.println("execute Test");
		/*
        File pomFile = new File ("src/test/resources/unit/xsd2wsdl-mojo/asgard-generator-plugin-config.xml");
        
        assertNotNull(pomFile);
        assertTrue(pomFile.exists());
        Xsd2WsdlMojo myMojo = (Xsd2WsdlMojo) lookupMojo("xsd2wsdl", pomFile);
        assertNotNull(myMojo);
        myMojo.execute();
        */
    }
}
