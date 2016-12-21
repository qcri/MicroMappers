package org.qcri.micromappers.service;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.GlobalEventDefinition;
import org.qcri.micromappers.repository.GlobalEventDefinitionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by jlucas on 12/11/16.
 */
@Service
public class GlobalEventDefinitionService {
    private static Logger logger = Logger.getLogger(GlobalEventDefinitionService.class);
    private static final int PAGE_SIZE = 10;

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
                new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.DESC, "createdAt");

        return globalEventDefinitionRepository.findAll(request);
    }
}
