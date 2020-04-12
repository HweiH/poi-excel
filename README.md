---
title: 关于自由报表导入技术方案细化
date: 2020-4-12 18:21:37
tags: 工作笔记
---

> 提供自由报表导入的详细思路

<!-- more -->
**关于自由报表导入技术方案细化**

1. **概述**

    继《关于自由报表导入技术方案概述》文档的分析之后，我们认为有必要对基于各种报表模板形成的基础数据表到实体对象 VO 转换的过程做具体的细化，落实到具体规范、约束以及技术细节。比如：VO 的结构、各模板的配置文件 \*.config 的格式、工厂模式的考虑以及扩展性等。

2. **后台设计**

	- **目标**

    系统将用户上传的基于1104某个模板（主要是G01，G21，G22，G2501，G2502，G33）形成的基础数据表统一转换成我们需要的 VO。

	- **模式设计**

    我们首先考虑的是采用工厂模式，但是这样就限制了生产出来的 VO 结构，即：一个工厂生产一种产品。由于 1104 报表模板众多以及后面 MPA 的模板，所以考虑进一步将工厂抽象化，形成抽象工厂模式。这样就可以完整兼容各种不同模板的数据导入了。如下图所示：

    <center>![](blob/master/media/239ff94b9aa4df33666f5dd40e6084f0.png)</center>

    这里，我特别地将符合 G2501 和 G2502 两类报表形式的模板对应的 Excel 数据报表统称为标准原材料，对应的工厂为标准工厂，对应的产品为标准产品，而从原材料到产品的加工过程由标准工厂对应的机器进行加工处理。此时，我们的抽象工厂模式就退化形成生产标准产品的工厂模式。如下图所示：

	<center>![](blob/master/media/ab295f32908eaf80db8dc41535bfffdd.png)</center>

	<center>![](blob/master/media/97ad438bdc1750aead6db3bcae281174.png)</center>

	针对标准工厂，我们形成的主体类有：


	| **类名**            | **备注**                                                                         |
	|---------------------|----------------------------------------------------------------------------------|
	| **StandardFactory** | 标准工厂，继承抽象工厂类                                                         |
	| **Matter**          | 标准原材料，封装Sheet，组合关系                                                  |
	| **Config**          | 配置对象，每个原材料对应一个                                                     |
	| **Machine**         | 标准机器，生产标准产品，在机器中置入标准原材料、配置对象即可运转机器生产标准产品 |
	| **StandardProduct** | 标准产品，继承标准产品接口类                                                     |


    标准工厂类图：

	<center>![](blob/master/media/4e1cf7609ad669eb9fb465c79264dbe3.png)</center>

    整个抽象工厂的类图：

	<center>![](blob/master/media/18a2cf3eed135e11bf271065a3274304.png)</center>

    - **扩展性考虑**

    配置文件中配置参数的可覆盖性，配置文件分为“三类两套”，即：content.properties、factory.properties、\*.config三类，src/main/java类路径下与src/main/resourcees资源路径下各一套，如下图：

	<center>![](blob/master/media/542f614f4aadc782a069896b71c936a0.png)</center>

    其中：

	* \*.properties 文件中的参数配置以 src/main/resources 的为主，即：src/main/resources 的参数优先级较高，会覆盖 src/main/java 对应的参数，如：src/main/resources 某个参数配置 A=1，src/main/java 对应的参数配置 A=2，那么最终 A 的配置值为 1；

    * \*.config 文件的配置则会完整的以 src/main/resources 下面的为主，即：如果 src/main/resources 存在 G01.config，src/main/java 也存在 G01.config，那么最终会完整采用 src/main/resources 的 G01.config；

    生产其它形式的产品，只要在抽象工厂类中增加生产其它形式产品的工厂方法，新建实际工厂类实现抽象工厂类即可，但要注意符合工厂模式的开发方法，如下图：

	<center>![](blob/master/media/1301c8f6966a3341040d9e3e43bc59e2.png)</center>

	- **配置文件**

	**content.properties 文件**

	普通的 key-value 结构配置，如下图：

	<center>![](blob/master/media/e260efa4d491dc60d8bbf6af3969567d.png)</center>

    其中，key是模板ID，value是模板名。

    **factory.properties 文件**

    普通的 key-value 结构配置，如下图：

	<center>![](blob/master/media/a440484cdc3f08ef7dd382a6af1b74e5.png)</center>

    其中，key 是对应的工厂全类名，value 是以英文逗号分隔的每个模板 ID，意味着配置的工厂能处理的模板。

    **\*.config 文件**

    自定义的一种配置文件结构，一行代表一个配置，配置形式满足 key:value，只能单行注释，用 // 或者 \# ，有对应的解析器 Config.Parser，文件内容如下图：

	<center>![](blob/master/media/4db23993a4ab4af130d7ad138a22f4c1.png)</center>

	其中，左边的是 G2501.config，右边的是 G2502.config，内容解析如下：

	| **符号**  | **说明**                                                                           |
	|-----------|------------------------------------------------------------------------------------|
	| **:**     | key和value的分隔符，其中key必须对应Config类中的字段，value要么是坐标，要么是纯文本 |
	| **\@**    | 标识坐标                                                                           |
	| **\( , \)** | 指定某个坐标                                                                       |
	| **-**     | 坐标轴的范围，要么是X轴范围，要么是Y轴范围                                         |
	| **&#x0007C;**    | 增加坐标配置                                                                       |
	| **+**     | 某个坐标的直接父坐标，且父坐标不能有范围，主要用于Y轴**头部嵌套**的情况            |
	| **\#**    | 标识纯文本                                                                         |

	- **VO对象**

	StandardProduct类，直接参考目标表建立，结构如下图：

	<center>![](blob/master/media/2c7b456713ee3df9d68c2768c2f3a0ee.png)</center>

	IProduct接口，参考模板表建立的产品属性规范，结构如下图：

	<center>![](blob/master/media/f672387e67f8332c3f1b4e14df0d292b.png)</center>

3. **附加事项**

	根据以上分析，有以下三点需要后期开发时注意：

	> 	(1) 非标准产品需要新增工厂，扩展抽象工厂的实现，具体可参考标准工厂；
 	> 	(2) VO数据的清洗、转换与校验，可以在两个过程做：Excel -> VO, VO -> PO；
 	> 	(3) PO的具体结构，需要符合所有的模板表以及数据库指标数据表；

4. **完整类图**

	<center>![](blob/master/media/c1b1e91f2e8b1ba89cc91a8b81ebc75b.jpg)</center>
