<div align="center">

<h1 align="center">Java代码风格审查</h1>

</div>

## 一、代码风格

目前主流的java代码风格主要有两类：

- [sun风格](https://www.oracle.com/java/technologies/javase/codeconventions-contents.html) ： sun风格由oracle提出
- [google风格](https://google.github.io/styleguide/javaguide.html)

这两个的风格的明显差异主要是两个方面：

|            |        sun风格         |              google风格              |
| :--------: | :--------------------: |:----------------------------------:|
|  缩进大小  |        4个空格         |                2个空格                |
| 访问修饰符 | 推荐明确指定访问修饰符 |         在没有特殊需求时使用默认的包级私有          |

国内也有一些主流的代码风格，例如阿里的java代码规范，可参考《java开发规范》一书j。基于国内代码开发的惯性以及与阿里代码规范的共性，本文档建议项目统一使用4个空格的代码缩进。

## 二、代码风格审查

### checkstyle两种使用方式

对于代码风格审查，本文档推荐使用 [checkstyle](https://checkstyle.sourceforge.io/index.html) 来实现。有两种应用方式：

1. 基于IDE的使用（例如IntelliJ IDEA，本文档后续都以此为例）
   - 在插件市场中安装CheckStyle-IDEA插件即可

<img src=".\img\checkstyleidea.png" alt="CheckStyle-IDEA插件"/>

2. 基于maven的使用（若要将代码风格审查集成至CI过程，则需要使用此方式！！），基础maven依赖如下

```xml
<dependency>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-checkstyle-plugin</artifactId>
    <version>3.1.2</version>
</dependency>
```

若要在编译的时候自动执行检查，不进行额外手动操作，可以进行如下引入：

```xml
<build>
    <pluginManagement>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-checkstyle-plugin</artifactId>
                <version>3.1.2</version>
                <dependencies>
                    <!--指定依赖的checkstyle版本-->
                    <dependency>
                        <groupId>com.puppycrawl.tools</groupId>
                        <artifactId>checkstyle</artifactId>
                        <version>8.29</version>
                    </dependency>
                </dependencies>
                <!--指定配置文件-->
                <configuration>
                    <configLocation>checkstyle.xml</configLocation>
                    <encoding>UTF-8</encoding>
                    <consoleOutput>true</consoleOutput>
                    <failsOnError>true</failsOnError>
                    <linkXRef>false</linkXRef>
                </configuration>
            </plugin>
        </plugins>
    </pluginManagement>
</build>
```

配置完成后，可以在 setting -> tools -> Checkstyle 中查看代码审查的配置信息。插件默认提供了sun风格和google风格的完整代码审查。

### 自定义代码风格

尽管Check style默认提供了sun风格和google风格的完整代码审查，但是对于具体的项目来说，并不是总需要代码风格中的所有条件，项目开发应该灵活使用风格中的代码审查并进行一些合适的调整。我们可以在 setting -> tools -> Checkstyle 中添加自己的代码审查文件，实现自定义代码风格。我们将给出完整的sun风格和google风格checkstyle文件，并给出我们推荐的代码风格：

- [sun风格](https://github.com/checkstyle/checkstyle/blob/master/src/main/resources/sun_checks.xml)
- [google风格](https://github.com/checkstyle/checkstyle/blob/master/src/main/resources/google_checks.xml)
- [基于sun风格的custom风格](../checkstyle.xml)

## 三、custom风格在sun基础上的调整

### 禁用

|          审查类型           |                             原因                             |
| :-------------------------: | :----------------------------------------------------------: |
|        JavadocMethod        | 这一检查针对类中方法或构造函数的注释信息，要求对所有方法都要描述其参数、返回、异常信息。我们认为这是不必要的，每一个方法只需要完整描述其功能即可 |
|       JavadocVariable       | 这一检查针对类中变量的注释信息，要求每个变量都需要有完整的注释信息，然而我们认为：在大部分方法类所使用参数可以通过良好的命名表示其信息；对于实体类，我们推荐使用完整的接口文档，并在@Schema注解中定义了类中变量的信息。 |
|    MissingJavadocMethod     | 这一检查针对方法的注释信息，要求所有方法都需要对描述其功能。同样的，我们推荐使用完整的接口文档，并在@Operation注解中定义方法的详细信息。 |
| HideUtilityClassConstructor | 这一检查要求所有实体类的构造方法必须显式定义，然而在spring项目中，我们在一些类中实际上不希望额外做出的构造方法的限制。因此，我们认为这一检查在我们的项目中是不必要的。 |
|       FinalParameters       | 这一检查要求方法的入参是不可修改的，然而在我们的需求中，这一参数有时是需要修改的，且额外定义变量似乎是没有必要的。 |

### 调整

|   审查类型   |                             原因                             |
| :----------: | :----------------------------------------------------------: |
| JavadocType  | 这一检查针对Javadoc中的标签，我们认为使用一些自定义的特殊标签是合法的，可以根据需要进行设置。 |
| JavadocStyle | 这一检查要求Javadoc的第一行必须以字符 '.' 结尾，我们认为这是不必要的。 |





