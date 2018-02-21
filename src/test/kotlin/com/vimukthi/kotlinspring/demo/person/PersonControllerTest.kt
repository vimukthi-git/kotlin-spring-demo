package com.vimukthi.kotlinspring.demo.person

import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content


@RunWith(SpringRunner::class)
@WebMvcTest(PersonController::class)
class PersonControllerTest {

    @MockBean
    private val service: PersonService? = null
    
    @Autowired
    val mockMvc: MockMvc? = null
    
    @Test
    fun testAll() {
        whenever(service!!.all()).thenReturn(listOf<PersonValue>(PersonValue("1", "vimukthi")))
        mockMvc!!.perform(get("/api/person/all"))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"id\":\"1\",\"firstName\":\"vimukthi\",\"lastName\":" +
                        "\"\",\"email\":\"\",\"gender\":\"\",\"ipAddress\":\"\"}]"))
        
        
    }
}
