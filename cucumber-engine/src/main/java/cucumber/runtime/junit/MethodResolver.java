package cucumber.runtime.junit;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class MethodResolver {

    Method resolve(String signatureWithPath) {
        try {
            String signature = signatureWithPath.split(" ")[0];
            int indexOfDotBetweenClassAndMethod = signature.lastIndexOf('.');
            String fullQualifiedClassName = signature.substring(0, indexOfDotBetweenClassAndMethod);
            String methodSignature = signature.substring(indexOfDotBetweenClassAndMethod + 1, signature.length());
            int indexOfOpeningBrace = methodSignature.lastIndexOf('(');
            String methodName = methodSignature.substring(0, indexOfOpeningBrace);
            String commaSeparatedArguments = methodSignature.substring(indexOfOpeningBrace + 1, methodSignature.length() - 1);
            Class<?> stepClass = Class.forName(fullQualifiedClassName);

            if (commaSeparatedArguments.isEmpty()) {
                return stepClass.getMethod(methodName);
            }

            Map<String, Class<?>> primitiveTypeResolverMap = new HashMap<>();
            primitiveTypeResolverMap.put("int", int.class);
            primitiveTypeResolverMap.put("double", double.class);
            primitiveTypeResolverMap.put("String", String.class);

            List<? extends Class<?>> parameterTypesList = Arrays.stream(commaSeparatedArguments.split(",")).map(primitiveTypeResolverMap::get).collect(Collectors.toList());
            Class<?>[] parameterTypes = parameterTypesList.toArray(new Class<?>[parameterTypesList.size()]);
            return stepClass.getMethod(methodName, parameterTypes);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new IllegalStateException("This should not happen. We are resolving a method from a detected step definition");
        }
    }
}
