<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<head>
    <script src="https://cdn.staticfile.org/jquery/3.2.1/jquery.min.js"></script>
    <script  src="<%=basePath%>/layui/layui.js"></script>
    <link rel="stylesheet" type='text/css' href='<%=basePath%>/layui/css/layui.css' >
    <link rel="stylesheet" href="<%=basePath%>/css/index.css">
</head>
<body>
<br>
<form id="searchForm" class="layui-form" method="post">
    <%--<div class="layui-form" method="post">--%>
        <div class="layui-inline" >
            <label class="layui-form-label">游戏ID</label>
            <div class="layui-input-inline">
                <input type="text" name="id" class="layui-input" id="search_id">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">游戏名称</label>
            <div class="layui-input-inline">
                <input type="text" name="game_name" autocomplete="off" class="layui-input" id="search_game_name">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">游戏CP</label>
            <div class="layui-input-inline">
                <input type="text" name="game_cp" autocomplete="off" class="layui-input" id="search_game_cp">
            </div>
        </div>

        <div class="layui-inline">
            <label class="layui-form-label">状态</label>
            <div class="layui-input-inline" style="width: 100px">
                <select name="state" id="search_state" >
                    <option value="">全部</option>
                    <option value="1">上线</option>
                    <option value="2">下线</option>
                </select>
            </div>
        </div>
        <div class="layui-inline">
            <button class="layui-btn layui-btn-primary" id="searchButton">查询</button>
        </div>
    <%--</div>--%>
</form>
<br>
<%--这个是添加框--%>
<div class="insert_button">
    <%--<button onclick = "openDialog()" class="btn btn-success" style="height: 40px;width: 125px">新增</button>--%>
    <button onclick = "openDialog()" class="layui-btn layui-input-inline" style="height: 40px;width: 125px">新增</button>
    <button class="layui-btn layui-input-inline" id="deleteButton" style="float: right;height: 40px;width: 125px">删除</button>
