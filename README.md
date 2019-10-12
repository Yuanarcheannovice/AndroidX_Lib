# AndroidX_Lib
适配androidx的lib
安卓端 工具 仓库
引入方式
```java
allprojects {
    repositories {
        google()
        jcenter()
        //工具类
        maven { url 'https://raw.githubusercontent.com/Yuanarcheannovice/AndroidX_Lib/master/maven' }
    }
 }
 
 dependencies {
    implementation 'com.archeanx.androidx:@libName:@version'
  }
```
lib 和 版本号 传送门:
https://github.com/Yuanarcheannovice/AndroidX_Lib/blob/master/lib_version.xml