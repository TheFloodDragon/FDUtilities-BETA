---
title: 扩展
sidebar_position: 3
---

## 类型转换

YAML 支持使用 **严格类型标签“!!”（双感叹号+目标类型）** 来强制转换类型

```YAML
# YAML
a: !!float '666' # !! 为严格类型标签
b: '666' # 其实双引号也算是类型转换符
c: !!str 666 # 整数转为字符串
d: !!str 666.66 # 浮点数转为字符串
e: !!str true # 布尔值转为字符串
f: !!str yes # 布尔值转为字符串
```

```JOSN
// JSON
"a": 666,
"b": "666",
"c": "666",
"d": "666.66",
"e": "true"
"f": "yes"
```

## 保留换行(Newlines preserved)

> 使用 **竖线符“ | ”** 来表示该语法，每行的缩进和行尾空白都会被去掉，而额外的缩进会被保留
```YAML
# YAML
lines: |
  我是第一行
  我是第二行
    我是吴彦祖
      我是第四行
  我是第五行
```

```JSON
// JSON
"lines": "我是第一行\n我是第二行\n  我是吴彦祖\n     我是第四行\n我是第五行"
```

## 折叠换行(Newlines folded)

> 使用 **右尖括号“ > ”** 来表示该语法，只有空白行才会被识别为换行，原来的换行符都会被转换成空格

```YAML
# YAML
lines: >
  我是第一行
  我也是第一行
  我仍是第一行
  我依旧是第一行

  我是第二行
  这么巧我也是第二行
```

```JSON
// JSON
"lines": "我是第一行 我也是第一行 我仍是第一行 我依旧是第一行\n我是第二行 这么巧我也是第二行"
```

## 数据重用与合并

- 为了保持内容的简洁，避免过多重复的定义，YAML 提供了由 **锚点标签“&”** 和 **引用标签“*”** 组成的语法，利用这套语法可以快速引用相同的一些数据...

```YAML
# YAML
a: &anchor # 设置锚点
  one: 1
  two: 2
  three: 3
b: *anchor # 引用锚点
```

```JSON
// JSON
"a": {
  "one": 1,
  "two": 2,
  "three": 3
},
"b": {
  "one": 1,
  "two": 2,
  "three": 3
}
```

- 配合 **合并标签“&lt;&lt;”** 使用可以与任意数据进行合并，你可以把这套操作想象成面向对象语言中的继承...

```YAML
# YAML
human: &base # 添加名为 base 的锚点
    body: 1
    hair: 999
singer:
    <<: *base # 引用 base 锚点，实例化时会自动展开
    skill: sing # 添加额外的属性
programer:
    <<: *base # 引用 base 锚点，实例化时会自动展开
    hair: 6 # 覆写 base 中的属性
    skill: code # 添加额外的属性
```

```JSON
// JSON
"human": { "body": 1, "hair": 999 },
"singer": { "body": 1, "hair": 999, "skill": "sing" },
"programer": { "body": 1, "hair": 6, "skill": "code" }
```
