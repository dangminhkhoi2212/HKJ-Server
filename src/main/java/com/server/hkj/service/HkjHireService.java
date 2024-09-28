package com.server.hkj.service;

import com.server.hkj.domain.HkjHire;
import com.server.hkj.repository.HkjHireRepository;
import com.server.hkj.service.dto.HkjHireDTO;
import com.server.hkj.service.mapper.HkjHireMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.server.hkj.domain.HkjHire}.
 */
@Service
@Transactional
public class HkjHireService {

    private static final Logger LOG = LoggerFactory.getLogger(HkjHireService.class);

    private final HkjHireRepository hkjHireRepository;

    private final HkjHireMapper hkjHireMapper;

    public HkjHireService(HkjHireRepository hkjHireRepository, HkjHireMapper hkjHireMapper) {
        this.hkjHireRepository = hkjHireRepository;
        this.hkjHireMapper = hkjHireMapper;
    }

    /**
     * Save a hkjHire.
     *
     * @param hkjHireDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjHireDTO save(HkjHireDTO hkjHireDTO) {
        LOG.debug("Request to save HkjHire : {}", hkjHireDTO);
        HkjHire hkjHire = hkjHireMapper.toEntity(hkjHireDTO);
        hkjHire = hkjHireRepository.save(hkjHire);
        return hkjHireMapper.toDto(hkjHire);
    }

    /**
     * Update a hkjHire.
     *
     * @param hkjHireDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjHireDTO update(HkjHireDTO hkjHireDTO) {
        LOG.debug("Request to update HkjHire : {}", hkjHireDTO);
        HkjHire hkjHire = hkjHireMapper.toEntity(hkjHireDTO);
        hkjHire.setIsPersisted();
        hkjHire = hkjHireRepository.save(hkjHire);
        return hkjHireMapper.toDto(hkjHire);
    }

    /**
     * Partially update a hkjHire.
     *
     * @param hkjHireDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HkjHireDTO> partialUpdate(HkjHireDTO hkjHireDTO) {
        LOG.debug("Request to partially update HkjHire : {}", hkjHireDTO);

        return hkjHireRepository
            .findById(hkjHireDTO.getId())
            .map(existingHkjHire -> {
                hkjHireMapper.partialUpdate(existingHkjHire, hkjHireDTO);

                return existingHkjHire;
            })
            .map(hkjHireRepository::save)
            .map(hkjHireMapper::toDto);
    }

    /**
     * Get one hkjHire by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HkjHireDTO> findOne(Long id) {
        LOG.debug("Request to get HkjHire : {}", id);
        return hkjHireRepository.findById(id).map(hkjHireMapper::toDto);
    }

    /**
     * Delete the hkjHire by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete HkjHire : {}", id);
        hkjHireRepository.deleteById(id);
    }
}
