GVM - Groovy 的版本管理工具
===========================

開發工具的安裝與版本切換，也需要一個好用的工具來控管。就像 RVM（Ruby Version Manager）及 NVM（Node Version Manager）等，Groovy 及 Grails 也有相同概念的管理工具 GVM（Groovy enVironment Manager），簡化安裝、升級與版本切換的操作流程。

GVM 不僅可用於 Groovy 的安裝，也支援以下的軟體（可選擇的軟體項目稱為 Candidate）：

* Groovy
* Grails
* Griffon
* Gradle
* Groovyserv
* Lazybones
* vert.x

目前 GVM 在 Linux 及 Mac OS X 可以良好運作，若是用於 Windows 開發環境，則必須另外搭配 Cygwin 使用（為了模擬 Bash Shell 環境）。

### 必須先安裝 JDK ###

很不幸的是，Java 的 JDK 並沒有如此便利的安裝工具，但是 GVM 及 Groovy 等又是建立在 Java VM 的基礎上，所以事先安裝 JDK 並且正確設定 JAVA_HOME 環境變數，是使用 GVM 之前就必須先做好；幸好在 Linux 及 Mac OS X 很容易就能把 JDK 搞定。

### GVM 安裝 ###

只要一行 command line 就能搞定 GVM 的安裝。

```
curl -s get.gvmtool.net | bash
```

請注意並不需要使用 ``sudo`` 來切換系統權限，因為 GVM 允許每位登入者具備自己的獨立環境（其相關檔案保存在 ~/.gvm 路徑底下）。

### 開始使用 GVM ###

使用 ``gvm list groovy`` 可以查詢 Groovy 工具可供安裝的版本。在版本代號前顯示 `` > * `` 符號表示已安裝且預設使用的版本。

```
============================================================
Available Groovy Versions
============================================================
     2.2.0-beta-2
     2.2.0-beta-1
     2.1.7
 > * 2.1.6
     2.1.5
```

若要安裝 Groovy 最新版本（目前是 2.1.7），只需要利用 ``install`` 指令並指定 Candidate 與版本代碼。

```
gvm install groovy 2.1.7
```

將目前 Bash Shell 使用的 Groovy 切換為指定版本，使用 ``use`` 指令。

```
groovy use groovy 2.1.7
```

若之後要固定使用某個版本，只要利用 ``default`` 指令。

```
groovy default groovy 2.1.7
```

Groovy 及 Grails 工具的版本更新頻繁，採用 GVM 工具簡化安裝與升級，將安裝流程變成只要一行指令，就能取代本來要耗費人工的事：

1. 下載並解壓縮
2. 設定環境變數（如 PATH 等）

更重要的是它能讓多個版本共存，並且隨時輕鬆切換。開發者自己也能利用 ``.bashrc`` 搭配 ``link`` 做到類似效果，但使用 GVM 連處理這些設定的時間都能省下。

如果不想使用 GVM，Linux 與 Mac OS X 也有其他工具可以管理安裝。

Ubuntu Linux

* APT 的 ``apt-get``

Mac OS X

* MacPorts 的 ``port``
* Homebrew 的 ``brew``

但是在近期的使用經驗中，這些工具對 Groovy 相關 Java 工具軟體的更新速度，通常無法跟隨官方更新的腳步，所以來自 Groovy 開發社群的 GVM 仍是比較好的選擇。

在本次主題的相關文章中，如有用到 Groovy、Gradle、Grails 等工具，都可以使用 GVM 將需要的環境快速建立。

* gvm install groovy
* gvm install gradle
* gvm install grails







