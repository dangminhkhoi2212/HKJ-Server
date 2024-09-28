package com.server.hkj.service;

import com.server.hkj.domain.HkjOrderImage;
import com.server.hkj.repository.HkjOrderImageRepository;
import com.server.hkj.service.dto.HkjOrderImageDTO;
import com.server.hkj.service.mapper.HkjOrderImageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.server.hkj.domain.HkjOrderImage}.
 */
@Service
@Transactional
public class HkjOrderImageService {

    private static final Logger LOG = LoggerFactory.getLogger(HkjOrderImageService.class);

    private final HkjOrderImageRepository hkjOrderImageRepository;

    private final HkjOrderImageMapper hkjOrderImageMapper;

    public HkjOrderImageService(HkjOrderImageRepository hkjOrderImageRepository, HkjOrderImageMapper hkjOrderImageMapper) {
        this.hkjOrderImageRepository = hkjOrderImageRepository;
        this.hkjOrderImageMapper = hkjOrderImageMapper;
    }

    /**
     * Save a hkjOrderImage.
     *
     * @param hkjOrderImageDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjOrderImageDTO save(HkjOrderImageDTO hkjOrderImageDTO) {
        LOG.debug("Request to save HkjOrderImage : {}", hkjOrderImageDTO);
        HkjOrderImage hkjOrderImage = hkjOrderImageMapper.toEntity(hkjOrderImageDTO);
        hkjOrderImage = hkjOrderImageRepository.save(hkjOrderImage);
        return hkjOrderImageMapper.toDto(hkjOrderImage);
    }

    /**
     * Update a hkjOrderImage.
     *
     * @param hkjOrderImageDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjOrderImageDTO update(HkjOrderImageDTO hkjOrderImageDTO) {
        LOG.debug("Request to update HkjOrderImage : {}", hkjOrderImageDTO);
        HkjOrderImage hkjOrderImage = hkjOrderImageMapper.toEntity(hkjOrderImageDTO);
        hkjOrderImage.setIsPersisted();
        hkjOrderImage = hkjOrderImageRepository.save(hkjOrderImage);
        return hkjOrderImageMapper.toDto(hkjOrderImage);
    }

    /**
     * Partially update a hkjOrderImage.
     *
     * @param hkjOrderImageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HkjOrderImageDTO> partialUpdate(HkjOrderImageDTO hkjOrderImageDTO) {
        LOG.debug("Request to partially update HkjOrderImage : {}", hkjOrderImageDTO);

        return hkjOrderImageRepository
            .findById(hkjOrderImageDTO.getId())
            .map(existingHkjOrderImage -> {
                hkjOrderImageMapper.partialUpdate(existingHkjOrderImage, hkjOrderImageDTO);

                return existingHkjOrderImage;
            })
            .map(hkjOrderImageRepository::save)
            .map(hkjOrderImageMapper::toDto);
    }

    /**
     * Get one hkjOrderImage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HkjOrderImageDTO> findOne(Long id) {
        LOG.debug("Request to get HkjOrderImage : {}", id);
        return hkjOrderImageRepository.findById(id).map(hkjOrderImageMapper::toDto);
    }

    /**
     * Delete the hkjOrderImage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete HkjOrderImage : {}", id);
        hkjOrderImageRepository.deleteById(id);
    }
}
