package com.server.hkj.service;

import com.server.hkj.domain.HkjMaterialImage;
import com.server.hkj.repository.HkjMaterialImageRepository;
import com.server.hkj.service.dto.HkjMaterialImageDTO;
import com.server.hkj.service.mapper.HkjMaterialImageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.server.hkj.domain.HkjMaterialImage}.
 */
@Service
@Transactional
public class HkjMaterialImageService {

    private static final Logger LOG = LoggerFactory.getLogger(HkjMaterialImageService.class);

    private final HkjMaterialImageRepository hkjMaterialImageRepository;

    private final HkjMaterialImageMapper hkjMaterialImageMapper;

    public HkjMaterialImageService(HkjMaterialImageRepository hkjMaterialImageRepository, HkjMaterialImageMapper hkjMaterialImageMapper) {
        this.hkjMaterialImageRepository = hkjMaterialImageRepository;
        this.hkjMaterialImageMapper = hkjMaterialImageMapper;
    }

    /**
     * Save a hkjMaterialImage.
     *
     * @param hkjMaterialImageDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjMaterialImageDTO save(HkjMaterialImageDTO hkjMaterialImageDTO) {
        LOG.debug("Request to save HkjMaterialImage : {}", hkjMaterialImageDTO);
        HkjMaterialImage hkjMaterialImage = hkjMaterialImageMapper.toEntity(hkjMaterialImageDTO);
        hkjMaterialImage = hkjMaterialImageRepository.save(hkjMaterialImage);
        return hkjMaterialImageMapper.toDto(hkjMaterialImage);
    }

    /**
     * Update a hkjMaterialImage.
     *
     * @param hkjMaterialImageDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjMaterialImageDTO update(HkjMaterialImageDTO hkjMaterialImageDTO) {
        LOG.debug("Request to update HkjMaterialImage : {}", hkjMaterialImageDTO);
        HkjMaterialImage hkjMaterialImage = hkjMaterialImageMapper.toEntity(hkjMaterialImageDTO);
        hkjMaterialImage.setIsPersisted();
        hkjMaterialImage = hkjMaterialImageRepository.save(hkjMaterialImage);
        return hkjMaterialImageMapper.toDto(hkjMaterialImage);
    }

    /**
     * Partially update a hkjMaterialImage.
     *
     * @param hkjMaterialImageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HkjMaterialImageDTO> partialUpdate(HkjMaterialImageDTO hkjMaterialImageDTO) {
        LOG.debug("Request to partially update HkjMaterialImage : {}", hkjMaterialImageDTO);

        return hkjMaterialImageRepository
            .findById(hkjMaterialImageDTO.getId())
            .map(existingHkjMaterialImage -> {
                hkjMaterialImageMapper.partialUpdate(existingHkjMaterialImage, hkjMaterialImageDTO);

                return existingHkjMaterialImage;
            })
            .map(hkjMaterialImageRepository::save)
            .map(hkjMaterialImageMapper::toDto);
    }

    /**
     * Get one hkjMaterialImage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HkjMaterialImageDTO> findOne(Long id) {
        LOG.debug("Request to get HkjMaterialImage : {}", id);
        return hkjMaterialImageRepository.findById(id).map(hkjMaterialImageMapper::toDto);
    }

    /**
     * Delete the hkjMaterialImage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete HkjMaterialImage : {}", id);
        hkjMaterialImageRepository.deleteById(id);
    }
}
