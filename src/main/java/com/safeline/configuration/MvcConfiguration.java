package com.safeline.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Created by alejandromoneomartinez on 13/7/17.
 */
@Configuration
public class MvcConfiguration extends WebMvcConfigurerAdapter {

    /* LANGUAGUE CONFIGURATION */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        //slr.setDefaultLocale(Locale.US);
        slr.setDefaultLocale(new Locale("es"));
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
    /* END LANGUAGUE CONFIGURATION */


    /* GSON CONFIGURATION */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter < ? >> converters) {
        GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
        converters.add(gsonHttpMessageConverter);
    }

    @Bean
    public HttpMessageConverters customConverters() {

        Collection<HttpMessageConverter<?>> messageConverters = new ArrayList<>();

        GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
        messageConverters.add(gsonHttpMessageConverter);

        return new HttpMessageConverters(true, messageConverters);
    }
    /* END CONFIGURATION */
}
