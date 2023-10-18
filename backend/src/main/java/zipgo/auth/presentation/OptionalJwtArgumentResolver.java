package zipgo.auth.presentation;

import java.util.Objects;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import zipgo.auth.dto.AuthCredentials;
import zipgo.auth.exception.TokenInvalidException;
import zipgo.auth.support.BearerTokenExtractor;
import zipgo.auth.support.JwtProvider;
import zipgo.auth.support.ZipgoTokenExtractor;
import zipgo.common.logging.LoggingUtils;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class OptionalJwtArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String ZIPGO_HEADER = "Refresh";

    private final JwtProvider jwtProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(OptionalAuth.class)
                && parameter.getParameterType().equals(AuthCredentials.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request.getHeader(AUTHORIZATION) == null) {
            return null;
        }
        if (request.getHeader(ZIPGO_HEADER) == null) {
            return null;
        }
        try {
            String accessToken = BearerTokenExtractor.extract(Objects.requireNonNull(request));
            String refreshToken = ZipgoTokenExtractor.extract(Objects.requireNonNull(request));
            String id = jwtProvider.getPayload(accessToken);
            return new AuthCredentials(Long.valueOf(id), refreshToken);
        } catch (TokenInvalidException e) {
            LoggingUtils.warn(e);
            return null;
        }
    }

}
