package com.server.hkj.service;

import com.server.hkj.domain.HkjTempImage;
import com.server.hkj.repository.HkjTempImageRepository;
import com.server.hkj.service.dto.HkjTempImageDTO;
import com.server.hkj.service.mapper.HkjTempImageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.server.hkj.domain.HkjTempImage}.
 */
@Service
@Transactional
public class HkjTempImageService {

    private static final Logger log = LoggerFactory.getLogger(HkjTempImageService.class);

    private final HkjTempImageRepository hkjTempImageRepository;

    private final HkjTempImageMapper hkjTempImageMapper;

    public HkjTempImageService(HkjTempImageRepository hkjTempImageRepository, HkjTempImageMapper hkjTempImageMapper) {
        this.hkjTempImageRepository = hkjTempImageRepository;
        this.hkjTempImageMapper = hkjTempImageMapper;
    }

    /**
     * Save a hkjTempImage.
     *
     * @param hkjTempImageDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjTempImageDTO save(HkjTempImageDTO hkjTempImageDTO) {
        log.debug("Request to save HkjTempImage : {}", hkjTempImageDTO);
        HkjTempImage hkjTempImage = hkjTempImageMapper.toEntity(hkjTempImageDTO);
        hkjTempImage = hkjTempImageRepository.save(hkjTempImage);
        return hkjTempImageMapper.toDto(hkjTempImage);
    }

    /**
     * Update a hkjTempImage.
     *
     * @param hkjTempImageDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjTempImageDTO update(HkjTempImageDTO hkjTempImageDTO) {
        log.debug("Request to update HkjTempImage : {}", hkjTempImageDTO);
        HkjTempImage hkjTempImage = hkjTempImageMapper.toEntity(hkjTempImageDTO);
        hkjTempImage.setIsPersisted();
        hkjTempImage = hkjTempImageRepository.save(hkjTempImage);
        return hkjTempImageMapper.toDto(hkjTempImage);
    }

    /**
     * Partially update a hkjTempImage.
     *
     * @param hkjTempImageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HkjTempImageDTO> partialUpdate(HkjTempImageDTO hkjTempImageDTO) {
        log.debug("Request to partially update HkjTempImage : {}", hkjTempImageDTO);

        return hkjTempImageRepository
            .findById(hkjTempImageDTO.getId())
            .map(existingHkjTempImage -> {
                hkjTempImageMapper.partialUpdate(existingHkjTempImage, hkjTempImageDTO);

                return existingHkjTempImage;
            })
            .map(hkjTempImageRepository::save)
            .map(hkjTempImageMapper::toDto);
    }

    /**
     * Get one hkjTempImage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HkjTempImageDTO> findOne(Long id) {
        log.debug("Request to get HkjTempImage : {}", id);
        return hkjTempImageRepository.findById(id).map(hkjTempImageMapper::toDto);
    }

    /**
     * Delete the hkjTempImage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete HkjTempImage : {}", id);
        hkjTempImageRepository.deleteById(id);
    }
}
