package me.heruji.powerform;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web层bean配置, 自动扫描web包
 */
@Configuration
@ComponentScan("me.heruji.powerform.web")
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
}
