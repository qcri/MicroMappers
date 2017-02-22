package org.qcri.micromappers.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.GlobalEventDefinition;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.repository.GlobalEventDefinitionRepository;
import org.qcri.micromappers.utility.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by jlucas on 12/11/16.
 */
@Service
public class GlobalEventDefinitionService {
    private static Logger logger = Logger.getLogger(GlobalEventDefinitionService.class);

    @Inject
    private GlobalEventDefinitionRepository globalEventDefinitionRepository;

    public GlobalEventDefinition create(GlobalEventDefinition globalEventDefinition)
    {
        return globalEventDefinitionRepository.save(globalEventDefinition);
    }

    public GlobalEventDefinition findByEventUrl(String eventUrl){
        return globalEventDefinitionRepository.findByEventUrl(eventUrl);
    }

    public Page<GlobalEventDefinition> listAllByPage(Integer pageNumber) {
        PageRequest request =
                new PageRequest(pageNumber - 1, Constants.DEFAULT_PAGE_SIZE, Sort.Direction.DESC, "createdAt");

        return globalEventDefinitionRepository.findAll(request);
    }

    public List<GlobalEventDefinition> listAllBySearchKeyword(String searchKeyword) {

        return globalEventDefinitionRepository.findAllBySearchKeyWord(searchKeyword);
    }


    public GlobalEventDefinition getById(Long id)
	{
		try{
			return globalEventDefinitionRepository.findById(id);
		}catch (Exception e) {
			logger.error("Error while fetching global event definition by id", e);
			throw new MicromappersServiceException("Error while fetching global event definition by id", e);
		}
	}

    public List<GlobalEventDefinition> findAllByState(String state){
        return globalEventDefinitionRepository.findByState(state);
    }

    public List<GlobalEventDefinition> findAllByStateAndTags(String state, String tags){
        if(!tags.contains(",")) {
            return globalEventDefinitionRepository.findByStateAndTag(state, tags);
        }

        String[] tag = tags.split(",");
        List<GlobalEventDefinition> globalEventDefinitionList = new ArrayList<GlobalEventDefinition>();
        for(int i =0; i < tag.length; i++){
            globalEventDefinitionList.addAll(globalEventDefinitionRepository.findByStateAndTag(state, tag[i]));
        }

        return globalEventDefinitionList;
    }
}
