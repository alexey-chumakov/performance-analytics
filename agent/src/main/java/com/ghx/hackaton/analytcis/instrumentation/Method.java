package com.ghx.hackaton.analytcis.instrumentation;

import javassist.CtClass;

import java.util.Map;

/**
 * @author Alexey Sheukun <asheukun@exadel.com> on 17.06.2015.
 */
public class Method {

    private String name;
    private String descriptor;
    private Map<String, CtClass> variables;
    private String before;
    private String after;

    public Method(String name, String descriptor, Map<String, CtClass> variables, String before, String after) {
        this.name = name;
        this.descriptor = descriptor;
        this.variables = variables;
        this.before = before;
        this.after = after;
    }

    public String getName() {
        return name;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public Map<String, CtClass> getVariables() {
        return variables;
    }

    public String getBefore() {
        return before;
    }

    public String getAfter() {
        return after;
    }
}
