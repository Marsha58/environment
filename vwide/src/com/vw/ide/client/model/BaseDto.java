/**
 * Sencha GXT 3.1.1 - Sencha for GWT
 * Copyright(c) 2007-2014, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.vw.ide.client.model;

import java.io.Serializable;
import java.util.List;

import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.TreeStore.TreeNode;

public class BaseDto implements Serializable, TreeStore.TreeNode<BaseDto> {

  private Integer id;
  private String name;
  private String type;
  private String relPath;
  private String absolutePath;
  
  protected BaseDto() {
    
  }
  
  public BaseDto(Integer id, String name) {
    this.id = id;
    this.name = name;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public String getType() {
	    return type;
	  }

  public void setType(String type) {
     this.type = type;
  }
  
  public String getRelPath() {
	    return relPath;
	  }

  public void setRelPath(String relPath) {
   this.relPath = relPath;
  }

  public String getAbsolutePath() {
	    return absolutePath;
  }

  public void setAbsolutePath(String absolutePath) {
	  this.absolutePath = absolutePath;
  }  

  
  
  
  @Override
  public BaseDto getData() {
    return this;
  }

  @Override
  public List<? extends TreeNode<BaseDto>> getChildren() {
    return null;
  }
  
  @Override
  public String toString() {
    return name != null ? name : super.toString();
  }

}
