package com.telecom.rr;

import org.springframework.web.servlet.view.AbstractUrlBasedView;

import java.util.Map;

/**
 * 渲染页面之前放入
 * @author
 */
public class FrameVelocityLayoutViewResolver extends
        org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver {

    @Override
    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        AbstractUrlBasedView view = super.buildView(viewName);
        Map attribute = view.getAttributesMap();
        String context = getServletContext().getContextPath();
        attribute.put("context", context);
        if ("".equals(context)) {
            attribute.put("home", "/");
        } else {
            attribute.put("home", context);
        }
        return view;
    }

}
