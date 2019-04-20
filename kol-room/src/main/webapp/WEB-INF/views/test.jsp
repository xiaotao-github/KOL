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
<form id="insert_form" class="layui-form">
id<input type="text" name="id">
昵称<input type="text" name="username">
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
<input type="button" id="button" value="提交">
</form>
</body>
<script>

    layui.use('form', function() {
        var form = layui.form();
    })
    $('#button').click(function () {
        $.ajax({
            url: '/User/doInsert',
            type: "post",
            //设置请求头，否则出现400错误
            // contentType: "application/json;charset=utf-8",//这个参数也是header参数
            data: $("#insert_form").serialize(),
            success:function (r) {
                alert("添加成功")
            },
            error:function () {
                alert("失败")
            }
        })
    })
</script>
</html>