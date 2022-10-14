# 古籍阅读

#### 介绍
古籍阅读管理系统

#### 软件架构
软件架构说明


#### 安装教程

1.  xxxx
2.  xxxx
3.  xxxx

#### 使用说明

1.  xxxx
2.  xxxx
3.  xxxx

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request

> 💡 建议将项目 fork 到个人 GitHub 目录下进行开发，通过发起 pull request 提交到源项目进行合并。
> 教程：`https://aaronflower.github.io/essays/github-fork-pull-workflow.html`


#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)


#### 后端结构
- Controller ：Spring mvc 的C的缩写 ，负责和前端交互，这里写上对应的接口和调用service
- Service： 义务逻辑层 ，负责调用dao层 ，处理Controller传来的值做主要的义务逻辑判断
- Dao：：负责调用数据库的数据并做封装后返回给service，也可以是将service传入的数据封装后写入数据库
- Pojo：做对象映射的，Dao或其他地方的数据封装就是用Pojo里面的类来完成的
- Utils:工具类放的地方,里面的方法必须是static 的，这样其他地方用这些方法的时候就不用实例化可以直接调用了
- security:做权限管理的（登录），spring security的东西大多放在里面的
- resource：这个地方放的是静态资源（图片，js之类的，静态页面），还有spring boot的配置文件，如邮箱功能的一些配置就在里面
- pom.xml：这个是maven的配置文件，里面放了一些工程需要的依赖（需要用的东西，比如要引入什么框架的时候就需要在里面加入一些依赖文件），并自动处理依赖相关
- config：放配置文件的地方（java文件也可以作为配置文件，Spring ioc的原理）
- mapper：mybatis和mybatis plus的内容
- test: 做单元测试的文件夹

