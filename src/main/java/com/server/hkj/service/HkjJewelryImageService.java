package com.server.hkj.service;

import com.server.hkj.domain.HkjJewelryImage;
import com.server.hkj.repository.HkjJewelryImageRepository;
import com.server.hkj.service.dto.HkjJewelryImageDTO;
import com.server.hkj.service.mapper.HkjJewelryImageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.server.hkj.domain.HkjJewelryImage}.
 */
@Service
@Transactional
public class HkjJewelryImageService {

    private static final Logger log = LoggerFactory.getLogger(HkjJewelryImageService.class);

    private final HkjJewelryImageRepository hkjJewelryImageRepository;

    private final HkjJewelryImageMapper hkjJewelryImageMapper;

    public HkjJewelryImageService(HkjJewelryImageRepository hkjJewelryImageRepository, HkjJewelryImageMapper hkjJewelryImageMapper) {
        this.hkjJewelryImageRepository = hkjJewelryImageRepository;
        this.hkjJewelryImageMapper = hkjJewelryImageMapper;
    }

    /**
     * Save a hkjJewelryImage.
     *
     * @param hkjJewelryImageDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjJewelryImageDTO save(HkjJewelryImageDTO hkjJewelryImageDTO) {
        log.debug("Request to save HkjJewelryImage : {}", hkjJewelryImageDTO);
        HkjJewelryImage hkjJewelryImage = hkjJewelryImageMapper.toEntity(hkjJewelryImageDTO);
        hkjJewelryImage = hkjJewelryImageRepository.save(hkjJewelryImage);
        return hkjJewelryImageMapper.toDto(hkjJewelryImage);
    }

    /**
     * Update a hkjJewelryImage.
     *
     * @param hkjJewelryImageDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjJewelryImageDTO update(HkjJewelryImageDTO hkjJewelryImageDTO) {
        log.debug("Request to update HkjJewelryImage : {}", hkjJewelryImageDTO);
        HkjJewelryImage hkjJewelryImage = hkjJewelryImageMapper.toEntity(hkjJewelryImageDTO);
        hkjJewelryImage.setIsPersisted();
        hkjJewelryImage = hkjJewelryImageRepository.save(hkjJewelryImage);
        return hkjJewelryImageMapper.toDto(hkjJewelryImage);
    }

    /**
     * Partially update a hkjJewelryImage.
     *
     * @param hkjJewelryImageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HkjJewelryImageDTO> partialUpdate(HkjJewelryImageDTO hkjJewelryImageDTO) {
        log.debug("Request to partially update HkjJewelryImage : {}", hkjJewelryImageDTO);

        return hkjJewelryImageRepository
            .findById(hkjJewelryImageDTO.getId())
            .map(existingHkjJewelryImage -> {
                hkjJewelryImageMapper.partialUpdate(existingHkjJewelryImage, hkjJewelryImageDTO);

                return existingHkjJewelryImage;
            })
            .map(hkjJewelryImageRepository::save)
            .map(hkjJewelryImageMapper::toDto);
    }

    /**
     * Get one hkjJewelryImage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HkjJewelryImageDTO> findOne(Long id) {
        log.debug("Request to get HkjJewelryImage : {}", id);
        return hkjJewelryImageRepository.findById(id).map(hkjJewelryImageMapper::toDto);
    }

    /**
     * Delete the hkjJewelryImage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete HkjJewelryImage : {}", id);
        hkjJewelryImageRepository.deleteById(id);
    }
}
