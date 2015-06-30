package com.ghx.hackaton.analytcis.instrumentation;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.*;

import javassist.*;

/**
 * @author Alexey Sheukun <asheukun@exadel.com> on 15.06.2015.
 */
public class ProfileTransformerAgent implements ClassFileTransformer {

    public static final String BEFORE_CODE = "start = System.currentTimeMillis();";
    public static final String AFTER_CODE = "com.ghx.hackaton.analytcis.agent.logger.RequestLogger.getInstance().logExternalSystemUsage(com.ghx.hackaton.analytcis.agent.commons.ExternalSystemType.MONGO_DB, System.currentTimeMillis() - start);";
    private ClassPool classPool;

    private Map<String, Set<Method>> methodsToProfile = new HashMap<String, Set<Method>>();

    public static void premain(String agentArgs, Instrumentation inst) {
        new ProfileTransformerAgent(inst);
    }

    public ProfileTransformerAgent(Instrumentation instrumentation) {
        classPool = ClassPool.getDefault();
        instrumentation.addTransformer(this);

        final Map<String, CtClass> variables = new HashMap<String, CtClass>();
        variables.put("start", CtClass.longType);
        methodsToProfile.put("com.mongodb.DBTCPConnector", new HashSet<Method>(Arrays.asList(
                new Method("doOperation",
                        "(Lcom/mongodb/DB;Lcom/mongodb/DBPort;Lcom/mongodb/DBPort$Operation;)Ljava/lang/Object;", variables,
                        BEFORE_CODE, AFTER_CODE),
                new Method("call",
                        "(Lcom/mongodb/DB;Lcom/mongodb/DBCollection;Lcom/mongodb/OutMessage;Lcom/mongodb/ServerAddress;ILcom/mongodb/ReadPreference;Lcom/mongodb/DBDecoder;)Lcom/mongodb/Response;",
                        variables, BEFORE_CODE, AFTER_CODE))));
        methodsToProfile.put("com.mongodb.Mongo", new HashSet<Method>(Arrays.asList(
                new Method("execute",
                        "(Lcom/mongodb/operation/WriteOperation;)Ljava/lang/Object;", variables,
                        BEFORE_CODE, AFTER_CODE),
                new Method("execute",
                        "(Lcom/mongodb/operation/ReadOperation;Lcom/mongodb/ReadPreference;)Ljava/lang/Object;",
                        variables, BEFORE_CODE, AFTER_CODE))));
    }

    public byte[] transform(ClassLoader loader, String className, Class classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer)
            throws IllegalClassFormatException {
        String rightClassName = className.replace("/", ".");
        if (methodsToProfile.containsKey(rightClassName)) {
            try {
                //we need to add class path of file we are instrumenting and its dependencies, note than we may loose some dependencies here
                final String filePath = protectionDomain.getCodeSource().getLocation().getFile();
                if (filePath.endsWith(".jar")) {//in case of instrumenting class from a jar file we add whole jar
                    classPool.appendClassPath(protectionDomain.getCodeSource().getLocation().getFile());
                } else { // in case of instrumenting class from unpacked application we try to find classes root
                    final String rootPackage = className.replaceAll("/.*", "");
                    final String rootPath = filePath.substring(0, filePath.indexOf(rootPackage));
                    classPool.insertClassPath(rootPath);
                }

                CtClass clazz = classPool.get(rightClassName);

                for (Method m : methodsToProfile.get(rightClassName)) {
                    CtMethod method = clazz.getMethod(m.getName(), m.getDescriptor());

                    for (String vName : m.getVariables().keySet()) {
                        method.addLocalVariable(vName, m.getVariables().get(vName));
                    }

                    method.insertBefore(m.getBefore());
                    method.insertAfter(m.getAfter());
                }

                return clazz.toBytecode();
            } catch (Exception e) {
                System.err.println(e.getMessage() + " transforming class " + className + "; returning uninstrumented class");
            }
        }
        return null;
    }
}
