package org.openlearn.transformer;

import org.openlearn.domain.Authority;
import org.openlearn.domain.StudentCourse;
import org.openlearn.dto.StudentCourseDTO;
import org.openlearn.repository.CourseRepository;
import org.openlearn.repository.StudentCourseRepository;
import org.openlearn.repository.UserRepository;
import org.openlearn.security.AuthoritiesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StudentCourseTransformer {

	private static final Authority STUDENT = new Authority(AuthoritiesConstants.STUDENT);

	private static final Logger log = LoggerFactory.getLogger(StudentCourseTransformer.class);

	private final CourseRepository courseRepository;

	private final CourseTransformer courseTransformer;

	private final StudentCourseRepository studentCourseRepository;

	private final StudentTransformer studentTransformer;

	private final UserRepository userRepository;

	public StudentCourseTransformer(final CourseRepository courseRepository, final CourseTransformer courseTransformer,
	                                final StudentCourseRepository studentCourseRepository,
	                                final StudentTransformer studentTransformer, final UserRepository userRepository) {
		this.courseRepository = courseRepository;
		this.courseTransformer = courseTransformer;
		this.studentCourseRepository = studentCourseRepository;
		this.studentTransformer = studentTransformer;
		this.userRepository = userRepository;
	}

	/**
	 * Transforms an entity into a DTO
	 *
	 * @param studentCourse entity to transform
	 * @return the new DTO with respective student and course DTOs
	 */
	public StudentCourseDTO transform(final StudentCourse studentCourse) {
		return transform(studentCourse, true, true);
	}

	/**
	 * Transforms an entity into a DTO
	 *
	 * @param studentCourse entity to transform
	 * @param withStudent flag indicating whether to include the student DTO
	 * @param withCourse flag indicating whether to include the course DTO
	 * @return the new DTO
	 */
	public StudentCourseDTO transform(final StudentCourse studentCourse, final boolean withStudent,
	                                  final boolean withCourse) {
		log.debug("Transforming student course to student course DTO with"
			+ (withStudent ? "" : "out") + " student and with"
			+ (withCourse ? "" : "out") + " course : {}", studentCourse);
		StudentCourseDTO studentCourseDTO = new StudentCourseDTO();
		studentCourseDTO.setId(studentCourse.getId());
		studentCourseDTO.setStudentId(studentCourse.getStudent().getId());
		studentCourseDTO.setCourseId(studentCourse.getCourse().getId());
		if (withStudent) {
			studentCourseDTO.setStudent(studentTransformer.transform(studentCourse.getStudent()));
		}
		if (withCourse) {
			studentCourseDTO.setCourse(courseTransformer.transform(studentCourse.getCourse()));
		}
		studentCourseDTO.setGrade(studentCourse.getGrade());
		studentCourseDTO.setEnrollDate(studentCourse.getEnrollDate());
		studentCourseDTO.setDropDate(studentCourse.getDropDate());
		studentCourseDTO.setComplete(studentCourse.getComplete());
		studentCourseDTO.setOnPortfolio(studentCourse.getOnPortfolio());
		return studentCourseDTO;
	}

	/**
	 * Transforms a DTO into an entity
	 *
	 * @param studentCourseDTO DTO to transform
	 * @return the new entity
	 */
	public StudentCourse transform(final StudentCourseDTO studentCourseDTO) {
		log.debug("Transforming student course DTO to student course : {}", studentCourseDTO);
		StudentCourse studentCourse = studentCourseDTO.getId() == null ? new StudentCourse() : studentCourseRepository.findOne(studentCourseDTO.getId());
		// TODO: Error handling
		studentCourse.setStudent(userRepository.findOneByIdAndAuthority(studentCourseDTO.getStudentId(), STUDENT));
		studentCourse.setCourse(courseRepository.findOne(studentCourseDTO.getCourseId()));
		studentCourse.setGrade(studentCourseDTO.getGrade());
		studentCourse.setEnrollDate(studentCourseDTO.getEnrollDate());
		studentCourse.setDropDate(studentCourseDTO.getDropDate());
		studentCourse.setComplete(studentCourseDTO.getComplete());
		studentCourse.setOnPortfolio(studentCourseDTO.getOnPortfolio());
		return studentCourse;
	}
}
