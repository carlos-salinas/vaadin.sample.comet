/*
 * Copyright 2009 IT Mill Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.osgi.spring;

import com.vaadin.Application;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class MyVaadinApplication extends Application {

	private Window window;


	public void init() {

		window = new Window("My Comet Vaadin Application");

		setMainWindow(window);
		
	}

	public class BackgroundThread extends Thread {

		@Override
		public void run() {
			// Simulate background work
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}

			// Update UI
			synchronized (MyVaadinApplication.this) {
				getMainWindow().addComponent(
						new Label("This label was pushed to client"));

			}

			// Push the changes
			// MyVaadinApplication.this.push.push();
		}
	}

}
