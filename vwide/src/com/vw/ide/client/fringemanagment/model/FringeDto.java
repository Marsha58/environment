/**
 * Sencha GXT 3.1.1 - Sencha for GWT
 * Copyright(c) 2007-2014, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.vw.ide.client.fringemanagment.model;

import com.vw.ide.shared.servlet.fringes.model.Fringe;


@SuppressWarnings("serial")
public class FringeDto extends BaseDto {

  private Fringe fringe;
  private String category;


  protected FringeDto() {
  }

  public FringeDto(Integer id, Fringe fringe, String category) {
    super(id, fringe.getName() + " : " + fringe.getClassname());
    this.fringe = fringe;
    this.category = category;
    setType("fringe");
  }

  public Fringe getFringe() {
    return fringe;
  }

  public void setFringe(Fringe fringe) {
    this.fringe = fringe;
  }

  public String getCategory() {
	  return category;
  }
  
  public void setCategory(String category) {
	  this.category = category;
  }
}

