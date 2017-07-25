package org.openlearn.service;

import org.openlearn.domain.Session;
import org.openlearn.domain.User;
import org.openlearn.repository.SessionRepository;
import org.openlearn.repository.UserRepository;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.security.Security;
import java.util.Optional;

/**
 * Service Implementation for managing Session.
 */
@Service
@Transactional
public class SessionService {

    private final Logger log = LoggerFactory.getLogger(SessionService.class);

    private final SessionRepository sessionRepository;

    private final UserRepository userRepository;

    public SessionService(SessionRepository sessionRepository, UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    /**
     * Save a session.
     *
     * @param session the entity to save
     * @return the persisted entity
     */
    public Session save(Session session) {
        log.debug("Request to save Session : {}", session);
		if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
			return sessionRepository.save(session);
		}
		Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
		Session orig = findOne(session.getId());
		if(orig != null){
			return sessionRepository.save(session);
		}
		return null;
    }

    /**
     *  Get all the sessions.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Session> findAll(Pageable pageable) {
        log.debug("Request to get all Sessions");
		if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
			return sessionRepository.findAll(pageable);
		}
		Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
		return sessionRepository.findAllByOrganization(pageable, user.get().organizationIds);
    }

	/**
	 *  Get all the sessions by user orgs
	 *
	 *  @param pageable the pagination information
	 *  @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<Session> findAllOrgSessions(Pageable pageable) {
		log.debug("Request to get all Sessions");
		if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
			return sessionRepository.findAll(pageable);
		}
		Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
		return sessionRepository.findAllByOrganization(pageable,user.get().getOrganizationIds());
	}

	/**
     *  Get one session by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Session findOne(Long id) {
        log.debug("Request to get Session : {}", id);
        if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
			return sessionRepository.findOneWithEagerRelationships(id);
		}
		Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        return sessionRepository.findOneByIdAndOrgIdsWithEagerRelationships(id, user.get().getOrganizationIds());
    }

    /**
     *  Delete the  session by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Session : {}", id);
		if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
			sessionRepository.delete(id);
		}
		Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
		Session orig = findOne(id);
		if(orig != null){
			sessionRepository.delete(id);
		}
    }
}
