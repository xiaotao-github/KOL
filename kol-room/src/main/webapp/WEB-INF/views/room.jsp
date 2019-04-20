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
            <label class="layui-form-label">ID</label>
            <div class="layui-input-inline">
                <input type="text" name="id" class="layui-input" id="search_id">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">房间名</label>
            <div class="layui-input-inline">
                <input type="text" name="room_name" autocomplete="off" class="layui-input" id="search_room_name">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">房主昵称</label>
            <div class="layui-input-inline">
                <input type="text" name="username" autocomplete="off" class="layui-input" id="search_username">
            </div>
        </div>
        <div class="layui-inline">
            <button class="layui-btn layui-btn-green" id="searchButton">查询</button>
        </div>
    <%--</div>--%>
</form>
<br>


<div id="show1">
    <table class="layui-table" id="room_table" lay-filter="room_table" ></table>
</div>
</body>
    <script>
        layui.use('table', function () {  // 引入 table模块
            var table = layui.table;
            //监听表格复选框选择
            table.on('checkbox(friends_table)', function(obj){
                console.log(obj)
            });
        })
        layui.use('table', function () {  // 引入 table模块
            var table = layui.table;
            $.ajax({
                type: "GET",
                url: '/kol_room/Room/findByProp',
                // data: $("#searchForm").serialize(),
                success: function (r) {
                    var data = r.data;
                    table.render({
                        elem: '#room_table'//指定表格元素
                        , data: data
                        // , cellMinWidth: 20 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
                        , skin: 'line ' //表格风格 line （行边框风格）row （列边框风格）nob （无边框风格）
                        , even: true    //隔行换色
                        , page: true  //开启分页
                        , limits: [5, 10, 15]  //每页条数的选择项，默认：[10,20,30,40,50,60,70,80,90]。
                        , limit: 8
                        , cols: [[ //表头
                            // {type: 'checkbox'}, //开启多选框
                            {field: 'id', title: '用户ID（房主）', align: "center"}
                            , {field: 'room_name', title: '房间名', align: "center"}
                            , {field: 'username', title: '房主昵称', align: "center"}
                            , {field: 'room_number', title: '房间人数', align: "center", sort: true}
                            , {field: 'room_position', title: '置顶状态', align: "center", templet: function (d) {
                                    if (d.room_position == 0) {
                                        return "置顶";
                                    } else if (d.room_position == 1){
                                        return "非置顶";
                                    }
                                }}
                            , {field: 'state', title: '房间状态', align: "center", templet: function (d) {
                                    if (d.state == 0) {
                                        return "关闭";
                                    } else if (d.state == 1){
                                        return "正常";
                                    }
                                }}
                            ,{fixed: 'right',title: '操作',width:250, align:'center', toolbar: '#barDemo'} //这里的toolbar值是模板元素的选择器
                        ]]
                        ,id: 'idTest'
                    });

                    <!--查询开始-->
                    $('#searchButton').click(function () {
                        $.ajax({
                            url: '/kol_room/Room/findByWords',
                            type: "post",
                            data: $("#searchForm").serialize(),
                            // dataType:'JSON',
                            success: function (r) {
                                layui.use('table', function () {  // 引入 table模块
                                    var table = layui.table;
                                    var data = r.data;
                                    table.render({
                                        elem: '#room_table'//指定表格元素
                                        , data: data
                                        // , cellMinWidth: 20 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
                                        , skin: 'line ' //表格风格 line （行边框风格）row （列边框风格）nob （无边框风格）
                                        , even: true    //隔行换色
                                        , page: true  //开启分页
                                        , limits: [5, 10, 15]  //每页条数的选择项，默认：[10,20,30,40,50,60,70,80,90]。
                                        , limit: 8
                                        , cols: [[ //表头
                                            // {type: 'checkbox'}, //开启多选框
                                            {field: 'id', title: '用户ID（房主）', align: "center"}
                                            , {field: 'room_name', title: '房间名', align: "center"}
                                            , {field: 'username', title: '房主昵称', align: "center"}
                                            , {field: 'room_number', title: '房间人数', align: "center", sort: true}
                                            , {field: 'room_position', title: '置顶状态', align: "center", templet: function (d) {
                                                    if (d.room_position == 0) {
                                                        return "置顶";
                                                    } else if (d.room_position == 1){
                                                        return "非置顶";
                                                    }
                                                }}
                                            , {field: 'state', title: '房间状态', align: "center", templet: function (d) {
                                                    if (d.state == 0) {
                                                        return "关闭";
                                                    } else if (d.state == 1){
                                                        return "正常";
                                                    }
                                                }}
                                            ,{fixed: 'right',title: '操作',width:250, align:'center', toolbar: '#barDemo'} //这里的toolbar值是模板元素的选择器
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
                    table.on('tool(room_table)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
                        var data = obj.data; //获得当前行数据
                        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
                        var tr = obj.tr; //获得当前行 tr 的DOM对象
                        var id = data.id;
                        <!--操作状态-->
                        if(layEvent === 'stick'){ //置顶
                            alert('ID:'+data.id+'的置顶操作');
                            window.location.href="/kol_room/Room/stick?id="+data.id;
                        } else if(layEvent === 'unstick'){ //取消置顶
                            alert('ID:'+data.id+'的取消置顶操作');
                            window.location.href="/kol_room/Room/unstick?id="+data.id;
                        }else if(layEvent === 'close'){ //关闭
                            <!--房间的关闭是否只是把状态置0？？？-->
                            alert('ID:'+data.id+'的关闭房间操作');
                            window.location.href="/kol_room/Room/close?id="+data.id;
                            // layer.confirm('确定关闭id为:'+id+"这个房间吗？", {
                            //     btn: ['确定', '取消'] //可以无限个按钮
                            // }, function(index, layero){
                            //     layer.msg('关闭成功', {icon: 1});
                            //     window.location.href="/Room/delete?id="+data.id;
                            // }, function(index){
                            // });
                        }else if (layEvent === 'unclose'){
                            alert('ID:'+data.id+'的取消关闭房间操作');
                            window.location.href="/kol_room/Room/unclose?id="+data.id;
                        }
                    });
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

    </script>


    <script type="text/html" id="barDemo">
        {{#  if(d.room_position == 0&&d.state==0){ }}
        <a class="layui-btn layui-btn-sm" lay-event="unstick">取消置顶</a>
        <a class="layui-btn layui-btn-sm" lay-event="unclose">取消关闭</a>
        {{#  }else if(d.room_position == 0&&d.state==1){ }}
        <a class="layui-btn layui-btn-sm" lay-event="unstick">取消置顶</a>
        <a class="layui-btn layui-btn-sm" lay-event="close">关闭</a>
        {{#  }else if(d.room_position == 1&&d.state==0){ }}
        <a class="layui-btn layui-btn-sm" lay-event="stick">置顶</a>
        <a class="layui-btn layui-btn-sm" lay-event="unclose">取消关闭</a>
        {{#  }else if(d.room_position == 1&&d.state==1){ }}
        <a class="layui-btn layui-btn-sm" lay-event="stick">置顶</a>
        <a class="layui-btn layui-btn-sm" lay-event="close">关闭</a>
        {{#  } }}
        <%--注意：属性 lay-event="" 是模板的关键所在，值可随意定义。--%>
    </script>


    <style type="text/css">
        .main
        {
            font-size:120%;
            color:red;
            background: #0b94ea;
        }
        .layui-table-page{text-align: center;}

        /*设置layui table行高*/
        .layui-table-cell{
            height:36px;
            line-height: 40px;
        }

        a.layui-btn.layui-btn-sm{
            width: 80px;
            height: 30px;
            /*text-align:center;*/
        }
    </style>
</html>