package ru.sber.serialization

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertEquals

class JsonCustomNamingPropertyStrategyTest {

    @Test
    fun `Кастомная стратегия десериализации`() {
        // given
        val data =
            """{"firstName": "Иван", "lastName": "Иванов", "middleName": "Иванович", "passportNumber": "123456", "passportSerial": "1234", "birthDate": "1990-01-01"}"""
        val objectMapper = ObjectMapper()
                .registerModules(KotlinModule(), JavaTimeModule())
                .enable(SerializationFeature.INDENT_OUTPUT)

        // when
        val client = objectMapper.readValue<Client1>(data)

        // then
        assertEquals("Иван", client.firstName)
        assertEquals("Иванов", client.lastName)
        assertEquals("Иванович", client.middleName)
        assertEquals("123456", client.passportNumber)
        assertEquals("1234", client.passportSerial)
        assertEquals(LocalDate.of(1990, 1, 1), client.birthDate)
    }
}
