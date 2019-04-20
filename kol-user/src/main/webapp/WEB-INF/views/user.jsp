<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<head>
    <script src="https://cdn.staticfile.org/jquery/3.2.1/jquery.min.js"></script>
    <script  src="<%=basePath%>/layui1/layui.all.js"></script>
    <link rel="stylesheet" type='text/css' href='<%=basePath%>/layui1/css/layui.css' >
    <link rel="stylesheet" href="<%=basePath%>/css/index.css">
</head>
<body>
<br>
<form id="searchForm" class="layui-form" method="post">
    <%--<div class="layui-form" method="post">--%>
        <div class="layui-inline" >
            <label class="layui-form-label">用户ID</label>
            <div class="layui-input-inline">
                <input type="text" name="id" class="layui-input" id="search_id">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">昵称</label>
            <div class="layui-input-inline">
                <input type="text" name="username" autocomplete="off" class="layui-input" id="search_username">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">性别</label>
            <div class="layui-input-inline" style="width: 100px">
                <select name="sex" id="search_sex">
                    <option value="">全部</option>
                    <option value="1">男</option>
                    <option value="2">女</option>
                </select>
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">用户类型</label>
            <div class="layui-input-inline" style="width: 100px">
                <select name="user_status" id="search_user_type">
                    <option value="">全部</option>
                    <option value="1">正常用户</option>
                    <option value="2">虚拟用户</option>
                    <option value="3">运营用户</option>
                </select>
            </div>
        </div>
        <%--<div class="layui-inline">
            <label class="layui-form-label">状态</label>
            <div class="layui-input-inline" style="width: 100px">
                <select name="state" id="search_state" >
                    <option value="">全部</option>
                    <option value="1">正常</option>
                    <option value="2">禁言</option>
                    <option value="3">冻结</option>
                    <option value="4">封号</option>
                </select>
            </div>
        </div>--%>
        <div class="layui-inline">
            <button class="layui-btn layui-btn-green" id="searchButton">查询</button>
        </div>
    <%--</div>--%>
</form>
<br>
<%--这个是添加框--%>
<div class="insert_button">
    <%--<button onclick = "openDialog()" class="btn btn-success" style="height: 40px;width: 125px">新增</button>--%>
    <button onclick = "openDialog()" class="layui-btn layui-input-inline" style="height: 40px;width: 125px">
        <i class="layui-icon">&#xe654;</i>
        新增
    </button>
    <%--<button class="layui-btn layui-btn-danger layui-input-inline" id="deleteButton" style="float: right;height: 40px;width: 125px">--%>
        <%--<i class="layui-icon">&#xe640;</i>--%>
        <%--删除--%>
    <%--</button>--%>
</div>
<div class="insert_content" id="insert_div">
    <form id="insert_form" class="layui-form" action="/kol_user/User/doInsert" method="post">
        <label class="layui-form-label">用户昵称</label>
        <div class="layui-input-inline">
            <input type="text" name="username" class="layui-input" id="username" lay-verify="username">
        </div>
        <label class="layui-form-label">手机号码</label>
        <div class="layui-input-inline">
            <input type="text" name="telephone" class="layui-input" id="telephone" lay-verify="required|phone">
        </div>
        <div>
            <label class="layui-form-label">性别</label>
            <div class="layui-input-inline" style="width: 100px">
                <select name="sex" id="sex">
                    <option value="1">男</option>
                    <option value="2">女</option>
                </select>
            </div>
        </div>
        <div>
            <label class="layui-form-label">用户类型</label>
            <div class="layui-input-inline" style="width: 100px">
                <select name="user_status" id="user_type">
                    <option value="1">正常用户</option>
                    <option value="2">虚拟用户</option>
                    <option value="3">运营用户</option>
                </select>
            </div>
        </div>
        <%--<div>
            <label class="layui-form-label">状态</label>
            <div class="layui-input-inline" style="width: 100px">
                <select name="state" id="state" >
                    <option value="1">正常</option>
                    <option value="2">禁言</option>
                    <option value="3">冻结</option>
                    <option value="4">封号</option>
                </select>
            </div>
        </div>--%>
        <%--<center>
        <button type="submit" id="insertButton" class="layui-btn-sm">
            提交
        </button>
        <button type="button" id="cancelButton" class="layui-btn-sm">
            取消
        </button>
        </center>--%>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit="/kol_user/User/doInsert" id="insertButton">立即提交</button>
                <%--<button type="reset" class="layui-btn layui-btn-primary">重置</button>--%>
                <button type="button" id="cancelButton" class="layui-btn">取消</button>
            </div>
        </div>
    </form>
