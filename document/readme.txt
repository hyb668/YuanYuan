在主module中用
@BindView(R.id.xxxx)
View view
的形式注解

在library（module）中
@BindView(R2.id.xxxx)
View view
的形式注解

XRecyclerView
compile 'com.jcodecraeer:xrecyclerview:1.3.2'

查看项目缺少的依赖库
gradlew MainFun:dependencies

列出所有依赖包
gradlew -q MainFun:dependencies

破解签名文件
http://blog.csdn.net/u013278099/article/details/52231170