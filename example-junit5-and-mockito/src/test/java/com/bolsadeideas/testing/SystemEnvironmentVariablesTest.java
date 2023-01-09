package com.bolsadeideas.testing;

import java.util.Map;

import org.junit.jupiter.api.Test;

public class SystemEnvironmentVariablesTest {
	
	@Test
	void printEnvironmentVariables() {
		Map<String, String> variables = System.getenv();
		variables.forEach((k, v) -> System.out.println(k + ": " + v));
	}

}
