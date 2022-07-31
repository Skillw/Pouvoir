package com.skillw.pouvoir.util

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.parser.Feature
import com.alibaba.fastjson.serializer.SerializerFeature


object GsonUtils {

    @JvmStatic
    fun Any.toJson(vararg features: SerializerFeature): String {
        return JSON.toJSONString(this, *features)
    }


    @JvmStatic
    fun <T> String.parse(clazz: Class<T>, vararg features: Feature): T? {
        return JSON.parseObject(this, clazz, *features)
    }
}