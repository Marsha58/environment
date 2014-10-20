/**
 * Sencha GXT 3.1.1 - Sencha for GWT
 * Copyright(c) 2007-2014, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.vw.ide.client.model;

import java.util.List;

@SuppressWarnings("serial")
public class FolderDto extends BaseDto {

  private List<BaseDto> children;

  protected FolderDto() {
	     setType("dir");
  }

  public FolderDto(Integer id, String name, String relPath, String absolutePath) {
    super(id, name);
    setType("dir");
    setRelPath(relPath);
    setAbsolutePath(absolutePath);
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
  
  public void addOrReplaceChild(BaseDto child) {
	  List<BaseDto> lst = getChildren();
	    for(int i = 0; i < lst.size(); i++) {
	    	if ((lst.get(i).getName().equalsIgnoreCase(child.getName()))&&
	    			(lst.get(i).getRelPath().equalsIgnoreCase(child.getRelPath()))) {
	    		lst.remove(i);
	    	}
	    }
	    lst.add(child);
	  }
  
}