</div>
<div class="insert_content" id="insert_div">
    <form id="insert_form" class="layui-form" enctype="multipart/form-data">
        <div>
            <label class="layui-form-label">游戏ID</label>
            <div class="layui-input-inline">
                <input type="text" name="id" class="layui-input" id="id">
            </div>
        </div>
        <label class="layui-form-label">游戏名称</label>
        <div class="layui-input-inline">
            <input type="text" name="game_name" class="layui-input" id="game_name">
        </div>
        <br>
        <label class="layui-form-label">游戏CP</label>
        <div class="layui-input-inline">
            <input type="text" name="game_cp" class="layui-input" id="game_cp">
        </div>
        <br>
        <label class="layui-form-label">版本号</label>
        <div class="layui-input-inline">
            <input type="text" name="version_number" class="layui-input" id="version_number">
        </div>
        <br>
        <div>
            <label class="layui-form-label">游戏评分</label>
            <div class="layui-input-inline">
                <input type="text" name="score" class="layui-input" id="score">
            </div>
        </div>
        <div>
            <label class="layui-form-label">游戏简介</label>
            <div class="layui-input-inline">
                <input type="text" name="game_introduction" class="layui-input" id="game_introduction">
            </div>
        </div>
        <div>
            <label class="layui-form-label">位置</label>
            <div class="layui-input-inline">
                <input type="text" name="position" class="layui-input" id="position">
            </div>
        </div>
        <%--标题图片、内容图片、应用安装包--%>
        <div class="layui-upload">
            <button type="button" class="layui-btn" id="test1">上传图片</button>
            <div class="layui-upload-list">
                <img class="layui-upload-img" id="img_demo1">
                <p id="demoText"></p>
            </div>
        </div>
    <%--<div class="layui-input-inline">--%>
            <%--<button type="button" class="layui-btn" name="game_image" id="test1">--%>
                <%--<i class="layui-icon">&#xe67c;</i>标题图片--%>
            <%--</button>--%>
        <%--</div>--%>
        <input type="button" id="insertButton" value="提交">
        <input type="button" id="cancelButton" value="取消">
    </form>
    <%--<form id="insert_form" class="layui-form">--%>
        <%--<div>--%>
            <%--<label class="layui-form-label">用户ID</label>--%>
            <%--<div class="layui-input-inline">--%>
                <%--<input type="text" name="id" class="layui-input" id="id">--%>
            <%--</div>--%>
        <%--</div>--%>
        <%--<label class="layui-form-label">用户昵称</label>--%>
        <%--<div class="layui-input-inline">--%>
            <%--<input type="text" name="username" class="layui-input" id="username">--%>
        <%--</div>--%>

        <%--<div>--%>
            <%--<label class="layui-form-label">性别</label>--%>
            <%--<div class="layui-input-inline" style="width: 100px">--%>
                <%--<select name="sex" id="sex">--%>
                    <%--<option value="">全部</option>--%>
                    <%--<option value="1">男</option>--%>
                    <%--<option value="2">女</option>--%>
                <%--</select>--%>
            <%--</div>--%>
        <%--</div>--%>

        <%--<br>--%>
        <%--<div>--%>
            <%--<label class="layui-form-label">用户属性</label>--%>
            <%--<div class="layui-input-inline" style="width: 100px">--%>
                <%--<select name="user_type" id="user_type">--%>
                    <%--<option value="">全部</option>--%>
                    <%--<option value="1">kol</option>--%>
                    <%--<option value="2">普通用户</option>--%>
                <%--</select>--%>
            <%--</div>--%>
        <%--</div>--%>
        <%--<br>--%>
        <%--&lt;%&ndash;<label class="layui-form-label">在线时长</label>&ndash;%&gt;--%>
        <%--&lt;%&ndash;<div class="layui-input-inline">&ndash;%&gt;--%>
        <%--&lt;%&ndash;<input type="date" name="login_time" class="layui-input" id="login_time">&ndash;%&gt;--%>
        <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
        <%--<div>--%>
            <%--<label class="layui-form-label">礼物总数</label>--%>
            <%--<div class="layui-input-inline">--%>
                <%--<input type="text" name="gifts_total" class="layui-input" id="gifts_total">--%>
            <%--</div>--%>
        <%--</div>--%>
        <%--<div>--%>
            <%--<label class="layui-form-label">礼物总价</label>--%>
            <%--<div class="layui-input-inline">--%>
                <%--<input type="text" name="gifts_value" class="layui-input" id="gifts_value">--%>
            <%--</div>--%>
        <%--</div>--%>
        <%--<div>--%>
            <%--<label class="layui-form-label">粉丝数</label>--%>
            <%--<div class="layui-input-inline">--%>
                <%--<input type="text" name="fans_number" class="layui-input" id="fans_number">--%>
            <%--</div>--%>
        <%--</div>--%>
        <%--<br>--%>
        <%--<div>--%>
            <%--<label class="layui-form-label">状态</label>--%>
            <%--<div class="layui-input-inline" style="width: 100px">--%>
                <%--<select name="state" id="state" >--%>
                    <%--<option value="">全部</option>--%>
                    <%--<option value="1">正常</option>--%>
                    <%--<option value="2">禁言</option>--%>
                    <%--<option value="3">冻结</option>--%>
                    <%--<option value="4">封号</option>--%>
                <%--</select>--%>
            <%--</div>--%>
        <%--</div>--%>
        <%--<input type="button" id="insertButton" value="提交">--%>
        <%--&lt;%&ndash;<input type="button" id="ctButton" value="提交">&ndash;%&gt;--%>
    <%--</form>--%>
</div>
<%--<div class="layui-input-inline">--%>
    <%--<button class="layui-btn layui-input-inline" id="deleteButton" style="float: right">删除</button>--%>
<%--</div>--%>

<div id="show1">
    <table class="layui-table" id="game_table" lay-filter="game_table" ></table>
</div>

