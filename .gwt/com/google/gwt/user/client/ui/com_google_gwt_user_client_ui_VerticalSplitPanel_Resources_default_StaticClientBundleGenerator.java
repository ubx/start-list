package com.google.gwt.user.client.ui;

import com.google.gwt.resources.client.ResourcePrototype;
import com.google.gwt.core.client.GWT;

public class com_google_gwt_user_client_ui_VerticalSplitPanel_Resources_default_StaticClientBundleGenerator implements com.google.gwt.user.client.ui.VerticalSplitPanel.Resources {
  public com.google.gwt.resources.client.ImageResource verticalSplitPanelThumb() {
    if (verticalSplitPanelThumb == null) {
      verticalSplitPanelThumb = new com.google.gwt.resources.client.impl.ImageResourcePrototype(
        "verticalSplitPanelThumb",
        internedUrl0,
        0, 0, 7, 7, false, false
      );
    }
    return verticalSplitPanelThumb;
  }
  private static final java.lang.String internedUrl0 = GWT.getModuleBaseURL() + "8603379B5088782D2C0620FAE856E112.cache.png";
  private static com.google.gwt.resources.client.ImageResource verticalSplitPanelThumb;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      verticalSplitPanelThumb(), 
    };
  }
  public native ResourcePrototype getResource(String name) /*-{
    switch (name) {
      case 'verticalSplitPanelThumb': return this.@com.google.gwt.user.client.ui.VerticalSplitPanel.Resources::verticalSplitPanelThumb()();
    }
    return null;
  }-*/;
}
