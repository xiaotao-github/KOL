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
    <div class="layui-inline" >
        <label class="layui-form-label">序号</label>
        <div class="layui-input-inline">
            <input type="text" name="id" class="layui-input" id="search_id">
        </div>
    </div>
    <div class="layui-inline">
        <label class="layui-form-label">文章标题</label>
        <div class="layui-input-inline">
            <input type="text" name="title" autocomplete="off" class="layui-input" id="search_title">
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
</form>
<br>
<%--这个是添加框--%>
<div class="insert_button">
    <button onclick = "openDialog()" class="layui-btn layui-input-inline" style="height: 40px;width: 125px"><i class="layui-icon">&#xe654;</i>新增</button>
    <button class="layui-btn layui-btn-danger layui-input-inline" id="deleteButton" style="float: right;height: 40px;width: 125px"><i class="layui-icon">&#xe640;</i>删除</button>
</div>
<div class="insert_content" id="insert_div" >
    <form id="insert_form" class="layui-form" action="/Message/doInsert" method="post" enctype="multipart/form-data">

        <label class="layui-form-label">消息标题</label>
        <div class="layui-input-inline">
            <input type="text" name="title" class="layui-input" id="title">
        </div>
        <br>
        <label class="layui-form-label">链接</label>
        <div class="layui-input-inline">
            <input type="text" name="url" class="layui-input" id="url">
        </div>
        <br>
        <label class="layui-form-label">位置</label>
        <div class="layui-input-inline" style="width: 120px">
            <select name="position" id="position" >
                <option value="1">发现-活动</option>
                <option value="2">消息-活动</option>
            </select>
        </div>
        <br>
        <label class="layui-form-label">发布人</label>
        <div class="layui-input-inline">
            <input type="text" name="editor" class="layui-input" id="editor">
        </div>
        <br>
        <label class="layui-form-label">状态</label>
        <div class="layui-input-inline" style="width: 120px">
            <select name="state" id="state" >
                <option value="1">上线</option>
                <option value="2">下线</option>
            </select>
        </div>
        <br>
        <label class="layui-form-label">上传图片</label>
        <div class="uploadHeadImage" >
            <div class="layui-upload-drag" id="headImg">
                <i class="layui-icon"></i>
                <p>点击上传图片，或将图片拖拽到此处</p>
            </div>
        </div>
        <br>
        <label class="layui-form-label">编辑内容</label>
        <div class="layui-input-block">
            <textarea id="content" name="content" style="display: none;" placeholder="请输入内容"></textarea>
        </div>
        <br>
        <br>
        <center>
            <button type="submit" id="insertButton" class="layui-btn-sm">
                提交
            </button>
            <button type="button" id="cancelButton" class="layui-btn-sm">
                取消
            </button>
        </center>
    </form>
</div>

<div id="show1">
    <table class="layui-table" id="friends_table" lay-filter="friends_table" ></table>
</div>

