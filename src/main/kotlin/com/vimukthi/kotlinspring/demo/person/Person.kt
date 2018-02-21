package com.vimukthi.kotlinspring.demo.person

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.persistence.Entity
import javax.persistence.Id

@Entity 
internal data class Person(@Id var id: String = "",
                          var firstName: String = "",
                          var lastName: String = "",
                          var email: String = "",
                          var gender: String = "",
                          var ipAddress: String = "") {
}

internal interface PersonRepository : JpaRepository<Person, String> {

    fun findByEmailContainingAndFirstNameContaining(email: String, firstName: String): List<Person>
}

/**
 * Expose entity outside of the package using a value object
 */
class PersonValue(val id: String = "",
                  val firstName: String = "",
                  val lastName: String = "",
                  val email: String = "",
                  val gender: String = "",
                  val ipAddress: String = "") {
    companion object Factory {
        internal fun from(person: Person): PersonValue =
                PersonValue(
                        person.id, person.firstName, person.lastName, person.email, person.gender, person.ipAddress)
    }
}

@Service
class PersonService() {

    @Autowired 
    internal var personRepository: PersonRepository? = null

    /**
     * The service must only be initialized within the package, because of its dependancy on PersonRepository
     */
    internal constructor(personRepository: PersonRepository?): this() {
        this.personRepository = personRepository
    }
    
    fun search(name: String, email: String): List<PersonValue> {
        return personRepository!!.findByEmailContainingAndFirstNameContaining(email, name).map { PersonValue.from(it) }
    }
    
    
    fun all(): List<PersonValue>{
        return personRepository!!.findAll().map { PersonValue.from(it) }
    }
}

@RestController
class PersonController(@Autowired val personService: PersonService) {

    @GetMapping("/api/person/search")
    fun search(@RequestParam(name="name") name: String, 
               @RequestParam(name="email") email: String): List<PersonValue> {
        return personService.search(email, name)
    }

    @GetMapping("/api/person/all")
    fun all(): List<PersonValue> {
        return personService.all()
    }
}
