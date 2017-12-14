package org.openlearn.service;

import org.openlearn.domain.Program;
import org.openlearn.domain.Session;
import org.openlearn.domain.User;
import org.openlearn.dto.SessionDTO;
import org.openlearn.repository.ProgramRepository;
import org.openlearn.repository.SessionRepository;
import org.openlearn.security.SecurityUtils;
import org.openlearn.transformer.SessionTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Session.
 */
@Service
@Transactional
public class SessionService {

	private static final Logger log = LoggerFactory.getLogger(SessionService.class);

	private final ProgramRepository programRepository;

	private final SessionRepository sessionRepository;

	private final SessionTransformer sessionTransformer;

	private final UserService userService;

	public SessionService(final ProgramRepository programRepository, final SessionRepository sessionRepository,
	                      final SessionTransformer sessionTransformer, final UserService userService) {
		this.programRepository = programRepository;
		this.sessionRepository = sessionRepository;
		this.sessionTransformer = sessionTransformer;
		this.userService = userService;
	}

	/**
	 * Save a session.
	 *
	 * @param sessionDTO the entity to save
	 * @return the persisted entity
	 */
	public SessionDTO save(final SessionDTO sessionDTO) {
		log.debug("Request to save Session : {}", sessionDTO);
		if (SecurityUtils.isAdmin() || inOrgOfCurrentUser(sessionDTO)) {
			return sessionTransformer.transform(sessionRepository.save(sessionTransformer.transform(sessionDTO)));
		}
		// TODO: Error handling / logging
		return null;
	}

	/**
	 * Get all the sessions.
	 *
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public List<SessionDTO> findAll() {
		log.debug("Request to get all Sessions");
		User user = userService.getCurrentUser();
		if (SecurityUtils.isAdmin()) {
			return sessionRepository.findAll()
				.stream()
				.map(sessionTransformer::transform)
				.collect(Collectors.toList());
		} else {
			return sessionRepository.findByOrganization(user.getOrganization())
				.stream()
				.map(sessionTransformer::transform)
				.collect(Collectors.toList());
		}
	}

	/**
	 * Get one session by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public SessionDTO findOne(final Long id) {
		log.debug("Request to get Session : {}", id);
		Session session = sessionRepository.findOne(id);
		if (session != null && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(session))) {
			return sessionTransformer.transform(session);
		}
		// TODO: Error handling / logging
		return null;
	}

	/**
	 * Delete the session by id.
	 *
	 * @param id the id of the entity
	 */
	public void delete(final Long id) {
		log.debug("Request to delete Session : {}", id);
		Session session = sessionRepository.findOne(id);
		if (session != null && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(session))) {
			sessionRepository.delete(id);
		} else {
			// TODO: Error handling / logging
		}
	}

	private boolean inOrgOfCurrentUser(final SessionDTO sessionDTO) {
		User user = userService.getCurrentUser();
		Program program = programRepository.findOne(sessionDTO.getProgramId());
		return program != null && user.getOrganization().equals(program.getOrganization());
	}

	private boolean inOrgOfCurrentUser(final Session session) {
		User user = userService.getCurrentUser();
		return user.getOrganization().equals(session.getProgram().getOrganization());
	}
}
