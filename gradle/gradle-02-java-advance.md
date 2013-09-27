使用 Gradle 自動化建置 Java 專案（二）
==================================

Gradle 是用途廣泛的建置工具，但最重要的一點，就是非常適合處理 Java 專案，它讓 Java 專案自動化建置（Build Automation）變得更容易上手。

本篇將介紹 Gradle 對於 Java 專案的進階設定，有關於相依性管理（Dependency Management）的部份。

![Building and Testing with Gradle](http://akamaicovers.oreilly.com/images/0636920019909/cat.gif)

Gradle 對於 Java 專案建置管理，最重要的一項功能就是相依性管理（Dependency Management），它讓開發者不必再深陷眾多函式庫背後錯綜複雜的蜘蛛網。

舉例來說，在 Apache Jakarta 專案底下的 HttpClient 函式庫，它提供 HTTP 協定相關存取功能，若你想在 Java 專案中使用它，可以從它的開源專案網站下載「httpcomponents-client-4.3-bin.zip」，這個壓縮檔包含所需的 Library 檔案（\*.jar）。

* HttpClient 4.3 Downloads - http://hc.apache.org/downloads.cgi

當你將它解壓縮後，會發現其實不只一個 JAR 檔案（實際上有以下 7 個檔案），這是因為 Java 函式庫的開發者，不會讓一個函式庫包山包海，於是像 MIME types 這種可被獨立重複利用的函式庫，就單獨存在一個 httpmime-4.3.jar 的 JAR 檔中；而除錯訊息當然也是直接利用既有的 Commons Logging 函式庫（commons-logging-1.1.3.jar）。

```
commons-codec-1.6.jar          httpclient-cache-4.3.jar
commons-logging-1.1.3.jar      httpcore-4.3.jar
fluent-hc-4.3.jar              httpmime-4.3.jar
httpclient-4.3.jar
```

如果你只將 httpclient-4.3.jar 放到專案的 libs 資料夾，就可能造成在編譯或執行階段，發生某個類別無法存取的意外，影響嚴重時程式就無法繼續執行。

所以在建置工具不支援相依性管理的情況下，我們可能只為了用 HttpClient 就必須將上面的 7 個 JAR 檔通通塞進專案的 libs 資料夾，並且需要維護這些檔案，避免同時存在多個僅版本代號不同的相同函式庫，或是某些函式庫之間有衝突問題需要解決。

當專案需要的函式庫數量愈來愈多，這個 libs 資料夾就會呈現爆炸性地成長。日後當某個函式庫需要更新版本時，又要花費時間檢視這些檔案。

許多 Java 專案將這些外部函式庫（即 libs 資料夾），整個往版本控制系統（如 SVN 或 GIT）上面丟，這種作法相當不理想，因為真正屬於專案的程式碼和資源，可能根本 10MB 不到，但這些函式庫 JAR 檔的體積，卻可能高達數十 MB 之多；但如果不這樣做，就必須要有其他替代的檔案伺服器與 Script 來處理這些共用的函式庫檔案。

Gradle 簡單而且近乎完美地解決 Java 專案的 Dependency Management 問題。

事實上應該是說 Maven 已經解決 Dependency Management 的問題，只可惜它並不像 Gradle 如此簡單易用；除非您想要自行建置 Maven Repository，否則可以考慮直接踏進 Gradle 的殿堂。

build.gradle

```
apply plugin: 'java'

repositories {
    mavenCentral()
}

dependencies {
    compile 'commons-codec:commons-codec:1.8'
}

jar {
    manifest {
        attributes 'Main-Class': 'com.example.Main'
    }
}
```

以上的 Gradle 設定檔，已增加 repositories 及 dependencies 設定。

repositories 是用於設定專案的 Repository 來源，也就是存放函式庫 JAR 檔的伺服器位置，內建的 mavenCentral() 是從官方定義的 Maven Repository 伺服器作為主要來源，它已經包含多數常用的開放源碼 Java 函式庫。若要自行添加其他 Maven Repository 來源，可以直接設定網址：

```
repositories {
    maven {
        url "http://repo.mycompany.com/maven2"
    }
}
```

dependencies 是專案依賴的函式庫定義，它可依據不同建置階段做設定，例如：

* compile - 原始碼編譯時期（Compile time）就需要的函式庫
* runtime - 程式執行時期（Runtime）才需要用到的函式庫
* testCompile - 單元測試（JUnit）原始碼編譯時期
* testRuntime - 單元測試執行階段

後面連接的套件定義，其格式為：「Group:Module:Version」。

Group 通常代表發行這個函式庫的組織，Module 是函式庫名稱，而 Version 是函式庫的版本代號（通常與原始開發團隊發行用的版本代碼一致）。例如以下是一些函式庫定義的範例：

```
runtime 'mysql:mysql-connector-java:5.1.26'
compile 'net.java.dev.jets3t:jets3t:0.9.0'
compile 'org.apache.httpcomponents:httpclient:4.3'
```

可以注意到 MySQL Connector/J （mysql-connector-java）使用 runtime 而不是 compile，這是因為一般在 Java 程式中，開發者僅會使用 Java SE 內建 JDBC 所提供的資料庫存取介面（在 java.sql.* package 底下的類別），利用 Class.forName("com.mysql.jdbc.Driver") 註冊資料庫的驅動器（Driver），而不會在程式中直接 import 由 Connector/J 定義的類別，所以在編譯階段不會依賴這個外部的 JAR 檔，一直到執行階段連結資料庫才會使用，這種情況我們就能將它定義為 runtime 而不必用 compile 設定。

在主程式中，我們加入使用 Base64 進行字串編碼的示範，這個範例使用 Commons Codec 函式庫提供的 Base64 類別。

src/main/java/com/example/Main.java

```
package com.example;

import org.apache.commons.codec.binary.Base64;

public class Main {

    public static void main(String args[]) {
        System.out.println(new Main().sayHello());

        Base64 base64 = new Base64();
        byte[] bytes = base64.encode("I Love Gradle".getBytes());

        System.out.println(new String(bytes));
    }

    public String sayHello() {
        return "Hello";
    }
}
```

利用 ``gradle build`` 指令自動建置專案，若編譯沒問題就可以產生 JAR 檔案。

到目前如果一切順利，可以成功打包 ex002.jar 檔案，但你可能發現無法用直接執行 JAR 程式（錯誤訊息摘要如下）：

```
$ java -jar build/libs/ex002.jar

Exception in thread "main" java.lang.NoClassDefFoundError: org/apache/commons/codec/binary/Base64
```

發生 NoClassDefFoundError 的錯誤，導致程式無法繼續執行，原因是我們打包的 JAR 檔，僅包含 Main.java 編譯產生的 Main.class 檔案；尚未包含 Commons Codec 所需的其他 JAR 檔案。

這個預設的結果其實是合理的，因為如果你將開發一個函式庫，打包成 JAR 給其他人使用，你並不會希望將其他外部函式庫也一併包含進去。

但如果你希望這個程式可以獨立被執行，將所有東西打包成一個 JAR 檔，讓使用者下載一個檔案就能執行，那麼你需要將專案打包成 FatJar 形式。

修改 build.gradle 的 jar 設定，可以輕鬆將其他相依函式庫的檔案也一起打包。

```
jar {
    from {
        configurations.runtime.collect {
            it.isDirectory() ? it : zipTree(it)
        }
    }
    manifest {
        attributes 'Main-Class': 'com.example.Main'
    }
    exclude('META-INF/*.txt')
}
```

重新執行一次建置，並利用 ``java -jar`` 指令執行，以下是程式執行結果的範例，可以看到字串已經經過 Base64 編碼。

```
$ java -jar build
$ java -jar build/libs/ex002.jar 
Hello
SSBMb3ZlIEdyYWRsZQ==
```

本文使用的範例程式碼，可在以下網址取得：

* http://git.io/6LFfHA

參考資料：

1. [Building and Testing with Gradle, O'Reilly](http://shop.oreilly.com/product/0636920019909.do)
2. [Gradle User Guide](http://www.gradle.org/documentation)
