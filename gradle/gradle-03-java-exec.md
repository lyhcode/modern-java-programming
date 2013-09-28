使用 Gradle 自動化建置 Java 專案（三）
==================================

Gradle 是用途廣泛的建置工具，但最重要的一點，就是非常適合處理 Java 專案，它讓 Java 專案自動化建置（Build Automation）變得更容易上手。

前兩篇文章中，使用相當精簡的 Gradle 設定檔，就能用來處理一般新建的 Java 專案。但是在專案變得更複雜，或者需要承接舊專案時，預設的設定直可能不合用，這時候就必須對 Gradle 的 Java Plugin 有更進一步認識。

* [The Java Plugin](http://www.gradle.org/docs/current/userguide/java_plugin.html)

![Building and Testing with Gradle](http://akamaicovers.oreilly.com/images/0636920019909/cat.gif)

### 自訂 JavaExec 任務 ###

在上篇文章中，我們修改設定讓 Gradle 的 jar 設定以產生 FatJar 檔案，解決預設的 CLASSPATH 不包含所需 Commons Codec 相依套件的問題。

另一種更直接的方式，就是在 Gradle 建置流程的最後一個步驟，執行 run 任務。很不幸地事情發生了，Java Plugin 並不提供 run 的任務定義，因為「如何執行 Java 程式」存在千變萬化的各種選項，所以我們必須自行定義。幸好 Java Plugin 已經提供「JavaExec」這個任務類型，所以我們真正需要做的事情並不多。

以下是 task run 的定義範例，僅簡單地做了兩件事：(1)指定執行哪個類別的 main 主程式；(2)設定 CLASSPATH 讓程式找得到所需的類別。

build.gradle

```
apply plugin: 'java'

repositories {
    mavenCentral()
}

dependencies {
    compile 'commons-codec:commons-codec:1.8'
}

task run(type: JavaExec) {
    main = 'com.example.Main'
    classpath = configurations.compile + sourceSets.main.output
}
```

既然 Gradle 會自動幫我們取得定義在 dependencies 的相關 JAR 檔案，但實際上這些檔案都放到哪去了呢？預設是保存在 ``~/.gradle/caches/`` 資料夾下。

預設情況下，Gradle 並不會機婆地把相關 JAR 檔案複製一份到專案資料夾下，因為在不同專案之間，這些 JAR 可能被重複共用，所以只保存一份通常是比較好的選擇。

以上範例的 ``configurations.compile`` 包含 runtime 與 compile 兩個 dependencies 設定所需的 JAR 路徑集合，而 ``sourceSets.main.output`` 則是原始碼編譯後輸出的位置（預設是 build/classes 資料夾）。

定義好 ``task run`` 之後，就能使用 ``gradle -q run`` 執行程式（參數 -q 可隱藏 Gradle 建置過程的除錯訊息）。

```
$ gradle -q run
Hello
SSBMb3ZlIEdyYWRsZQ==
```

到目前為止，Gradle 的自動化建置流程，已經可以完成編譯並自動執行；開發者撰寫完一段程式，如果想看到執行結果，只需要重複執行 ``gradle -q run`` 即可，搭配一個終端機（console）畫面就能搞定，不必再依賴開發工具的功能。

本文使用的範例程式碼，可在以下網址取得：

* http://git.io/XtFD2g

參考資料：

1. [Building and Testing with Gradle, O'Reilly](http://shop.oreilly.com/product/0636920019909.do)
2. [Gradle User Guide](http://www.gradle.org/documentation)
