package com.google.gwt.i18n.client.impl;

import com.google.gwt.i18n.client.constants.NumberConstantsImpl;
import com.google.gwt.i18n.client.constants.NumberConstants;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.constants.DateTimeConstants;
import com.google.gwt.i18n.client.constants.DateTimeConstantsImpl;

public class LocaleInfoImpl_ extends LocaleInfoImpl_shared {
  @Override
  public String getLocaleName() {
    return "default";
  }
  
  @Override
  public DateTimeConstants getDateTimeConstants() {
    return GWT.create(com.google.gwt.i18n.client.constants.DateTimeConstantsImpl.class);
  }
  
  @Override
  public NumberConstants getNumberConstants() {
    return GWT.create(com.google.gwt.i18n.client.constants.NumberConstantsImpl.class);
  }
}
