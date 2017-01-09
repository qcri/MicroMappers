package org.qcri.micromappers.controller;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.GlideMaster;
import org.qcri.micromappers.models.GdeltMaster;
import org.qcri.micromappers.entity.GlobalEventDefinition;
import org.qcri.micromappers.models.PageInfo;
import org.qcri.micromappers.service.GlideMasterService;
import org.qcri.micromappers.service.GlobalEventDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jlucas on 12/21/16.
 */
@Controller
@RequestMapping("global/events/")
public class GlobalEventController {
    private static Logger logger = Logger.getLogger(GlobalEventController.class);

    @Autowired
    GlobalEventDefinitionService globalEventDefinitionService;

    @Autowired
    GlideMasterService glideMasterService;

    @RequestMapping(value={"","/","snopes"})
    public String index(Model model, HttpServletRequest request,
    		@RequestParam(value = "page", defaultValue = "1") String page) {
    	
        int pageNumber = Integer.valueOf(page);
        Page<GlobalEventDefinition> pages =  globalEventDefinitionService.listAllByPage(pageNumber);

        PageInfo<GlobalEventDefinition> pageInfo = new PageInfo<>(pages);
        pageInfo.setList(pages.getContent());


        model.addAttribute("page", pageInfo);
        return "snopes";
    }

    @RequestMapping(value={"/glides"})
    public String glides(Model model, HttpServletRequest request,
                        @RequestParam(value = "page", defaultValue = "1") String page) {

        int pageNumber = Integer.valueOf(page);
        Page<GlideMaster> pages =  glideMasterService.listAllByPage(pageNumber);

        PageInfo<GlideMaster> pageInfo = new PageInfo<>(pages);
        pageInfo.setList(pages.getContent());


        model.addAttribute("page", pageInfo);
        return "glides";
    }

}
