package com.batch.config;

import com.batch.steps.ItemDecompressStep;
import com.batch.steps.ItemProcessorStep;
import com.batch.steps.ItemReaderStep;
import com.batch.steps.ItemWriterStep;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfig{

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;


    @Bean
    @JobScope
    public ItemDecompressStep itemDecompressStep(){
        return new ItemDecompressStep();
    }

    @Bean
    @JobScope
    public ItemReaderStep itemReaderStep(){
        return new ItemReaderStep();
    }

    @Bean
    @JobScope
    public ItemProcessorStep itemProcessorStep(){
        return new ItemProcessorStep();
    }

    @Bean
    @JobScope
    public ItemWriterStep itemWriterStep(){
        return new ItemWriterStep();
    }

    @Bean
    public Step decompresFileStep(){
        return stepBuilderFactory.get("itemDecompressStep")
                .tasklet(itemDecompressStep())
                .build();
    }

    @Bean
    public Step readPersonStep() {
        return stepBuilderFactory.get("readPersonStep")
                .tasklet(itemReaderStep())
                .build();
    }

    @Bean
    public Step processPersonStep(){
        return stepBuilderFactory.get("processPerson")
                .tasklet(itemProcessorStep())
                .build();
    }

    @Bean
    public Step writePersonStep() {
        return stepBuilderFactory.get("writePersonStep")
                .tasklet(itemWriterStep())
                .build();
    }

    @Bean
    public Job readCSVJob() {
        return jobBuilderFactory.get("readCSVJob")
                .start(decompresFileStep())
                .next(readPersonStep())
                .next(processPersonStep())
                .next(writePersonStep())
                .build();
    }
}
