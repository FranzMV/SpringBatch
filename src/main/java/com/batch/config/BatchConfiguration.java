package com.batch.config;

import com.batch.steps.ItemProcessorStep;
import com.batch.steps.ItemReaderStep;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchConfiguration {

    @Bean
    public ItemReaderStep itemReaderStep(){ return new ItemReaderStep(); }

    @Bean
    public ItemProcessorStep itemProcessorStep(){ return new ItemProcessorStep(); }
}