</div>

<div id="show1">
    <table class="layui-table" id="user_table" lay-filter="user_table" ></table>
</div>

<%--编辑框--%>
<div class="update_content" id="update_div" style="display: none">
    <form id="update_form" class="layui-form" action="/kol_user/User/update" method="post">
        <input type="text" name="id" id="update_id" hidden="hidden">

        <label class="layui-form-label">用户昵称</label>
        <div class="layui-input-inline">
            <input type="text" name="username" class="layui-input" id="update_username" lay-verify="username">
        </div>
        <br>
        <div>
            <label class="layui-form-label">用户类型</label>
            <div class="layui-input-inline" style="width: 100px">
                <select name="user_status" id="update_user_status">
                    <option value="1">正常用户</option>
                    <option value="2">虚拟用户</option>
                    <option value="3">运营用户</option>
                </select>
            </div>
        </div>
        <%--<div>--%>
            <%--<label class="layui-form-label">状态</label>--%>
            <%--<div class="layui-input-inline" style="width: 100px">--%>
                <%--<select name="state" id="update_state" >--%>
                    <%--<option value="1">正常</option>--%>
                    <%--<option value="2">禁言</option>--%>
                    <%--<option value="3">冻结</option>--%>
                    <%--<option value="4">封号</option>--%>
                <%--</select>--%>
            <%--</div>--%>
        <%--</div>--%>
        <br>
        <%--<center>
        <button type="submit" id="updateButton" class="layui-btn-sm">
            提交
        </button>
        <button type="button" id="cancelButton2" class="layui-btn-sm">
            取消
        </button>
        </center>--%>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit="/kol_user/User/update" id="updateButton">立即提交</button>
                <%--<button type="reset" class="layui-btn layui-btn-primary">重置</button>--%>
                <button type="button" id="cancelButton2" class="layui-btn">取消</button>
            </div>
        </div>
    </form>
