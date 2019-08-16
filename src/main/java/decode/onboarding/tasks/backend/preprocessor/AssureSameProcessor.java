package decode.onboarding.tasks.backend.preprocessor;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Stream;

public class AssureSameProcessor {

    private HashMap<String, Object> collected = new HashMap<>();

    private static Stream<Class> getClasses(Class rootClass) throws IOException, ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        Enumeration<URL> resources = classLoader.getResources(rootClass
                .getPackage()
                .getName()
                .replace(".", "/"));

        return Stream.generate(() -> resources.hasMoreElements() ? resources.nextElement() : null)
                .filter(Objects::nonNull)
                .map(url -> new File(url.getFile()))
                .flatMap(file -> getClassesInURL(file, rootClass.getPackage().getName()))
                .map(AssureSameProcessor::forNameDecorator);
    }

    private static Stream<String> getClassesInURL(File root, String packageName) {
        if (root.isFile() && root.getName().endsWith(".class")) {
            return Stream.of(packageName + root.getName());
        } else if (!root.exists() || !root.isDirectory() || root.listFiles() == null) {
            return Stream.empty();
        }
        return Stream.of(root.listFiles())
                .filter(Objects::nonNull)
                .flatMap(file -> getClassesInURL(file, packageName + "." + file.getName()));
    }

    private static Class forNameDecorator(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            wrapInRuntimeExceptionAndThrow(e);
        }
        return null;
    }

    private static URL createUrl(URL root, String fileName) {
        try {
            return new URL(root, fileName);
        } catch (MalformedURLException e) {
            wrapInRuntimeExceptionAndThrow(e);
            return null;
        }
    }

    private static void wrapInRuntimeExceptionAndThrow(Exception e) {
        RuntimeException exception = new RuntimeException(e.getMessage());
        exception.setStackTrace(e.getStackTrace());
        throw exception;
    }

    public void init(Class rootClass) {
        try {
            getClasses(rootClass)
                    .flatMap(clazz -> Stream.of(clazz.getDeclaredFields()))
                    .map(field -> field.getDeclaredAnnotation(AssureSame.class))
                    .filter(Objects::nonNull);
        } catch (ClassNotFoundException | IOException e) {
            wrapInRuntimeExceptionAndThrow(e);
        }
    }
}
