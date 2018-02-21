package com.vimukthi.kotlinspring.demo.person

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.database.JpaItemWriter
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.batch.item.file.mapping.DefaultLineMapper
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import javax.persistence.EntityManagerFactory

@Configuration
@EnableBatchProcessing
class ImportPeopleJobConfiguration(@Autowired val jobBuilderFactory: JobBuilderFactory,
                                   @Autowired val stepBuilderFactory: StepBuilderFactory,
                                   @Autowired val entityManagerFactory: EntityManagerFactory) {
    
    @Bean
    fun importUserJob(): Job {
        return jobBuilderFactory.get("importPersonJob")
                .incrementer(RunIdIncrementer())
                .flow(step1())
                .end()
                .build()
    }

    @Bean
    fun step1(): Step {
        return stepBuilderFactory.get("step1")
                .chunk<Person, Person>(10)
                .reader(reader())
                .writer(writer())
                .build()
    }

    @Bean
    internal fun reader(): FlatFileItemReader<Person> {
        val reader = FlatFileItemReader<Person>()
        reader.setResource(ClassPathResource("sample-data.csv"))
        reader.setLineMapper(object : DefaultLineMapper<Person>() {
            init {
                setLineTokenizer(object : DelimitedLineTokenizer() {
                    init {
                        setNames(arrayOf("id", "firstName", "lastName", "email", "gender", "ipAddress"))
                    }
                })
                setFieldSetMapper(object : BeanWrapperFieldSetMapper<Person>() {
                    init {
                        setTargetType(Person::class.java)
                    }
                })
            }
        })
        reader.setLinesToSkip(1)
        return reader
    }

    @Bean
    internal fun writer(): JpaItemWriter<Person> {
        val writer = JpaItemWriter<Person>()
        writer.setEntityManagerFactory(entityManagerFactory)
        return writer
    }
}