package cn.fd.utilities.core.element.api

import cn.fd.utilities.core.element.ElementHandler
import cn.fd.utilities.core.element.ElementService
import taboolib.common.LifeCycle
import taboolib.common.inject.ClassVisitor
import taboolib.common.io.getInstance
import taboolib.common.platform.Awake
import java.util.function.Supplier

/**
 * ElementRegister
 *
 * @author: TheFloodDragon
 * @since 2023/8/14 15:50
 */
@Awake
class ElementRegister : ClassVisitor(0) {

    override fun visitStart(clazz: Class<*>, instance: Supplier<*>?) {
        if (clazz.isAnnotationPresent(NewElement::class.java) && ElementHandler::class.java.isAssignableFrom(clazz)) {
            val anno = clazz.getAnnotation(NewElement::class.java)
            try {
                /**
                 * 处理器
                 */
                val handler =
                    if (instance == null)
                        clazz.asSubclass(ElementHandler::class.java).getInstance(true)!!.get()
                    else instance.get() as ElementHandler
                /**
                 * 注册
                 */
                ElementService.registerElementType(
                    space = anno.space,
                    name = anno.name,
                    alias = anno.alias.toSet(),
                    handler = handler
                )
            } catch (e: Exception) {
                e.printStackTrace()
                error("Unable to register element form class $clazz!")
            }
        }
    }

    override fun getLifeCycle(): LifeCycle {
        return LifeCycle.INIT
    }

}
