package org.openlearn.transformer;

import org.openlearn.domain.Assignment;
import org.openlearn.dto.AssignmentDTO;
import org.openlearn.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AssignmentTransformer {

	private static final Logger log = LoggerFactory.getLogger(AssignmentTransformer.class);

	private final CourseRepository courseRepository;

	public AssignmentTransformer(CourseRepository courseRepository) {
		this.courseRepository = courseRepository;
	}

	/**
	 * Transforms an entity into a DTO
	 *
	 * @param assignment entity to transform
	 * @return the new DTO
	 */
	public AssignmentDTO transform(final Assignment assignment) {
		log.debug("Transforming assignment to assignment DTO : {}", assignment);
		AssignmentDTO assignmentDTO = new AssignmentDTO();
		assignmentDTO.setId(assignment.getId());
		assignmentDTO.setName(assignment.getName());
		assignmentDTO.setDescription(assignment.getDescription());
		assignmentDTO.setCourseId(assignment.getCourse().getId());
		return assignmentDTO;
	}

	/**
	 * Transforms a DTO into an entity
	 *
	 * @param assignmentDTO DTO to transform
	 * @return the new entity
	 */
	public Assignment transform(final AssignmentDTO assignmentDTO) {
		log.debug("Transforming assignment DTO to assignment : {}", assignmentDTO);
		Assignment assignment = new Assignment();
		assignment.setId(assignmentDTO.getId());
		assignment.setName(assignmentDTO.getName());
		assignment.setDescription(assignmentDTO.getDescription());
		assignment.setCourse(courseRepository.findOne(assignmentDTO.getCourseId()));
		assignment.setOrganization(assignment.getCourse().getOrganization());
		return assignment;
	}
}
