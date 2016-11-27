package org.qcri.micromappers.batch;

import javax.sql.DataSource;

import org.qcri.micromappers.model.GdeltMaster;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.*;
import java.net.*;
import java.util.*;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    // tag::readerwriterprocessor[]

    @Bean
    public FlatFileItemReader<GdeltMaster> reader() {
        FlatFileItemReader<GdeltMaster> reader = new FlatFileItemReader<GdeltMaster>();

        reader.setResource(new ClassPathResource("sample-gdelt.csv"));

        reader.setLineMapper(new DefaultLineMapper<GdeltMaster>() {{

            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "mmURL"});
            }});


            setFieldSetMapper(new BeanWrapperFieldSetMapper<GdeltMaster>() {{
                setTargetType(GdeltMaster.class);
            }});

        }});
        return reader;
    }

    @Bean
    public GdeltMasterItemProcessor processor() {
        return new GdeltMasterItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<GdeltMaster> writer() {
        JdbcBatchItemWriter<GdeltMaster> writer = new JdbcBatchItemWriter<GdeltMaster>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<GdeltMaster>());
        writer.setSql("INSERT INTO gdeltmaster (mmURL, mmType) VALUES (:mmURL, :mmType)");
        writer.setDataSource(dataSource);
        return writer;
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<GdeltMaster, GdeltMaster> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
    // end::jobstep[]
}
