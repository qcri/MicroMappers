package org.qcri.micromappers.interceptor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.qcri.micromappers.service.BaseCollectionService;
import org.qcri.micromappers.utility.ResponseCode;
import org.qcri.micromappers.utility.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Intercepts whether the requestor is collaborator or having role = admin to the requested collection or not.
 * @author Kushal
 *
 */
public class CollectionCollaboratorInterceptor implements HandlerInterceptor {

	@Autowired
	BaseCollectionService baseCollectionService;
	
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
    	Long collectionId = Long.parseLong(request.getParameter("id"));
        
        Boolean permitted = baseCollectionService.isCurrentUserPermitted(collectionId);
		if(permitted == null || !permitted){
			setResponse(response);
			return false;
		}
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {

    }
    
    private void setResponse(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        ResponseWrapper responseWrapper = new ResponseWrapper(null, false, ResponseCode.FAILED.toString(), "Current user is not permitted to perform this task.");
        PrintWriter writer = response.getWriter();
        writer.print((new ObjectMapper()).writeValueAsString(responseWrapper));
        writer.flush();
    }

}
