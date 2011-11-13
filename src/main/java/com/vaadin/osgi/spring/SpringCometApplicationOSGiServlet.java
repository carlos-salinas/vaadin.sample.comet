/*
 * Copyright 2011 Thingtrack, S.L.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.osgi.spring;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.vaadin.addons.serverpush.ServerPush;
import org.vaadin.addons.serverpush.ServerPushWebApplicationContext;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.SpringApplicationOSGiServlet;
import com.vaadin.terminal.gwt.server.WebApplicationContext;

@SuppressWarnings("serial")
public class SpringCometApplicationOSGiServlet extends
		SpringApplicationOSGiServlet {
	
	
	@Override
	protected Application getNewApplication(HttpServletRequest request)
			throws ServletException {
		
		final Application application = super.getNewApplication(request);
		final String contextPath = request.getContextPath();
		
		if (application != null) {
            new Thread() {
                public void run() {
                    while (!application.isRunning()) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            // ignore
                        }
                    }
                    synchronized (application) {
                        application.getMainWindow().addComponent(new ServerPush(contextPath));
                    }
                }
            }.start();
        }
        return application;
	}
	
	
	@Override
	protected WebApplicationContext getApplicationContext(HttpSession session) {
		
		WebApplicationContext cx = (WebApplicationContext)session.getAttribute(WebApplicationContext.class.getName());
        if (cx == null) {
            cx = new ServerPushWebApplicationContext(session);
            session.setAttribute(WebApplicationContext.class.getName(), cx);
        }
        return cx;
		
	}

}
