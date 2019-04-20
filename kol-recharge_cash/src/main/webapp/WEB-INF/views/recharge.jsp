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
    <script src="<%=basePath%>/layui/lay/modules/laydate.js"></script>
    <link rel="stylesheet" href="<%=basePath%>/layui/css/modules/laydate/default/laydate.css">
</head>
<body>
<br>
<form id="searchForm" class="layui-form" method="post">
    <%--<div class="layui-form" method="post">--%>
        <div class="layui-inline">
            <label class="layui-form-label">日期范围</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="date" id="search_date" placeholder="默认查询前15天">
            </div>
        </div>
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
            <label class="layui-form-label">订单号</label>
            <div class="layui-input-inline">
                <input type="text" name="order_number" autocomplete="off" class="layui-input" id="search_order_number">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">支付方式</label>
            <div class="layui-input-inline" style="width: 100px">
                <select name="payment" id="search_payment" >
                    <option value="">全部</option>
                    <option value="1">微信</option>
                    <option value="2">支付宝</option>
                </select>
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">状态</label>
            <div class="layui-input-inline" style="width: 100px">
                <select name="state" id="search_state" >
                    <option value="">全部</option>
                    <option value="0">成功</option>
                    <option value="-1">失败</option>
                    <option value="1">待定</option>
                </select>
            </div>
        </div>
        <div class="layui-inline">
            <button class="layui-btn layui-btn-green" id="searchButton">查询</button>
        </div>
    <%--</div>--%>
</form>
<br>

<div id="show1">
    <table class="layui-table" id="recharge_table" lay-filter="recharge_table" ></table>
</div>

</body>
    <script>
        layui.use('table', function () {  // 引入 table模块
            var table = layui.table;
            //监听表格复选框选择
            table.on('checkbox(recharge_table)', function(obj){
                console.log(obj)
            });
        })

        var tableIns;
        layui.use('table', function () {  // 引入 table模块
            var table = layui.table;
            $.ajax({
                type: "GET",
                url: '/kol_recharge_cash/Recharge/findByProp',
                // data: $("#searchForm").serialize(),
                success: function (r) {
                    var data = r.data;
                    tableIns =table.render({
                        elem: '#recharge_table'//指定表格元素
                        , data: data
                        // , cellMinWidth: 20 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
                        , skin: 'line ' //表格风格 line （行边框风格）row （列边框风格）nob （无边框风格）
                        , even: true    //隔行换色
                        , page: true  //开启分页
                        , limits: [5, 10, 15]  //每页条数的选择项，默认：[10,20,30,40,50,60,70,80,90]。
                        , limit: 8
                        , cols: [[ //表头
                            // {type: 'checkbox'}, //开启多选框
                            {field: 'date', title: '日期', align: "center", sort: true,templet : "<div>{{layui.util.toDateString(d.date, 'yyyy-MM-dd HH:mm:ss')}}</div>"}
                            , {field: 'order_number', title: '订单号', align: "center"}
                            , {field: 'id', title: '用户ID', align: "center"}
                            , {field: 'username', title: '姓名', align: "center"}
                            , {field: 'operant_hehavior', title: '操作', align: "center", templet: function (d) {
                                    return "充值"
                                }}
                            , {field: 'money', title: '金额', align: "center"}
                            , {field: 'payment', title: '支付方式', align: "center", templet: function (d) {
                                    if (d.payment == 1) {
                                        return "微信";
                                    } else if (d.payment == 2){
                                        return "支付宝";
                                    }
                                }}
                            , {field: 'account_balance', title: '账号余额', align: "center"}
                            , {field: 'state', title: '状态', align: "center", templet: function (d) {
                                    if (d.state == 0) {
                                        return "成功";
                                    } else if (d.state == -1){
                                        return "失败";
                                    }else if (d.state == 1){
                                        return "待定";
                                    }
                                }}
                            // ,{fixed: 'right',title: '操作',width:150, align:'center', toolbar: '#barDemo'} //这里的toolbar值是模板元素的选择器
                        ]]
                        ,id: 'idTest'
                    });

                    <!--查询开始-->
                    $('#searchButton').click(function () {
                        $.ajax({
                            url: '/kol_recharge_cash/Recharge/findByWords',
                            type: "post",
                            data: $("#searchForm").serialize(),
                            // dataType:'JSON',
                            success: function (r) {
                                layui.use('table', function () {  // 引入 table模块
                                    var table = layui.table;
                                    var data = r.data;
                                    table.render({
                                        elem: '#recharge_table'//指定表格元素
                                        , data: data
                                        // , cellMinWidth: 20 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
                                        , skin: 'line ' //表格风格 line （行边框风格）row （列边框风格）nob （无边框风格）
                                        , even: true    //隔行换色
                                        , page: true  //开启分页
                                        , limits: [5, 10, 15]  //每页条数的选择项，默认：[10,20,30,40,50,60,70,80,90]。
                                        , limit: 8
                                        , cols: [[ //表头
                                            // {type: 'checkbox'}, //开启多选框
                                            {field: 'date', title: '日期', align: "center",templet : "<div>{{layui.util.toDateString(d.date, 'yyyy-MM-dd HH:mm:ss')}}</div>"}
                                            , {field: 'order_number', title: '订单号', align: "center"}
                                            , {field: 'id', title: '用户ID', align: "center"}
                                            , {field: 'username', title: '姓名', align: "center"}
                                            , {field: 'operant_hehavior', title: '操作', align: "center", templet: function (d) {
                                                    return "充值"
                                                }}
                                            , {field: 'money', title: '金额', align: "center"}
                                            , {field: 'payment', title: '支付方式', align: "center", templet: function (d) {
                                                    if (d.payment == 1) {
                                                        return "微信";
                                                    } else if (d.payment == 2){
                                                        return "支付宝";
                                                    }
                                                }}
                                            , {field: 'account_balance', title: '账号余额', align: "center"}
                                            , {field: 'state', title: '状态', align: "center", templet: function (d) {
                                                    if (d.state == 0) {
                                                        return "成功";
                                                    } else if (d.state == -1){
                                                        return "失败";
                                                    }else if (d.state == 1){
                                                        return "待定";
                                                    }
                                                }}
                                            // ,{fixed: 'right',title: '操作',width:150, align:'center', toolbar: '#barDemo'} //这里的toolbar值是模板元素的选择器
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
                }
            });
        });


        $('#insertButton').click(function () {
            $.ajax({
                url: '/Home/doInsert',
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

    </script>
    <%--日期范围--%>
    <script>
        layui.use('laydate', function() {
            var laydate = layui.laydate;
            var date;
            laydate.render({
                elem: '#search_date'
                , range: ','
                // ,type: 'datetime'
                //laydate的默认是显示现在这个月和下一个月，使用下面配置实现显示上个月和这个月
                ,ready: function(date){
                    $(".laydate-main-list-0 .laydate-prev-m").click();
                }

            });

        })

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
    </style>
</html>