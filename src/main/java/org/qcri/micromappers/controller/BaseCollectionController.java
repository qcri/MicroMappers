package org.qcri.micromappers.controller;

import org.apache.log4j.Logger;
import org.qcri.micromappers.utility.ResponseWrapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

public abstract class BaseCollectionController {

	protected static Logger logger = Logger.getLogger(BaseCollectionController.class);

	@RequestMapping(value = "/start", method=RequestMethod.GET)
	@ResponseBody
	protected abstract ResponseWrapper start(@RequestParam Long id);

	@RequestMapping(value = "/stop", method=RequestMethod.GET)
	@ResponseBody
	protected abstract ResponseWrapper stop(@RequestParam  Long id) ;

	@RequestMapping(value = "/restart", method=RequestMethod.GET)
	@ResponseBody
	protected abstract ResponseWrapper restart(@RequestParam Long id);
	
	@RequestMapping(value = "/status", method=RequestMethod.GET)
	@ResponseBody
	protected abstract ResponseWrapper getStatus(@RequestParam Long id);
	
	/** Returning the collection count from db.
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/count", method=RequestMethod.GET)
	@ResponseBody
	public abstract ResponseWrapper getCount(@RequestParam("id") Long id);
}