<%--编辑框--%>
<div class="update_content" id="update_div" style="display: none">
    <%--<p>游戏ID：<input type="text" name="game_name" class="layui-input" id="update_game_name"></p>--%>
    <form id="update_form" class="layui-form" enctype="multipart/form-data">
        <label class="layui-form-label">游戏ID</label>
        <div class="layui-input-inline">
            <input type="text" name="id" class="layui-input" id="update_id">
        </div>
        <label class="layui-form-label">游戏名称</label>
        <div class="layui-input-inline">
            <input type="text" name="game_name" class="layui-input" id="update_game_name">
        </div>
        <label class="layui-form-label">游戏CP</label>
        <div class="layui-input-inline">
            <input type="text" name="game_cp" class="layui-input" id="update_game_cp">
        </div>
        <label class="layui-form-label">版本号</label>
        <div class="layui-input-inline">
            <input type="text" name="version_number" class="layui-input" id="update_version_number">
        </div>
        <div>
            <label class="layui-form-label">游戏评分</label>
            <div class="layui-input-inline">
                <input type="text" name="score" class="layui-input" id="update_score">
            </div>
        </div>
        <div>
            <label class="layui-form-label">简介</label>
            <div class="layui-input-inline">
                <input type="text" name="game_introduction" class="layui-input" id="update_game_introduction">
            </div>
        </div>
        <div>
            <label class="layui-form-label">位置</label>
            <div class="layui-input-inline">
                <input type="text" name="position" class="layui-input" id="update_position">
            </div>
        </div>
        <div class="layui-input-inline">
            <button type="button" class="layui-btn" id="update_test">
                <i class="layui-icon">&#xe67c;</i>标题图片
            </button>
            <div>
                <img class="layui-upload-img" id="demo1">
            </div>
        </div>
        <div>
            <label class="layui-form-label">状态</label>
            <div class="layui-input-inline">
                <input type="text" name="state" class="layui-input" id="update_state">
            </div>
        </div>
        <input type="button" id="updateButton" value="提交">
        <input type="button" id="cancelButton2" value="取消">
    </form>
