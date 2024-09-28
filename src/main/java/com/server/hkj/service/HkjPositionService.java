package com.server.hkj.service;

import com.server.hkj.domain.HkjPosition;
import com.server.hkj.repository.HkjPositionRepository;
import com.server.hkj.service.dto.HkjPositionDTO;
import com.server.hkj.service.mapper.HkjPositionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.server.hkj.domain.HkjPosition}.
 */
@Service
@Transactional
public class HkjPositionService {

    private static final Logger LOG = LoggerFactory.getLogger(HkjPositionService.class);

    private final HkjPositionRepository hkjPositionRepository;

    private final HkjPositionMapper hkjPositionMapper;

    public HkjPositionService(HkjPositionRepository hkjPositionRepository, HkjPositionMapper hkjPositionMapper) {
        this.hkjPositionRepository = hkjPositionRepository;
        this.hkjPositionMapper = hkjPositionMapper;
    }

    /**
     * Save a hkjPosition.
     *
     * @param hkjPositionDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjPositionDTO save(HkjPositionDTO hkjPositionDTO) {
        LOG.debug("Request to save HkjPosition : {}", hkjPositionDTO);
        HkjPosition hkjPosition = hkjPositionMapper.toEntity(hkjPositionDTO);
        hkjPosition = hkjPositionRepository.save(hkjPosition);
        return hkjPositionMapper.toDto(hkjPosition);
    }

    /**
     * Update a hkjPosition.
     *
     * @param hkjPositionDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjPositionDTO update(HkjPositionDTO hkjPositionDTO) {
        LOG.debug("Request to update HkjPosition : {}", hkjPositionDTO);
        HkjPosition hkjPosition = hkjPositionMapper.toEntity(hkjPositionDTO);
        hkjPosition.setIsPersisted();
        hkjPosition = hkjPositionRepository.save(hkjPosition);
        return hkjPositionMapper.toDto(hkjPosition);
    }

    /**
     * Partially update a hkjPosition.
     *
     * @param hkjPositionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HkjPositionDTO> partialUpdate(HkjPositionDTO hkjPositionDTO) {
        LOG.debug("Request to partially update HkjPosition : {}", hkjPositionDTO);

        return hkjPositionRepository
            .findById(hkjPositionDTO.getId())
            .map(existingHkjPosition -> {
                hkjPositionMapper.partialUpdate(existingHkjPosition, hkjPositionDTO);

                return existingHkjPosition;
            })
            .map(hkjPositionRepository::save)
            .map(hkjPositionMapper::toDto);
    }

    /**
     * Get one hkjPosition by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HkjPositionDTO> findOne(Long id) {
        LOG.debug("Request to get HkjPosition : {}", id);
        return hkjPositionRepository.findById(id).map(hkjPositionMapper::toDto);
    }

    /**
     * Delete the hkjPosition by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete HkjPosition : {}", id);
        hkjPositionRepository.deleteById(id);
    }
}
