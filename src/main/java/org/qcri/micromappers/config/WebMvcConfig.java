package org.qcri.micromappers.config;

import org.qcri.micromappers.interceptor.AdminInterceptor;
import org.qcri.micromappers.interceptor.CollectionCollaboratorInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by jlucas on 12/20/16.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor()).addPathPatterns("≈**");
        
        //Intercepts whether the requestor is collaborator/admin or not on the requested Collection
        registry.addInterceptor(getCollectionCollaboratorInterceptor()).addPathPatterns("/*/start", "/*/stop", "/*/restart", 
        		"/*/status", "/*/update", "/*/collaborators", "/*/addCollaborator" ,"/*/removeCollaborator",
        		"/collection/delete", "/collection/untrash", "/collection/count", "/collection/view/details", "/collection/view/update");
    }
    
    @Bean
    CollectionCollaboratorInterceptor getCollectionCollaboratorInterceptor(){
    	return new CollectionCollaboratorInterceptor();
    }
}
