package org.openlearn.service;

import org.openlearn.domain.Organization;
import org.openlearn.domain.User;
import org.openlearn.exceptions.NotAuthorizedException;
import org.openlearn.repository.OrganizationRepository;
import org.openlearn.repository.UserRepository;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing Organization.
 */
@Service
@Transactional
public class OrganizationService {

    private final Logger log = LoggerFactory.getLogger(OrganizationService.class);

    private final OrganizationRepository organizationRepository;

    private final UserRepository userRepository;

    public OrganizationService(OrganizationRepository organizationRepository, UserRepository userRepository) {
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
    }

    /**
     * Save a organization.
     *
     * @param organization the entity to save
     * @return the persisted entity
     */
	public Organization save(Organization organization) {
        log.debug("Request to save Organization : {}", organization);
		Organization result = organizationRepository.save(organization);
		return result;
    }

    /**
     *  Get all the organizations.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Organization> findAll(Pageable pageable) {
        log.debug("Request to get all Organizations");
        if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
			Page<Organization> result = organizationRepository.findAll(pageable);
			return result;
		}
		Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        return organizationRepository.findAllByIdIn(pageable, user.get().getOrganizationIds());
    }

    /**
     *  Get one organization by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Organization findOne(Long id) {
        log.debug("Request to get Organization : {}", id);
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        if(user.get().getOrganizationIds().contains(id)){
			return organizationRepository.findOne(id);
		}
        return null;
    }

    /**
     *  Delete the  organization by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Organization : {}", id);
		organizationRepository.delete(id);
    }

    public Set<User> getUsersForOrganization(Long organizationId){
    	log.debug("Request to get Users for organization : {}", organizationId);
		Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
		if(user.get().getOrganizationIds().contains(organizationId)){
			return userRepository.findAllByOrganizationIds(organizationId);
		}
		return null;
	}

	public Organization addUserToOrganization(Long organizationId, Long userId) {
    	User user = userRepository.findOne(userId);
    	Organization organization = organizationRepository.findOne(organizationId);
		if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
			organization.getUserIds().add(userId);
			organizationRepository.save(organization);
		}
    	return null;
	}

	public Organization removeUserFromOrganization(Long organizationId, Long userId) {
    	Organization organization = organizationRepository.findOne(organizationId);
		if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
			organization.getUserIds().remove(userId);
			organizationRepository.save(organization);
		}
    	return null;
	}
}
