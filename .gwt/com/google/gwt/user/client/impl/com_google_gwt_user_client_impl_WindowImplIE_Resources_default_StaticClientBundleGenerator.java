package com.google.gwt.user.client.impl;

import com.google.gwt.resources.client.ResourcePrototype;
import com.google.gwt.core.client.GWT;

public class com_google_gwt_user_client_impl_WindowImplIE_Resources_default_StaticClientBundleGenerator implements com.google.gwt.user.client.impl.WindowImplIE.Resources {
  public com.google.gwt.resources.client.TextResource initWindowCloseHandler() {
    if (initWindowCloseHandler == null) {
      initWindowCloseHandler = new com.google.gwt.resources.client.TextResource() {
        // jar:file:/C:/eclipsehome/3.5/eclipse/plugins/com.google.gwt.eclipse.sdkbundle.2.0.3_2.0.3.v201002191036/gwt-2.0.3/gwt-user.jar!/com/google/gwt/user/client/impl/initWindowCloseHandler.js
        public String getText() {
          return "function __gwt_initWindowCloseHandler(beforeunload, unload) {\r\n  var wnd = window\r\n  , oldOnBeforeUnload = wnd.onbeforeunload\r\n  , oldOnUnload = wnd.onunload;\r\n  \r\n  wnd.onbeforeunload = function(evt) {\r\n    var ret, oldRet;\r\n    try {\r\n      ret = beforeunload();\r\n    } finally {\r\n      oldRet = oldOnBeforeUnload && oldOnBeforeUnload(evt);\r\n    }\r\n    // Avoid returning null as IE6 will coerce it into a string.\r\n    // Ensure that \"\" gets returned properly.\r\n    if (ret != null) {\r\n      return ret;\r\n    }\r\n    if (oldRet != null) {\r\n      return oldRet;\r\n    }\r\n    // returns undefined.\r\n  };\r\n  \r\n  wnd.onunload = function(evt) {\r\n    try {\r\n      unload();\r\n    } finally {\r\n      oldOnUnload && oldOnUnload(evt);\r\n      wnd.onresize = null;\r\n      wnd.onscroll = null;\r\n      wnd.onbeforeunload = null;\r\n      wnd.onunload = null;\r\n    }\r\n  };\r\n  \r\n  // Remove the reference once we've initialize the handler\r\n  wnd.__gwt_initWindowCloseHandler = undefined;\r\n}\r\n";
        }
        public String getName() {
          return "initWindowCloseHandler";
        }
      }
      ;
    }
    return initWindowCloseHandler;
  }
  public com.google.gwt.resources.client.TextResource initWindowResizeHandler() {
    if (initWindowResizeHandler == null) {
      initWindowResizeHandler = new com.google.gwt.resources.client.TextResource() {
        // jar:file:/C:/eclipsehome/3.5/eclipse/plugins/com.google.gwt.eclipse.sdkbundle.2.0.3_2.0.3.v201002191036/gwt-2.0.3/gwt-user.jar!/com/google/gwt/user/client/impl/initWindowResizeHandler.js
        public String getText() {
          return "function __gwt_initWindowResizeHandler(resize) {\r\n  var wnd = window, oldOnResize = wnd.onresize;\r\n  \r\n  wnd.onresize = function(evt) {\r\n    try {\r\n      resize();\r\n    } finally {\r\n      oldOnResize && oldOnResize(evt);\r\n    }\r\n  };\r\n  \r\n  // Remove the reference once we've initialize the handler\r\n  wnd.__gwt_initWindowResizeHandler = undefined;\r\n}\r\n";
        }
        public String getName() {
          return "initWindowResizeHandler";
        }
      }
      ;
    }
    return initWindowResizeHandler;
  }
  public com.google.gwt.resources.client.TextResource initWindowScrollHandler() {
    if (initWindowScrollHandler == null) {
      initWindowScrollHandler = new com.google.gwt.resources.client.TextResource() {
        // jar:file:/C:/eclipsehome/3.5/eclipse/plugins/com.google.gwt.eclipse.sdkbundle.2.0.3_2.0.3.v201002191036/gwt-2.0.3/gwt-user.jar!/com/google/gwt/user/client/impl/initWindowScrollHandler.js
        public String getText() {
          return "function __gwt_initWindowScrollHandler(scroll) {\r\n  var wnd = window, oldOnScroll = wnd.onscroll;\r\n  \r\n  wnd.onscroll = function(evt) {\r\n    try {\r\n      scroll();\r\n    } finally {\r\n      oldOnScroll && oldOnScroll(evt);\r\n    }\r\n  };\r\n  \r\n  // Remove the reference once we've initialize the handler\r\n  wnd.__gwt_initWindowScrollHandler = undefined;\r\n}\r\n";
        }
        public String getName() {
          return "initWindowScrollHandler";
        }
      }
      ;
    }
    return initWindowScrollHandler;
  }
  private static com.google.gwt.resources.client.TextResource initWindowCloseHandler;
  private static com.google.gwt.resources.client.TextResource initWindowResizeHandler;
  private static com.google.gwt.resources.client.TextResource initWindowScrollHandler;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      initWindowCloseHandler(), 
      initWindowResizeHandler(), 
      initWindowScrollHandler(), 
    };
  }
  public native ResourcePrototype getResource(String name) /*-{
    switch (name) {
      case 'initWindowCloseHandler': return this.@com.google.gwt.user.client.impl.WindowImplIE.Resources::initWindowCloseHandler()();
      case 'initWindowResizeHandler': return this.@com.google.gwt.user.client.impl.WindowImplIE.Resources::initWindowResizeHandler()();
      case 'initWindowScrollHandler': return this.@com.google.gwt.user.client.impl.WindowImplIE.Resources::initWindowScrollHandler()();
    }
    return null;
  }-*/;
}
