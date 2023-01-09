package com.bolsadeideas.testing;

import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;

public class SystemTests {
	
	@Test
	@EnabledOnOs(OS.WINDOWS) //@DisabledOnOs({ OS.LINUX, OS.MAC })
	void testSoloWindows() {
		System.out.println("Este test debería ejecutarse sólo en Windows");
	}
	
	@Test
	@EnabledOnOs({ OS.LINUX, OS.MAC }) //@DisabledOnOs(OS.WINDOWS)
	void testSoloLinuxOMac() {
		System.out.println("Este test debería ejecutarse sólo en Linux o Mac");
	}
	
	@Test
	@EnabledOnJre(JRE.JAVA_8)
	void soloJdk8() {
		System.out.println("Este test debería ejecutarse sólo en JDK 8");
	}
	
	@Test
	@EnabledOnJre(JRE.JAVA_17)
	void soloJdk17() {
		System.out.println("Este test debería ejecutarse sólo en JDK 17");
	}
	
	@Test
	void printSystemProperties() {
		Properties properties = System.getProperties();
		//properties.forEach((k, v) -> System.out.println(k + ": " + v));
	}
	
	@Test
	@EnabledIfSystemProperty(named = "java.version", matches = "17.*")
	void testJavaVersion17Something() {
		System.out.println("java.version is 17.*");
	}

}
