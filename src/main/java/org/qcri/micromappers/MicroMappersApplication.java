package org.qcri.micromappers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by jlucas on 11/25/16.
 */
@SpringBootApplication(exclude = {BatchAutoConfiguration.class})
@ImportResource("spring-batch-context.xml")
public class MicroMappersApplication {
    public static void main(String[] args) {
        //String[] str = {"spring-batch-context.xml","context-datasource.xml","context-model.xml"};
        //ApplicationContext context = new ClassPathXmlApplicationContext(str);

        SpringApplication.run(MicroMappersApplication.class, args);
    }
}
