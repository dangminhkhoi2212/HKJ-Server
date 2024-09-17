package com.server.hkj.service;

import com.server.hkj.domain.HkjTask;
import com.server.hkj.repository.HkjTaskRepository;
import com.server.hkj.service.dto.HkjTaskDTO;
import com.server.hkj.service.mapper.HkjTaskMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.server.hkj.domain.HkjTask}.
 */
@Service
@Transactional
public class HkjTaskService {

    private static final Logger log = LoggerFactory.getLogger(HkjTaskService.class);

    private final HkjTaskRepository hkjTaskRepository;

    private final HkjTaskMapper hkjTaskMapper;

    public HkjTaskService(HkjTaskRepository hkjTaskRepository, HkjTaskMapper hkjTaskMapper) {
        this.hkjTaskRepository = hkjTaskRepository;
        this.hkjTaskMapper = hkjTaskMapper;
    }

    /**
     * Save a hkjTask.
     *
     * @param hkjTaskDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjTaskDTO save(HkjTaskDTO hkjTaskDTO) {
        log.debug("Request to save HkjTask : {}", hkjTaskDTO);
        HkjTask hkjTask = hkjTaskMapper.toEntity(hkjTaskDTO);
        hkjTask = hkjTaskRepository.save(hkjTask);
        return hkjTaskMapper.toDto(hkjTask);
    }

    /**
     * Update a hkjTask.
     *
     * @param hkjTaskDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjTaskDTO update(HkjTaskDTO hkjTaskDTO) {
        log.debug("Request to update HkjTask : {}", hkjTaskDTO);
        HkjTask hkjTask = hkjTaskMapper.toEntity(hkjTaskDTO);
        hkjTask.setIsPersisted();
        hkjTask = hkjTaskRepository.save(hkjTask);
        return hkjTaskMapper.toDto(hkjTask);
    }

    /**
     * Partially update a hkjTask.
     *
     * @param hkjTaskDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HkjTaskDTO> partialUpdate(HkjTaskDTO hkjTaskDTO) {
        log.debug("Request to partially update HkjTask : {}", hkjTaskDTO);

        return hkjTaskRepository
            .findById(hkjTaskDTO.getId())
            .map(existingHkjTask -> {
                hkjTaskMapper.partialUpdate(existingHkjTask, hkjTaskDTO);

                return existingHkjTask;
            })
            .map(hkjTaskRepository::save)
            .map(hkjTaskMapper::toDto);
    }

    /**
     * Get one hkjTask by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HkjTaskDTO> findOne(Long id) {
        log.debug("Request to get HkjTask : {}", id);
        return hkjTaskRepository.findById(id).map(hkjTaskMapper::toDto);
    }

    /**
     * Delete the hkjTask by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete HkjTask : {}", id);
        hkjTaskRepository.deleteById(id);
    }
}
