package org.openlearn.transformer;

import org.openlearn.domain.Authority;
import org.openlearn.domain.Course;
import org.openlearn.dto.CourseDTO;
import org.openlearn.dto.InstructorDTO;
import org.openlearn.repository.CourseRepository;
import org.openlearn.repository.SessionRepository;
import org.openlearn.repository.UserRepository;
import org.openlearn.security.AuthoritiesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CourseTransformer {

	private static final Authority INSTRUCTOR = new Authority(AuthoritiesConstants.INSTRUCTOR);

	private static final Logger log = LoggerFactory.getLogger(CourseTransformer.class);

	private final CourseRepository courseRepository;

	private final SessionRepository sessionRepository;

	private final UserRepository userRepository;

	private final UserTransformer userTransformer;

	private final SessionTransformer sessionTransformer;

	public CourseTransformer(final CourseRepository courseRepository, final SessionRepository sessionRepository,
	                         final UserRepository userRepository, final UserTransformer userTransformer,
	                         final SessionTransformer sessionTransformer) {
		this.courseRepository = courseRepository;
		this.sessionRepository = sessionRepository;
		this.userRepository = userRepository;
		this.userTransformer = userTransformer;
		this.sessionTransformer = sessionTransformer;
	}

	/**
	 * Transforms an entity into a DTO
	 *
	 * @param course entity to transform
	 * @return the new DTO
	 */
	public CourseDTO transform(final Course course) {
		log.debug("Transforming course to course DTO : {}", course);
		CourseDTO courseDTO = new CourseDTO();
		courseDTO.setId(course.getId());
		courseDTO.setName(course.getName());
		courseDTO.setDescription(course.getDescription());
		courseDTO.setStartDate(course.getStartDate());
		courseDTO.setEndDate(course.getEndDate());
		courseDTO.setSessionId(course.getSession().getId());
		courseDTO.setInstructorId(course.getInstructor().getId());
		courseDTO.setLocations(course.getLocations());
		courseDTO.setTimes(course.getTimes());
		courseDTO.setSession(this.sessionTransformer.transform(course.getSession()));
		InstructorDTO instructorDTO = new InstructorDTO();
		this.userTransformer.transformUserToDTO(instructorDTO, course.getInstructor());
		courseDTO.setInstructor(instructorDTO);
		return courseDTO;
	}

	/**
	 * Transforms a DTO into an entity
	 *
	 * @param courseDTO DTO to transform
	 * @return the new entity
	 */
	public Course transform(final CourseDTO courseDTO) {
		log.debug("Transforming course DTO to course : {}", courseDTO);
		Course course = courseDTO.getId() == null ? new Course() : courseRepository.findOne(courseDTO.getId());
		// TODO: Error handling
		course.setName(courseDTO.getName());
		course.setDescription(courseDTO.getDescription());
		course.setStartDate(courseDTO.getStartDate());
		course.setEndDate(courseDTO.getEndDate());
		course.setSession(sessionRepository.findOne(courseDTO.getSessionId()));
		course.setOrganization(course.getSession().getOrganization());
		course.setInstructor(userRepository.findOneByIdAndAuthority(courseDTO.getInstructorId(), INSTRUCTOR));
		course.setLocations(courseDTO.getLocations());
		course.setTimes(courseDTO.getTimes());
		return course;
	}
}
