# ZcyiTranslation
一款英汉互译App。

开发环境

开发软件采用Android Studio版本2020.3.1 Patch 2

![image](https://user-images.githubusercontent.com/95206359/212315728-cc5f06ca-9e22-42e9-a394-9f93b2c52478.png)

开发语言采用JAVA，软件相应依赖如下:

![image](https://user-images.githubusercontent.com/95206359/212315803-628b9dc5-88ef-48c4-bda4-42b454424fcd.png)

Room：https://developer.android.google.cn/jetpack/androidx/releases/room?hl=en

Navigation: https://developer.android.google.cn/guide/navigation?hl=en

Retrofit: https://square.github.io/retrofit/

Viewmodel: https://developer.android.google.cn/topic/libraries/architecture/viewmodel

翻译api采用有道: https://ai.youdao.com/    JAVA生成API工具类示例:

![image](https://user-images.githubusercontent.com/95206359/212315912-1fae6595-79a6-4613-88b1-88e6b1a3013c.png)
![image](https://user-images.githubusercontent.com/95206359/212315927-55adee16-1915-4bdc-9a47-a6f2970c737e.png)

设计思路
	软件采用MVVM架构.
用户登录后记录用户信息到SQLite，登录后进入主页.用户输入要翻译的内容后拼接字符串URL生成API通过retrofit发送请求后收到返回Json字符串，分析Json内容并把内容渲染到对应的文本控件和RecyclerView控件上,并把数据存储到对应的ViewModel类中，这样切换页面数据也不会丢失.添加生词功能采用room来存储到本地的SQLite数据库中.

应用截图

登录:	

![image](https://user-images.githubusercontent.com/95206359/212316498-21ae3980-5148-4570-b3e4-00ef4b3d8203.png)

注册:

![image](https://user-images.githubusercontent.com/95206359/212316289-626c6f3f-986f-4aac-a7a9-cf10d2f2b453.png)

翻译:	

![image](https://user-images.githubusercontent.com/95206359/212316748-2690cd1e-fa15-47d7-9cfc-0a04c14b9701.png)


生词表:

![image](https://user-images.githubusercontent.com/95206359/212316735-0477ec53-41d6-46d5-b4ee-ba87f3055325.png)

英译汉效果:

![image](https://user-images.githubusercontent.com/95206359/212316837-7ec37e1b-1375-4414-9d26-bf6f1c86d521.png)

汉译英效果:	

![image](https://user-images.githubusercontent.com/95206359/212316881-be8b1d43-fd47-490c-9f97-985e6ea7e266.png)

生词列表:	

![image](https://user-images.githubusercontent.com/95206359/212316962-f3874c5b-70b2-4a88-b08d-a0ad353a0e2d.png)

生词相应详情:

![image](https://user-images.githubusercontent.com/95206359/212316989-832c3e34-b12b-485b-b43e-d6afc4116923.png)








