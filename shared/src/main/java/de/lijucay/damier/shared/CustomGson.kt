package de.lijucay.damier.shared

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

object CustomGson {
    fun buildGson(): Gson = GsonBuilder()
        .registerTypeAdapter(UUID::class.java, object : JsonSerializer<UUID>, JsonDeserializer<UUID> {
            override fun serialize(src: UUID, typeOfSrc: Type, context: JsonSerializationContext) =
                JsonPrimitive(src.toString())

            override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) =
                UUID.fromString(json.asString)
        })
        .registerTypeAdapter(LocalDateTime::class.java, object : JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
            override fun serialize(src: LocalDateTime, typeOfSrc: Type, context: JsonSerializationContext) =
                JsonPrimitive(src.toString())

            override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) =
                LocalDateTime.parse(json.asString)
        })
        .registerTypeAdapter(LocalDate::class.java, object : JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
            override fun serialize(src: LocalDate, typeOfSrc: Type, context: JsonSerializationContext) =
                JsonPrimitive(src.toString())
            override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) =
                LocalDate.parse(json.asString)
        })
        .create()
}