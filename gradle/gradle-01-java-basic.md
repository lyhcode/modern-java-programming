使用 Gradle 自動化建置 Java 專案（一）
==================================

Gradle 是用途廣泛的建置工具，但最重要的一點，就是非常適合處理 Java 專案，它讓 Java 專案自動化建置（Build Automation）變得更容易上手。

![Building and Testing with Gradle](http://akamaicovers.oreilly.com/images/0636920019909/cat.gif)

多數 Java 專案都有相似的建置流程：編譯原始碼（\*.java）、執行一些單元測試（UnitTests），然後將完成的 Byte Code 程式（\*.class）打包成 JAR 檔發佈。

許多開發者依賴開發工具，如 Eclipse、NetBeans 或 IntelliJ IDEA 提供的功能，來完成專案建置的工作。

對於比較進階的需求，例如在建置過程中，進行一些檔案處理的工作，這些 IDE 無法完成的事，就必須仰賴自動化建置專用的工具。例如 C / C++ 開發者常用 Makefile，過去 Java 開發者也常用 Apache Ant 來設計建置流程。

Gradle 對 Java 專案建置的支援，提供一系列預先定義好的 Task，並以 Plug-in 方式提供。

在上一篇文章中，我們已經介紹 Gradle 使用 build.gradle 命名的檔案定義任務。使用 apply plugin 語法，就能宣告使用指定的 Plug-in 名稱。

以下是宣告使用 Java Plug-in。

```
apply plugin: 'java'
```

只要這一行程式碼，我們的 build.gradle 就已經擁有對 Java 專案的基本建置功能。

我們可以利用 ``gradle -q tasks`` 查詢 Java Plug-in 提供哪些 Task 可用，其中已包含常用的 Java 專案建置流程，例如：

* compileJava 編譯 Java 原始碼
* test 使用 JUnit 測試程式
* jar 打包成 JAR 檔案

以及最重要、目前唯一需要知道的：

* build 自動執行 compile、test、jar 等泛用的流程

沒錯，我們只要 ``gradle build`` 指令，就能處理一般新建的 Java 專案。對於剛入門的開發者來說，這真是太簡單了，從頭到尾我們只寫了一行 ``apply plugin: 'java'`` 程式碼。

許多人看到這應該一頭霧水，那我們的程式碼要放在哪裡？編譯產生的東西又在哪呢？

Gradle 的 Java Plug-in 事先「假設」開發者依照這個目錄結構保存程式碼：

* src/main/java 存放原始碼（\*.java）
* src/main/resources 在程式碼中會存取的資源（例如圖片或二進位資料）
* src/test/java 使用 JUnit 定義的測試案例

至於自動打包產生的 JAR 檔，則會儲存在 build/libs 路徑。

我們建立的第一個命名為「Main」的 Java Class，其 Package Name 是「com.example」，依照 Java 原始碼路徑的慣例，檔案路徑是：

* src/main/java/com/example/Main.java

其內容為：

```
package com.example;

public class Main {

    public static void main(String args[]) {
        System.out.println(new Main().sayHello());
    }

    public String sayHello() {
        return "Hello";
    }
}
```

建立好 build.gradle 與 Main.java 兩個檔案，就能利用 ``gradle build`` 自動建置 Java 專案（參考以下執行結果）。

```
$ gradle build
:compileJava
:processResources UP-TO-DATE
:classes
:jar
:assemble
:compileTestJava UP-TO-DATE
:processTestResources UP-TO-DATE
:testClasses UP-TO-DATE
:test UP-TO-DATE
:check UP-TO-DATE
:build

BUILD SUCCESSFUL

Total time: 9.762 secs
```

第一次的建置，產生的 JAR 檔路徑是 build/libs/ex001.jar（ex001 是資料夾名稱），可以使用 java 指令立即執行。

```
java -cp build/libs/ex001.jar com.example.Main
```

執行後將會在畫面顯示 Hello 文字。

因為我們並沒有在 build.gradle 幫專案代碼命名，因此 Gradle 會自動使用目前專案的資料夾名稱（範例為 ex001）當做命名，因此產生的 JAR 檔案名稱為 ex001.jar。

目前的 JAR 檔案無法直接被執行，因為我們還沒指定 Main-Class 定義，也就是告訴 JAR 啟動時應該執行哪個類別的 main() 函數。

在 build.gradle 的 apply plugin 後面，增加以下的設定。

```
jar {
    manifest {
        attributes 'Main-Class': 'com.example.Main'
    }
}
```

這是 Gradle 使用的 Groovy 語言定義的 DSL 語法，從程式碼就不難清楚看出，這幾行設定是針對 JAR 的 Manifest 增加屬性（attributes）設定。

再重新執行一次 ``gradle build`` 指令，接下來就能利用 ``java -jar build/libs/ex001.jar`` 的方式執行 JAR 檔案。

```
$ gradle build
$ java -jar build/libs/ex001.jar
Hello
```

最後，如果要將自動建置所產生的檔案清除，可以利用 ``gradle clean`` 指令。

```
gradle clean
```

後續的文章，我們將陸續介紹進階的設定、整合 Eclipse 開發工具的方法，以及在自動化建置中加入 JUnit 單元測試。

本文使用的範例程式碼，可在以下網址取得：

* http://git.io/r1fnnw

參考資料：

1. [Building and Testing with Gradle, O'Reilly](http://shop.oreilly.com/product/0636920019909.do)
2. [Gradle User Guide](http://www.gradle.org/documentation)
