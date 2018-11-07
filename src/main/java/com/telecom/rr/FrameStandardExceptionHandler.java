package com.telecom.rr;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.telecom.rr.domain.User;
import com.telecom.rr.commons.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.telecom.rr.commons.json.Request;
import com.telecom.rr.commons.json.Response;
import com.telecom.rr.commons.json.Result;

/**
 * 全局统一异常处理
 * @author
 */
public class FrameStandardExceptionHandler extends SimpleMappingExceptionResolver {

    private static final Logger LOG = LoggerFactory.getLogger(FrameStandardExceptionHandler.class);

    // support for tomcat6
    @Deprecated
    private void handleFileTooLargeException(Exception ex, HttpServletRequest request, HttpServletResponse response) {

    }

    /**
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @return
     */
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {

        User user = null;
        String erp = "anonymous";
        if (user != null) {
            erp = user.getName();
        }
        String uri = request.getRequestURI();

        if (ex instanceof org.springframework.web.multipart.MaxUploadSizeExceededException) {
            handleFileTooLargeException(ex, request, response);
        }

        // 异步请求
        if (isResponseBody(handler) || Request.isAjax(request)) {
            return handleResponseBodyException(erp, uri, ex, request, response);
        }
        // 其他
        else {
            return handleNormalException(erp, uri, ex, request, response);
        }
    }

    private boolean isResponseBody(Object handler) {
        HandlerMethod method = (HandlerMethod) handler;
        ResponseBody isResponseBody = null;
        if (method != null) {
            isResponseBody = method.getMethodAnnotation(ResponseBody.class);
        }
        return isResponseBody != null;
    }

    /**
     * 创建日志构造对象
     */
    private Parameter newParameter(String erp, String uri, Exception ex) {
        Parameter parameter = new Parameter();
        parameter.put("erp", erp);
        parameter.put("uri", uri);
        parameter.put("exception", ex);
        return parameter;
    }

    /**
     * 处理异步请求或者返回json
     */
    private ModelAndView handleResponseBodyException(String erp, String uri, Exception ex, HttpServletRequest request,
            HttpServletResponse response) {
        // 业务异常
        if (ex instanceof FrameBSHandledException) {

            LOG.warn("{}", new Object[] { newParameter(erp, uri, ex) });
            Response.push(response, new Result(Result.ERROR, ex.getMessage()));
        }
        // 权限异常
        else if (ex instanceof FrameIllegalPrivilegeException) {

            LOG.warn("{}", new Object[] { newParameter(erp, uri, ex) });
            Response.push(response, new Result(Result.ERROR, "对不起，您没有该项的访问权限。"));
        }
        // 其他
        else {
            LOG.error("{}", new Object[] { newParameter(erp, uri, ex), ex });
            Response.push(response, new Result(Result.ERROR, "遇到了意外情况，无法完成您的请求"));
        }
        // 不返回空
        return new ModelAndView();
    }

    /**
     * 处理普通请求
     */
    private ModelAndView handleNormalException(String erp, String uri, Exception ex, HttpServletRequest request,
            HttpServletResponse response) {

        String viewName = super.determineViewName(ex, request);
        if (viewName != null) {

            if (ex != null && (ex instanceof FrameBSHandledException || ex instanceof FrameIllegalPrivilegeException)) {
                LOG.warn("{}", new Object[] { newParameter(erp, uri, ex) });
            } else {
                LOG.error("{}", new Object[] { newParameter(erp, uri, ex), ex });
            }

            Integer statusCode = super.determineStatusCode(request, viewName);
            if (statusCode != null) {
                super.applyStatusCodeIfPossible(request, response, statusCode.intValue());
            }

            ModelAndView mv = super.getModelAndView(viewName, ex, request);
            if (ex != null && (ex instanceof FrameBSHandledException)) {
                mv.addObject("message", ex.getMessage());
            }
            return mv;

        } else {

            return null;
        }
    }

}
