package Mua.Mua_backend.global.swagger;

import Mua.Mua_backend.global.annotation.ApiExceptions;
import io.swagger.v3.oas.models.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;

@RequiredArgsConstructor
public class SwaggerOperationCustomizer implements OperationCustomizer {

    private final SwaggerErrorExampleGenerator errorExampleGenerator;

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        ApiExceptions apiExceptions = resolveApiExceptions(handlerMethod);

        if (apiExceptions != null) {
            errorExampleGenerator.addErrorResponse(operation, apiExceptions.value());
        }

        return operation;
    }

    //현재 실행 중인 컨트롤러 메서드에 @ApiExceptions 어노테이션이 붙어있으면 그걸 찾고, 없으면 인터페이스에 붙어있는 걸 대신 찾아서 반환한다.
    private ApiExceptions resolveApiExceptions(HandlerMethod handlerMethod) {
        ApiExceptions ann = handlerMethod.getMethodAnnotation(ApiExceptions.class);
        if (ann != null) return ann;

        Method method = handlerMethod.getMethod();
        Class<?> beanType = handlerMethod.getBeanType();
        for (Class<?> i : beanType.getInterfaces()) {
            try {
                Method interfaceMethod = i.getMethod(method.getName(), method.getParameterTypes());
                ApiExceptions fromInterface = interfaceMethod.getAnnotation(ApiExceptions.class);
                if (fromInterface != null) return fromInterface;
            } catch (NoSuchMethodException ignored) {
            }
        }
        return null;
    }
}
