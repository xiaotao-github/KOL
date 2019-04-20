package com.kol_friends.comment;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EncodingFilter implements Filter {
    //存储编码格式信息
    private String encode = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 将请求和响应强制转换成Http形式
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) request;
         /*
          * 判断在web.xml文件中是否配置了编码格式的信息
          * 如果为空，则设置编码格式为配置文件中的编码格式
          * 否则编码格式设置为utf-8
          */
        if(this.encode != null && !this.encode.equals("")){
        request.setCharacterEncoding(this.encode);
        response.setCharacterEncoding(this.encode);
        }else{
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        }
        /*
             * 使用doFilter方法调用链中的下一个过滤器或目标资源（servlet或JSP页面）。
            * chain.doFilter处理过滤器的其余部分（如果有的话），最终处理请求的servlet或JSP页面。
          */
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }
}