</div>
</body>
    <script>
        <%--加载form，显示下拉框--%>
        layui.use("form",function () {
            var form = layui.form;
            form.verify({
                username: function(value, item){ //value：表单的值、item：表单的DOM对象
                    if(!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)){
                        return '用户名不能有特殊字符';
                    }
                    if(/(^\_)|(\__)|(\_+$)/.test(value)){
                        return '用户名首尾不能出现下划线\'_\'';
                    }
                    if(value.length > 10){
                        return '用户名必须1到10位，且不能出现空格';
                    }
                }

                //我们既支持上述函数式的方式，也支持下述数组的形式
                //数组的两个值分别代表：[正则匹配、匹配不符时的提示文字]
                // ,pass: [
                //     /^[\S]{1,10}$/
                //     ,'密码必须1到10位，且不能出现空格'
                // ]
            });
            form.render();
        });
        layui.use('table', function () {  // 引入 table模块
            var table = layui.table;
            //监听表格复选框选择
            table.on('checkbox(user_table)', function(obj){
                console.log(obj)
            });
        })
        layui.use('table', function () {  // 引入 table模块
            var table = layui.table;
            $.ajax({
                type: "GET",
                url: '/kol_user/User/findByProp',
                // data: $("#searchForm").serialize(),
                success: function (r) {
                    var data = r.data;
                    table.render({
                        elem: '#user_table'//指定表格元素
                        , data: data
                        // , cellMinWidth: 20 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
                        , skin: 'line ' //表格风格 line （行边框风格）row （列边框风格）nob （无边框风格）
                        , even: true    //隔行换色
                        , page: true  //开启分页
                        , limits: [5, 10, 15]  //每页条数的选择项，默认：[10,20,30,40,50,60,70,80,90]。
                        , limit: 10
                        , cols: [[ //表头
                            // {type: 'checkbox'}, //开启多选框
                            {field: 'id', title: '用户ID', align: "center"}
                            , {field: 'username', title: '昵称', align: "center"}
                            , {
                                field: 'sex', title: '性别', align: "center", templet: function (d) {
                                    switch (d.sex) {
                                        case 1:
                                            return "男";
                                            break;
                                        case 2:
                                            return "女";
                                            break;
                                        default :
                                            return "";
                                            break;
                                    }
                                }
                            }
                            ,{field: 'identity_type', title: '用户登录类型', align: "center", templet: function (d) {
                                    if (d.identity_type == 1) {
                                        return "手机密码登录";
                                    }
                                    else if (d.identity_type == 2) {
                                        return "手机验证码登录";
                                    } else if (d.identity_type == 3) {
                                        return "微信登录";
                                    }else if (d.identity_type == 4) {
                                        return "QQ登录";
                                    }else {
                                        return "";
                                    }
                                }}
                            // , {field: 'login_time', title: '在线时长', align: "center", sort: true}
                            , {field: 'user_status', title: '用户类型', align: "center", templet: function (d) {
                                    if (d.user_status == 1) {
                                        return "正常用户";
                                    }else if(d.user_status == 2) {
                                        return "虚拟用户";
                                    }else if(d.user_status == 3) {
                                        return "运营用户";
                                    }else {
                                        return "";
                                    }
                                }}
                           /* , {field: 'state', title: '状态', align: "center", templet: function (d) {
                                    if (d.state == 0) {
                                        return "已删除";
                                    }else if (d.state == 1) {
                                        return "正常";
                                    } else if(d.state == 2) {
                                        return "禁言";
                                    }else if(d.state == 3) {
                                        return "冻结";
                                    }else if(d.state == 4) {
                                        return "封号";
                                    }else {
                                        return "";
                                    }
                                }}*/
                            ,{fixed: 'right',title: '操作',width:350, align:'center', toolbar: '#barDemo'} //这里的toolbar值是模板元素的选择器
                        ]]
                        ,id: 'idTest'
                    });

                    <!--查询开始-->
                    $('#searchButton').click(function () {
                        $.ajax({
                            url: '/kol_user/User/findByWords',
                            type: "post",
                            data: $("#searchForm").serialize(),
                            // dataType:'JSON',
                            success: function (r) {
                                layui.use('table', function () {  // 引入 table模块
                                    var table = layui.table;
                                    var data = r.data;
                                    table.render({
                                        elem: '#user_table'//指定表格元素
                                        , data: data
                                        // , cellMinWidth: 20 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
                                        , skin: 'line ' //表格风格 line （行边框风格）row （列边框风格）nob （无边框风格）
                                        , even: true    //隔行换色
                                        , page: true  //开启分页
                                        , limits: [5,10,15]  //每页条数的选择项，默认：[10,20,30,40,50,60,70,80,90]。
                                        , limit: 10
                                        , cols: [[ //表头
                                            // {type: 'checkbox'}, //开启多选框
                                            {field: 'id', title: '用户ID', align: "center"}
                                            , {field: 'username', title: '昵称', align: "center"}
                                            , {
                                                field: 'sex', title: '性别', align: "center", templet: function (d) {
                                                    switch (d.sex) {
                                                        case 1:
                                                            return "男";
                                                            break;
                                                        case 2:
                                                            return "女";
                                                            break;
                                                        default :
                                                            return "";
                                                            break;
                                                    }
                                                }
                                            }
                                            ,{field: 'identity_type', title: '用户登录类型', align: "center", templet: function (d) {
                                                if (d.identity_type == 1) {
                                                    return "手机密码登录";
                                                }
                                                else if (d.identity_type == 2) {
                                                    return "手机验证码登录";
                                                } else if (d.identity_type == 3) {
                                                    return "微信登录";
                                                }else if (d.identity_type == 4) {
                                                    return "QQ登录";
                                                }else {
                                                    return "";
                                                }
                                            }}
                                            // , {field: 'login_time', title: '在线时长', align: "center", sort: true}
                                            , {field: 'user_status', title: '用户类型', align: "center", templet: function (d) {
                                                    if (d.user_status == 1) {
                                                        return "正常用户";
                                                    }else if(d.user_status == 2) {
                                                        return "虚拟用户";
                                                    }else if(d.user_status == 3) {
                                                        return "运营用户";
                                                    }else {
                                                        return "";
                                                    }
                                                }}
                                            /*, {field: 'state', title: '状态', align: "center", templet: function (d) {
                                                    if (d.state == 0) {
                                                        return "已删除";
                                                    }else if (d.state == 1) {
                                                        return "正常";
                                                    } else if(d.state == 2) {
                                                        return "禁言";
                                                    }else if(d.state == 3) {
                                                        return "冻结";
                                                    }else if(d.state == 4) {
                                                        return "封号";
                                                    }else {
                                                        return "";
                                                    }
                                                }}*/
                                            ,{fixed: 'right',title: '操作',width:90, align:'center', toolbar: '#barDemo'} //这里的toolbar值是模板元素的选择器
                                        ]]
                                    });
                                })
                            },
                            error: function (xhr, textStatus) {
                                layer.open({
                                    type: 1,
                                    content: '错误' + textStatus
                                });
                            }
                        });
                    <!--注意，此处加上return false，是为了阻止表单的自动提交，不阻止不会触发ajax事件-->
                    return false;
                    });


                    //监听工具条(操作功能开始)
                    table.on('tool(user_table)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
                        var data = obj.data; //获得当前行数据
                        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
                        var tr = obj.tr; //获得当前行 tr 的DOM对象
                        var id = data.id;

                        <!--操作状态-->
                            if(layEvent === 'shutup'){ //禁言
                            alert('ID:'+data.id+'的禁言操作');
                            window.location.href="/kol_user/User/shutup?id="+data.id;
                        } else if(layEvent === 'freeze'){ //冻结
                                alert('ID:'+data.id+'的冻结操作');
                                window.location.href="/kol_user/User/freeze?id="+data.id;
                        } else if(layEvent === 'ban'){ //封号
                            alert('ID:'+data.id+'的封号操作');
                                window.location.href="/kol_user/User/ban?id="+data.id;
                        }else if (layEvent === 'relieve'){//解除（正常）
                            alert('ID:'+data.id+'的解除操作');
                                window.location.href="/kol_user/User/relieve?id="+data.id;
                        } else if (layEvent === 'editing'){//编辑
                            $("#update_div").show();
                            alert('ID:'+data.id+'的编辑操作');
                            $("#update_id").val(data.id);
                            $("#update_username").val(data.username);
                            $("#update_telephone").val(data.telephone);
                            $("#update_password").val(data.password);
                            $("#update_sex").val(data.sex);
                            $("#update_user_status").val(data.user_status);
                            $("#update_state").val(data.state);
                                layui.form.render();
                        }
                    });

                    <!--全选删除框开始-->
                    var checkStatus;
                    $('#deleteButton').on('click', function() {
                        checkStatus = table.checkStatus('idTest'); //idTest 即为基础参数 id 对应的值
                        // console.log(checkStatus.data) //获取选中行的数据
                        // console.log(checkStatus.data.length) //获取选中行数量，可作为是否有选中行的条件
                        var data = checkStatus.data;
                        if(data.length>0){
                            for(var i=0;i<data.length;i++){
                                //创建id数组
                                ids+=data[i].id+","
                            }
                            <!--此处有个问题，从0开始会出现一个undefined，从9开始是为了去除undefined-->
                            var ids=ids.substring(9,ids.length-1);//字符串中的最后一位逗号去掉
                        }
                        // var jsonIds= JSON.stringify(ids);
                        layer.confirm('确定删除id为:'+ids+"这"+data.length+"条数据吗？", {
                            btn: ['确定', '取消'] //可以无限个按钮
                        }, function(index, layero){
                            layer.msg('删除成功', {icon: 1});
                            window.location.href="/kol_user/User/delete?id="+ids;
                        }, function(index){
                        });
                    })
                }
            });
        });

        $('#cancelButton').click(function () {
            $("#insert_div").hide();
            return false;
        })
        $('#cancelButton2').click(function () {
            $("#update_div").hide();
            return false;
        })
        // insert_div
    </script>




    <script type="text/html" id="barDemo">

        <%--自定义按钮模板，支持 laytpl 语法--%>
        {{#  if(d.state == 1){ }}
        <%--正常--%>
        <%--<a class="layui-btn layui-btn-sm " lay-event="shutup">禁言</a>
        <a class="layui-btn layui-btn-sm" lay-event="freeze">冻结</a>
        <a class="layui-btn layui-btn-sm" lay-event="ban">封号</a>--%>
        <a class="layui-btn layui-btn-sm" lay-event="editing"><i class="layui-icon">&#xe642;</i>编辑</a>
        {{#  } else if(d.state == 2){ }}
        <%--禁言--%>
        <%--<a class="layui-btn layui-btn-sm " lay-event="relieve" style="background-color: #00CCFF">取消禁言</a>
        <a class="layui-btn layui-btn-sm " lay-event="freeze">冻结</a>
        <a class="layui-btn layui-btn-sm " lay-event="ban">封号</a>--%>
        <a class="layui-btn layui-btn-sm" lay-event="editing"><i class="layui-icon">&#xe642;</i>编辑</a>
        {{#  } else if(d.state == 3){ }}
        <%--冻结--%>
       <%-- <a class="layui-btn layui-btn-sm" lay-event="shutup">禁言</a>
        <a class="layui-btn layui-btn-sm " lay-event="relieve" style="background-color: #33CCFF">取消冻结</a>
        <a class="layui-btn layui-btn-sm" lay-event="ban">封号</a>--%>
        <a class="layui-btn layui-btn-sm" lay-event="editing"><i class="layui-icon">&#xe642;</i>编辑</a>
        {{#  } else if(d.state == 4){ }}
        <%--封号--%>
   <%--     <a class="layui-btn layui-btn-sm" lay-event="shutup">禁言</a>
        <a class="layui-btn layui-btn-sm" lay-event="freeze">冻结</a>
        <a class="layui-btn layui-btn-sm " lay-event="relieve" style="background-color: #33CCFF">取消封号</a>--%>
        <a class="layui-btn layui-btn-sm" lay-event="editing"><i class="layui-icon">&#xe642;</i>编辑</a>
        {{#  } }}
        <%--注意：属性 lay-event="" 是模板的关键所在，值可随意定义。--%>
    </script>

    <script>
        //开启悬浮框
        function openDialog(){
            document.getElementById('insert_div').style.display='block';
        }
        //关闭悬浮框
        function closeDialog(){
            document.getElementById('insert_div').style.display='none';
        }
    </script>
    <style type="text/css">
        .main
        {
            font-size:120%;
            color:red;
            background: #0b94ea;
        }
        .layui-table-page{text-align: center;}

        .insert_content{
            width: 400px;
            height: 500px;
        }
        .update_content{
            width: 400px;
            height: 500px;
        }

        /*设置layui table行高*/
        .layui-table-cell{
            height:36px;
            line-height: 40px;
        }

        /**设置操作按钮大小*/
        a.layui-btn.layui-btn-sm{
            width: 70px;
            height: 30px;
            /*text-align:center;*/
        }

    </style>
</html>