<#assign getLangType="util.GetLangType"?new()> <#-- 定义得到目标语言数据类型的函数 -->

//表名
${table.name}
//表CODE
${table.code}
//表CODE，去下划线，首字母小写
${table.lFormatCode}
//表CODE，去下划线，首字母大写
${table.uFormatCode}

//表列集合
<#list table.columns as col>
	列名：      ${col.name}
	列CODE：${col.code}   格式化的列CODE：${col.uFormatCode},${col.lFormatCode}
	列类型： ${col.type}   转换后的列类型：<#assign langtype = getLangType(${col.type,"java"})>${langtype}
</#list>

//表的主键（即主键只有一列）
单主键：@{table.primaryColumn.name}，@{table.primaryColumn.code}，@{table.primaryColumn.uFormatCode}，@{table.primaryColumn.lFormatCode}

//复合主键（即主键由多列组成）
<#list table.primaryKey as col>
	列名：      ${col.name}
	列CODE：${col.code}   格式化的列CODE：${col.uFormatCode},${col.lFormatCode}
	列类型： ${col.type}   转换后的列类型：<#assign langtype = getLangType(${col.type,"java"})>${langtype}
</#list>

//表的外键
<#list table.refs as ref>
	子表：CODE=${ref.childTableCode}，名称=${ref.childTableName}   //ref.childTable得到子表
	父表：CODE=${ref.parentTableCode}，名称=${ref.parentTableName} //ref.parentTable得到父表
	子表的列：CODE=${ref.childColumn.code}，名称=${ref.childColumn.name}
	父表的列：CODE=${ref.parentColumn.code}，名称=${ref.parentColumn.name}
</#list>
