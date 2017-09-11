package org.openlearn.transformer;

import org.openlearn.domain.Authority;
import org.openlearn.domain.StudentCourse;
import org.openlearn.dto.StudentCourseDTO;
import org.openlearn.repository.CourseRepository;
import org.openlearn.repository.UserRepository;
import org.openlearn.security.AuthoritiesConstants;
import org.springframework.stereotype.Service;

@Service
public class StudentCourseTransformer {

	private static final Authority STUDENT = new Authority(AuthoritiesConstants.STUDENT);

	private final CourseRepository courseRepository;

	private final CourseTransformer courseTransformer;

	private final StudentTransformer studentTransformer;

	private final UserRepository userRepository;

	public StudentCourseTransformer(CourseRepository courseRepository, CourseTransformer courseTransformer,
	                                StudentTransformer studentTransformer, UserRepository userRepository) {
		this.courseRepository = courseRepository;
		this.courseTransformer = courseTransformer;
		this.studentTransformer = studentTransformer;
		this.userRepository = userRepository;
	}

	public StudentCourseDTO transform(StudentCourse studentCourse) {
		return transform(studentCourse, true, true);
	}

	public StudentCourseDTO transform(StudentCourse studentCourse, boolean withStudent, boolean withCourse) {
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

	public StudentCourse transform(StudentCourseDTO studentCourseDTO) {
		StudentCourse studentCourse = new StudentCourse();
		studentCourse.setId(studentCourseDTO.getId());
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
