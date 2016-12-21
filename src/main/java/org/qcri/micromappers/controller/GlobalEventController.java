package org.qcri.micromappers.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.GlobalEventDefinition;
import org.qcri.micromappers.models.PageInfo;
import org.qcri.micromappers.service.GlobalEventDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(value={"","/","snopes"})
    public String index(Model model, HttpServletRequest request) {
        String page = request.getParameter("page");
        page = StringUtils.defaultIfBlank(page, "1");
        int pageNumber = Integer.valueOf(page);

        Page<GlobalEventDefinition> pages =  globalEventDefinitionService.listAllByPage(pageNumber);


        int total = (int)pages.getTotalElements();
        PageInfo<GlobalEventDefinition> pageInfo = new PageInfo<>(pages);
        pageInfo.setList(pages.getContent());


        model.addAttribute("page", pageInfo);
        return "snopes";
    }

}
