package org.openlearn.config;

import org.openlearn.domain.recaptcha.ReCaptchaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ReCaptchaFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(ReCaptchaFilter.class);

    private final RestTemplate restTemplate;

    private final ApplicationProperties properties;

    public ReCaptchaFilter(final RestTemplateBuilder restTemplateBuilder, ApplicationProperties properties) {
        this.restTemplate = restTemplateBuilder.build();
        this.properties = properties;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (request.getMethod().equals(HttpMethod.POST.name())) {
            log.debug("Verifying reCaptcha.");

            try {
                String reCaptchaResponse = request.getHeader("ReCaptcha-Response");
                if (reCaptchaResponse == null) {
                    throw new IllegalArgumentException("ReCaptcha-Response header must not be null.");
                }

                MultiValueMap<String,String> parameters = new LinkedMultiValueMap<>();
                parameters.add("secret", properties.getRecaptcha().getSiteSecret());
                parameters.add("response", reCaptchaResponse);

                ReCaptchaResponse verificationResponse = restTemplate
                    .postForObject(properties.getRecaptcha().getVerificationUrl(), parameters, ReCaptchaResponse.class);

                if (verificationResponse.isSuccess()) {
                    log.debug("reCaptcha successfully verified.");
                    chain.doFilter(request, response);
                } else {
                    log.error("Provided reCaptcha response failed verification: " + reCaptchaResponse);
                    response.sendError(HttpStatus.FORBIDDEN.value(), "Provided reCaptcha failed verification.");
                }

            } catch(IllegalArgumentException iae) {
                log.error("reCaptcha verification failed due to exception: ", iae);
                response.sendError(HttpStatus.BAD_REQUEST.value(), iae.getMessage());
            } catch(Exception e) {
                log.error("reCaptcha verification failed due to exception: ", e);
                response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "reCaptcha verification failed.");
            }
        } else {
            chain.doFilter(request, response);
        }
    }

}
