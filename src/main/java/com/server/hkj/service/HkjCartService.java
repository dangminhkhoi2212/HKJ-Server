package com.server.hkj.service;

import com.server.hkj.domain.HkjCart;
import com.server.hkj.repository.HkjCartRepository;
import com.server.hkj.service.dto.HkjCartDTO;
import com.server.hkj.service.mapper.HkjCartMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.server.hkj.domain.HkjCart}.
 */
@Service
@Transactional
public class HkjCartService {

    private static final Logger LOG = LoggerFactory.getLogger(HkjCartService.class);

    private final HkjCartRepository hkjCartRepository;

    private final HkjCartMapper hkjCartMapper;

    public HkjCartService(HkjCartRepository hkjCartRepository, HkjCartMapper hkjCartMapper) {
        this.hkjCartRepository = hkjCartRepository;
        this.hkjCartMapper = hkjCartMapper;
    }

    /**
     * Save a hkjCart.
     *
     * @param hkjCartDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjCartDTO save(HkjCartDTO hkjCartDTO) {
        LOG.debug("Request to save HkjCart : {}", hkjCartDTO);
        HkjCart hkjCart = hkjCartMapper.toEntity(hkjCartDTO);
        hkjCart = hkjCartRepository.save(hkjCart);
        return hkjCartMapper.toDto(hkjCart);
    }

    /**
     * Update a hkjCart.
     *
     * @param hkjCartDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjCartDTO update(HkjCartDTO hkjCartDTO) {
        LOG.debug("Request to update HkjCart : {}", hkjCartDTO);
        HkjCart hkjCart = hkjCartMapper.toEntity(hkjCartDTO);
        hkjCart.setIsPersisted();
        hkjCart = hkjCartRepository.save(hkjCart);
        return hkjCartMapper.toDto(hkjCart);
    }

    /**
     * Partially update a hkjCart.
     *
     * @param hkjCartDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HkjCartDTO> partialUpdate(HkjCartDTO hkjCartDTO) {
        LOG.debug("Request to partially update HkjCart : {}", hkjCartDTO);

        return hkjCartRepository
            .findById(hkjCartDTO.getId())
            .map(existingHkjCart -> {
                hkjCartMapper.partialUpdate(existingHkjCart, hkjCartDTO);

                return existingHkjCart;
            })
            .map(hkjCartRepository::save)
            .map(hkjCartMapper::toDto);
    }

    /**
     * Get one hkjCart by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HkjCartDTO> findOne(Long id) {
        LOG.debug("Request to get HkjCart : {}", id);
        return hkjCartRepository.findById(id).map(hkjCartMapper::toDto);
    }

    /**
     * Delete the hkjCart by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete HkjCart : {}", id);
        hkjCartRepository.deleteById(id);
    }
}
