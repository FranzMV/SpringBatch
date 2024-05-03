package com.batch.steps;

import com.batch.entities.Person;
import com.batch.service.IPersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Ultimo paso: Escribir en BD
 */
@Slf4j
public class ItemWriterStep implements Tasklet {

    @Autowired
    private IPersonService service;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        log.info("------------------ Inicio del paso de escritura ----------------------");

        List<Person> personList = (List<Person>) chunkContext.getStepContext()
                                                            .getStepExecution()
                                                            .getJobExecution()
                                                            .getExecutionContext()
                                                            .get("personList");

        personList.forEach(person -> {
            if(person != null)
                log.info(person.toString());
        });

        service.saveAll(personList);
        log.info("------------------ Fin del paso de escritura ----------------------");

        return RepeatStatus.FINISHED;
    }
}
