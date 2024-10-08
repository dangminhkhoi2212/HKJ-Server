package com.server.hkj.service;

import com.server.hkj.domain.HkjTemplate;
import com.server.hkj.repository.HkjTemplateRepository;
import com.server.hkj.service.dto.HkjTemplateDTO;
import com.server.hkj.service.mapper.HkjTemplateMapper;
import java.util.Optional;
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

    private static final Logger LOG = LoggerFactory.getLogger(HkjTemplateService.class);

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
        LOG.debug("Request to save HkjTemplate : {}", hkjTemplateDTO);
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
        LOG.debug("Request to update HkjTemplate : {}", hkjTemplateDTO);
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
        LOG.debug("Request to partially update HkjTemplate : {}", hkjTemplateDTO);

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
     * Get one hkjTemplate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HkjTemplateDTO> findOne(Long id) {
        LOG.debug("Request to get HkjTemplate : {}", id);
        return hkjTemplateRepository.findById(id).map(hkjTemplateMapper::toDto);
    }

    /**
     * Delete the hkjTemplate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete HkjTemplate : {}", id);
        hkjTemplateRepository.deleteById(id);
    }
}
