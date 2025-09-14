package com.order;

import io.cucumber.junit.platform.engine.Cucumber;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("order.feature")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.order")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty:target/cucumber-reports/pretty.txt,html:target/cucumber-reports/report.html,json:target/cucumber-reports/report.json")
public class CucumberTestRunner {
}
