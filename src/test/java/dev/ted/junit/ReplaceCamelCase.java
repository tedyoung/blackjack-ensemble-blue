package dev.ted.junit;

import org.junit.jupiter.api.DisplayNameGenerator;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReplaceCamelCase extends DisplayNameGenerator.Simple {
    private final static Pattern CAMEL_CASE_BOUNDARY = Pattern.compile("(([A-Z]?[a-z]+)|([A-Z]))");

    @Override
    public String generateDisplayNameForClass(Class<?> testClass) {
        return insertSpaceBeforeCapital(super.generateDisplayNameForClass(testClass));
    }

    @Override
    public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
        return insertSpaceBeforeCapital(super.generateDisplayNameForNestedClass(nestedClass));
    }

    @Override
    public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
        return insertSpaceBeforeCapital(super.generateDisplayNameForMethod(testClass, testMethod));
    }

    private static String insertSpaceBeforeCapital(String name) {
        Matcher matcher = CAMEL_CASE_BOUNDARY.matcher(name);
        StringBuilder sentence = new StringBuilder();
        while (matcher.find()) {
            sentence.append(matcher.group(0)).append(' ');
        }
        sentence.deleteCharAt(sentence.length() - 1);
        sentence.setCharAt(0, Character.toUpperCase(sentence.charAt(0)));
        return sentence.toString();
    }

}