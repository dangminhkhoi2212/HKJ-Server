package com.server.hkj.service;

import com.server.hkj.domain.HkjPosition;
import com.server.hkj.repository.HkjPositionRepository;
import com.server.hkj.service.dto.HkjPositionDTO;
import com.server.hkj.service.mapper.HkjPositionMapper;
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
 * Service Implementation for managing {@link com.server.hkj.domain.HkjPosition}.
 */
@Service
@Transactional
public class HkjPositionService {

    private static final Logger log = LoggerFactory.getLogger(HkjPositionService.class);

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
        log.debug("Request to save HkjPosition : {}", hkjPositionDTO);
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
        log.debug("Request to update HkjPosition : {}", hkjPositionDTO);
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
        log.debug("Request to partially update HkjPosition : {}", hkjPositionDTO);

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
     *  Get all the hkjPositions where HkjHire is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<HkjPositionDTO> findAllWhereHkjHireIsNull() {
        log.debug("Request to get all hkjPositions where HkjHire is null");
        return StreamSupport.stream(hkjPositionRepository.findAll().spliterator(), false)
            .filter(hkjPosition -> hkjPosition.getHkjHire() == null)
            .map(hkjPositionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one hkjPosition by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HkjPositionDTO> findOne(Long id) {
        log.debug("Request to get HkjPosition : {}", id);
        return hkjPositionRepository.findById(id).map(hkjPositionMapper::toDto);
    }

    /**
     * Delete the hkjPosition by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete HkjPosition : {}", id);
        hkjPositionRepository.deleteById(id);
    }
}
