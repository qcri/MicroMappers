package org.qcri.micromappers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by jlucas on 11/25/16.
 */

@EnableScheduling
@EnableAsync
@SpringBootApplication(exclude = {BatchAutoConfiguration.class})
@ImportResource("spring-batch-context.xml")
public class MicroMappersApplication {
	private static  ConfigurableApplicationContext context;
    public static void main(String[] args) {
        //String[] str = {"spring-batch-context.xml","context-datasource.xml","context-model.xml"};
        //ApplicationContext context = new ClassPathXmlApplicationContext(str);

    	context = SpringApplication.run(MicroMappersApplication.class, args);

    }
    
    public static ConfigurableApplicationContext getApplicationContext(){
    	return context;
    }
}