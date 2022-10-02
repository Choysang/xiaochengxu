所有的activity位于：src\main\java\com\example\androiddingjing\activities
所有的fragment位于：activity文件夹下的ui文件夹中

已实现的功能：
注册登录界面————LoginAndRegisterActivity
     注册界面（发送邮箱验证码，填写相关信息，存储合法的注册信息到数据库）————SignUpFragment
     登录界面（检查输入的账号密码是否在数据库中，判断是否登录成功。登录后将用户的个人信息保存到本地，读取方法看下面。）————LoginFragment
主界面————MainActivity
     我的界面————MineFragment
            头像、昵称展示
            查看信息——QueryInfoActivity
                    展示账号、昵称、绑定邮箱
            修改信息————ChangeInfoActivity
                    修改昵称、密码、邮箱（需验证码）
            退出登录————点击就回到LoginAndRegisterActivity
     专注圈界面————GroupFragment
     测试界面————TestFragment
            本地测试————入口UI
            在线测试————入口UI
            历史测试————入口UI

关于用户数据的获取：
用户成功登录后会将他的：昵称、账号、密码、邮箱，存储到本地。读取方式如下：
SharedPreferences read = this.getSharedPreferences("AccountInfo", Context.MODE_PRIVATE);//获取保存用户信息的文件
read.getString("account", null);//读取想要的信息，分别为：name account password email

/**
新闻模块：
1.侧边栏看不同新闻的设计暂时取消，点展开侧边栏就会报空指针异常，暂时没找到解决方法（原本不会，合并进来之后才会..）
2.新增了 GroupActivity gson文件夹 util文件夹 Title TitleAdapter文件 若干layout，menu..
*/



### 8.30
完成从本地数据库读历史（但表结构仍未定
完成调用摄像头并preview （但是行为暂未定

/*
增添了视频调取摄像头（见camera相关文件夹及activty_face.xml
与上面相对应 需要用时换接口即可
*/