</div>
</body>
    <script>
        layui.use('table', function () {  // 引入 table模块
            var table = layui.table;
            //监听表格复选框选择
            table.on('checkbox(game_table)', function(obj){
                console.log(obj)
            });
        })
        //上传图片
        layui.use('upload', function(){
            var upload = layui.upload;
            //执行实例
            var uploadInst = upload.render({
                elem: '#test1' //绑定元素
                ,url: '/Game/doInsert' //上传接口
                ,auto: false //选择文件后不自动上传
                ,before: function(obj){
                    //预读本地文件示例，不支持ie8
                    obj.preview(function(index, file, result){
                        $('#img_demo1').attr('src', result); //图片链接（base64）
                    });
                    $('#insertButton').click(function () {
                        $.ajax({
                            url: '/Game/doInsert',
                            type: "post",
                            //设置请求头，否则出现400错误
                            // contentType: "application/json;charset=utf-8",//这个参数也是header参数
                            data: $("#insert_form").serialize(),
                            success:function () {
                                alert("添加成功")
                                $("#insert_div").hide();
                            },
                            error:function () {
                                alert("失败")
                            }
                        })
                    })
                }
                ,done: function(res){
                    //如果上传失败
                    if(res.code > 0){
                        return layer.msg('上传失败');
                    }
                    //上传成功
                    // $('#insertButton').click(function () {
                    //     $.ajax({
                    //         url: '/Game/doInsert',
                    //         type: "post",
                    //         //设置请求头，否则出现400错误
                    //         // contentType: "application/json;charset=utf-8",//这个参数也是header参数
                    //         data: $("#insert_form").serialize(),
                    //         success:function () {
                    //             alert("添加成功")
                    //             $("#insert_div").hide();
                    //         },
                    //         error:function () {
                    //             alert("失败")
                    //         }
                    //     })
                    // })
                }
                ,error: function(){
                    var dd=res.responseText.replace(/<\/?.+?>/g,"");
                    var text=dd.replace(/ /g,"");//去掉所有空格
                    o.msg("请求上传接口出现异常"+text),
                        console.log(text);
                    m(e)
                    //演示失败状态，并实现重传
                    // var demoText = $('#demoText');
                    // demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-mini demo-reload">重试</a>');
                    // demoText.find('.demo-reload').on('click', function(){
                    //     uploadInst.upload();
                    // });
                }

                // ,done: function(res){
                //     //上传完毕回调
                // }
                // // ,accept: 'file' //允许上传的文件类型
                // // ,size: 50 //最大允许上传的文件大小
                // ,error: function(){
                //     //请求异常回调
                // }
            });
        });
        layui.use('table', function () {  // 引入 table模块
            var table = layui.table;
            $.ajax({
                type: "GET",
                url: '/Game/findByProp',
                // data: $("#searchForm").serialize(),
                success: function (r) {
                    var data = r.data;
                    table.render({
                        elem: '#game_table'//指定表格元素
                        , data: data
                        // , cellMinWidth: 20 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
                        , skin: 'line ' //表格风格 line （行边框风格）row （列边框风格）nob （无边框风格）
                        , even: true    //隔行换色
                        , page: true  //开启分页
                        , limits: [5, 10, 15]  //每页条数的选择项，默认：[10,20,30,40,50,60,70,80,90]。
                        , limit: 8
                        , cols: [[ //表头
                            {type: 'checkbox'}, //开启多选框
                            {field: 'id', title: '游戏ID', align: "center"}
                            , {field: 'game_name', title: '游戏名称', align: "center"}
                            , {field: 'game_cp', title: '游戏CP', align: "center"}
                            , {field: 'version_number', title: '版本号', align: "center"}
                            , {field: 'game_introduction', title: '游戏简介', align: "center"}
                            , {field: 'game_image', title: '游戏截图', align: "center"}
                            , {field: 'position', title: '位置', align: "center"}
                            , {field: 'state', title: '状态', align: "center", templet: function (d) {
                                    if (d.state == 1) {
                                        return "上线";
                                    } else {
                                        return "下线";
                                    }
                                }}
                            ,{fixed: 'right',title: '操作',width:200, align:'center', toolbar: '#barDemo'} //这里的toolbar值是模板元素的选择器
                        ]]
                        ,id: 'idTest'
                    });

                    <!--查询开始-->
                    $('#searchButton').click(function () {
                        $.ajax({
                            url: '/Game/findByWords',
                            type: "post",
                            data: $("#searchForm").serialize(),
                            // dataType:'JSON',
                            success: function (r) {
                                layui.use('table', function () {  // 引入 table模块
                                    var table = layui.table;
                                    var data = r.data;
                                    table.render({
                                        elem: '#game_table'//指定表格元素
                                        , data: data
                                        // , cellMinWidth: 20 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
                                        , skin: 'line ' //表格风格 line （行边框风格）row （列边框风格）nob （无边框风格）
                                        , even: true    //隔行换色
                                        , page: true  //开启分页
                                        , limits: [5, 10, 15]  //每页条数的选择项，默认：[10,20,30,40,50,60,70,80,90]。
                                        , limit: 8
                                        , cols: [[ //表头
                                            {type: 'checkbox'}, //开启多选框
                                            {field: 'id', title: '游戏ID', align: "center"}
                                            , {field: 'game_name', title: '游戏名称', align: "center"}
                                            , {field: 'game_cp', title: '游戏CP', align: "center"}
                                            , {field: 'version_number', title: '版本号', align: "center"}
                                            , {field: 'game_introduction', title: '游戏简介', align: "center"}
                                            , {field: 'game_image', title: '游戏截图', align: "center"}
                                            , {field: 'position', title: '位置', align: "center"}
                                            , {field: 'state', title: '状态', align: "center", templet: function (d) {
                                                    if (d.state == 1) {
                                                        return "上线";
                                                    } else {
                                                        return "下线";
                                                    }
                                                }}
                                            ,{fixed: 'right',title: '操作',width:200, align:'center', toolbar: '#barDemo'} //这里的toolbar值是模板元素的选择器
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
                    table.on('tool(game_table)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
                        var data = obj.data; //获得当前行数据
                        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
                        var tr = obj.tr; //获得当前行 tr 的DOM对象
                        var id = data.id;
                        <!--操作状态-->
                        // else
                            if(layEvent === 'update'){ //更新
                            alert('ID:'+data.id+'的更新操作');
                            $.ajax({
                                type: "POST",
                                url: '/Game/update',
                                data:{id:id},
                                success: function (r) {
                                    obj.update({
                                        state: '2'
                                    });
                                    layer.alert("成功")
                                },
                                error:function () {
                                    layer.alert("失败")
                                }
                            })
                        } else if(layEvent === 'online'){ //上线
                            alert('ID:'+data.id+'的上线操作');
                            $.ajax({
                                type: "POST",
                                url: '/Game/online',
                                data:{id:id},
                                success: function (r) {
                                    obj.update({
                                        state: '1'
                                    });
                                    alert("成功")
                                },
                                error:function () {
                                    alert("失败")
                                }
                            })
                        } else if(layEvent === 'offline'){ //下线
                            alert('ID:'+data.id+'的下线操作');
                            $.ajax({
                                type: "POST",
                                url: '/Game/offline',
                                data:{id:id},
                                success: function (r) {
                                    obj.update({
                                        state: '2'
                                    });
                                    alert("成功")
                                },
                                error:function () {
                                    alert("失败")
                                }
                            })
                        }else if (layEvent === 'editing'){//编辑
                            $("#update_div").show();
                            alert('ID:'+data.id+'的编辑操作');
                            alert(data.state)
                            $("#update_id").val(data.id);
                            $("#update_game_name").val(data.game_name);
                            $("#update_game_cp").val(data.game_cp);
                            $("#update_version_number").val(data.version_number);
                            $("#update_game_introduction").val(data.game_introduction);
                            $("#update_score").val(data.score);
                            $("#update_game_image").val(data.game_image);
                            $("#update_position").val(data.position);
                            $("#update_state").val(data.state);
                                $('#updateButton').on('click', function() {
                                    alert("提交按钮触发")
                                    $.ajax({
                                        type: "POST",
                                        url: '/Game/editing',
                                        data: $("#update_form").serialize(),
                                        success: function (r) {
                                            alert("成功")
                                            $("#update_div").hide();
                                        },
                                        error: function () {
                                            alert("失败")
                                        }
                                    })
                                })
                                //layui中需要使用layui.form.render()才可以实现回显下拉框
                                layui.form.render();
                        }
                    });

                    // table.on('edit(user_table)', function(obj){
                    //     var value = obj.value //得到修改后的值
                    //         ,data = obj.data //得到所在行所有键值
                    //         ,field = obj.field; //得到字段
                    //     layer.msg('[ID: '+ data.id +'] ' + field + ' 字段更改为：'+ value);
                    // });

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
                        alert("确定删除id为:"+ids+"这"+data.length+"条数据吗？");  //获取数据？
                        $.ajax({
                            type : "post",
                            url : "/Game/delete",
                            data:{id:ids},
                            success : function(datas){
                                alert("删除成功")
                                // if (datas == 1) {
                                //     layer.closeAll('loading');
                                //     $(".layui-laypage-btn")[0].click(); // 当前页刷新.思路是模拟点击分页按钮
                                //
                                // }else{
                                //
                                // }
                            }
                            ,error:function () {
                                alert("删除失败")
                            }
                        });
                    })
                }
            });
        });


        // $('#insertButton').click(function () {
        //     $.ajax({
        //         url: '/Game/doInsert',
        //         type: "post",
        //         //设置请求头，否则出现400错误
        //         // contentType: "application/json;charset=utf-8",//这个参数也是header参数
        //         data: $("#insert_form").serialize(),
        //         success:function () {
        //             alert("添加成功")
        //             $("#insert_div").hide();
        //         },
        //         error:function () {
        //             alert("失败")
        //         }
        //     })
        // })


        // $('#insertButton').click(function () {
        //     $.ajax({
        //         url: '/User/doInsert',
        //         type: "post",
        //         //设置请求头，否则出现400错误
        //         contentType: "application/json;charset=utf-8",//这个参数也是header参数
        //         data: $("#insert_form").serialize(),
        //         // dataType:'json',
        //         success: function (r) {
        //             $("#insert_div").hide();
        //             alert("新增成功");
        //             // layui.use('table', function () {  // 引入 table模块
        //             //     var table = layui.table;
        //             //     var data = r.data;
        //             //     table.render({
        //             //         elem: '#user_table'//指定表格元素
        //             //         , data: data
        //             //         // , cellMinWidth: 20 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
        //             //         , skin: 'line ' //表格风格 line （行边框风格）row （列边框风格）nob （无边框风格）
        //             //         , even: true    //隔行换色
        //             //         , page: true  //开启分页
        //             //         , limits: [5, 10, 15]  //每页条数的选择项，默认：[10,20,30,40,50,60,70,80,90]。
        //             //         , limit: 8
        //             //         , cols: [[ //表头
        //             //             {type: 'checkbox'}, //开启多选框
        //             //             {field: 'id', title: '用户ID', align: "center"}
        //             //             , {field: 'username', title: '昵称', align: "center"}
        //             //             , {
        //             //                 field: 'sex', title: '性别', align: "center", templet: function (d) {
        //             //                     if (d.sex == 1) {
        //             //                         return "男";
        //             //                     } else {
        //             //                         return "女";
        //             //                     }
        //             //                 }
        //             //             }
        //             //             , {field: 'user_type', title: '用户属性', align: "center", templet: function (d) {
        //             //                     if (d.user_type == 1) {
        //             //                         return "kol";
        //             //                     } else {
        //             //                         return "普通用户";
        //             //                     }
        //             //                 }}
        //             //             , {field: 'login_time', title: '在线时长', align: "center", sort: true}
        //             //             , {field: 'gifts_total', title: '礼物总数', align: "center", sort: true}
        //             //             , {field: 'gifts_value', title: '礼物总价', align: "center", sort: true}
        //             //             , {field: 'fans_number', title: '粉丝数', align: "center", sort: true}
        //             //             , {field: 'state', title: '状态', align: "center", templet: function (d) {
        //             //                     if (d.state == 0) {
        //             //                         return "已删除";
        //             //                     }else if (d.state == 1) {
        //             //                         return "正常";
        //             //                     } else if(d.state == 2) {
        //             //                         return "禁言";
        //             //                     }else if(d.state == 3) {
        //             //                         return "冻结";
        //             //                     }else if(d.state == 4) {
        //             //                         return "封号";
        //             //                     }
        //             //                 }}
        //             //             , {field: 'remain_time', title: '剩余时间', align: "center"}
        //             //             ,{fixed: 'right',title: '操作',width:300, align:'center', toolbar: '#barDemo'} //这里的toolbar值是模板元素的选择器
        //             //         ]]
        //             //     });
        //             // })
        //         },
        //         error: function (xhr, textStatus) {
        //             layer.open({
        //                 type: 1,
        //                 content: '错误' + textStatus
        //             });
        //         }
        //     });
        //     // return false;
        // })

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
        <a class="layui-btn layui-btn-xs" lay-event="update">更新</a>
        <a class="layui-btn layui-btn-xs" lay-event="online">上线</a>
        <a class="layui-btn layui-btn-xs" lay-event="offline">下线</a>
        <a class="layui-btn layui-btn-xs" lay-event="editing">编辑</a>
        <%--注意：属性 lay-event="" 是模板的关键所在，值可随意定义。--%>
    </script>

    <script>

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
    </style>
</html>