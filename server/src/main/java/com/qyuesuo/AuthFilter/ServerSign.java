package com.qyuesuo.AuthFilter;

import com.qyuesuo.utils.RSAKeyPair;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;


@WebFilter("/*")
public class ServerSign implements Filter {

    private static Logger logger = Logger.getLogger(ServerSign.class);
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("权限校验===================》");
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        boolean result =false;
        //校验
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String sid = request.getHeader("X-SID");
        String sign = request.getHeader("X-Signature");
        logger.info("【权限校验】 sid=======================+》"+sid);
        logger.info("【权限校验】 sign=======================》"+sign);
        // RSA验签
        //判断是否有头信息
        if (sid == null || sign == null || sid.length() <= 0 || sign.length() <= 0) {
            logger.info("请求头中没有包含签名信息");
            response.setStatus(403);
            return;
        }

        //解密对比签名
        try {
            if (RSAKeyPair.verify(Base64.getUrlDecoder().decode(sid), Base64.getUrlDecoder().decode(sign))){
                filterChain.doFilter(request, response);
            }else{
                logger.info("签名信息验证出错");
                response.setStatus(403);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            response.setStatus(403);
            return;
        }
    }

    public void destroy() {

    }
}
