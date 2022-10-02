package com.hotdas.travel.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotdas.travel.domain.ResultInfo;
import com.hotdas.travel.domain.User;
import com.hotdas.travel.service.UserService;
import com.hotdas.travel.service.impl.UserServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @program: IntelliJ IDEA
 * @description:
 * @author:
 * @create: 2022-10-01 22:04
 */
@WebServlet("/registerUserServlet")
public class RegisterUserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String check = request.getParameter("check");
        //从session中获取验证码
        String checkcode_server = (String) request.getSession().getAttribute("CHECKCODE_SERVER");
        //删除session
        request.getSession().removeAttribute("CHECKCODE_SERVER");
        if (checkcode_server!=null && !checkcode_server.equalsIgnoreCase(check)){
            //验证码有误 封装返回的数据对象
            ResultInfo info = new ResultInfo();
            //注册失败
            info.setErrorMsg("验证码有误!");
            info.setFlag(false);
            ObjectMapper objectMapper = new ObjectMapper();
            String valueAsString = objectMapper.writeValueAsString(info);
            //设置编码和类型
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(valueAsString);
            return;
        }
        //获取所有表单的数据
        Map<String, String[]> parameterMap = request.getParameterMap();
        //封装user对象
        User user = new User();
        try {
            BeanUtils.populate(user,parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //调用service
        UserService userService = new UserServiceImpl();
        Boolean flag =  userService.regirst(user);
        //验证码有误 封装返回的数据对象
        ResultInfo info = new ResultInfo();
        if (flag){
            //注册用户成功
            info.setFlag(true);
        }else{
            //注册用户失败
            //注册失败
            info.setErrorMsg("注册失败!");
            info.setFlag(false);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(info);
        //设置编码和类型
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(valueAsString);

    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }


}
