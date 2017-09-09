package org.openlearn.transformer;

import org.openlearn.domain.Authority;
import org.openlearn.domain.Course;
import org.openlearn.dto.CourseDTO;
import org.openlearn.repository.SessionRepository;
import org.openlearn.repository.UserRepository;
import org.openlearn.security.AuthoritiesConstants;
import org.springframework.stereotype.Service;

@Service
public class CourseTransformer {

	private static final Authority INSTRUCTOR = new Authority(AuthoritiesConstants.INSTRUCTOR);

	private final SessionRepository sessionRepository;

	private final UserRepository userRepository;

	public CourseTransformer(SessionRepository sessionRepository, UserRepository userRepository) {
		this.sessionRepository = sessionRepository;
		this.userRepository = userRepository;
	}

	public CourseDTO transform(final Course course) {
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
		return courseDTO;
	}

	public Course transform(final CourseDTO courseDTO) {
		Course course = new Course();
		course.setId(courseDTO.getId());
		course.setName(courseDTO.getName());
		course.setDescription(courseDTO.getDescription());
		course.setStartDate(courseDTO.getStartDate());
		course.setEndDate(courseDTO.getEndDate());
		course.setSession(sessionRepository.findOne(courseDTO.getSessionId()));
		course.setInstructor(userRepository.findOneByIdAndAuthority(courseDTO.getInstructorId(), INSTRUCTOR));
		course.setLocations(courseDTO.getLocations());
		course.setTimes(courseDTO.getTimes());
		return course;
	}
}
