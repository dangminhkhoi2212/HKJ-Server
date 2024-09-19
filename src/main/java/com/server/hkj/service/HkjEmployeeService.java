package com.server.hkj.service;

import com.server.hkj.domain.HkjEmployee;
import com.server.hkj.repository.HkjEmployeeRepository;
import com.server.hkj.service.dto.HkjEmployeeDTO;
import com.server.hkj.service.mapper.HkjEmployeeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.server.hkj.domain.HkjEmployee}.
 */
@Service
@Transactional
public class HkjEmployeeService {

    private static final Logger log = LoggerFactory.getLogger(HkjEmployeeService.class);

    private final HkjEmployeeRepository hkjEmployeeRepository;

    private final HkjEmployeeMapper hkjEmployeeMapper;

    public HkjEmployeeService(HkjEmployeeRepository hkjEmployeeRepository, HkjEmployeeMapper hkjEmployeeMapper) {
        this.hkjEmployeeRepository = hkjEmployeeRepository;
        this.hkjEmployeeMapper = hkjEmployeeMapper;
    }

    /**
     * Save a hkjEmployee.
     *
     * @param hkjEmployeeDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjEmployeeDTO save(HkjEmployeeDTO hkjEmployeeDTO) {
        log.debug("Request to save HkjEmployee : {}", hkjEmployeeDTO);
        HkjEmployee hkjEmployee = hkjEmployeeMapper.toEntity(hkjEmployeeDTO);
        hkjEmployee = hkjEmployeeRepository.save(hkjEmployee);
        return hkjEmployeeMapper.toDto(hkjEmployee);
    }

    /**
     * Update a hkjEmployee.
     *
     * @param hkjEmployeeDTO the entity to save.
     * @return the persisted entity.
     */
    public HkjEmployeeDTO update(HkjEmployeeDTO hkjEmployeeDTO) {
        log.debug("Request to update HkjEmployee : {}", hkjEmployeeDTO);
        HkjEmployee hkjEmployee = hkjEmployeeMapper.toEntity(hkjEmployeeDTO);
        hkjEmployee.setIsPersisted();
        hkjEmployee = hkjEmployeeRepository.save(hkjEmployee);
        return hkjEmployeeMapper.toDto(hkjEmployee);
    }

    /**
     * Partially update a hkjEmployee.
     *
     * @param hkjEmployeeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HkjEmployeeDTO> partialUpdate(HkjEmployeeDTO hkjEmployeeDTO) {
        log.debug("Request to partially update HkjEmployee : {}", hkjEmployeeDTO);

        return hkjEmployeeRepository
            .findById(hkjEmployeeDTO.getId())
            .map(existingHkjEmployee -> {
                hkjEmployeeMapper.partialUpdate(existingHkjEmployee, hkjEmployeeDTO);

                return existingHkjEmployee;
            })
            .map(hkjEmployeeRepository::save)
            .map(hkjEmployeeMapper::toDto);
    }

    /**
     *  Get all the hkjEmployees where HkjHire is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<HkjEmployeeDTO> findAllWhereHkjHireIsNull() {
        log.debug("Request to get all hkjEmployees where HkjHire is null");
        return StreamSupport.stream(hkjEmployeeRepository.findAll().spliterator(), false)
            .filter(hkjEmployee -> hkjEmployee.getHkjHire() == null)
            .map(hkjEmployeeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one hkjEmployee by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HkjEmployeeDTO> findOne(Long id) {
        log.debug("Request to get HkjEmployee : {}", id);
        return hkjEmployeeRepository.findById(id).map(hkjEmployeeMapper::toDto);
    }

    /**
     * Delete the hkjEmployee by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete HkjEmployee : {}", id);
        hkjEmployeeRepository.deleteById(id);
    }
}