<%--编辑框--%>
<div class="update_content" id="update_div" style="display: none">
    <%--<p>游戏ID：<input type="text" name="game_name" class="layui-input" id="update_game_name"></p>--%>
    <form id="update_form" class="layui-form" enctype="multipart/form-data" action="/Message/editing" method="post">
        <input type="text" name="id" id="update_id" hidden="hidden">
        <label class="layui-form-label">消息标题</label>
        <div class="layui-input-inline">
            <input type="text" name="title" class="layui-input" id="update_title">
        </div>
        <br>
        <label class="layui-form-label">链接</label>
        <div class="layui-input-inline">
            <input type="text" name="url" class="layui-input" id="update_url">
        </div>
        <br>
        <label class="layui-form-label">位置</label>
        <div class="layui-input-inline" style="width: 120px">
            <select name="position" id="update_position" >
                <option value="1">发现-活动</option>
                <option value="2">消息-活动</option>
            </select>
        </div>
        <br>
        <label class="layui-form-label">发布人</label>
        <div class="layui-input-inline">
            <input type="text" name="editor" class="layui-input" id="update_editor">
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
        <label class="layui-form-label">上传图片</label>
        <div class="uploadHeadImage" >
            <div class="layui-upload-drag" id="update_headImg">
                <i class="layui-icon"></i>
                <p>点击上传图片，或将图片拖拽到此处</p>
            </div>
        </div>
        <br>
        <label class="layui-form-label">内容</label>
        <div class="layui-input-block">
            <textarea id="update_content" name="content" placeholder="请输入内容"></textarea>
        </div>
        <%--</div>--%>
        <br>
        <br>
        <br>
        <center>
            <button type="submit" id="updateButton" class="layui-btn-sm">提交</button>
            <button type="button" id="cancelButton2" class="layui-btn-sm">取消</button>
        </center>
    </form>
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
    //上传图片
    layui.use(["jquery", "upload", "form", "layer", "element"], function () {
        var $ = layui.$,
            element = layui.element,
            layer = layui.layer,
            upload = layui.upload,
            form = layui.form;
        //拖拽上传
        var uploadInst = upload.render({
            elem: '#headImg'
            , url: '/Message/upload/headImg'
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
            , url: '/Message/upload/headImg'
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

    //table开始
    layui.use('table', function () {  // 引入 table模块
        var table = layui.table;
        $.ajax({
            type: "GET",
            url: '/Message/findByProp',
            // data: $("#searchForm").serialize(),
            success: function (r) {
                var data = r.data;
                table.render({
                    elem: '#friends_table'//指定表格元素
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
                        , {field: 'title', title: '文章标题', align: "center"}
                        , {field: 'position', title: '位置', align: "center", templet: function (d) {
                                if (d.position == 1) {
                                    return "发现-活动";
                                } else {
                                    return "消息-活动";
                                }
                            }}
                        , {field: 'url', title: '链接', align: "center"}
                        , {field: 'time', title: '发布时间', align: "center"}
                        , {field: 'editor', title: '发布人', align: "center"}
                        , {field: 'image', title: '缩略图', align: "center",templet: '#image'}
                        , {field: 'state', title: '状态', align: "center", templet: function (d) {
                                if (d.state == 1) {
                                    return "上线";
                                } else if (d.state == 2) {
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
                        url: '/Message/findByWords',
                        type: "post",
                        data: $("#searchForm").serialize(),
                        // dataType:'JSON',
                        success: function (r) {
                            layui.use('table', function () {  // 引入 table模块
                                var table = layui.table;
                                var data = r.data;
                                table.render({
                                    elem: '#friends_table'//指定表格元素
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
                                        , {field: 'title', title: '文章标题', align: "center"}
                                        , {field: 'position', title: '位置', align: "center", templet: function (d) {
                                                if (d.position == 1) {
                                                    return "发现-活动";
                                                } else if (d.position == 2){
                                                    return "消息-活动";
                                                }
                                            }}
                                        , {field: 'url', title: '链接', align: "center"}
                                        , {field: 'time', title: '发布时间', align: "center"}
                                        , {field: 'editor', title: '发布人', align: "center"}
                                        , {field: 'image', title: '缩略图', align: "center",templet: '#image'}
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
                table.on('tool(friends_table)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
                    var data = obj.data; //获得当前行数据
                    var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
                    var tr = obj.tr; //获得当前行 tr 的DOM对象
                    var id = data.id;
                    <!--操作状态-->
                    if(layEvent === 'on_offline'){ //上线
                        if (data.state==1){//下线操作
                            alert('ID:'+data.id+'的下线操作');
                            window.location.href="/Message/offline?id="+data.id;
                        }
                        if (data.state==2){
                            alert('ID:'+data.id+'的上线操作');
                            window.location.href="/Message/online?id="+data.id;
                        }
                    }else if (layEvent === 'editing'){//编辑
                        $("#update_div").show();
                        alert('ID:'+data.id+'的编辑操作');
                        $("#update_id").val(data.id);
                        $("#update_title").val(data.title);
                        $("#update_position").val(data.position);
                        $("#update_url").val(data.url);

                        $("#update_content").val(data.content);
                        $("#update_state").val(data.state);
                        $("#update_editor").val(data.editor);
                        //layui中需要使用layui.form.render()才可以实现回显下拉框
                        layui.form.render();

                        layui.use('layedit', function(){
                            var layedit  = layui.layedit;
                            var newIndex = layedit.build('update_content',{});
                            layedit.setContent(newIndex,data.content);
                            layedit.getText(newIndex);
                        })
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
                    layer.confirm('确定删除id为:'+ids+"这"+data.length+"条数据吗？", {
                        btn: ['确定', '取消'] //可以无限个按钮
                    }, function(index, layero){
                        layer.msg('删除成功', {icon: 1});
                        window.location.href="/Message/delete?id="+ids;
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


<%--富文本编辑--%>
<script>
    $=layui.jquery;
    layui.use('layedit', function(){
        var layedit  = layui.layedit;
        // //插入图片接口
        layedit.set({
            uploadImage: {
                url: '/Message/upload' //接口url
                ,type: 'post' //默认post
            }
        });
        // //注意：layedit.set 一定要放在 build 前面，否则配置全局接口将无效。
        var index = layedit.build('content',{
            height: 180, //设置编辑器高度
            width:100
        }); //建立编辑器
        // var layedit2 = layui.layedit;
        layedit.getText(index);
        //建立编辑器2
        // var newIndex = layedit2.build('update_content',{});

        // layedit2.setContent(newIndex,content1);
        // layedit2.getText(newIndex);
    });
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
    <div>无图片</div>
    {{#  } else { }}
    <%--<div><img src="/friends/{{ d.image }}"></div>--%>
    <div><img src="{{ d.image }}"></div>
    {{#  } }}
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

    /*table底层分页框*/
    .layui-table-page{text-align: center;}

    /*layedit大小*/
    .layui-layedit{
        width: 400px;
        height: 300px
    }

    .insert_content{
        width: 600px;
        height: 500px;
    }
    .update_content{
        width: 600px;
        height: 500px;
    }


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