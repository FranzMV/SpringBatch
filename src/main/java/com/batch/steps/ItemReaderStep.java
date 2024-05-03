package com.batch.steps;

import com.batch.constants.BatchConstants;
import com.batch.entities.Person;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Segundo paso: Despues de descomprimir el archivo, lo leemos
 */
@Slf4j
public class ItemReaderStep implements Tasklet {

    /**
     * Objeto de Spring que nos ayuda a importar archivos desde la carpeta resources.
     */
    @Autowired
    private ResourceLoader resourceLoader;

    /**
     *
     * @param contribution permite reportar el estado del proceso, reportar errores, etc.
     * @param chunkContext permite acceder al contexto de la ejecucion. a traves de definicion de datos CLAVE-VALOR
     * @return RepeatStatus.FINISHED
     * @throws Exception ex
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        log.info("------------------ Inicio del paso de lectura ----------------------");

        Reader reader = new FileReader(resourceLoader.getResource(BatchConstants.FILE_PATH).getFile());

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(BatchConstants.CSV_SEPARATOR)
                .build();

        CSVReader csvReader = new CSVReaderBuilder(reader)
                .withCSVParser(parser)
                .withSkipLines(1)
                .build();

        List<Person> personList = new ArrayList<>();
        String[] actualLine;

        while ((actualLine = csvReader.readNext()) != null){
            Person person = new Person();
            person.setName(actualLine[0]);
            person.setSurname(actualLine[1]);
            person.setAge(Integer.parseInt(actualLine[2]));

            personList.add(person);
        }

        csvReader.close();
        reader.close();
        log.info("------------------ Fin del paso de lectura ----------------------");

        chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .put("personList", personList);

        return RepeatStatus.FINISHED;
    }
}
