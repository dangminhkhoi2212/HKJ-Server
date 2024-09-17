package com.server.hkj.service;

import com.server.hkj.domain.HkjProject;
import com.server.hkj.repository.HkjProjectRepository;
import com.server.hkj.service.dto.HkjProjectDTO;
import com.server.hkj.service.mapper.HkjProjectMapper;
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
 * Service Implementation for managing {@link com.server.hkj.domain.HkjProject}.
 */
@Service
@Transactional
public class HkjProjectService {

    private static final Logger log = LoggerFactory.getLogger(HkjProjectService.class);

    private final HkjProjectRepository hkjProjectRepository;

    private final HkjProjectMapper hkjProjectMapper;

    public HkjProjectService(HkjProjectRepository hkjProjectRepository, HkjProjectMapper hkjProjectMapper) {
        this.hkjProjectRepository = hkjProjectRepository;
        this.hkjProjectMapper = hkjProjectMapper;
    }

    /**
     * Save a hkjProject.
     *
     * @param hkjProjectDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjProjectDTO save(HkjProjectDTO hkjProjectDTO) {
        log.debug("Request to save HkjProject : {}", hkjProjectDTO);
        HkjProject hkjProject = hkjProjectMapper.toEntity(hkjProjectDTO);
        hkjProject = hkjProjectRepository.save(hkjProject);
        return hkjProjectMapper.toDto(hkjProject);
    }

    /**
     * Update a hkjProject.
     *
     * @param hkjProjectDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjProjectDTO update(HkjProjectDTO hkjProjectDTO) {
        log.debug("Request to update HkjProject : {}", hkjProjectDTO);
        HkjProject hkjProject = hkjProjectMapper.toEntity(hkjProjectDTO);
        hkjProject.setIsPersisted();
        hkjProject = hkjProjectRepository.save(hkjProject);
        return hkjProjectMapper.toDto(hkjProject);
    }

    /**
     * Partially update a hkjProject.
     *
     * @param hkjProjectDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HkjProjectDTO> partialUpdate(HkjProjectDTO hkjProjectDTO) {
        log.debug("Request to partially update HkjProject : {}", hkjProjectDTO);

        return hkjProjectRepository
            .findById(hkjProjectDTO.getId())
            .map(existingHkjProject -> {
                hkjProjectMapper.partialUpdate(existingHkjProject, hkjProjectDTO);

                return existingHkjProject;
            })
            .map(hkjProjectRepository::save)
            .map(hkjProjectMapper::toDto);
    }

    /**
     *  Get all the hkjProjects where HkjOrder is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<HkjProjectDTO> findAllWhereHkjOrderIsNull() {
        log.debug("Request to get all hkjProjects where HkjOrder is null");
        return StreamSupport.stream(hkjProjectRepository.findAll().spliterator(), false)
            .filter(hkjProject -> hkjProject.getHkjOrder() == null)
            .map(hkjProjectMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one hkjProject by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HkjProjectDTO> findOne(Long id) {
        log.debug("Request to get HkjProject : {}", id);
        return hkjProjectRepository.findById(id).map(hkjProjectMapper::toDto);
    }

    /**
     * Delete the hkjProject by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete HkjProject : {}", id);
        hkjProjectRepository.deleteById(id);
    }
}
