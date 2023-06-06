package cn.fd.utilities.bukkit.loader

import cn.fd.utilities.env.BukkitEnv
import cn.fd.utilities.env.CommonEnv
import taboolib.common.env.RuntimeEnv
import taboolib.common.platform.Awake

//�о��е�С�����
object DependenciesLoader {

    /**
     * ����������
     */
    @Awake
    fun loadDependency() {
        RuntimeEnv.ENV.loadDependency(CommonEnv::class.java, true)
        RuntimeEnv.ENV.loadDependency(BukkitEnv::class.java, true)
    }

}