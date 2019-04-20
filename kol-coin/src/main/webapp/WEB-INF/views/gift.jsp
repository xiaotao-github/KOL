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
<form id="searchForm" class="layui-form" method="post" >
    <%--<div class="layui-form" method="post">--%>
        <div class="layui-inline" >
            <label class="layui-form-label">礼物ID</label>
            <div class="layui-input-inline">
                <input type="text" name="id" class="layui-input" id="search_id">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">名称</label>
            <div class="layui-input-inline">
                <input type="text" name="gift_name" autocomplete="off" class="layui-input" id="search_gift_name">
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
            <button class="layui-btn layui-btn-green" id="searchButton">查询</button>
        </div>
    <%--</div>--%>
</form>
<br>
<%--这个是添加框--%>
<div class="insert_button">
    <button onclick = "openDialog()" class="layui-btn layui-input-inline" style="height: 40px;width: 125px"><i class="layui-icon">&#xe654;</i>新增</button>
    <button class="layui-btn layui-btn-danger layui-input-inline" id="deleteButton" style="float: right;height: 40px;width: 125px"><i class="layui-icon">&#xe640;</i>删除</button>
</div>
<%--添加内容--%>
<div class="insert_content" id="insert_div">
    <form id="insert_form" class="layui-form" action="/kol_coin/Gift/doInsert" method="post" enctype="multipart/form-data">
        <label class="layui-form-label">礼物名称</label>
        <div class="layui-input-inline">
            <input type="text" name="gift_name" class="layui-input" id="gift_name" lay-verify="required">
        </div>
        <label class="layui-form-label">价值</label>
        <div class="layui-input-inline">
            <input type="text" name="value" class="layui-input" id="value" lay-verify="required|noPoint">
        </div>
        <div>
            <label class="layui-form-label">是否可连击</label>
            <div class="layui-input-inline" style="width: 100px">
                <select name="isHit" id="isHit">
                    <option value="0">否</option>
                    <option value="1">是</option>
                </select>
            </div>
        </div>
        <div>
            <%--礼物类别还没确定--%>
            <label class="layui-form-label">状态</label>
            <div class="layui-input-inline" style="width: 100px">
                <select name="state" id="state">
                    <option value="1">上线</option>
                    <option value="2">下线</option>
                </select>
            </div>
        </div>
        <label class="layui-form-label">礼物值</label>
        <div class="layui-input-inline">
            <input type="text" name="gift_value" class="layui-input" id="gift_value" lay-verify="required|noPoint">
        </div>
        <label class="layui-form-label">魅力值</label>
        <div class="layui-input-inline">
            <input type="text" name="charm_value" class="layui-input" id="charm_value" lay-verify="required|noPoint">
        </div>
        <label class="layui-form-label">可选择数量</label>
        <div class="layui-input-inline">
            <input type="text" name="amount" class="layui-input" id="amount" lay-verify="required" placeholder="1/20/30/60">
        </div>
        <label class="layui-form-label">礼物图片</label>
        <div class="uploadHeadImage" >
            <div class="layui-upload-drag" id="headImg">
                <i class="layui-icon"></i>
                <p>点击上传图片，或将图片拖拽到此处</p>
            </div>
        </div>
        <div class="layui-input-inline">
            <div class="layui-upload-list">
                <img class="layui-upload-img headImage" id="demo1">
                <p id="demoText"></p>
            </div>
        </div>
        <br>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit="/kol_coin/Gift/doInsert" id="insertButton">立即提交</button>
                <%--<button type="reset" class="layui-btn layui-btn-primary">重置</button>--%>
                <button type="button" id="cancelButton" class="layui-btn">取消</button>
            </div>
        </div>
    </form>
</div>

<div id="show1">
    <table class="layui-table" id="gift_table" lay-filter="gift_table" ></table>
</div>

