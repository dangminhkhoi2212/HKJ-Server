package com.server.hkj.service;

import com.server.hkj.domain.HkjOrderItem;
import com.server.hkj.repository.HkjOrderItemRepository;
import com.server.hkj.service.dto.HkjOrderItemDTO;
import com.server.hkj.service.mapper.HkjOrderItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.server.hkj.domain.HkjOrderItem}.
 */
@Service
@Transactional
public class HkjOrderItemService {

    private static final Logger LOG = LoggerFactory.getLogger(HkjOrderItemService.class);

    private final HkjOrderItemRepository hkjOrderItemRepository;

    private final HkjOrderItemMapper hkjOrderItemMapper;

    public HkjOrderItemService(HkjOrderItemRepository hkjOrderItemRepository, HkjOrderItemMapper hkjOrderItemMapper) {
        this.hkjOrderItemRepository = hkjOrderItemRepository;
        this.hkjOrderItemMapper = hkjOrderItemMapper;
    }

    /**
     * Save a hkjOrderItem.
     *
     * @param hkjOrderItemDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjOrderItemDTO save(HkjOrderItemDTO hkjOrderItemDTO) {
        LOG.debug("Request to save HkjOrderItem : {}", hkjOrderItemDTO);
        HkjOrderItem hkjOrderItem = hkjOrderItemMapper.toEntity(hkjOrderItemDTO);
        hkjOrderItem = hkjOrderItemRepository.save(hkjOrderItem);
        return hkjOrderItemMapper.toDto(hkjOrderItem);
    }

    /**
     * Update a hkjOrderItem.
     *
     * @param hkjOrderItemDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjOrderItemDTO update(HkjOrderItemDTO hkjOrderItemDTO) {
        LOG.debug("Request to update HkjOrderItem : {}", hkjOrderItemDTO);
        HkjOrderItem hkjOrderItem = hkjOrderItemMapper.toEntity(hkjOrderItemDTO);
        hkjOrderItem.setIsPersisted();
        hkjOrderItem = hkjOrderItemRepository.save(hkjOrderItem);
        return hkjOrderItemMapper.toDto(hkjOrderItem);
    }

    /**
     * Partially update a hkjOrderItem.
     *
     * @param hkjOrderItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HkjOrderItemDTO> partialUpdate(HkjOrderItemDTO hkjOrderItemDTO) {
        LOG.debug("Request to partially update HkjOrderItem : {}", hkjOrderItemDTO);

        return hkjOrderItemRepository
            .findById(hkjOrderItemDTO.getId())
            .map(existingHkjOrderItem -> {
                hkjOrderItemMapper.partialUpdate(existingHkjOrderItem, hkjOrderItemDTO);

                return existingHkjOrderItem;
            })
            .map(hkjOrderItemRepository::save)
            .map(hkjOrderItemMapper::toDto);
    }

    /**
     * Get one hkjOrderItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HkjOrderItemDTO> findOne(Long id) {
        LOG.debug("Request to get HkjOrderItem : {}", id);
        return hkjOrderItemRepository.findById(id).map(hkjOrderItemMapper::toDto);
    }

    /**
     * Delete the hkjOrderItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete HkjOrderItem : {}", id);
        hkjOrderItemRepository.deleteById(id);
    }
}
