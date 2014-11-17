/**
 * Sencha GXT 3.1.1 - Sencha for GWT
 * Copyright(c) 2007-2014, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.vw.ide.client.fringemanagment.model;

import java.util.List;

@SuppressWarnings("serial")
public class FolderDto extends BaseDto {

  private List<BaseDto> children;

  protected FolderDto() {

  }

  public FolderDto(Integer id, String name) {
    super(id, name);
  }

  public List<BaseDto> getChildren() {
    return children;
  }

  public void setChildren(List<BaseDto> children) {
    this.children = children;
  }

  public void addChild(BaseDto child) {
    getChildren().add(child);
  }
}
