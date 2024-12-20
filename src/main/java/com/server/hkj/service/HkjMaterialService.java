package com.server.hkj.service;

import com.server.hkj.domain.HkjMaterial;
import com.server.hkj.repository.HkjMaterialRepository;
import com.server.hkj.service.dto.HkjMaterialDTO;
import com.server.hkj.service.mapper.HkjMaterialMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.server.hkj.domain.HkjMaterial}.
 */
@Service
@Transactional
public class HkjMaterialService {

    private static final Logger LOG = LoggerFactory.getLogger(HkjMaterialService.class);

    private final HkjMaterialRepository hkjMaterialRepository;

    private final HkjMaterialMapper hkjMaterialMapper;

    public HkjMaterialService(HkjMaterialRepository hkjMaterialRepository, HkjMaterialMapper hkjMaterialMapper) {
        this.hkjMaterialRepository = hkjMaterialRepository;
        this.hkjMaterialMapper = hkjMaterialMapper;
    }

    /**
     * Save a hkjMaterial.
     *
     * @param hkjMaterialDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjMaterialDTO save(HkjMaterialDTO hkjMaterialDTO) {
        LOG.debug("Request to save HkjMaterial : {}", hkjMaterialDTO);
        HkjMaterial hkjMaterial = hkjMaterialMapper.toEntity(hkjMaterialDTO);
        hkjMaterial = hkjMaterialRepository.save(hkjMaterial);
        return hkjMaterialMapper.toDto(hkjMaterial);
    }

    /**
     * Update a hkjMaterial.
     *
     * @param hkjMaterialDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjMaterialDTO update(HkjMaterialDTO hkjMaterialDTO) {
        LOG.debug("Request to update HkjMaterial : {}", hkjMaterialDTO);
        HkjMaterial hkjMaterial = hkjMaterialMapper.toEntity(hkjMaterialDTO);
        hkjMaterial.setIsPersisted();
        hkjMaterial = hkjMaterialRepository.save(hkjMaterial);
        return hkjMaterialMapper.toDto(hkjMaterial);
    }

    /**
     * Partially update a hkjMaterial.
     *
     * @param hkjMaterialDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HkjMaterialDTO> partialUpdate(HkjMaterialDTO hkjMaterialDTO) {
        LOG.debug("Request to partially update HkjMaterial : {}", hkjMaterialDTO);

        return hkjMaterialRepository
            .findById(hkjMaterialDTO.getId())
            .map(existingHkjMaterial -> {
                hkjMaterialMapper.partialUpdate(existingHkjMaterial, hkjMaterialDTO);

                return existingHkjMaterial;
            })
            .map(hkjMaterialRepository::save)
            .map(hkjMaterialMapper::toDto);
    }

    /**
     * Get one hkjMaterial by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HkjMaterialDTO> findOne(Long id) {
        LOG.debug("Request to get HkjMaterial : {}", id);
        return hkjMaterialRepository.findById(id).map(hkjMaterialMapper::toDto);
    }

    /**
     * Delete the hkjMaterial by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete HkjMaterial : {}", id);
        hkjMaterialRepository.deleteById(id);
    }
}
