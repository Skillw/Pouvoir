package com.skillw.pouvoir.util

import com.google.common.reflect.TypeToken
import com.google.gson.*
import org.bukkit.inventory.ItemStack
import java.lang.reflect.Type

object GsonUtils {
    @JvmStatic
    val gson: Gson by lazy {
        GsonBuilder()
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setPrettyPrinting()
            .registerTypeAdapter(ItemStack::class.java, ItemStackSerializer())
            .create()
    }

    class ItemStackSerializer : JsonDeserializer<ItemStack>, JsonSerializer<ItemStack> {
        @Throws(JsonParseException::class)
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ItemStack {
            return ItemStack.deserialize(
                GsonBuilder().create().fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
            )
        }

        override fun serialize(src: ItemStack, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
            return GsonBuilder().create().toJsonTree(src.serialize())
        }
    }
}