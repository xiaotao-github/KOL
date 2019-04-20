<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <!-- 阿里云图标字体引入 -->
    <!-- <link rel="stylesheet" href="http://at.alicdn.com/t/font_960386_l2a47mmbrr.css"> -->
    <link rel="stylesheet" href="<%=basePath%>/index/css/iconfont/iconfont.css">
    <!-- 重置样式 -->
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/index/css/reset.css">
    <!-- 页面布局样式 -->
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/index/css/layout.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/index/css/custom.css">
    <title>后台布局页面</title>
    <%--<script src="https://cdn.staticfile.org/jquery/3.2.1/jquery.min.js"></script>--%>
    <%--<script  src="<%=basePath%>/layui/layui.js"></script>--%>
    <%--<link rel="stylesheet" type='text/css' href='<%=basePath%>/layui/css/layui.css' >--%>
    <%--<link rel="stylesheet" href="<%=basePath%>/css/index.css">--%>
</head>
    <body>
    <div class="layout">
        <!-- 头部 -->
        <div class="layout-header">
            <i class="logo"></i>
            <!-- 头部右侧下啦 -->
            <%--<div class="header-side">--%>
                <%--<div class="header-dropdown">--%>
                    <%--<i class="icon icon-fenlei"></i>--%>
                    <%--<span>数据报表分类</span>--%>
                    <%--<i class="iconfont icon-right"></i>--%>
                    <%--<div class="sub-list header-down">--%>
                        <%--<div class="inner">--%>
                            <%--<a href="#">aaaaaa</a>--%>
                            <%--<a href="#">bbbbb</a>--%>
                            <%--<a href="#">cccc</a>--%>
                            <%--<a href="#">ddddd</a>--%>
                            <%--<a href="#">eeee</a>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <%--<a href="#" class="icon icon-question"></a>--%>
                <%--<div class="header-dropdown">--%>
                    <%--<i class="icon icon-user"></i>--%>
                    <%--<div class="header-down user-down">--%>
                        <%--<div class="inner">--%>
                            <%--<div class="user-name"><i class="icon icon-user-grey"></i>动感用户名</div>--%>
                            <%--<div class="controls">--%>
                                <%--<button class="btn-primary-jianta">账号管理</button>--%>
                                <%--<button class="btn-primary-jianta">注销</button>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                <%--</div>--%>
            <%--</div>--%>
            <!-- 用户中心 -->
            <div class="userinfo-pop">
                <div class="info-box">
                    <i class="iconfont icon-close"></i>
                    <div class="info-title">账户管理</div>
                    <div class="info-content">
                        <div class="small-box" id="info-edit-box">
                            <div class="small-title">
                                账户信息
                                <div class="control-btn"><span class="to-edit"><i class="iconfont icon-editor"></i>编辑</span><span class="confirm-edit">完成</span></div>
                            </div>
                            <div class="input-box">
                                <span>账号</span><b class="value">jianta@qq.com</b>
                            </div>
                            <div class="input-box">
                                <span>姓名</span><input type="text" name="username" class="edit-input" value="测试员">
                            </div>
                            <div class="input-box">
                                <span>手机</span><input type="text" name="phone" class="edit-input" value="1382646654d">
                            </div>
                            <div class="input-box">
                                <span>QQ</span><input type="text" name="qq" class="edit-input" value="999888777">
                            </div>
                        </div>
                        <div class="small-box">
                            <div class="small-title">登录信息</div>
                            <div class="other-info-box">
                                <div>
                                    <div class="info-label">注册信息：</div>
                                    <div class="info-details">2017-07-19</div>
                                </div>
                                <div>
                                    <div class="info-label">近期登录：</div>
                                    <ul class="info-details">
                                        <li>2018-11-06 15:44:53 IP:122.70.150.108 北京</li>
                                        <li>2018-11-06 15:44:53 IP:122.70.150.108 北京</li>
                                        <li>2018-11-06 15:44:53 IP:122.70.150.108 北京</li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="layout-body">
            <!-- 侧边导航 -->
            <div class="layout-sider open">
                <div class="menu-box ">
                    <ul class="menu">
                        <!-- <li class="has-sub">
                          <i class="iconfont icon-diannao"></i>
                          <div>行为分析<i class="iconfont icon-right"></i></div>
                          <ul class="sub-menu">
                            <li class="has-sub">
                              <div>事件分析<i class="iconfont icon-right"></i></div>
                              <ul class="sub-menu">
                                <li>
                                  <div class="">事件分析</div>
                                </li>
                                <li>
                                  <div>事件分析</div>
                                </li>
                              </ul>
                            </li>
                            <li>
                              <div>漏斗转化</div>
                            </li>
                          </ul>
                        </li>
                        <li class="has-sub">
                          <i class="iconfont icon-pingguo"></i>
                          <div>类B<i class="iconfont icon-right"></i></div>
                          <ul class="sub-menu">
                            <li class="has-sub">
                              <div>类B -> 子类<i class="iconfont icon-right"></i></div>
                              <ul class="sub-menu">
                                <li>
                                  <div>事件分析</div>
                                </li>
                                <li>
                                  <div>事件分析</div>
                                </li>
                              </ul>
                            </li>

                          </ul>
                        </li> -->
                    </ul>
                </div>
                <div class="menu-control">
                    <i class="iconfont icon-zhankai"></i> 收起菜单
                </div>
            </div>
            <!-- 内容主体部分 -->
            <div class="layout-content">
                <!-- 内容导航栏 -->
                <div class="layout-content-nav">
                    <div class="iconfont icon-xitongguanli content-nav-icon"></div>
                    <!-- <div class="nav-item"><span class="iconfont icon-shuaxin"></span><p>你来自哪个国家？你是谁吗？</p><b class="rt-side"><i class="iconfont icon-close"></i></b></div> -->
                </div>
                <!-- 内容主体 -->
                <div class="layout-content-body">
                    <!-- <div class="iframe-item">
                      <iframe src="http://www.baidu.com" frameborder="0" id="iframe"></iframe>
                    </div> -->
                </div>
                <!-- 底部 -->
                <div class="layout-footer">

                </div>
            </div>
        </div>
    </div>
    <script type="text/javascript" src="<%=basePath%>/index/js/jquery-1.11.3.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/index/js/layout.js"></script>
    <script type="text/javascript" src="<%=basePath%>/index/js/index.js"></script>
    <script type="text/javascript">

        // 目录结构配置，最多三级目录
        var config = {
            // 一级目录的需要配置icon，如“icon-name”,表示前面的图标, 图标字体的目录，在"css/iconfont"中，具体有哪些，可以打开 demo_index.html页面
            menu: [
                {
                    icon: 'icon-shuju',  // 图标(根据页面导入的图标字体，如果图标字体没有，则图标就不会显示出来了)
                    name: '用户模块管理',
                    sub: [
                        {
                            name: '用户管理',
                            path: 'http://129.204.52.213:8085/kol_user/User/showPage'
                            // path: 'http://192.168.12.93/User/showPage'
                        }
                    ]
                },
                {
                    icon: 'icon-shuju',
                    name: '平台币模块管理',
                    sub: [
                        {
                            name: '礼物管理',
                            path: 'http://129.204.52.213:8083/kol_coin/Gift/showPage'
                        }
                    ]
                },
                {
                    icon: 'icon-shuju',
                    name: '交友模块管理',
                    sub: [
                        {
                            name: '消息推送管理',
                            path: 'http://129.204.52.213:8084/kol_friends/Message/showPage'
                        }
                    ]
                },
                {
                    icon: 'icon-shuju',
                    name: '房间模块管理',
                    sub: [
                        {
                            name: '房间管理',
                            path: 'http://129.204.52.213:8086/kol_room/Room/showPage'
                        }
                    ]
                },
                {
                    icon: 'icon-shuju',
                    name: '首页轮播图管理',
                    sub: [
                        {
                            name: '轮播图管理',
                            path: 'http://129.204.52.213:8082/kol_home/Home/showPage'
                        }
                    ]
                },
                {
                    icon: 'icon-shuju',  // 图标(根据页面导入的图标字体，如果图标字体没有，则图标就不会显示出来了)
                    name: '充值模块管理',
                    sub: [
                        {
                            name: '充值订单管理',
                            path: 'http://129.204.52.213:8081/kol_recharge_cash/Recharge/showPage'
                        },
                        //新增子模块
                        {
                            name: 'X币兑换比例管理',
                            path: 'http://129.204.52.213:8081/kol_recharge_cash/CoinProportion/showPage'
                        },
                        {
                            name: '礼物收支记录管理',
                            path: 'http://129.204.52.213:8081/kol_recharge_cash/GiftRecord/showPage'
                        }
                    ]
                }
            ],

            // ,
            // defaultPage: {
            //   name: '默认初始页面',
            //   path: 'http://www.aliyun.com'
            // }
            mode:'single'
        };

        // 执行初始化
        layout.init(config);

    </script>
    </body>
</html>