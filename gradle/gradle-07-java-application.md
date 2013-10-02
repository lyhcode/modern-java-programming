使用 Gradle 自動化建置 Java 專案（七）
=================================

Gradle 是用途廣泛的建置工具，但最重要的一點，就是非常適合處理 Java 專案，它讓 Java 專案自動化建置（Build Automation）變得更容易上手。

前面介紹的 Java Plugin 可以幫助專案編譯、測試 Java 程式，但是並不包含「執行」的部份。Gradle 將 Java 程式的「執行」與「封裝」，透過另一個 Application Plugin 來提供。

![Building and Testing with Gradle](http://akamaicovers.oreilly.com/images/0636920019909/cat.gif)

### Application Plugin ###

在 build.gradle 加入 Application Plugin 的設定，除了 apply plugin 之外還要設定 ``mainClassName`` 的完整類別名稱。

build.gradle

```
apply plugin: 'java'
apply plugin: 'application'

javadoc.options.encoding = 'UTF-8'
compileJava.options.encoding = 'UTF-8'

mainClassName = 'com.example.Main'

repositories {
    mavenCentral()
}

dependencies {
    compile 'commons-codec:commons-codec:1.8'
}

```

在之前的範例中，我們自行定義一個 JavaExec 類型的 run 任務，用來執行 Java 主程式。

```
task run(type: JavaExec) {
    main = 'com.example.Main'
    classpath = configurations.compile + sourceSets.main.output
}
```

但是在加入 Application Plugin 之後，它已經定義好 ``run`` 任務，並且會依照專案的 dependencies 自動設定正確的 CLASSPATH 內容；因此我們不需要再自行定義 run 這個任務。

只需要定義 mainClassName 的內容，就能讓 Java 主程式可以被執行。

```
gradle run
```

封裝也是 Application Plugin 的主要功能之一，分別有 distZip 與 distTar 兩種指令。

```
gradle distZip
```

使用 distZip 指令會產生 build/distributions/ex007.zip 檔案。

```
gradle distTar
```

而 distTar 則會產生 build/distributions/ex007.tar 檔案。

將這個 ex007.zip 解壓縮後，可以得知它包含以下檔案：

```
ex007/bin/ex007
ex007/bin/ex007.bat
ex007/lib/commons-codec-1.8.jar
ex007/lib/ex007.jar
```

Application Plugin 幫我們產生 ex007 與 ex007.bat 兩個可執行檔案，其中沒有副檔名的 ex007 是 Bash Shell 指令檔，可用於 Linux 與 Mac OS X 系統的執行；而 ex007.bat 則是給 Windows 系統使用的批次檔。

在壓縮檔內含的 lib 資料夾中，也會將專案在 dependencies 設定的相依套件加入，例如這個範例使用的 commons-codec-1.8.jar 函式庫 JAR 檔。

只要使用這個壓縮檔，就能方便在其他已裝有 JRE 環境的系統上執行，能夠解決在不同地方執行可能缺少 JAR 檔案的問題。

本文使用的範例程式碼，可在以下網址取得：

* http://git.io/MtHC9g

參考資料：

1. [Building and Testing with Gradle, O'Reilly](http://shop.oreilly.com/product/0636920019909.do)
2. [Gradle User Guide](http://www.gradle.org/documentation)
