package cn.fd.ratziel.common.event

import cn.fd.ratziel.core.element.Element
import cn.fd.ratziel.core.element.event.ElementEvent
import taboolib.common.platform.event.ProxyEvent

/**
 * ElementLoadEvent
 * 元素加载时触发
 *
 * @author TheFloodDragon
 * @since 2023/9/2 10:32
 */
class ElementLoadEvent(
    override var element: Element
) : ElementEvent, ProxyEvent()