package com.company.YouTubeUz.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecuredFilterConfig {
    @Autowired
    private JwtFilter jwtTokenFilter;

    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(jwtTokenFilter);
        bean.addUrlPatterns("/profile/adm/*");
        bean.addUrlPatterns("/profile/*");
        bean.addUrlPatterns("/auth/adm/*");
        bean.addUrlPatterns("/category/adm/*");
        bean.addUrlPatterns("/category/public/*");
        bean.addUrlPatterns("/tag/adm/*");
        bean.addUrlPatterns("/email/adm/*");
        bean.addUrlPatterns("/attach/adm/*");
        bean.addUrlPatterns("/tag/public/*");
        bean.addUrlPatterns("/channel/public/*");
        bean.addUrlPatterns("/channel/adm/*");
        bean.addUrlPatterns("/playlist/*");
        bean.addUrlPatterns("/video/*");
        bean.addUrlPatterns("/video_tag/*");
        bean.addUrlPatterns("/like/*");
        bean.addUrlPatterns("/comment/*");
        bean.addUrlPatterns("/playlist_video/*");
        bean.addUrlPatterns("/report/*");
        bean.addUrlPatterns("/subscriptions/*");
        return bean;
    }
}
