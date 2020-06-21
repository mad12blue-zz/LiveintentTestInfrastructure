package com.liveintent.configuration;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

/*******************************************
 * RETRY ANALYZER LISTENER FOR TESTNG TESTS *
 *******************************************/
public class TestRetryAnalyzerListener implements IAnnotationTransformer {

	@SuppressWarnings("rawtypes")
	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		annotation.setRetryAnalyzer(TestRetryAnalyzer.class);
	}
}