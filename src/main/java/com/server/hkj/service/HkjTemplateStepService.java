package com.server.hkj.service;

import com.server.hkj.domain.HkjTemplateStep;
import com.server.hkj.repository.HkjTemplateStepRepository;
import com.server.hkj.service.dto.HkjTemplateStepDTO;
import com.server.hkj.service.mapper.HkjTemplateStepMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.server.hkj.domain.HkjTemplateStep}.
 */
@Service
@Transactional
public class HkjTemplateStepService {

    private static final Logger log = LoggerFactory.getLogger(HkjTemplateStepService.class);

    private final HkjTemplateStepRepository hkjTemplateStepRepository;

    private final HkjTemplateStepMapper hkjTemplateStepMapper;

    public HkjTemplateStepService(HkjTemplateStepRepository hkjTemplateStepRepository, HkjTemplateStepMapper hkjTemplateStepMapper) {
        this.hkjTemplateStepRepository = hkjTemplateStepRepository;
        this.hkjTemplateStepMapper = hkjTemplateStepMapper;
    }

    /**
     * Save a hkjTemplateStep.
     *
     * @param hkjTemplateStepDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjTemplateStepDTO save(HkjTemplateStepDTO hkjTemplateStepDTO) {
        log.debug("Request to save HkjTemplateStep : {}", hkjTemplateStepDTO);
        HkjTemplateStep hkjTemplateStep = hkjTemplateStepMapper.toEntity(hkjTemplateStepDTO);
        hkjTemplateStep = hkjTemplateStepRepository.save(hkjTemplateStep);
        return hkjTemplateStepMapper.toDto(hkjTemplateStep);
    }

    /**
     * Update a hkjTemplateStep.
     *
     * @param hkjTemplateStepDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjTemplateStepDTO update(HkjTemplateStepDTO hkjTemplateStepDTO) {
        log.debug("Request to update HkjTemplateStep : {}", hkjTemplateStepDTO);
        HkjTemplateStep hkjTemplateStep = hkjTemplateStepMapper.toEntity(hkjTemplateStepDTO);
        hkjTemplateStep.setIsPersisted();
        hkjTemplateStep = hkjTemplateStepRepository.save(hkjTemplateStep);
        return hkjTemplateStepMapper.toDto(hkjTemplateStep);
    }

    /**
     * Partially update a hkjTemplateStep.
     *
     * @param hkjTemplateStepDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HkjTemplateStepDTO> partialUpdate(HkjTemplateStepDTO hkjTemplateStepDTO) {
        log.debug("Request to partially update HkjTemplateStep : {}", hkjTemplateStepDTO);

        return hkjTemplateStepRepository
            .findById(hkjTemplateStepDTO.getId())
            .map(existingHkjTemplateStep -> {
                hkjTemplateStepMapper.partialUpdate(existingHkjTemplateStep, hkjTemplateStepDTO);

                return existingHkjTemplateStep;
            })
            .map(hkjTemplateStepRepository::save)
            .map(hkjTemplateStepMapper::toDto);
    }

    /**
     *  Get all the hkjTemplateSteps where HkjTask is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<HkjTemplateStepDTO> findAllWhereHkjTaskIsNull() {
        log.debug("Request to get all hkjTemplateSteps where HkjTask is null");
        return StreamSupport.stream(hkjTemplateStepRepository.findAll().spliterator(), false)
            .filter(hkjTemplateStep -> hkjTemplateStep.getHkjTask() == null)
            .map(hkjTemplateStepMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one hkjTemplateStep by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HkjTemplateStepDTO> findOne(Long id) {
        log.debug("Request to get HkjTemplateStep : {}", id);
        return hkjTemplateStepRepository.findById(id).map(hkjTemplateStepMapper::toDto);
    }

    /**
     * Delete the hkjTemplateStep by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete HkjTemplateStep : {}", id);
        hkjTemplateStepRepository.deleteById(id);
    }
}
