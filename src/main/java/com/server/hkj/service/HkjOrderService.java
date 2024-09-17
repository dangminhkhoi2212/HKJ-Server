package com.server.hkj.service;

import com.server.hkj.domain.HkjOrder;
import com.server.hkj.repository.HkjOrderRepository;
import com.server.hkj.service.dto.HkjOrderDTO;
import com.server.hkj.service.mapper.HkjOrderMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.server.hkj.domain.HkjOrder}.
 */
@Service
@Transactional
public class HkjOrderService {

    private static final Logger log = LoggerFactory.getLogger(HkjOrderService.class);

    private final HkjOrderRepository hkjOrderRepository;

    private final HkjOrderMapper hkjOrderMapper;

    public HkjOrderService(HkjOrderRepository hkjOrderRepository, HkjOrderMapper hkjOrderMapper) {
        this.hkjOrderRepository = hkjOrderRepository;
        this.hkjOrderMapper = hkjOrderMapper;
    }

    /**
     * Save a hkjOrder.
     *
     * @param hkjOrderDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjOrderDTO save(HkjOrderDTO hkjOrderDTO) {
        log.debug("Request to save HkjOrder : {}", hkjOrderDTO);
        HkjOrder hkjOrder = hkjOrderMapper.toEntity(hkjOrderDTO);
        hkjOrder = hkjOrderRepository.save(hkjOrder);
        return hkjOrderMapper.toDto(hkjOrder);
    }

    /**
     * Update a hkjOrder.
     *
     * @param hkjOrderDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjOrderDTO update(HkjOrderDTO hkjOrderDTO) {
        log.debug("Request to update HkjOrder : {}", hkjOrderDTO);
        HkjOrder hkjOrder = hkjOrderMapper.toEntity(hkjOrderDTO);
        hkjOrder.setIsPersisted();
        hkjOrder = hkjOrderRepository.save(hkjOrder);
        return hkjOrderMapper.toDto(hkjOrder);
    }

    /**
     * Partially update a hkjOrder.
     *
     * @param hkjOrderDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HkjOrderDTO> partialUpdate(HkjOrderDTO hkjOrderDTO) {
        log.debug("Request to partially update HkjOrder : {}", hkjOrderDTO);

        return hkjOrderRepository
            .findById(hkjOrderDTO.getId())
            .map(existingHkjOrder -> {
                hkjOrderMapper.partialUpdate(existingHkjOrder, hkjOrderDTO);

                return existingHkjOrder;
            })
            .map(hkjOrderRepository::save)
            .map(hkjOrderMapper::toDto);
    }

    /**
     * Get one hkjOrder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HkjOrderDTO> findOne(Long id) {
        log.debug("Request to get HkjOrder : {}", id);
        return hkjOrderRepository.findById(id).map(hkjOrderMapper::toDto);
    }

    /**
     * Delete the hkjOrder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete HkjOrder : {}", id);
        hkjOrderRepository.deleteById(id);
    }
}
