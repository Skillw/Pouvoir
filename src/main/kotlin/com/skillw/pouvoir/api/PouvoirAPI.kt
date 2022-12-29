package com.skillw.pouvoir.api

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.function.context.IContext
import com.skillw.pouvoir.api.manager.sub.message.PersonalManager.Companion.getMessageType
import com.skillw.pouvoir.api.message.MessageData
import com.skillw.pouvoir.internal.core.function.TextHandler
import com.skillw.pouvoir.internal.core.function.context.SimpleContext
import com.skillw.pouvoir.internal.manager.PouFunctionManagerImpl
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

object PouvoirAPI {
    /**
     * 解析文本中的内联函数 例如:
     * - "测试内联函数: {random 0 to 1}"
     *
     * @return String 解析后的文本
     * @receiver String 含内联函数的文本
     */
    @JvmStatic
    fun String.analysis(): String {
        return TextHandler.analysis(this)
    }

    /**
     * 解析文本中的内联函数 例如:
     * - "测试内联函数: {random 0 to 1}"
     *
     * @param context 上下文
     * @return String 解析后的文本
     * @receiver String 含内联函数的文本
     */
    @JvmStatic
    fun String.analysis(context: IContext = SimpleContext()): String {
        return TextHandler.analysis(this, context)
    }

    /**
     * 执行一段内联函数
     *
     * @return Any 结果
     * @receiver String 内联函数
     */
    @JvmStatic
    fun String.eval(): Any {
        return PouFunctionManagerImpl.eval(this, context = SimpleContext()) ?: this
    }

    /**
     * 执行一段内联函数
     *
     * @param namespaces 命名空间(含哪些空间的函数)
     * @param context 上下文
     * @return Any 结果
     * @receiver String 内联函数
     */
    @JvmStatic
    fun String.eval(
        namespaces: Array<String> = arrayOf("common"),
        context: IContext = SimpleContext(),
    ): Any {
        return Pouvoir.pouFunctionManager.eval(this, namespaces = namespaces, context = context) ?: this
    }


    /**
     * 解析占位符(PouPAPI & PAPI)
     *
     * @param entity LivingEntity 实体
     * @return String 解析后的文本
     * @receiver String 待解析的文本
     */
    @JvmStatic
    fun String.placeholder(entity: LivingEntity): String {
        return Pouvoir.pouPlaceHolderAPI.replace(entity, this)
    }


    /**
     * 解析占位符(PouPAPI & PAPI)
     *
     * @param entity LivingEntity 实体
     * @param analysis Boolean 是否解析内联函数
     * @return String 解析后的文本
     * @receiver String 待解析的文本
     */
    @JvmStatic
    fun String.placeholder(entity: LivingEntity, analysis: Boolean = true): String {
        return Pouvoir.pouPlaceHolderAPI.replace(entity, this, analysis)
    }

    /**
     * 向玩家发送消息
     *
     * @param type String 消息类型
     * @param dataFunc MessageData接收器 用于处理MessageData
     * @receiver Player 向谁发送
     */
    @JvmStatic
    fun Player.send(type: String, dataFunc: MessageData.() -> Unit) {
        Pouvoir.messagerBuilderManager.build(type, this, dataFunc)
    }


    /**
     * 向玩家发送消息 消息类型取玩家的个人设置
     *
     * @param dataFunc MessageData接收器 用于处理MessageData
     * @receiver Player 向谁发送
     */
    @JvmStatic
    fun Player.sendFast(dataFunc: MessageData.() -> Unit) {
        Pouvoir.messagerBuilderManager.build(getMessageType(), this, dataFunc)
    }

}