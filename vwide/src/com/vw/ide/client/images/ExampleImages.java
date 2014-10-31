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

public interface ExampleImages extends ClientBundle {

  public ExampleImages INSTANCE = GWT.create(ExampleImages.class);

  @Source("add16.gif")
  ImageResource add16();

  @Source("add24.gif")
  ImageResource add24();

  @Source("add32.gif")
  ImageResource add32();
  
  @Source("table.png")
  ImageResource table();

  @Source("application_side_list.png")
  ImageResource side_list();
  
  @Source("enabled//list.gif")
  ImageResource list_en();

  @Source("application_form.png")
  ImageResource form();

  @Source("connect.png")
  ImageResource connect();

  @Source("user_add.png")
  ImageResource user_add();

  @Source("user_delete.png")
  ImageResource user_delete();

  @Source("accordion.gif")
  ImageResource accordion();

  @Source("add.gif")
  ImageResource add();

  @Source("delete.gif")
  ImageResource delete();

  @Source("calendar.gif")
  ImageResource calendar();

  @Source("menu-show.gif")
  ImageResource menu_show();

  @Source("list-items.gif")
  ImageResource list_items();

  @Source("album.gif")
  ImageResource album();

  @Source("text.png")
  ImageResource text();

  @Source("plugin.png")
  ImageResource plugin();
  
  @Source("music.png")
  ImageResource music();
  
  
  @Source("user.png")
  ImageResource user();
  
  @Source("user_kid.png")
  ImageResource userKid();
  
  @Source("user_female.png")
  ImageResource userFemale();
  
  @Source("css.png")
  ImageResource css();
  
  @Source("java.png")
  ImageResource java();
  
  @Source("text.png")
  ImageResource json();
  
  @Source("html.png")
  ImageResource html();
  
  @Source("xml.png")
  ImageResource xml();
  
  @Source("folder.png")
  ImageResource folder();
  
  @Source("enabled//save_edit.gif")
  ImageResource save_edit_en();
  
  @Source("disabled//save_edit.gif")
  ImageResource save_edit_dis();  
  
  @Source("enabled//saveas_edit.gif")
  ImageResource saveas_edit_en();

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
  
  @Source("enabled//debug_persp.gif")
  ImageResource debug_persp_en();    

  @Source("enabled//skip_brkp.gif")
  ImageResource skip_brkp_en();    

  @Source("enabled//search.gif")
  ImageResource search_en();
  
  @Source("enabled//terminate_obj.gif")
  ImageResource terminate_obj_en();
  
  @Source("enabled//suspend_en.gif")
  ImageResource suspend_en();

  @Source("enabled//print_edit.gif")
  ImageResource print_edit_en();

  @Source("disabled//print_edit.gif")
  ImageResource print_edit_dis();
  
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

  @Source("git\\elcl16\\continue.gif")
  ImageResource continue_exec();    
  
  @Source("enabled//delete_edit.gif")
  ImageResource delete_edit_en();

  @Source("enabled//signed-off.png")
  ImageResource rename_file_en();

  @Source("enabled//copy_edit.gif")
  ImageResource copy_edit_en();  

  
}
