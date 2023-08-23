package cn.fd.utilities.common.loader

import cn.fd.utilities.core.element.Element
import cn.fd.utilities.core.element.api.loader.FileElementLoader
import cn.fd.utilities.core.element.type.ElementTypeMatcher
import java.io.File
import java.util.concurrent.CompletableFuture

/**
 * DefaultFileElementLoader
 *
 * @author TheFloodDragon
 * @since 2023/8/22 16:38
 */
object DefaultElementLoader : FileElementLoader {

    override fun load(file: File): CompletableFuture<Set<Element>> {
        return CompletableFuture.supplyAsync {
            // 获取 Config
            val conf = ConfigFileLoader.loadFromFile(file)
            // 成功加载的所有元素
            val successes: MutableSet<Element> = mutableSetOf()
            // 获取所有元素标识符
            conf?.getKeys(false)?.forEach { id ->
                // 获取当前元素下的所有元素类型
                conf.getConfigurationSection(id)?.getKeys(false)?.forEach { expression ->
                    ElementTypeMatcher.match(expression) //元素类型
                        ?.let { type ->
                            successes.add( //如果非空就加入
                                Element(
                                    id,
                                    file,
                                    type,
                                    conf = conf.getConfigurationSection(id)
                                )
                            )
                        }
                }

            }
            successes
        }
    }

}