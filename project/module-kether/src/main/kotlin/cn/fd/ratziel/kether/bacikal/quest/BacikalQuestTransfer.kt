package cn.fd.ratziel.kether.bacikal.quest

/**
 * @author Lanscarlos
 * @since 2023-08-21 20:37
 */
interface BacikalQuestTransfer {

    val name: String

    fun transfer(source: StringBuilder)

    /**
     * 抽取所有字符并清空容器
     * */
    fun StringBuilder.extract(): String {
        val result = toString()
        clear()
        return result
    }

}