package org.openlearn.service;

import org.openlearn.domain.Address;
import org.openlearn.repository.AddressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing Address.
 */
@Service
@Transactional
public class AddressService {

	private static final Logger log = LoggerFactory.getLogger(AddressService.class);

	private final AddressRepository addressRepository;

	public AddressService(final AddressRepository addressRepository) {
		this.addressRepository = addressRepository;
	}

	/**
	 * Save a address.
	 *
	 * @param address the entity to save
	 * @return the persisted entity
	 */
	public Address save(final Address address) {
		log.debug("Request to save Address : {}", address);
		return addressRepository.save(address);
	}

	/**
	 * Get all the addresses.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<Address> findAll(final Pageable pageable) {
		log.debug("Request to get all Addresses");
		return addressRepository.findAll(pageable);
	}

	/**
	 * Get one address by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public Address findOne(final Long id) {
		log.debug("Request to get Address : {}", id);
		return addressRepository.findOne(id);
	}

	/**
	 * Delete the  address by id.
	 *
	 * @param id the id of the entity
	 */
	public void delete(final Long id) {
		log.debug("Request to delete Address : {}", id);
		addressRepository.delete(id);
	}
}
