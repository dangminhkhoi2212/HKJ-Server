package com.server.hkj.service;

import com.server.hkj.domain.HkjJewelryModel;
import com.server.hkj.repository.HkjJewelryModelRepository;
import com.server.hkj.service.dto.HkjJewelryModelDTO;
import com.server.hkj.service.mapper.HkjJewelryModelMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.server.hkj.domain.HkjJewelryModel}.
 */
@Service
@Transactional
public class HkjJewelryModelService {

    private static final Logger LOG = LoggerFactory.getLogger(HkjJewelryModelService.class);

    private final HkjJewelryModelRepository hkjJewelryModelRepository;

    private final HkjJewelryModelMapper hkjJewelryModelMapper;

    public HkjJewelryModelService(HkjJewelryModelRepository hkjJewelryModelRepository, HkjJewelryModelMapper hkjJewelryModelMapper) {
        this.hkjJewelryModelRepository = hkjJewelryModelRepository;
        this.hkjJewelryModelMapper = hkjJewelryModelMapper;
    }

    /**
     * Save a hkjJewelryModel.
     *
     * @param hkjJewelryModelDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjJewelryModelDTO save(HkjJewelryModelDTO hkjJewelryModelDTO) {
        LOG.debug("Request to save HkjJewelryModel : {}", hkjJewelryModelDTO);
        HkjJewelryModel hkjJewelryModel = hkjJewelryModelMapper.toEntity(hkjJewelryModelDTO);
        hkjJewelryModel = hkjJewelryModelRepository.save(hkjJewelryModel);
        return hkjJewelryModelMapper.toDto(hkjJewelryModel);
    }

    /**
     * Update a hkjJewelryModel.
     *
     * @param hkjJewelryModelDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjJewelryModelDTO update(HkjJewelryModelDTO hkjJewelryModelDTO) {
        LOG.debug("Request to update HkjJewelryModel : {}", hkjJewelryModelDTO);
        HkjJewelryModel hkjJewelryModel = hkjJewelryModelMapper.toEntity(hkjJewelryModelDTO);
        hkjJewelryModel.setIsPersisted();
        hkjJewelryModel = hkjJewelryModelRepository.save(hkjJewelryModel);
        return hkjJewelryModelMapper.toDto(hkjJewelryModel);
    }

    /**
     * Partially update a hkjJewelryModel.
     *
     * @param hkjJewelryModelDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HkjJewelryModelDTO> partialUpdate(HkjJewelryModelDTO hkjJewelryModelDTO) {
        LOG.debug("Request to partially update HkjJewelryModel : {}", hkjJewelryModelDTO);

        return hkjJewelryModelRepository
            .findById(hkjJewelryModelDTO.getId())
            .map(existingHkjJewelryModel -> {
                hkjJewelryModelMapper.partialUpdate(existingHkjJewelryModel, hkjJewelryModelDTO);

                return existingHkjJewelryModel;
            })
            .map(hkjJewelryModelRepository::save)
            .map(hkjJewelryModelMapper::toDto);
    }

    /**
     * Get one hkjJewelryModel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HkjJewelryModelDTO> findOne(Long id) {
        LOG.debug("Request to get HkjJewelryModel : {}", id);
        return hkjJewelryModelRepository.findById(id).map(hkjJewelryModelMapper::toDto);
    }

    /**
     * Delete the hkjJewelryModel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete HkjJewelryModel : {}", id);
        hkjJewelryModelRepository.deleteById(id);
    }
}
