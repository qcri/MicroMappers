package org.qcri.micromappers.controller;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.GlideMaster;
import org.qcri.micromappers.models.GlobalDataSources;
import org.qcri.micromappers.models.PageInfo;
import org.qcri.micromappers.service.GlobalDataSourcesService;
import org.qcri.micromappers.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jlucas on 1/17/17.
 */
@Controller
@RequestMapping("dashboard")
public class DashboardController {
    private static Logger logger = Logger.getLogger(DashboardController.class);

    @Autowired
    GlobalDataSourcesService globalDataSourcesService;

    @RequestMapping(value={"/global"})
    public String glides(Model model, HttpServletRequest request,
                         @RequestParam(value = "page", defaultValue = "1") String page) {

        int pageNumber = Integer.valueOf(page);

        List<GlobalDataSources> globalDataSourcesList = globalDataSourcesService.findAll();

        PageRequest pageRequestreq =
                new PageRequest(pageNumber - 1, Constants.DEFAULT_PAGE_SIZE, Sort.Direction.DESC, "createdAt");

        int max = (Constants.DEFAULT_PAGE_SIZE*(pageNumber+1)>globalDataSourcesList.size())?
                globalDataSourcesList.size(): Constants.DEFAULT_PAGE_SIZE*(pageNumber+1);

   //     Page<GlobalDataSources> pages0 =
     //           new PageImpl<GlobalDataSources>(globalDataSourcesList, pageRequestreq, globalDataSourcesList.size());

//impiantos.subList(page*count, max)
        int i = (pageNumber -1) * Constants.DEFAULT_PAGE_SIZE;
        int j = i + (Constants.DEFAULT_PAGE_SIZE-1);
        if(j >= globalDataSourcesList.size()){
            j = globalDataSourcesList.size() - 1;
        }

        System.out.println("sublist : "  + i +" - "+ j);

        List<GlobalDataSources> pageDataSet = new ArrayList<GlobalDataSources>();
        for(int k= 0; k < globalDataSourcesList.size(); k++){
            if(k>=i && k <= j){
                pageDataSet.add(globalDataSourcesList.get(k));
            }
        }
       // List<GlobalDataSources> pageDataSet =  globalDataSourcesList.subList(i, j );

        Page<GlobalDataSources> pages =
                new PageImpl<GlobalDataSources>(pageDataSet, pageRequestreq, globalDataSourcesList.size());

        PageInfo<GlobalDataSources> pageInfo = new PageInfo<>(pages);
        pageInfo.setList(pages.getContent());

        model.addAttribute("page", pageInfo);
        model.addAttribute("globaldataset", globalDataSourcesList);
        return "/dashboard/global";
    }

}
