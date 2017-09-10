package org.openlearn.transformer;

import org.openlearn.domain.Assignment;
import org.openlearn.dto.AssignmentDTO;
import org.openlearn.repository.CourseRepository;
import org.springframework.stereotype.Service;

@Service
public class AssignmentTransformer {

	private final CourseRepository courseRepository;

	public AssignmentTransformer(CourseRepository courseRepository) {
		this.courseRepository = courseRepository;
	}

	public AssignmentDTO transform(final Assignment assignment) {
		AssignmentDTO assignmentDTO = new AssignmentDTO();
		assignmentDTO.setId(assignment.getId());
		assignmentDTO.setName(assignment.getName());
		assignmentDTO.setDescription(assignment.getDescription());
		assignmentDTO.setCourseId(assignment.getCourse().getId());
		return assignmentDTO;
	}

	public Assignment transform(final AssignmentDTO assignmentDTO) {
		Assignment assignment = new Assignment();
		assignment.setId(assignmentDTO.getId());
		assignment.setName(assignmentDTO.getName());
		assignment.setDescription(assignmentDTO.getDescription());
		assignment.setCourse(courseRepository.findOne(assignmentDTO.getCourseId()));
		assignment.setOrganization(assignment.getCourse().getOrganization());
		return assignment;
	}
}
