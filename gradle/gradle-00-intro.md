認識 Gradle 專案建置自動化工具
===========================

Gradle 簡單說就是 Java 世界的 Makefile，它可以幫忙打理那些在專案開發過程中的瑣事，舉凡編譯、測試、檢查程式碼、產生文件、清理或壓縮檔案、上傳、發佈、重新啟動伺服器到送出電子郵件，都可以利用 Gradle 撰寫的 Script 來自動完成作業。

![Building and Testing with Gradle](http://akamaicovers.oreilly.com/images/0636920019909/cat.gif)

它的應用不僅只用於 Java 的開發領域，它和其他語言的開發環境也可以搭配使用，同樣可以有很多派上用場的機會。

Gradle 的優點很多，主要包括：

1. **簡單易用**：它以 Groovy 語言為基礎，用簡單易懂的 DSL 語法定義任務，開發者不必瞭解 Java 語言就能上手。
2. **套件豐富**：內建的 Plug-in 設計讓它有愈來愈豐富的套件可用，可依照專案的需求尋找合適的套件或自己開發一個。
3. **後台強硬**：由於 Java 長久發展累積豐富的函式庫，可以用 JasperReport 產生專業報表、用 JavaMail 處理電子郵件、執行 Jetty 內嵌網頁伺服器、透過 Rhino 執行 JavaScript 程式、以 JGit 處理專案版本控管任務等。
4. **跨平台**：在裝有 Java 的作業系統，就能執行定義好的 Gradle 程式。像是 Makefile 就必須考慮用的指令需要在其他作業系統也能有相應的程式。

很多 Groovy 或 Java 的開發者，早在 2011-2012 年就開始使用 Gradle 作為專案的建置工具，只是早期（1.0版以前）它有不少 Bug 並且效能不彰；但這一切都在今年有所改變---Gradle 正在以更快的速度成長，愈來愈多開發者使用它。

Google 在 2013 年將 Gradle 正式用於 Android SDK 的 Build System，這是再適合不過的選擇，對於 Java 專案的開發來說，目前並沒有比 Gradle 更好的選擇。

對於 Java 開發者來說，「自動建置」一直存在許多問題：

1. 受歡迎的 Eclipse IDE 「專案」僅內建陽春的編譯和測試、打包流程；過去通常搭配 Ant 來處理進階的自動建置需求。
2. Ant 可以處理相當多任務，但 Ant 本身無法管理 Dependency 問題，需要搭配 Ivy 處理函式庫依賴關係。
3. 時至今日專案更講究套件管理（Package Management）的觀念，Maven 及許多 Open Source Maven Repository 的出現，終於有解決方案；但是使用及定義 Maven 又有不低的學習門檻。

Maven 的出現讓 Java Library 有統一的形式定義 metadata，並讓 Open Source 的 Library 更容易共享。例如專案如果用到 Apache Commons Codec 這個 Library，就只要用一段 XML 宣告依賴關係。

```
<dependency>
	<groupId>commons-codec</groupId>
	<artifactId>commons-codec</artifactId>
	<version>1.8</version>
</dependency>
```

在過去 Java 開發者經常要尋找 *.jar 下載到專案的 lib/ 資料夾下，如果 A.jar 依賴其他 Library 如 B.jar 或 C.jar，還要另外下載並避免相依性不足或衝突的問題。當一個專案變得龐大，這些套件檔案佔用磁碟容量變得相當大，不只造成版本管理系統無謂的負擔，而且需要耗費人力去維護更新及清理用不到的檔案。

在 Groovy 語言加入 Grapes（the GRoovy Adaptable Packaging Engine）的 Grab() 功能後，想使用一個 3rd Party 的 Open Source Java Library 變得非常簡單，只要在程式開頭加入一行宣告。事實上它使用的就是來自 Maven 建立的套件庫（Maven Repositories）。

```
@Grab('commons-codec:commons-codec:1.8')
```

多數常用的 Java Library 可以在 MVNRepository 網站找到可用的版本定義：

http://mvnrepository.com

對於一家具有規模的資訊服務公司，也可以自行定義專屬的 Maven Repository 供內部專案使用，讓可以重複利用的 Java Library 變成套件方式定義，並管理其版本以及和其他套件之間的相依性。

例如 Grails（Groovy 的 MVC Framework）就將其 Plug-ins 建立專屬的 Maven Repository，無論是官方或第三方開發者製作的 Plug-in，都可以放到集中的 Repository 伺服器，讓全世界的 Grails 開發者共享這些 Plug-ins 資源，並只要在設定檔中做簡單的宣告定義即可。

Gradle 建立在這些基礎資源上：

1. 用 Groovy 提供簡單的 DSL 程式語法定義任務，比 XML 提供更強的可程式特性
2. 從 Maven Repository 獲取豐富的套件庫，並徹底簡化定義及使用方式
3. 融合 Ant 既有的任務功能，且使用 Groovy DSL 語法定義

對於已經使用 Ant 定義過的 build.xml 設定，仍可用 ant.importBuild 引入既有的任務定義並直接在 Gradle Script 中使用。

```
ant.importBuild 'build.xml'
```

### 安裝 Gradle ###

Gradle 需要 Java 環境，所以需要先裝好 Java（1.6以上）並正確設定 ``JAVA_HOME`` 環境變數。

推薦的 Gradle 安裝方式，是透過 GVM 的 ``gvm install gradle`` 指令自動下載安裝最新版（請參考先前有關 GVM 安裝的介紹）。

手動安裝則需要下載檔案（例如：gradle-1.8-bin.zip）並解壓縮，然後正確將執行檔路徑增加到 PATH 環境變數設定（如 C:\gradle-1.8\bin 路徑）。

下載 Gradle 的網址 http://www.gradle.org/downloads

### 使用 Gradle ###

Gradle 也能撰寫 Hello World 程式，最精簡的程式如下。

```
task hello {
    println "Hello"
}
```

將檔案儲存為 ``build.groovy``，這個範例包含一個最簡單的 ``hello`` 任務定義。

在 build.groovy 的同一個路徑下，執行 ``gradle -q hello`` 指令，就可以看到執行結果。

```
$ gradle -q hello
Hello
```

與其他建置工具如 Ant 有一個很大的差異，就是 Gradle 並不使用 XML 格式定義任務，而使用 Groovy DSL 語法，所以 Groovy 的程式碼理所當然也能在 Gradle 的任務中使用，例如一個從 1 數到 10 的 Groovy 迴圈程式：

```
task hello {
    (1..10).each {
        println it
    }
}
```

因此我們如果要在資料夾中建立文字檔，使用簡單的 Groovy 程式碼就能搞定。以下範例會產生 1.txt ~ 10.txt 文字檔。

```
task hello {
    (1..10).each {
        new File("${it}.txt") << it
    }
}
```

在 Gradle 任務中調用 Ant 功能也相當容易，以下範例將 *.txt 先壓縮成 archive.zip 檔案，再把 *.txt 檔案移除，用到 ant.zip 與 ant.delete 功能。

```
task hello {
    (1..10).each {
        new File("${it}.txt") << it
    }
    ant.zip(destfile: 'archive.zip', basedir: '.', includes: '*.txt')
    ant.delete(dir: '.', includes: '*.txt')
}
```

若要在任務中執行外部程式，可以使用 Groovy 的方式。

```
task hello {
    'javac Hello.java'.execute()
}
```

當然 Gradle 提供更好的做法，若要執行外部程式可以對 Exec 任務進行擴充，用 DSL 的語法讓新任務看起來更有自動化建置工具的風格，例如定義一個「停用 Tomcat 伺服器」專用的任務（到指定資料夾下執行一個程式）：

```
task stopTomcat(type:Exec) {
    workingDir '../tomcat6/bin'

    commandLine './stop.sh'

    standardOutput = new ByteArrayOutputStream()

    ext.output = {
        return standardOutput.toString()
    }
}
```

是的！您可以將許多需要自動化執行的瑣事，透過 Gradle 定義好任務，然後利用持續整合（CI）或系統自動排程（如 Crontab）自動執行。例如資料庫的維護作業，您甚至可以先用 JDBC 連線到資料庫下完某些查詢後，再去執行 command-line 的工具進行維護作業。

Gradle 賦予任務更明確的定義，使得任務可以被擴充進而重複使用，像是使用 doFirst 或 doLast 讓某個任務的執行前或後先做點其他工作。

本篇先撇開 Java 專案的建置不談，因為 Gradle 可以應用的範圍很廣。但實際上 Gradle 的發展，最初就是為 Java 及 Groovy 專案而生，它對 Java 專案自動化建置流程的處理，肯定會讓第一次使用 Gradle 的開發者有種相見恨晚的感覺。

讓我們明天繼續…



