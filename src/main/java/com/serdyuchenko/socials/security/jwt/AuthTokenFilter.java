package com.serdyuchenko.socials.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import com.serdyuchenko.socials.security.userdetails.UserDetailsServiceImpl;

import java.io.IOException;

/**
 * Фильтр извлекает JWT из заголовка Authorization и кладет аутентификацию в SecurityContext.
 */
@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {
    private static final String SWAGGER_UI_PATH = "/swagger-ui";
    private static final String OPEN_API_PATH = "/v3/api-docs";
    private static final String SWAGGER_HTML_PATH = "/swagger-ui.html";

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * Проверяет токен из запроса и, если он валиден, аутентифицирует пользователя в текущем контексте.
     *
     * @param request входящий HTTP-запрос
     * @param response HTTP-ответ
     * @param filterChain цепочка фильтров
     * @throws ServletException если произошла ошибка сервлета
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Исключает Swagger-маршруты из JWT-проверки, чтобы документация была доступна без токена.
     *
     * @param request входящий HTTP-запрос
     * @return {@code true}, если текущий путь относится к Swagger или OpenAPI
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        return servletPath.startsWith(SWAGGER_UI_PATH)
                || servletPath.startsWith(OPEN_API_PATH)
                || SWAGGER_HTML_PATH.equals(servletPath);
    }

    /**
     * Извлекает токен из заголовка вида {@code Authorization: Bearer <token>}.
     *
     * @param request входящий HTTP-запрос
     * @return строка JWT или {@code null}, если заголовок отсутствует либо имеет неверный формат
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
