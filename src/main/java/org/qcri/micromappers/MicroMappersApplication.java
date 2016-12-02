package org.qcri.micromappers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

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