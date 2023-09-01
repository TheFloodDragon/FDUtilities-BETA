# Ratziel

基于Taboolib开发的一个插件。

脑子大了，暂停开发

## 功能

- <h5>暂无</h5>

## 用法

- <h5>元素类型表达式</h5> 

  格式 `命名空间:元素类型`

- <h5>元素的定义</h5>

   ```yaml
   元素标识符:
     元素类型:
       ... # 元素属性
   ```

## 开发

- <h5>注册元素类型</h5>

  - 使用 `@NewElement` 注解

    ```Kotlin
    @NewElement(
      // 元素类型名称
      name = "action",
      // 元素类型别名
      alias = ["actions"],
      // 命名空间
      space = ["actions"]
    )
    ```

    - 默认绑定处理器为 `@NewElement` 所在类 (如果实现了 `ElementHandler`)

## 需求

- <h5>Java 17 (最好)</h5>

- <h5>优先支持Minecraft 最新版本 (1.20.1)</h5>

- <h5>若无需要将不考虑低版本兼容</h5>

***

## 版本

该项目库内所有版本号为测试版本号,正式版本不采用

### Pre Alpha-第一阶段 （当前阶段）

<h6>版本号：A.0.x.x.x</h6>

- [ ] 插件前期内容开发（功能实现）

#### 计划

- [x] 日志
- [x] 工作空间
- [x] 元素加载器
- [ ] 元素处理器
- [ ] 多线程加载配置
- [ ] 动态更新元素文件
- [ ] `press` Kether动作实现
- [ ] ~~`Action`动作元素实现~~
- [ ] ~~FItem实现~~
- [ ] ~~FItem的其他插件支持~~
- [ ] ~~完成脚本动态加载、卸载功能~~

### Alpha-第二阶段 (未达到)

<h6>版本号：A.x.x.x</h6>

- [ ] 继续完善功能（完成了部分功能）

### Beta-第三阶段 (未达到，远得很)

<h6>版本号：B.x.x.x</h6>

- [ ] 插件基本内容开发完成，开始测试

#### 计划

- [ ] 实现脚本功能

### RC-第四阶段 (没个好几年达不到)

<h6>版本号：0.0.1-RC</h6>

- [ ] 第一个发布版，也就是本储存库删除时

***

### 构建

* [Gradle](https://gradle.org/) - 依赖关系管理

**Windows:**

    gradlew.bat clean build

**macOS/Linux:**

    ./gradlew clean build

构建工件在 `./outs` 文件夹中
