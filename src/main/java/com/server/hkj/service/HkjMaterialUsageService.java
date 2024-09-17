package com.server.hkj.service;

import com.server.hkj.domain.HkjMaterialUsage;
import com.server.hkj.repository.HkjMaterialUsageRepository;
import com.server.hkj.service.dto.HkjMaterialUsageDTO;
import com.server.hkj.service.mapper.HkjMaterialUsageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.server.hkj.domain.HkjMaterialUsage}.
 */
@Service
@Transactional
public class HkjMaterialUsageService {

    private static final Logger log = LoggerFactory.getLogger(HkjMaterialUsageService.class);

    private final HkjMaterialUsageRepository hkjMaterialUsageRepository;

    private final HkjMaterialUsageMapper hkjMaterialUsageMapper;

    public HkjMaterialUsageService(HkjMaterialUsageRepository hkjMaterialUsageRepository, HkjMaterialUsageMapper hkjMaterialUsageMapper) {
        this.hkjMaterialUsageRepository = hkjMaterialUsageRepository;
        this.hkjMaterialUsageMapper = hkjMaterialUsageMapper;
    }

    /**
     * Save a hkjMaterialUsage.
     *
     * @param hkjMaterialUsageDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjMaterialUsageDTO save(HkjMaterialUsageDTO hkjMaterialUsageDTO) {
        log.debug("Request to save HkjMaterialUsage : {}", hkjMaterialUsageDTO);
        HkjMaterialUsage hkjMaterialUsage = hkjMaterialUsageMapper.toEntity(hkjMaterialUsageDTO);
        hkjMaterialUsage = hkjMaterialUsageRepository.save(hkjMaterialUsage);
        return hkjMaterialUsageMapper.toDto(hkjMaterialUsage);
    }

    /**
     * Update a hkjMaterialUsage.
     *
     * @param hkjMaterialUsageDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjMaterialUsageDTO update(HkjMaterialUsageDTO hkjMaterialUsageDTO) {
        log.debug("Request to update HkjMaterialUsage : {}", hkjMaterialUsageDTO);
        HkjMaterialUsage hkjMaterialUsage = hkjMaterialUsageMapper.toEntity(hkjMaterialUsageDTO);
        hkjMaterialUsage.setIsPersisted();
        hkjMaterialUsage = hkjMaterialUsageRepository.save(hkjMaterialUsage);
        return hkjMaterialUsageMapper.toDto(hkjMaterialUsage);
    }

    /**
     * Partially update a hkjMaterialUsage.
     *
     * @param hkjMaterialUsageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HkjMaterialUsageDTO> partialUpdate(HkjMaterialUsageDTO hkjMaterialUsageDTO) {
        log.debug("Request to partially update HkjMaterialUsage : {}", hkjMaterialUsageDTO);

        return hkjMaterialUsageRepository
            .findById(hkjMaterialUsageDTO.getId())
            .map(existingHkjMaterialUsage -> {
                hkjMaterialUsageMapper.partialUpdate(existingHkjMaterialUsage, hkjMaterialUsageDTO);

                return existingHkjMaterialUsage;
            })
            .map(hkjMaterialUsageRepository::save)
            .map(hkjMaterialUsageMapper::toDto);
    }

    /**
     * Get one hkjMaterialUsage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HkjMaterialUsageDTO> findOne(Long id) {
        log.debug("Request to get HkjMaterialUsage : {}", id);
        return hkjMaterialUsageRepository.findById(id).map(hkjMaterialUsageMapper::toDto);
    }

    /**
     * Delete the hkjMaterialUsage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete HkjMaterialUsage : {}", id);
        hkjMaterialUsageRepository.deleteById(id);
    }
}
