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
<%--这个是添加框--%>
<div class="insert_button">
    <button onclick = "openDialog()" class="layui-btn layui-input-inline" style="height: 40px;width: 125px"><i class="layui-icon">&#xe654;</i>新增</button>
    <button class="layui-btn layui-btn-danger layui-input-inline" id="deleteButton" style="float: right;height: 40px;width: 125px"><i class="layui-icon">&#xe640;</i>删除</button>
</div>
<div class="insert_content" id="insert_div">
    <form id="insert_form" class="layui-form" action="/kol_recharge_cash/CoinProportion/doInsert" method="post">
        <div>
            <label class="layui-form-label">序号</label>
            <div class="layui-input-inline">
                <input type="text" name="id" class="layui-input" id="id" lay-verify="required|number">
            </div>
        </div>
        <div>
            <label class="layui-form-label">X币售价</label>
            <div class="layui-input-inline">
                <input type="text" name="coin_price" class="layui-input" id="coin_price" lay-verify="required|number">
            </div>
        </div>
        <label class="layui-form-label">X币数量</label>
        <div class="layui-input-inline">
            <input type="text" name="coin_amount" class="layui-input" id="coin_amount" lay-verify="required|number">
        </div>
        <br>
        <label class="layui-form-label">编辑人</label>
        <div class="layui-input-inline">
            <input type="text" name="editor" class="layui-input" id="editor">
        </div>
        <br>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit="/kol_recharge_cash/CoinProportion/doInsert" id="insertButton">立即提交</button>
                <%--<button type="reset" class="layui-btn layui-btn-primary">重置</button>--%>
                <button type="button" id="cancelButton" class="layui-btn">取消</button>
            </div>
        </div>
    </form>
</div>

<div id="show1">
    <table class="layui-table" id="coinProportion_table" lay-filter="coinProportion_table" ></table>
</div>

<%--编辑框--%>
<div class="update_content" id="update_div" style="display: none">
    <%--<p>游戏ID：<input type="text" name="game_name" class="layui-input" id="update_game_name"></p>--%>
    <form id="update_form" class="layui-form" action="/kol_recharge_cash/CoinProportion/editing" method="post">
        <%--<label class="layui-form-label">序号</label>--%>
        <%--<div class="layui-input-inline">--%>
            <%--<input type="text" name="id" class="layui-input" id="update_id" lay-verify="required|number">--%>
        <%--</div>--%>
        <input type="text" name="id" id="update_id" hidden="hidden">

        <label class="layui-form-label">X币售价</label>
        <div class="layui-input-inline">
            <input type="text" name="coin_price" class="layui-input" id="update_coin_price" lay-verify="required|number">
        </div>
        <label class="layui-form-label">X币数量</label>
        <div class="layui-input-inline">
            <input type="text" name="coin_amount" class="layui-input" id="update_coin_amount" lay-verify="required|number">
        </div>
        <label class="layui-form-label">编辑人</label>
        <div class="layui-input-inline">
            <input type="text" name="editor" class="layui-input" id="update_editor">
        </div>
        <br>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit="/kol_recharge_cash/CoinProportion/editing" id="updateButton">立即提交</button>
                <%--<button type="reset" class="layui-btn layui-btn-primary">重置</button>--%>
                <button type="button" id="cancelButton2" class="layui-btn">取消</button>
            </div>
        </div>
    </form>
