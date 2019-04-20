<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<head>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/css/style.css">
    <script type="text/javascript" src="<%=basePath%>/js/jquery.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/js/vector.js"></script>
</head>
<body>
    <div id="container">
        <div id="output">
            <div class="containerT">
                <h1>用户登录</h1>
                <form class="form" id="entry_form" action="/User/login" method="post">
                    <input type="text" placeholder="用户名" id="entry_name" value="admin">
                    <input type="password" placeholder="密码" id="entry_password">
                    <button type="button" id="entry_btn">登录</button>
                    <div id="prompt" class="prompt"></div>
                </form>
            </div>
        </div>
    </div>

    <script type="text/javascript">
        $(function(){
            Victor("container", "output");   //登录背景函数
            $("#entry_name").focus();
            $(document).keydown(function(event){
                if(event.keyCode==13){
                    $("#entry_btn").click();
                }
            });
        });
    </script>
</body>
</html>