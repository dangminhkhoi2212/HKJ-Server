package com.server.hkj.service;

import com.server.hkj.domain.HkjTemplate;
import com.server.hkj.repository.HkjTemplateRepository;
import com.server.hkj.service.dto.HkjTemplateDTO;
import com.server.hkj.service.mapper.HkjTemplateMapper;
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
 * Service Implementation for managing {@link com.server.hkj.domain.HkjTemplate}.
 */
@Service
@Transactional
public class HkjTemplateService {

    private static final Logger log = LoggerFactory.getLogger(HkjTemplateService.class);

    private final HkjTemplateRepository hkjTemplateRepository;

    private final HkjTemplateMapper hkjTemplateMapper;

    public HkjTemplateService(HkjTemplateRepository hkjTemplateRepository, HkjTemplateMapper hkjTemplateMapper) {
        this.hkjTemplateRepository = hkjTemplateRepository;
        this.hkjTemplateMapper = hkjTemplateMapper;
    }

    /**
     * Save a hkjTemplate.
     *
     * @param hkjTemplateDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjTemplateDTO save(HkjTemplateDTO hkjTemplateDTO) {
        log.debug("Request to save HkjTemplate : {}", hkjTemplateDTO);
        HkjTemplate hkjTemplate = hkjTemplateMapper.toEntity(hkjTemplateDTO);
        hkjTemplate = hkjTemplateRepository.save(hkjTemplate);
        return hkjTemplateMapper.toDto(hkjTemplate);
    }

    /**
     * Update a hkjTemplate.
     *
     * @param hkjTemplateDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjTemplateDTO update(HkjTemplateDTO hkjTemplateDTO) {
        log.debug("Request to update HkjTemplate : {}", hkjTemplateDTO);
        HkjTemplate hkjTemplate = hkjTemplateMapper.toEntity(hkjTemplateDTO);
        hkjTemplate.setIsPersisted();
        hkjTemplate = hkjTemplateRepository.save(hkjTemplate);
        return hkjTemplateMapper.toDto(hkjTemplate);
    }

    /**
     * Partially update a hkjTemplate.
     *
     * @param hkjTemplateDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HkjTemplateDTO> partialUpdate(HkjTemplateDTO hkjTemplateDTO) {
        log.debug("Request to partially update HkjTemplate : {}", hkjTemplateDTO);

        return hkjTemplateRepository
            .findById(hkjTemplateDTO.getId())
            .map(existingHkjTemplate -> {
                hkjTemplateMapper.partialUpdate(existingHkjTemplate, hkjTemplateDTO);

                return existingHkjTemplate;
            })
            .map(hkjTemplateRepository::save)
            .map(hkjTemplateMapper::toDto);
    }

    /**
     *  Get all the hkjTemplates where HkjProject is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<HkjTemplateDTO> findAllWhereHkjProjectIsNull() {
        log.debug("Request to get all hkjTemplates where HkjProject is null");
        return StreamSupport.stream(hkjTemplateRepository.findAll().spliterator(), false)
            .filter(hkjTemplate -> hkjTemplate.getHkjProject() == null)
            .map(hkjTemplateMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one hkjTemplate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HkjTemplateDTO> findOne(Long id) {
        log.debug("Request to get HkjTemplate : {}", id);
        return hkjTemplateRepository.findById(id).map(hkjTemplateMapper::toDto);
    }

    /**
     * Delete the hkjTemplate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete HkjTemplate : {}", id);
        hkjTemplateRepository.deleteById(id);
    }
}
