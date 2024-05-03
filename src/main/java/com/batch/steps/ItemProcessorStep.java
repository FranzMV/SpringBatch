package com.batch.steps;

import com.batch.constants.BatchConstants;
import com.batch.entities.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Tercer paso: Procesoar el archivo
 */
@Slf4j
public class ItemProcessorStep implements Tasklet {


    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        log.info("------------------ Inicio del paso de procesamiento ----------------------");
        //Recuperar la lista de personas a partir del contexto
        List<Person> personList = (List<Person>) chunkContext.getStepContext()
                                              .getStepExecution()
                                              .getJobExecution()
                                              .getExecutionContext()
                                              .get("personList");

        List<Person> personFinalList = personList.stream().map(person -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(BatchConstants.DATE_FORMAT);
            person.setDateAct(formatter.format(LocalDateTime.now()));
            return person;
        }).toList();

        //Enviamos la lista actualizada al siguiente paso
        chunkContext.getStepContext()
                    .getStepExecution()
                    .getJobExecution()
                    .getExecutionContext()
                    .put("personList", personFinalList);

        log.info("------------------ Fin del paso de procesamiento ----------------------");

        return RepeatStatus.FINISHED;
    }
}