<%--编辑--%>
<div class="update_content" id="update_div" style="display: none">
    <form id="update_form" class="layui-form" enctype="multipart/form-data" action="/kol_coin/Gift/editing" method="post">
        <input type="text" name="id" id="update_id" hidden="hidden">

        <label class="layui-form-label">礼物名称</label>
        <div class="layui-input-inline">
            <input type="text" name="gift_name" class="layui-input" id="update_gift_name" lay-verify="required">
        </div>
        <label class="layui-form-label">价值</label>
        <div class="layui-input-inline">
            <input type="text" name="value" class="layui-input" id="update_value" lay-verify="required|noPoint">
        </div>
        <br>
        <label class="layui-form-label">是否可连击</label>
        <div class="layui-input-inline" style="width: 120px">
            <select name="isHit" id="update_isHit" >
                <option value="0">否</option>
                <option value="1">是</option>
            </select>
        </div>
        <br>
        <label class="layui-form-label">礼物值</label>
        <div class="layui-input-inline">
            <input type="text" name="gift_value" class="layui-input" id="update_gift_value" lay-verify="required|noPoint2">
        </div>
        <br>
        <label class="layui-form-label">魅力值</label>
        <div class="layui-input-inline">
            <input type="text" name="charm_value" class="layui-input" id="update_charm_value" lay-verify="required|noPoint">
        </div>
        <br>
        <label class="layui-form-label">可选择数量</label>
        <div class="layui-input-inline">
            <input type="text" name="amount" class="layui-input" id="update_amount" lay-verify="required" placeholder="1/20/30/60">
        </div>
        <br>
        <label class="layui-form-label">状态</label>
        <div class="layui-input-inline" style="width: 120px">
            <select name="state" id="update_state" >
                <option value="1">上线</option>
                <option value="2">下线</option>
            </select>
        </div>
        <br>
        <%--上传图片--%>
        <label class="layui-form-label">礼物图片</label>
        <div class="uploadHeadImage" >
            <div class="layui-upload-drag" id="update_headImg">
                <i class="layui-icon"></i>
                <p>点击上传图片，或将图片拖拽到此处</p>
            </div>
        </div>
        <div class="layui-input-inline">
            <div class="layui-upload-list">
                <img class="layui-upload-img headImage" id="update_demo1">
                <p id="update_demoText"></p>
            </div>
        </div>
        <br>
        <%--<center>
            <button type="submit" id="updateButton" class="layui-btn-sm">提交</button>
            <button type="button" id="cancelButton2" class="layui-btn-sm">取消</button>
        </center>--%>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit="/kol_coin/Gift/editing" id="updateButton">立即提交</button>
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
            table.on('checkbox(gift_table)', function(obj){
                console.log(obj)
            });
        })
        //上传图片
        layui.use(["jquery", "upload", "form", "layer", "element"], function () {
            var $ = layui.$,
                element = layui.element,
                layer = layui.layer,
                upload = layui.upload,
                form = layui.form;

            //自定义验证规则
            form.verify({
                noPoint: [
                    /^[0-9]*[1-9][0-9]*$/
                    ,'必须是正整数'
                ],
                noPoint2: [
                    /^-?[1-9]\d*$/
                    ,'必须是整数'
                ]
            });

            //拖拽上传
            var uploadInst = upload.render({
                elem: '#headImg'
                , url: '/kol_coin/Gift/upload/headImg'
                ,acceptMime:'image/*'   // 只显示图片
                ,exts : 'jpg|png|gif|bmp|jpeg'
                // ,multiple: true //允许多文件上传
                // ,bindAction: '#upd' //指定一个按钮触发上传
                , size: 2000
                ,auto: false //选择文件后不自动上传
                ,bindAction:'#insertButton'
                ,dataType:'json'
                // ,field:'file'
                , before: function (obj) {
                    //预读本地文件示例，不支持ie8
                    obj.preview(function (index, file, result) {
                        $('#demo1').attr('src', result); //图片链接（base64）
                    });
                    console.log(index); //得到文件索引
                    console.log(file); //得到文件对象
                    console.log(result); //得到文件base64编码，比如图片
                }
                , done: function (res) {
                    //如果上传失败
                    if (res.code > 0) {
                        return layer.msg('上传失败');
                    }

                    //上传成功
                    //打印后台传回的地址: 把地址放入一个隐藏的input中, 和表单一起提交到后台, 此处略..
                    /*   console.log(res.data.src);*/
                    // window.parent.uploadHeadImage(res.data.src);
                    var demoText = $('#demoText');
                    demoText.html('<span style="color: #8f8f8f;">上传成功!!!</span>');
                }
                , error: function () {
                    //演示失败状态，并实现重传
                    var demoText = $('#demoText');
                    demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-mini demo-reload">重试</a>');
                    demoText.find('.demo-reload').on('click', function () {
                        uploadInst.upload();
                    });
                }
            });

            var uploadInst2 = upload.render({
                elem: '#update_headImg'
                , url: '/kol_coin/Gift/upload/headImg'
                ,acceptMime:'image/*'   // 只显示图片
                ,exts : 'jpg|png|gif|bmp|jpeg'
                // ,multiple: true //允许多文件上传
                // ,bindAction: '#upd' //指定一个按钮触发上传
                , size: 2000
                ,auto: false //选择文件后不自动上传
                ,bindAction:'#updateButton'
                ,dataType:'json'
                // ,field:'file'
                , before: function (obj) {
                    //预读本地文件示例，不支持ie8
                    obj.preview(function (index, file, result) {
                        $('#update_demo1').attr('src', result); //图片链接（base64）
                    });
                    console.log(index); //得到文件索引
                    console.log(file); //得到文件对象
                    console.log(result); //得到文件base64编码，比如图片
                }
                , done: function (res) {
                    //如果上传失败
                    if (res.code > 0) {
                        return layer.msg('上传失败');
                    }

                    //上传成功
                    //打印后台传回的地址: 把地址放入一个隐藏的input中, 和表单一起提交到后台, 此处略..
                    /*   console.log(res.data.src);*/
                    // window.parent.uploadHeadImage(res.data.src);
                    var demoText = $('#update_demoText');
                    demoText.html('<span style="color: #8f8f8f;">上传成功!!!</span>');
                }
                , error: function () {
                    //演示失败状态，并实现重传
                    var demoText = $('#update_demoText');
                    demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-mini demo-reload">重试</a>');
                    demoText.find('.demo-reload').on('click', function () {
                        uploadInst.upload();
                    });
                }
            })
            element.init();
        });

        var tableIns;
        layui.use('table', function () {  // 引入 table模块
            var table = layui.table;
            $.ajax({
                type: "GET",
                url: '/kol_coin/Gift/findByProp',
                // data: $("#searchForm").serialize(),
                success: function (r) {
                    var data = r.data;
                    tableIns =table.render({
                        elem: '#gift_table'//指定表格元素
                        , data: data
                        // , cellMinWidth: 20 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
                        , skin: 'line ' //表格风格 line （行边框风格）row （列边框风格）nob （无边框风格）
                        , even: true    //隔行换色
                        , page: true  //开启分页
                        , limits: [5, 10, 15]  //每页条数的选择项，默认：[10,20,30,40,50,60,70,80,90]。
                        , limit: 8
                        , cols: [[ //表头
                            {type: 'checkbox'}, //开启多选框
                            {field: 'id', title: '礼物ID', align: "center"}
                            , {field: 'gift_name', title: '礼物名称', align: "center"}
                            , {field: 'isHit', title: '是否可连击', align: "center", templet: function (d) {
                                    if (d.isHit == 1) {
                                        return "是";
                                    } else if (d.isHit == 0){
                                        return "否";
                                    }
                                }}
                            , {field: 'value', title: '价值(币)', align: "center"}
                            , {field: 'gift_value', title: '礼物值', align: "center", sort: true}
                            , {field: 'charm_value', title: '魅力值', align: "center", sort: true}
                            , {field: 'amount', title: '可选择数量', align: "center"}
                            , {field: 'image', title: '缩略图', align: "center",templet: '#image'}
                            , {field: 'state', title: '状态', align: "center", templet: function (d) {
                                    if (d.state == 1) {
                                        return "上线";
                                    } else if (d.state == 2){
                                        return "下线";
                                    }
                                }}
                            ,{fixed: 'right',title: '操作',width:150, align:'center', toolbar: '#barDemo'} //这里的toolbar值是模板元素的选择器
                        ]]
                        ,id: 'idTest'
                    });

                    <!--查询开始-->
                    $('#searchButton').click(function () {
                        $.ajax({
                            url: '/kol_coin/Gift/findByWords',
                            type: "post",
                            data: $("#searchForm").serialize(),
                            // dataType:'JSON',
                            success: function (r) {
                                layui.use('table', function () {  // 引入 table模块
                                    var table = layui.table;
                                    var data = r.data;
                                    table.render({
                                        elem: '#gift_table'//指定表格元素
                                        , data: data
                                        // , cellMinWidth: 20 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
                                        , skin: 'line ' //表格风格 line （行边框风格）row （列边框风格）nob （无边框风格）
                                        , even: true    //隔行换色
                                        , page: true  //开启分页
                                        , limits: [5, 10, 15]  //每页条数的选择项，默认：[10,20,30,40,50,60,70,80,90]。
                                        , limit: 8
                                        , cols: [[ //表头
                                            {type: 'checkbox'}, //开启多选框
                                            {field: 'id', title: '内容ID', align: "center"}
                                            , {field: 'gift_name', title: '礼物名称', align: "center"}
                                            , {field: 'isHit', title: '礼物类别', align: "center", templet: function (d) {
                                                    if (d.isHit == 1) {
                                                        return "是";
                                                    } else if (d.isHit == 0){
                                                        return "否";
                                                    }
                                                }}
                                            , {field: 'value', title: '价值(币)', align: "center"}
                                            , {field: 'gift_value', title: '礼物值', align: "center", sort: true}
                                            , {field: 'charm_value', title: '魅力值', align: "center", sort: true}
                                            , {field: 'amount', title: '可选择数量', align: "center"}
                                            , {field: 'image', title: '缩略图', align: "center",templet: '#image'}
                                            , {field: 'state', title: '状态', align: "center", templet: function (d) {
                                                    if (d.state == 1) {
                                                        return "上线";
                                                    } else if (d.state == 2){
                                                        return "下线";
                                                    }
                                                }}
                                            ,{fixed: 'right',title: '操作',width:150, align:'center', toolbar: '#barDemo'} //这里的toolbar值是模板元素的选择器
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
                    table.on('tool(gift_table)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
                        var data = obj.data; //获得当前行数据
                        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
                        var tr = obj.tr; //获得当前行 tr 的DOM对象
                        var id = data.id;
                        <!--操作状态-->
                        //上下线操作
                        if(layEvent === 'on_offline'){ //上下线
                            if (data.state==1){//下线操作
                                alert('ID:'+data.id+'的下线操作');
                                $.ajax({
                                    type: "POST",
                                    url: '/kol_coin/Gift/offline',
                                    data:{id:id},
                                    success: function (r) {
                                        window.location.href="/kol_coin/Gift/showPage";
                                    },
                                    error:function () {
                                        alert("失败")
                                    }
                                })
                            }
                            if (data.state==2){
                                alert('ID:'+data.id+'的上线操作');
                                $.ajax({
                                    type: "POST",
                                    url: '/kol_coin/Gift/online',
                                    data:{id:id},
                                    success: function (r) {
                                        window.location.href="/kol_coin/Gift/showPage";
                                    },
                                    error:function () {
                                        alert("失败")
                                    }
                                })
                            }
                        }
                        //编辑操作
                        else if (layEvent === 'editing'){//编辑
                            $("#update_div").show();
                            alert('ID:'+data.id+'的编辑操作');
                            $("#update_id").val(data.id);
                            $("#update_gift_name").val(data.gift_name);
                            $("#update_isHit").val(data.isHit);
                            $("#update_value").val(data.value);
                            $("#update_charm_value").val(data.charm_value);
                            $("#update_amount").val(data.amount);
                            $("#update_image").val(data.image);
                            $("#update_state").val(data.state);
                            $("#update_gift_value").val(data.gift_value);
                            $("#update_charm_value").val(data.charm_value);
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
                            window.location.href="/kol_coin/Gift/delete?id="+ids;
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
            // $("#update_thematicAdvertisement_div").hide();
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
        {{#  if(d.state == 1){ }}
        <a class="layui-btn layui-btn-sm" lay-event="on_offline" id="testButton1">下线</a>
        {{#  } else if(d.state == 2){ }}
        <a class="layui-btn layui-btn-sm" lay-event="on_offline" id="testButton2">上线</a>
        {{#  } }}
        <a class="layui-btn layui-btn-sm" lay-event="editing"><i class="layui-icon">&#xe642</i>编辑</a>
        <%--注意：属性 lay-event="" 是模板的关键所在，值可随意定义。--%>
    </script>

    <%--图片展示，配置Tomcat虚拟路径，没有直接{{ d.works_image }}--%>
    <script type="text/html" id="image">
        {{#  if(d.image == null){ }}
        无图片
        {{#  } else { }}
        <div><img src="{{ d.image }}"></div>
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

        /**设置操作按钮大小*/
        /*a.layui-btn.layui-btn-xs{*/
            /*width: 80px;*/
            /*height: 30px;*/
            /*!*text-align:center;*!*/
        /*}*/
    </style>
</html>