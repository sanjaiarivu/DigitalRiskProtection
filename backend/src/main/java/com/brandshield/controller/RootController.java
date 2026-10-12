package com.brandshield.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Handles root entry point requests.
 * If static frontend is available, forwards to /index.html.
 * Otherwise, gracefully redirects to Swagger UI API documentation.
 */
@Controller
public class RootController {

    private final Resource indexResource = new ClassPathResource("static/index.html");

    @GetMapping("/")
    public String index() {
        if (indexResource.exists()) {
            return "forward:/index.html";
        }
        return "redirect:/swagger-ui.html";
    }
}
