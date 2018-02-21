package com.vimukthi.kotlinspring.demo.person

import com.vimukthi.kotlinspring.demo.DemoApplication
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@RunWith(SpringRunner::class)
@SpringBootTest(classes = [ 
    DemoApplication::class, 
    ImportPeopleJobConfiguration::class, 
    ImportPeopleJobTest.BatchTestConfig::class 
])
class ImportPeopleJobTest {

    @Autowired
    val jobLauncherTestUtils: JobLauncherTestUtils? = null
    
    @Autowired
    val personService: PersonService? = null
    
    @Test
    fun testJob() {

        val jobExecution = jobLauncherTestUtils!!.launchJob()

        Assert.assertEquals("COMPLETED", jobExecution.exitStatus.exitCode);
        Assert.assertEquals(250, personService!!.all().size)
    }
    
    @Configuration
    internal class BatchTestConfig {

        @Bean
        fun jobLauncherTestUtils(): JobLauncherTestUtils {
            return JobLauncherTestUtils()
        }
        
    }
    
}
