package com.vw.ide.shared.servlet.fringes;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.vw.ide.shared.servlet.fringes.model.Category;
import com.vw.ide.shared.servlet.fringes.model.Fringe;

/**
 * Implemented by Remote fringes servlet
 * @author OMelnyk
 *
 */
@RemoteServiceRelativePath("fringes")
public interface RemoteFringeService extends RemoteService {
	/**
	 * Returns list of all categories
	 */
	public RequestGetCategoriesResult getCategories();
	/**
	 * Creates new category
	 */
	public RequestAddCategoryResult addCategory(Category category);
	/**
	 * Updates category
	 */
	public RequestUpdateCategoryResult updateCategory(Category category);
	/**
	 * Deletes  category
	 */
	public RequestDeleteCategoryResult deleteCategory(Integer fringeId);
	/**
	 * Returns list of all fringes
	 */
	public RequestGetFringesResult getFringes();
	/**
	 * Creates new fringe
	 */	
	public RequestAddFringeResult addFringe(Fringe fringe);
	/**
	 * Updates fringe
	 */	
	public RequestUpdateFringeResult updateFringe(Fringe fringe);
	/**
	 * Deletes fringe
	 */	
	public RequestDeleteFringeResult deleteFringe(Integer fringeId);

	/**
	 * Get fringes grouped by categories
	 */		
	public RequestGetFringesInCategoriesResult getFringesInCategories();

	
}
