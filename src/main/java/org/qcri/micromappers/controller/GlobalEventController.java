package org.qcri.micromappers.controller;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.qcri.micromappers.entity.Gdelt3W;
import org.qcri.micromappers.entity.GdeltMMIC;
import org.qcri.micromappers.entity.GlideMaster;
import org.qcri.micromappers.models.GdeltMaster;
import org.qcri.micromappers.entity.GlobalEventDefinition;
import org.qcri.micromappers.models.PageInfo;
import org.qcri.micromappers.service.Gdelt3WService;
import org.qcri.micromappers.service.GdeltMMICService;
import org.qcri.micromappers.service.GlideMasterService;
import org.qcri.micromappers.service.GlobalEventDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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

    @Autowired
    Gdelt3WService gdelt3WService;

    @Autowired
    GdeltMMICService gdeltMMICService;

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

    @RequestMapping(value={"/gdelt/data3w"})
    public String get3WData(Model model, HttpServletRequest request,HttpServletResponse response,
                         @RequestParam(value = "page", defaultValue = "1") String page,
                            @RequestParam(value = "glideCode") String glideCode,
                            @RequestParam(value = "dw", defaultValue = "") String dw) {

        if(dw != null){
            if(!dw.isEmpty())
            {
                this.download3WData(response, glideCode);
            }
        }

        int pageNumber = Integer.valueOf(page);
        Page<Gdelt3W> pages =  gdelt3WService.findbyGlideCode(pageNumber, glideCode);

        PageInfo<Gdelt3W> pageInfo = new PageInfo<>(pages);
        List<Gdelt3W> gdelt3Ws = pages.getContent();
        JSONParser parser = new JSONParser();
        gdelt3Ws.forEach((temp) -> {
            try {
                if(temp.getWheres() != null){
                    JSONArray array = (JSONArray)parser.parse(temp.getWheres());
                    temp.setJsWheres(array);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        pageInfo.setList(gdelt3Ws);


        model.addAttribute("page", pageInfo);
        model.addAttribute("glideCode",glideCode);

        return "/gdelt/data3w";
    }

    private void download3WData(HttpServletResponse response, String glideCode){
        List<Gdelt3W> gdelt3Ws = gdelt3WService.findAllByGlideCode(glideCode);

        response.setContentType("text/csv");
        String reportName = glideCode+"_3w_"+new Date().getTime()+".csv";
        response.setHeader("Content-disposition", "attachment;filename="+reportName);

        gdelt3Ws.forEach((temp) -> {
            try {
                response.getOutputStream().print(temp.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        try {
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value={"/gdelt/datammic"})
    public String getMMICData(Model model, HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(value = "page", defaultValue = "1") String page,
                              @RequestParam(value = "glideCode") String glideCode,
                              @RequestParam(value = "dw", defaultValue = "") String dw) {

        if(dw != null){
            if(!dw.isEmpty())
            {
                this.downloadMMICData(response, glideCode);
            }
        }

        int pageNumber = Integer.valueOf(page);
        Page<GdeltMMIC> pages =  gdeltMMICService.findbyGlideCode(pageNumber, glideCode);

        PageInfo<GdeltMMIC> pageInfo = new PageInfo<>(pages);
        pageInfo.setList(pages.getContent());


        model.addAttribute("page", pageInfo);
        model.addAttribute("glideCode",glideCode);

        return "/gdelt/datammic";
    }

    private void downloadMMICData(HttpServletResponse response, String glideCode){
        List<GdeltMMIC> gdeltMMICs = gdeltMMICService.findAllByGlideCode(glideCode);

        response.setContentType("text/csv");
        String reportName = glideCode+"_mmic_"+new Date().getTime()+".csv";
        response.setHeader("Content-disposition", "attachment;filename="+reportName);

        gdeltMMICs.forEach((temp) -> {
            try {
                response.getOutputStream().print(temp.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        try {
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
