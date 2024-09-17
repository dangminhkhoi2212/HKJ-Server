package com.server.hkj.service;

import com.server.hkj.domain.HkjSalary;
import com.server.hkj.repository.HkjSalaryRepository;
import com.server.hkj.service.dto.HkjSalaryDTO;
import com.server.hkj.service.mapper.HkjSalaryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.server.hkj.domain.HkjSalary}.
 */
@Service
@Transactional
public class HkjSalaryService {

    private static final Logger log = LoggerFactory.getLogger(HkjSalaryService.class);

    private final HkjSalaryRepository hkjSalaryRepository;

    private final HkjSalaryMapper hkjSalaryMapper;

    public HkjSalaryService(HkjSalaryRepository hkjSalaryRepository, HkjSalaryMapper hkjSalaryMapper) {
        this.hkjSalaryRepository = hkjSalaryRepository;
        this.hkjSalaryMapper = hkjSalaryMapper;
    }

    /**
     * Save a hkjSalary.
     *
     * @param hkjSalaryDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjSalaryDTO save(HkjSalaryDTO hkjSalaryDTO) {
        log.debug("Request to save HkjSalary : {}", hkjSalaryDTO);
        HkjSalary hkjSalary = hkjSalaryMapper.toEntity(hkjSalaryDTO);
        hkjSalary = hkjSalaryRepository.save(hkjSalary);
        return hkjSalaryMapper.toDto(hkjSalary);
    }

    /**
     * Update a hkjSalary.
     *
     * @param hkjSalaryDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjSalaryDTO update(HkjSalaryDTO hkjSalaryDTO) {
        log.debug("Request to update HkjSalary : {}", hkjSalaryDTO);
        HkjSalary hkjSalary = hkjSalaryMapper.toEntity(hkjSalaryDTO);
        hkjSalary.setIsPersisted();
        hkjSalary = hkjSalaryRepository.save(hkjSalary);
        return hkjSalaryMapper.toDto(hkjSalary);
    }

    /**
     * Partially update a hkjSalary.
     *
     * @param hkjSalaryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HkjSalaryDTO> partialUpdate(HkjSalaryDTO hkjSalaryDTO) {
        log.debug("Request to partially update HkjSalary : {}", hkjSalaryDTO);

        return hkjSalaryRepository
            .findById(hkjSalaryDTO.getId())
            .map(existingHkjSalary -> {
                hkjSalaryMapper.partialUpdate(existingHkjSalary, hkjSalaryDTO);

                return existingHkjSalary;
            })
            .map(hkjSalaryRepository::save)
            .map(hkjSalaryMapper::toDto);
    }

    /**
     * Get one hkjSalary by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HkjSalaryDTO> findOne(Long id) {
        log.debug("Request to get HkjSalary : {}", id);
        return hkjSalaryRepository.findById(id).map(hkjSalaryMapper::toDto);
    }

    /**
     * Delete the hkjSalary by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete HkjSalary : {}", id);
        hkjSalaryRepository.deleteById(id);
    }
}
