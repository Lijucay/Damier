package de.lijucay.damier.core.data

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

fun buildGson(): Gson = GsonBuilder()
    .registerTypeAdapter(LocalDateTime::class.java, object : JsonSerializer<LocalDateTime> {
        override fun serialize(src: LocalDateTime, typeOfSrc: Type, context: JsonSerializationContext) =
            JsonPrimitive(src.toString())
    })
    .registerTypeAdapter(LocalDateTime::class.java, object : JsonDeserializer<LocalDateTime> {
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) =
            LocalDateTime.parse(json.asString)
    })
    .registerTypeAdapter(LocalDate::class.java, object : JsonSerializer<LocalDate> {
        override fun serialize(src: LocalDate, typeOfSrc: Type, context: JsonSerializationContext) =
            JsonPrimitive(src.toString())
    })
    .registerTypeAdapter(LocalDate::class.java, object : JsonDeserializer<LocalDate> {
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) =
            LocalDate.parse(json.asString)
    })
    .create()