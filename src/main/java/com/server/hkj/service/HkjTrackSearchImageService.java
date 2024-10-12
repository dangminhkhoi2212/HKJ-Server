package com.server.hkj.service;

import com.server.hkj.domain.HkjTrackSearchImage;
import com.server.hkj.repository.HkjTrackSearchImageRepository;
import com.server.hkj.service.dto.HkjTrackSearchImageDTO;
import com.server.hkj.service.mapper.HkjTrackSearchImageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.server.hkj.domain.HkjTrackSearchImage}.
 */
@Service
@Transactional
public class HkjTrackSearchImageService {

    private static final Logger LOG = LoggerFactory.getLogger(HkjTrackSearchImageService.class);

    private final HkjTrackSearchImageRepository hkjTrackSearchImageRepository;

    private final HkjTrackSearchImageMapper hkjTrackSearchImageMapper;

    public HkjTrackSearchImageService(
        HkjTrackSearchImageRepository hkjTrackSearchImageRepository,
        HkjTrackSearchImageMapper hkjTrackSearchImageMapper
    ) {
        this.hkjTrackSearchImageRepository = hkjTrackSearchImageRepository;
        this.hkjTrackSearchImageMapper = hkjTrackSearchImageMapper;
    }

    /**
     * Save a hkjTrackSearchImage.
     *
     * @param hkjTrackSearchImageDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjTrackSearchImageDTO save(HkjTrackSearchImageDTO hkjTrackSearchImageDTO) {
        LOG.debug("Request to save HkjTrackSearchImage : {}", hkjTrackSearchImageDTO);
        HkjTrackSearchImage hkjTrackSearchImage = hkjTrackSearchImageMapper.toEntity(hkjTrackSearchImageDTO);
        hkjTrackSearchImage = hkjTrackSearchImageRepository.save(hkjTrackSearchImage);
        return hkjTrackSearchImageMapper.toDto(hkjTrackSearchImage);
    }

    /**
     * Update a hkjTrackSearchImage.
     *
     * @param hkjTrackSearchImageDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjTrackSearchImageDTO update(HkjTrackSearchImageDTO hkjTrackSearchImageDTO) {
        LOG.debug("Request to update HkjTrackSearchImage : {}", hkjTrackSearchImageDTO);
        HkjTrackSearchImage hkjTrackSearchImage = hkjTrackSearchImageMapper.toEntity(hkjTrackSearchImageDTO);
        hkjTrackSearchImage.setIsPersisted();
        hkjTrackSearchImage = hkjTrackSearchImageRepository.save(hkjTrackSearchImage);
        return hkjTrackSearchImageMapper.toDto(hkjTrackSearchImage);
    }

    /**
     * Partially update a hkjTrackSearchImage.
     *
     * @param hkjTrackSearchImageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HkjTrackSearchImageDTO> partialUpdate(HkjTrackSearchImageDTO hkjTrackSearchImageDTO) {
        LOG.debug("Request to partially update HkjTrackSearchImage : {}", hkjTrackSearchImageDTO);

        return hkjTrackSearchImageRepository
            .findById(hkjTrackSearchImageDTO.getId())
            .map(existingHkjTrackSearchImage -> {
                hkjTrackSearchImageMapper.partialUpdate(existingHkjTrackSearchImage, hkjTrackSearchImageDTO);

                return existingHkjTrackSearchImage;
            })
            .map(hkjTrackSearchImageRepository::save)
            .map(hkjTrackSearchImageMapper::toDto);
    }

    /**
     * Get one hkjTrackSearchImage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HkjTrackSearchImageDTO> findOne(Long id) {
        LOG.debug("Request to get HkjTrackSearchImage : {}", id);
        return hkjTrackSearchImageRepository.findById(id).map(hkjTrackSearchImageMapper::toDto);
    }

    /**
     * Delete the hkjTrackSearchImage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete HkjTrackSearchImage : {}", id);
        hkjTrackSearchImageRepository.deleteById(id);
    }
}
