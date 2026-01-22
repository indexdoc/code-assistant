# CodeAsst 编码助理
编码助理（CodeAsst）是一款功能强大的代码生成工具，核心原理遵循 **数据模型 + 模板 = 代码** 的设计理念，助力开发团队和个人实现批量、标准化的代码生成，大幅提升开发效率。

目前支持 PowerDesigner 生成的 PDM 文件，可从 Oracle、MySQL、MS SQL Server、PostgreSQL 等多种数据库获取表模型，也支持通过 XML 文件手动编辑数据模型。

模板语法采用类 Velocity（与 Freemarker 类似）风格，支持自定义**模板集**。一个模板集可包含多个模板文件，通过模板集能够一键生成一个软件项目的所有代码文件与配置文件。

此工具适用于各类软件公司、工作室、开发团队及个人开发者。
数据模型是软件系统的基础，系统的大部分操作都围绕基础数据的增删查改展开。如果你习惯于**先设计数据模型，再编写代码**，那么 CodeAsst 将会是你的高效开发助手。熟练使用后，你可以批量生成一个系统的几乎所有代码；更可以基于已有系统的代码模板，批量生成多个系统的 Demo 版本——这并非夸大，而是 CodeAsst 已经实现的功能。

[![Python Version](https://img.shields.io/badge/python-3.10+-green.svg)](https://www.python.org/) [![GitHub Stars](https://img.shields.io/github/stars/indexdoc/indexdoc-model-to-code?style=social)](https://github.com/indexdoc/indexdoc-model-to-code)

## 📝 软件授权方式
此软件采用**共享软件**授权方式，未注册用户在功能使用上会受到限制。

- **注册费用**：初次注册费用为 960 元，有效期 1 年；后续每年续费费用为 180 元。
- **授权限制**：每份注册软件仅允许在同一台机器上安装。若更换电脑或进行硬件升级，请联系技术支持处理。
- **购买渠道**：
  1.  前往 [软行天下网站](http://www.sharebank.com.cn/) 购买
  2.  直接联系作者（邮箱：Jooze@qq.com）咨询购买事宜
- **注册码发放**：购买后 24 小时内，用户将收到软件注册码，续费相关事宜请直接联系作者。

## ✨ 代码生成流程
![生成的文件示例](https://github.com/indexdoc/indexdoc-model-to-code/raw/main/codeGenerationProcess.png)
- **数据模型**：用数据模型工具生成的文件，CodeAsst支持PowerDesigner生成的PDM文件（需要安装PowerDesigner15.3以上版本）和Oracle、MySQL、MS SQL Server、PostgreSQL等四种数据库的表模型。
- **代码模板**：用户自定义的模板文件，一般是根据已有代码，将其中需要数据模型替换的内容用CodeAsst自定义的变量进行替换。
- **数据类型映射**：从数据模型到编程语言之间的数据类型映射关系，比如Oracle的Varchar2对应Java中的String。
- **代码生成**：数据模型+代码模板，通过数据类型映射关系生成代码。


## 🚀 代码生成操作
### 系统主页面
![生成的文件示例](https://github.com/indexdoc/indexdoc-model-to-code/raw/main/mainPage.png)
### “数据类型映射”界面
![生成的文件示例](https://github.com/indexdoc/indexdoc-model-to-code/raw/main/dataTypeMapping.png)

数据类型映射关系可以保存在文件中，CodeAsst有一个默认的数据类型映射文件default.typemap，用户可以编辑自己的数据类型映射文件。
### “编辑模板”界面
![生成的文件示例](https://github.com/indexdoc/indexdoc-model-to-code/raw/main/editTemplate.png)

CodeAsst支持模板集，一个模板集包括多个模板，比如你可以定义Java程序的模板集，也可以定义一个.NET程序的模板集。
#### 新建模板
点击“新建模板”按钮可以新建一个模板文件，在这个界面中可以导入已有的模板文件的内容。
模板类型有两种：面向单个表的TPL_TYPE_TABLE和面向整个数据模型的TPL_TYPE_MODEL。TPL_TYPE_TABLE适合于对单表的增删查改操作的代码模板，TPL_TYPE_MODEL适合于面向多个表的操作文件。
![生成的文件示例](https://github.com/indexdoc/indexdoc-model-to-code/raw/main/newTemplate.png)
#### TPL_TYPE_TALBE模板使用说明
此模板通过内置变量table获取数据模型中的一个table的相关属性。
模板的注释使用“#*”“*#”，此两者之间包括的内容的生成代码时会略掉。
```bush
#*模板使用方法

 1、此模板使用Velocity语法；

 2、模板类型为“TPL_TYPE_TABLE”需使用table作为引用对象，模板类型为“TPL_TYPE_MODEL”的需使用model作为引用对象；

 3、此软件内置了一些功能函数，需用fun进行引用，目前支持函数列表如下，如需新的函数功能，请联系作者：

    fun.lFormat(String code):将输出参数为下划线分隔的字符串中的下划线去掉，第一个词的首字母小写，其余分隔词的首字母大写；

    fun.uFormat(String code):将输出参数为下划线分隔的字符串中的下划线去掉，第一个词的首字母大写，其余分隔词的首字母大写；

    fun.lowerFirst(String str):将字符串的首字母小写，其余字母保持不变

    fun.upperFirst(String str):将字符串的首字母大写，其余字母保持不变

    fun.lower(String str):将字符串改为小写

    fun.upper(String str):将字符串改为大写

    fun.toLangDataType(String dbdt, String lang):查找typemap表，根据数据库的字段类型得到对应的语言的数据类型。

 4、生成文件名规则的设置也使用Velocity语法，可以在文件名规则中使用子目录规则，文件路径分隔符可以用"${sep}"，也可以直接用反斜杠"/"

  *#

//表名

${table.name} $table.name

//表CODE

${table.code} $table.code

//表CODE，去下划线，首字母小写

${fun.lFormat($table.code)}

//表CODE，去下划线，首字母大写

${fun.uFormat($table.code)}

 

 

//表列集合

#foreach($col in $table.columns)

第${count}列

    列名：      ${col.name}

    列CODE：${col.code}   格式化的列CODE：${fun.uFormat($col.code)},${fun.lFormat($col.code)}

    列类型： ${col.type}   目标语言列类型：${fun.toLangDataType($col.type,"JAVA")}

#end

 

#if ($table.primaryKey.size()>0) ##先判断是否有主键

//表的主键（即主键只有一列）

单主键：${table.primaryColumn.name}，${table.primaryColumn.code}，${fun.lFormat($table.primaryColumn.code)}，${fun.uFormat($table.primaryColumn.code)}

//复合主键（即主键由多列组成）

#foreach ($col in $table.primaryKey)

    列名：      ${col.name}

    列CODE：${col.code}   格式化的列CODE：${fun.uFormat($col.code)},${fun.lFormat($col.code)}

    列类型： ${col.type}   目标语言列类型：${fun.toLangDataType($col.type,"JAVA")}

#end

#end
 
//表的外键

#if ($table.refs.size() > 0)

#foreach ($ref in $table.refs)

    子表：CODE=${ref.childTable.code}，名称=${ref.childTable.name}   //ref.childTable得到子表

    父表：CODE=${ref.parentTable.code}，名称=${ref.parentTable.name} //ref.parentTable得到父表

    子表的列：CODE=${ref.childColumn.code}，名称=${ref.childColumn.name}

    父表的列：CODE=${ref.parentColumn.code}，名称=${ref.parentColumn.name}

#end
#end
```
#### TPL_TYPE_MODEL模板使用说明
此模板通过内置变量model获取数据模型中的相关属性。
模板的注释使用“#*”“*#”，此两者之间包括的内容的生成代码时会略掉。
```bush
#*模板使用方法

 1、此模板使用Velocity语法；

 2、模板类型为“TPL_TYPE_TABLE”需使用table作为引用对象，模板类型为“TPL_TYPE_MODEL”的需使用model作为引用对象；

 3、此软件内置了一些功能函数，需用fun进行引用，目前支持函数列模型如下，如需新的函数功能，请联系作者：

    fun.lFormat(String code):将输出参数为下划线分隔的字符串中的下划线去掉，第一个词的首字母小写，其余分隔词的首字母大写；

    fun.uFormat(String code):将输出参数为下划线分隔的字符串中的下划线去掉，第一个词的首字母大写，其余分隔词的首字母大写；

    fun.toLangDataType(String dbdt, String lang):查找typemap模型，根据数据库的字段类型得到对应的语言的数据类型。

    fun.lowerFirst(String str):将字符串的首字母小写，其余字母保持不变

    fun.upperFirst(String str):将字符串的首字母大写，其余字母保持不变

    fun.lower(String str):将字符串改为小写

    fun.upper(String str):将字符串改为大写

 4、生成文件名规则的设置也使用Velocity语法，可以在文件名规则中使用子目录规则，文件路径分隔符可以用"${sep}"，也可以直接用反斜杠"/"

  *#

 

//模型名

${model.name} $model.name

 

//遍历全部表

#foreach($table in $model.tables)

第${count}个表：表名：${table.name}    表CODE：${table.code} 

#end

 

//遍历已选择的表

#foreach($table in $model.selectedTables)

第${count}个表：表名：${table.name}    表CODE：${table.code} 

#end

 

//模型里所有外键

#if ($model.refs.size() > 0)

#foreach ($ref in $model.refs)

    子表：CODE=${ref.childTable.code}，名称=${ref.childTable.name}   //ref.childTable得到子模型

    父表：CODE=${ref.parentTable.code}，名称=${ref.parentTable.name} //ref.parentTable得到父模型

    子表的列：CODE=${ref.childColumn.code}，名称=${ref.childColumn.name}

    父表的列：CODE=${ref.parentColumn.code}，名称=${ref.parentColumn.name}
#end
#end
 

```
#### 模板文件命名规则
每个模板可以自定义一个模板生成文件的命名规则
模板类型为TPL_TYPE_TABLE 的命名规则可以使用内置变量table进行定义，同时支持内置函数和子目录，例如表USER_BOOK对应命名规则为“txt/${fun.uFormat(${table.code})}_example.txt”，则生成的文件为目录txt下的“UserBook_example.txt”。
类似，模板类型为TPL_TYPE_MODEL的命名规则使用内置变量model进行定义。
#### 模板集
![生成的文件示例](https://github.com/indexdoc/indexdoc-model-to-code/raw/main/templateSet.png)
#### 模板内容
模板内容可以直接在CodeAsst中进行编辑，也可以使用自己熟悉的文本编辑器编辑。
![生成的文件示例](https://github.com/indexdoc/indexdoc-model-to-code/raw/main/modelSet.png)
### “打开数据模型”界面
![生成的文件示例](https://github.com/indexdoc/indexdoc-model-to-code/raw/main/openDataModel.png)
#### 打开PDM文件
需要安装PowerDesigner15.3及以上版本，用户在文件选择对话框中选择了PDM文件后，CodeAsst会打开PowerDesigner获取PDM文件中的数据模型信息。
#### 使用已打开的PDM文件
需要安装PowerDesigner15.3及以上版本，CodeAsst会自动连接PowerDesigner获取用户已经打开的PDM文件。
#### 打开XML文件
CodeAsst自定义了自己的数据模型XML文件，用户可以自行编辑XML文件以定义自己的数据模型。在“选择表模型”的界面中，用户可以将已打开的数据模型导出为XML文件进行编辑。
#### 连接ORACLE、MySQL、Sql Server、PostgreSQL数据库
点了相应按钮后，会弹出连接数据库的对话框，在输入JDBC的连接地址、用户名和密码后，点“连接”按钮获取数据库的用户列表。

![生成的文件示例](https://github.com/indexdoc/indexdoc-model-to-code/raw/main/connectDatabase.png)

在选中的用户名中选择一个，点确定后，CodeAsst会读取此用户下的所有表的属性结构信息，可能会花费几分钟时间完成相应操作。

![生成的文件示例](https://github.com/indexdoc/indexdoc-model-to-code/raw/main/choseDatabase.png)
### “选择表模型”界面
在数据模型已打开的情况下，在“选择表模型”界面可以查看并选择需要生成代码的表模型。
同时，可以将数据模型导出为XML文件（后缀名为.model.xml）。
![生成的文件示例](https://github.com/indexdoc/indexdoc-model-to-code/raw/main/choseTableModel.png)
### 代码生成
在代码模板已经确认，数据模型已经确认的情况下，可以打开“代码生成”界面。
![生成的文件示例](https://github.com/indexdoc/indexdoc-model-to-code/raw/main/codeProduce.png)

如图，点击红圈内的按钮可以设置生成文件的目录，点击“生成文件”按钮可以生成文件。

![生成的文件示例](https://github.com/indexdoc/indexdoc-model-to-code/raw/main/createFlies.png)

非授权用户仅能生成3个文件。

## 📝 附录
### 内置函数列表
```bush
fun.lFormat(String code):将输出参数为下划线分隔的字符串中的下划线去掉，第一个词的首字母小写，其余分隔词的首字母大写；

fun.uFormat(String code):将输出参数为下划线分隔的字符串中的下划线去掉，第一个词的首字母大写，其余分隔词的首字母大写；

fun.lowerFirst(String str):将字符串的首字母小写，其余字母保持不变

fun.upperFirst(String str):将字符串的首字母大写，其余字母保持不变

fun.lower(String str):将字符串改为小写

fun.upper(String str):将字符串改为大写

fun.toLangDataType(String dbdt, String lang):查找数据类型映射表，根据数据库的字段类型得到对应的语言的数据类型。

样例：

//表CODE，去下划线，首字母大写

${fun.uFormat($table.code)}
```
### 内置变量列表
#### TPL_TYPE_TABLE类型的模板
TPL_TYPE_TALBE类型的模板以table作为主变量获取选中的表的字段信息。
```bush
l 表名：table.name

l 表CODE：table.code

l 表列集合：table.columns

#foreach($col in $table.columns)

第${count}列

    列名：      ${col.name}

    列CODE：${col.code}   格式化的列CODE：${fun.uFormat($col.code)},${fun.lFormat($col.code)}

    列类型： ${col.type}   目标语言列类型：${fun.toLangDataType($col.type,"JAVA")}

#end

l 主键：table.primaryKey

#if ($table.primaryKey.size()>0) ##先判断是否有主键

//表的主键（即主键只有一列）

单主键：${table.primaryColumn.name}，${table.primaryColumn.code}，${fun.lFormat($table.primaryColumn.code)}，${fun.uFormat($table.primaryColumn.code)}

//复合主键（即主键由多列组成）

#foreach ($col in $table.primaryKey)

    列名：      ${col.name}

    列CODE：${col.code}   格式化的列CODE：${fun.uFormat($col.code)},${fun.lFormat($col.code)}

    列类型： ${col.type}   目标语言列类型：${fun.toLangDataType($col.type,"JAVA")}

#end

#end

l 外键:table.refs

#if ($table.refs.size() > 0)

#foreach ($ref in $table.refs)

    子表：CODE=${ref.childTable.code}，名称=${ref.childTable.name}   //ref.childTable得到子表

    父表：CODE=${ref.parentTable.code}，名称=${ref.parentTable.name} //ref.parentTable得到父表

    子表的列：CODE=${ref.childColumn.code}，名称=${ref.childColumn.name}

    父表的列：CODE=${ref.parentColumn.code}，名称=${ref.parentColumn.name}

#end

#end
```
#### TPL_TYPE_MODEL类型模板
```bush
l 模型名：model.name

l 模型中的表集合：model.tables

#foreach($table in $model.tables)

第${count}个表：表名：${table.name}    表CODE：${table.code} 

#end

l 模型中已选择的表集合：model.selectedTables

#foreach($table in $model.selectedTables)

第${count}个表：表名：${table.name}    表CODE：${table.code} 

#end

l 模型中的外键集合：model.refs

#if ($model.refs.size() > 0)

#foreach ($ref in $model.refs)

    子表：CODE=${ref.childTable.code}，名称=${ref.childTable.name}   //ref.childTable得到子模型

    父表：CODE=${ref.parentTable.code}，名称=${ref.parentTable.name} //ref.parentTable得到父模型

    子表的列：CODE=${ref.childColumn.code}，名称=${ref.childColumn.name}

    父表的列：CODE=${ref.parentColumn.code}，名称=${ref.parentColumn.name}

#end

#end

```
### 模板样例
#### 模板集合
![生成的文件示例](https://github.com/indexdoc/indexdoc-model-to-code/raw/main/modelSet.png)
#### 模板内容
模板内容可以直接在CodeAsst中进行编辑，也可以使用自己熟悉的文本编辑器编辑。
![生成的文件示例](https://github.com/indexdoc/indexdoc-model-to-code/raw/main/templateContent.png)
### 数据类型映射文件样例
CodeAsst自带了一个数据类型映射文件default.typemap，用户可以根据自己需要进行新增和修改。
![生成的文件示例](https://github.com/indexdoc/indexdoc-model-to-code/raw/main/dataTypeMapping.png)
### 数据模型XML文件说明
CodeAsst自定义了数据模型的XML语法，用户可以用PowerDesigner编辑数据模型后导出为XML文件，然后用文本编辑器看看文件格式。
## 🔧常见问题
- 支持PowerDesigner的哪几个版本：目前测试过PowerDesigner15.3和16.1。低于15.3的PowerDesigner版本本软件无法支持。如果有发现高于15.3的软件版本支持不了的，请和作者联系处理。

- 支持哪些数据库系统：理论上所有关系型数据库系统都可以支撑，目前测试过Oracle、MySQL、Sql Server、PostgreSQL四种数据库系统。如果有需要支持其它数据库系统，请和作者联系。

- 可以定制化开发么：当然可以，请和作者联系，根据定制化开发的工作量进行收费。

- 如何联系作者和技术支持：购买了本软件后可以获得作者的技术支持。联系邮箱和QQ为：Jooze@qq.com。

## 📞 联系方式

- 作者：杭州智予数信息技术有限公司

- 邮箱：indexdoc@qq.com

- 官方网站：https://www.indexdoc.com/


