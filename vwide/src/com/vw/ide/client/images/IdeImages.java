/**
 * Sencha GXT 3.1.1 - Sencha for GWT
 * Copyright(c) 2007-2014, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.vw.ide.client.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface IdeImages extends ClientBundle {

  public IdeImages INSTANCE = GWT.create(IdeImages.class);

  @Source("connect.png")
  ImageResource connect();

  @Source("user_add.png")
  ImageResource user_add();

  @Source("user_delete.png")
  ImageResource user_delete();

  @Source("add.gif")
  ImageResource add();

  @Source("delete.gif")
  ImageResource delete();

  @Source("text.png")
  ImageResource text();

  @Source("plugin.png")
  ImageResource plugin();
  
  @Source("css.png")
  ImageResource css();
  
  @Source("java.png")
  ImageResource java();
  
  @Source("text.png")
  ImageResource json();

  @Source("folder.png")
  ImageResource folder();
  
  @Source("enabled//save_edit.gif")
  ImageResource save_edit_en();
  
  @Source("disabled//save_edit.gif")
  ImageResource save_edit_dis();  
  
  @Source("enabled//saveall_edit.gif")
  ImageResource saveall_edit_en();

  @Source("disabled//saveall_edit.gif")
  ImageResource saveall_edit_dis();

  @Source("close.gif")
  ImageResource close();
  
  @Source("file_obj.gif")
  ImageResource file_obj();
  
  @Source("prj_obj.gif")
  ImageResource prj_obj();

  @Source("file_obj.gif")
  ImageResource open_en();

  @Source("file_obj.gif")
  ImageResource open_dis();
  
  @Source("enabled//search.gif")
  ImageResource search_en();
  
  @Source("enabled//terminate_obj.gif")
  ImageResource terminate_obj_en();

  @Source("disabled//terminate_co.gif")
  ImageResource terminate_obj_dis();
  
  @Source("enabled//suspend_en.gif")
  ImageResource suspend_en();

  @Source("disabled//suspend_co_dis.gif")
  ImageResource suspend_dis();

  @Source("enabled//new_con.gif")
  ImageResource new_con_en();

  @Source("disabled//new_con.gif")
  ImageResource new_con_dis();

  @Source("enabled//new_wiz.gif")
  ImageResource new_wiz_en();

  @Source("disabled//new_wiz.gif")
  ImageResource new_wiz_dis();
  
  @Source("enabled//vwml.gif")
  ImageResource vwml();
  
  @Source("java-facet.gif")
  ImageResource java_facet();

  @Source("enabled//execution_obj.gif")
  ImageResource exec_en();    

  @Source("disabled//run_exc.gif")
  ImageResource exec_dis();    
  
  @Source("enabled//delete_edit.gif")
  ImageResource delete_edit_en();

  @Source("disabled//delete_edit.gif")
  ImageResource delete_edit_dis();
  
  @Source("enabled//signed-off.png")
  ImageResource rename_file_en();  
  
}
