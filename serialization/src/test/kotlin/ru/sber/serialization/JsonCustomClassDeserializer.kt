package ru.sber.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class JsonCustomClassDeserializer {

    @Test
    fun `Нобходимо десериализовать данные в класс`() {
        // given
        val data = """{"client": "Иванов Иван Иванович"}"""
        val module = SimpleModule()
            .addDeserializer(Client7::class.java, Client7CustomDeserializer())
        val objectMapper = ObjectMapper()
            .registerModules(KotlinModule(), module)

        // when
        val client = objectMapper.readValue<Client7>(data)

        // then
        assertEquals("Иван", client.firstName)
        assertEquals("Иванов", client.lastName)
        assertEquals("Иванович", client.middleName)
    }

    class Client7CustomDeserializer : StdDeserializer<Client7>(Client7::class.java) {
        override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): Client7 {
            val jsonNode =
                jp.readValueAsTree<TreeNode>()
                    .get("client")
                    .toString()
                    .replace("\"", "")
                    .split(" ")
            return Client7(jsonNode[1], jsonNode[0], jsonNode[2])
        }

    }
}

