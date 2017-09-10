package org.openlearn.transformer;

import org.openlearn.domain.Session;
import org.openlearn.dto.SessionDTO;
import org.openlearn.repository.ProgramRepository;
import org.springframework.stereotype.Service;

@Service
public class SessionTransformer {

	private final ProgramRepository programRepository;

	public SessionTransformer(ProgramRepository programRepository) {
		this.programRepository = programRepository;
	}

	public SessionDTO transform(final Session session) {
		SessionDTO sessionDTO = new SessionDTO();
		sessionDTO.setId(session.getId());
		sessionDTO.setName(session.getName());
		sessionDTO.setDescription(session.getDescription());
		sessionDTO.setStartDate(session.getStartDate());
		sessionDTO.setEndDate(session.getEndDate());
		sessionDTO.setProgramId(session.getProgram().getId());
		return sessionDTO;
	}

	public Session transform(final SessionDTO sessionDTO) {
		Session session = new Session();
		session.setId(sessionDTO.getId());
		session.setName(sessionDTO.getName());
		session.setDescription(sessionDTO.getDescription());
		session.setStartDate(sessionDTO.getStartDate());
		session.setEndDate(sessionDTO.getEndDate());
		session.setProgram(programRepository.findOne(sessionDTO.getProgramId()));
		session.setOrganization(session.getProgram().getOrganization());
		return session;
	}
}
