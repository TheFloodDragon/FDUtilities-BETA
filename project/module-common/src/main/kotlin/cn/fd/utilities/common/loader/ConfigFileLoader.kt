package cn.fd.utilities.common.loader

import cn.fd.utilities.common.log
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type
import java.io.File

/**
 * FileLoader
 *
 * @author TheFloodDragon
 * @since 2023/8/22 16:41
 */
object ConfigFileLoader {

    fun loadFromFile(file: File): Configuration? {
        // 文件类型
        val type = Configuration.getTypeFromExtension(file.extension)

        // 不支持 HOCON 类型
        return if (type != Type.HOCON) {
            Configuration.loadFromFile(file)
        } else {
            log("Unsupported type 'HOCON' !")
            null
        }
    }

}