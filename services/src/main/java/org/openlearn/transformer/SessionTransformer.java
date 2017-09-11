package org.openlearn.transformer;

import org.openlearn.domain.Session;
import org.openlearn.dto.SessionDTO;
import org.openlearn.repository.ProgramRepository;
import org.openlearn.repository.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SessionTransformer {

	private static final Logger log = LoggerFactory.getLogger(SessionTransformer.class);

	private final ProgramRepository programRepository;

	private final SessionRepository sessionRepository;

	public SessionTransformer(final ProgramRepository programRepository, final SessionRepository sessionRepository) {
		this.programRepository = programRepository;
		this.sessionRepository = sessionRepository;
	}

	/**
	 * Transforms an entity into a DTO
	 *
	 * @param session entity to transform
	 * @return the new DTO
	 */
	public SessionDTO transform(final Session session) {
		log.debug("Transforming session to session DTO : {}", session);
		SessionDTO sessionDTO = new SessionDTO();
		sessionDTO.setId(session.getId());
		sessionDTO.setName(session.getName());
		sessionDTO.setDescription(session.getDescription());
		sessionDTO.setStartDate(session.getStartDate());
		sessionDTO.setEndDate(session.getEndDate());
		sessionDTO.setProgramId(session.getProgram().getId());
		return sessionDTO;
	}

	/**
	 * Transforms a DTO into an entity
	 *
	 * @param sessionDTO DTO to transform
	 * @return the new entity
	 */
	public Session transform(final SessionDTO sessionDTO) {
		log.debug("Transforming session DTO to session : {}", sessionDTO);
		Session session = sessionDTO.getId() == null ? new Session() : sessionRepository.findOne(sessionDTO.getId());
		// TODO: Error handling
		session.setName(sessionDTO.getName());
		session.setDescription(sessionDTO.getDescription());
		session.setStartDate(sessionDTO.getStartDate());
		session.setEndDate(sessionDTO.getEndDate());
		session.setProgram(programRepository.findOne(sessionDTO.getProgramId()));
		session.setOrganization(session.getProgram().getOrganization());
		return session;
	}
}
