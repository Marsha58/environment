package com.vw.ide.shared.servlet.fringes;

import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.vw.ide.server.servlet.fringes.persistance.dao.ItemDAO;

public class ItemCache<T> {
	
	private Logger logger = Logger.getLogger(ItemCache.class);
	
	private LoadingCache<Integer, T> cache;
	private ItemDAO<T> itemDAO;
	private String allItemsMapCheckSum = "";
	
	public ItemCache(ItemDAO<T> itemDAO) {
		this.itemDAO = (ItemDAO<T>) itemDAO;
		initCache();
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
	
	
	public  ConcurrentMap<Integer, T> getAll() {
		Long timeBefore = System.currentTimeMillis();
		String newCheckSum = itemDAO.getHash();
		Long timeAfter = System.currentTimeMillis();
		logger.debug("timing itemDAO.getHash() : " + (timeAfter - timeBefore));
//		logger.debug("allItemsMapCheckSum: " + allItemsMapCheckSum);
//		logger.debug("newCheckSum: " + newCheckSum);
		if (!allItemsMapCheckSum.equalsIgnoreCase(newCheckSum)) {
			allItemsMapCheckSum = newCheckSum;
			timeBefore = System.currentTimeMillis();
			cache.cleanUp();
			cache.putAll(itemDAO.getAllMap());
			timeAfter = System.currentTimeMillis();
			logger.debug("timing catch refilling: " + (timeAfter - timeBefore));
			
		} else {
			logger.debug("cache isn't touched");			
		}
		return cache.asMap();
	}
	



	
		

}
