package org.qcri.micromappers.controller;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.qcri.micromappers.entity.*;
import org.qcri.micromappers.models.GdeltMaster;
import org.qcri.micromappers.models.GlobalDataSources;
import org.qcri.micromappers.models.PageInfo;
import org.qcri.micromappers.service.*;
import org.qcri.micromappers.utility.Constants;
import org.qcri.micromappers.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

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

    @Autowired
    GdeltImageClassifierService gdeltImageClassifierService;

    @Autowired
    private Util util;


    @RequestMapping(value={"","/","snopes"})
    public String index(Model model, HttpServletRequest request) {

        List<GlobalEventDefinition> globalEventDefinitionList = globalEventDefinitionService.findAllByState(Constants.SNOPES_STATE_ACTIVE);

        Comparator<GlobalEventDefinition> globalEventComparator = (o1, o2)->o1.getCreatedAt().compareTo(o2.getCreatedAt());
        globalEventDefinitionList.sort(globalEventComparator.reversed());

        model.addAttribute("page", globalEventDefinitionList);
        return "snopes";
    }

    @RequestMapping(value={"/gdelt/glides"})
    public String glides(Model model, HttpServletRequest request) {

      //  PageInfo<GlideMaster> pageInfo = new PageInfo<>(pages);
      //  pageInfo.setList(glideMasterService.findAll());

        model.addAttribute("page", glideMasterService.findAll());

        return "/gdelt/glides";
    }

    @RequestMapping(value={"/gdelt/classifiers"})
    public String getclassifiers(Model model, HttpServletRequest request, HttpServletResponse response) {
        List<GdeltImageClassifier> gdeltImageClassifiers = gdeltImageClassifierService.getAll();

        gdeltImageClassifiers.forEach(item->{
            item.setGdeltImageClassifierProfile();
        });

        model.addAttribute("page", gdeltImageClassifiers);

        return "/gdelt/classifiers";
    }

    @RequestMapping(value={"/gdelt/request"})
    public String requestClassifiers(Model model, HttpServletRequest request, HttpServletResponse response,
                                     @RequestParam(value = "data", defaultValue = "") String data) {
        if(!data.isEmpty()){
            Account account = util.getAuthenticatedUser();
            gdeltImageClassifierService.saveOrUpdate(data, account);
            return this.getclassifiers(model, request, response);
        }
        else
            return "/gdelt/request";
    }


    @RequestMapping(value={"/gdelt/data3w"})
    public String get3WData(Model model, HttpServletRequest request,HttpServletResponse response,
                            @RequestParam(value = "glideCode") String glideCode,
                            @RequestParam(value = "dw", defaultValue = "") String dw) {

        if(dw != null){
            if(!dw.isEmpty())
            {
                this.download3WData(response, glideCode);
            }
        }

        List<Gdelt3W> gdelt3Ws = gdelt3WService.findAllByGlideCode(glideCode);
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

        model.addAttribute("page", gdelt3Ws);
        model.addAttribute("glideCode",glideCode);

        return "/gdelt/data3w";
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

        model.addAttribute("page", gdeltMMICService.findAllByGlideCode(glideCode));
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

}
