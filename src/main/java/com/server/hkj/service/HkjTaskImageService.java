package com.server.hkj.service;

import com.server.hkj.domain.HkjTaskImage;
import com.server.hkj.repository.HkjTaskImageRepository;
import com.server.hkj.service.dto.HkjTaskImageDTO;
import com.server.hkj.service.mapper.HkjTaskImageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.server.hkj.domain.HkjTaskImage}.
 */
@Service
@Transactional
public class HkjTaskImageService {

    private static final Logger log = LoggerFactory.getLogger(HkjTaskImageService.class);

    private final HkjTaskImageRepository hkjTaskImageRepository;

    private final HkjTaskImageMapper hkjTaskImageMapper;

    public HkjTaskImageService(HkjTaskImageRepository hkjTaskImageRepository, HkjTaskImageMapper hkjTaskImageMapper) {
        this.hkjTaskImageRepository = hkjTaskImageRepository;
        this.hkjTaskImageMapper = hkjTaskImageMapper;
    }

    /**
     * Save a hkjTaskImage.
     *
     * @param hkjTaskImageDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjTaskImageDTO save(HkjTaskImageDTO hkjTaskImageDTO) {
        log.debug("Request to save HkjTaskImage : {}", hkjTaskImageDTO);
        HkjTaskImage hkjTaskImage = hkjTaskImageMapper.toEntity(hkjTaskImageDTO);
        hkjTaskImage = hkjTaskImageRepository.save(hkjTaskImage);
        return hkjTaskImageMapper.toDto(hkjTaskImage);
    }

    /**
     * Update a hkjTaskImage.
     *
     * @param hkjTaskImageDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjTaskImageDTO update(HkjTaskImageDTO hkjTaskImageDTO) {
        log.debug("Request to update HkjTaskImage : {}", hkjTaskImageDTO);
        HkjTaskImage hkjTaskImage = hkjTaskImageMapper.toEntity(hkjTaskImageDTO);
        hkjTaskImage.setIsPersisted();
        hkjTaskImage = hkjTaskImageRepository.save(hkjTaskImage);
        return hkjTaskImageMapper.toDto(hkjTaskImage);
    }

    /**
     * Partially update a hkjTaskImage.
     *
     * @param hkjTaskImageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HkjTaskImageDTO> partialUpdate(HkjTaskImageDTO hkjTaskImageDTO) {
        log.debug("Request to partially update HkjTaskImage : {}", hkjTaskImageDTO);

        return hkjTaskImageRepository
            .findById(hkjTaskImageDTO.getId())
            .map(existingHkjTaskImage -> {
                hkjTaskImageMapper.partialUpdate(existingHkjTaskImage, hkjTaskImageDTO);

                return existingHkjTaskImage;
            })
            .map(hkjTaskImageRepository::save)
            .map(hkjTaskImageMapper::toDto);
    }

    /**
     * Get one hkjTaskImage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HkjTaskImageDTO> findOne(Long id) {
        log.debug("Request to get HkjTaskImage : {}", id);
        return hkjTaskImageRepository.findById(id).map(hkjTaskImageMapper::toDto);
    }

    /**
     * Delete the hkjTaskImage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete HkjTaskImage : {}", id);
        hkjTaskImageRepository.deleteById(id);
    }
}