</div>
</body>
    <script>
        layui.use('table', function () {  // 引入 table模块
            var table = layui.table;
            //监听表格复选框选择
            table.on('checkbox(coinProportion_table)', function(obj){
                console.log(obj)
            });
        })
        var tableIns;
        layui.use('table', function () {  // 引入 table模块
            var table = layui.table;
            $.ajax({
                type: "GET",
                url: '/kol_recharge_cash/CoinProportion/findByProp',
                // data: $("#searchForm").serialize(),
                success: function (r) {
                    var data = r.data;
                    tableIns =table.render({
                        elem: '#coinProportion_table'//指定表格元素
                        , data: data
                        // , cellMinWidth: 20 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
                        , skin: 'line ' //表格风格 line （行边框风格）row （列边框风格）nob （无边框风格）
                        , even: true    //隔行换色
                        , page: true  //开启分页
                        , limits: [5, 10, 15]  //每页条数的选择项，默认：[10,20,30,40,50,60,70,80,90]。
                        , limit: 8
                        , cols: [[ //表头
                            {type: 'checkbox'}, //开启多选框
                            {field: 'id', title: '序号', align: "center"}
                            ,{field: 'date', title: '日期', align: "center", sort: true,templet : "<div>{{layui.util.toDateString(d.date, 'yyyy-MM-dd HH:mm:ss')}}</div>"}
                            , {field: 'coin_price', title: 'X币售价', align: "center",templet : "<div>￥{{d.coin_price}}</div>"}
                            , {field: 'coin_amount', title: 'X币数量', align: "center"}
                            // , {field: 'order_amount', title: '可提现金额', align: "center"}
                            , {field: 'editor', title: '编辑人', align: "center"}
                            ,{fixed: 'right',title: '操作',width:150, align:'center', toolbar: '#barDemo'} //这里的toolbar值是模板元素的选择器
                        ]]
                        ,id: 'idTest'
                    });

                    //监听工具条(操作功能开始)
                    table.on('tool(coinProportion_table)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
                        var data = obj.data; //获得当前行数据
                        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
                        var tr = obj.tr; //获得当前行 tr 的DOM对象
                        var id = data.id;
                        <!--操作状态-->
                        //编辑操作
                        if (layEvent === 'editing'){//编辑
                            $("#update_div").show();
                            alert('ID:'+data.id+'的编辑操作');
                            $("#update_id").val(data.id);
                            $("#update_coin_price").val(data.coin_price);
                            $("#update_coin_amount").val(data.coin_amount);
                            $("#update_editor").val(data.editor);
                                //layui中需要使用layui.form.render()才可以实现回显下拉框
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
                        // alert("确定删除id为:"+ids+"这"+data.length+"条数据吗？");  //获取数据？
                        layer.confirm('确定删除id为:'+ids+"这"+data.length+"条数据吗？", {
                            btn: ['确定', '取消'] //可以无限个按钮
                        }, function(index, layero){
                            layer.msg('删除成功', {icon: 1});
                            window.location.href="/kol_recharge_cash/CoinProportion/delete?id="+ids;
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
        $('.cancel').click(function () {
            $("#update_div").hide();
            return false;
        })
        $('#cancelButton2').click(function () {
            $("#update_div").hide();
            return false;
        })

    </script>

    <%--操作栏--%>
    <script type="text/html" id="barDemo">
        <%--自定义按钮模板，支持 laytpl 语法--%>
        <a class="layui-btn layui-btn-sm" lay-event="editing"><i class="layui-icon">&#xe642</i>编辑</a>
        <%--注意：属性 lay-event="" 是模板的关键所在，值可随意定义。--%>
    </script>

    <%--图片展示，配置Tomcat虚拟路径，没有直接{{ d.works_image }}--%>
    <script type="text/html" id="image">
        {{#  if(d.image == null){ }}
        无图片
        {{#  } else { }}
        <div><img src="/upload/{{ d.image }}"></div>
        {{#  } }}
    </script>

    <script>
        //开启悬浮框
        function openDialog(){
            // document.getElementById('insert_div').style.display='block';
            document.getElementById('insert_div').style.display='block';
        }
        //关闭悬浮框
        function closeDialog(){
            // document.getElementById('insert_div').style.display='none';
            document.getElementById('insert_div').style.display='block';
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
        /*设置layui table行高*/
        .layui-table-cell{
            height:36px;
            line-height: 40px;
        }
    </style>
</html>