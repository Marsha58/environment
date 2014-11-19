package com.vw.ide.shared.servlet.fringes;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.vw.ide.server.servlet.fringes.persistance.dao.ItemDAO;

public class ItemCache<T> {
	
	private LoadingCache<Integer, T> cache;
	private ItemDAO<T> itemDAO;
	
	public ItemCache(ItemDAO<T> itemDAO) {
		this.itemDAO = (ItemDAO<T>) itemDAO;
	}
	
	public void initCache() {
		cache = (LoadingCache<Integer, T>) CacheBuilder.newBuilder().build(new CacheLoader<Integer, T>() {
			@Override
			public T load(Integer key) throws Exception {
				return (T) itemDAO.findById(key);
			}
		});
	}

	public T getItem(Integer key) {
		return cache.getUnchecked(key);
	}
	
	
	public void loadAll() {
	   cache.putAll(itemDAO.getAllMap());	
	}



	
		

}
