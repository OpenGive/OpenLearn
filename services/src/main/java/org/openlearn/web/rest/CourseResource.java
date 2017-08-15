package org.openlearn.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.openlearn.domain.Course;
import org.openlearn.domain.CourseStudent;
import org.openlearn.domain.ItemLink;
import org.openlearn.service.CourseService;
import org.openlearn.service.StudentCourseService;
import org.openlearn.web.rest.util.HeaderUtil;
import org.openlearn.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing Course.
 */
@RestController
@RequestMapping("/api")
public class CourseResource {

	private final Logger log = LoggerFactory.getLogger(CourseResource.class);

	private static final String ENTITY_NAME = "course";

	private final CourseService courseService;

	private final StudentCourseService studentCourseService;

	public CourseResource(final CourseService courseService, final StudentCourseService studentCourseService) {
		this.courseService = courseService;
		this.studentCourseService = studentCourseService;
	}

	/**
	 * POST  /courses : Create a new course.
	 *
	 * @param course the course to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new course, or with status 400 (Bad Request) if the course has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/courses")
	@Timed
	public ResponseEntity<Course> createCourse(@Valid @RequestBody final Course course) throws URISyntaxException {
		log.debug("REST request to save Course : {}", course);
		if (course.getId() != null)
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new course cannot already have an ID")).body(null);
		final Course result = courseService.save(course);
		return ResponseEntity.created(new URI("/api/courses/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /courses : Updates an existing course.
	 *
	 * @param course the course to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated course,
	 * or with status 400 (Bad Request) if the course is not valid,
	 * or with status 500 (Internal Server Error) if the course couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/courses")
	@Timed
	public ResponseEntity<Course> updateCourse(@Valid @RequestBody final Course course) throws URISyntaxException {
		log.debug("REST request to update Course : {}", course);
		if (course.getId() == null)
			return createCourse(course);
		final Course result = courseService.save(course);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, course.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /courses : get all the courses.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of courses in body
	 */
	@GetMapping("/courses")
	@Timed
	public ResponseEntity<List<Course>> getAllCourses(@ApiParam final Pageable pageable) {
		log.debug("REST request to get a page of Courses");
		final Page<Course> page = courseService.findAll(pageable);
		final HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/courses");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET  /courses/:id : get the "id" course.
	 *
	 * @param id the id of the course to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the course, or with status 404 (Not Found)
	 */
	@GetMapping("/courses/{id}")
	@Timed
	public ResponseEntity<Course> getCourse(@PathVariable final Long id) {
		log.debug("REST request to get Course : {}", id);
		final Course course = courseService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(course));
	}

	/**
	 * DELETE  /courses/:id : delete the "id" course.
	 *
	 * @param id the id of the course to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/courses/{id}")
	@Timed
	public ResponseEntity<Void> deleteCourse(@PathVariable final Long id) {
		log.debug("REST request to delete Course : {}", id);
		courseService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * Get students for course
	 *
	 * @param id the id of the course to add the student to
	 * @return the students in the course
	 */
	@GetMapping("/courses/{id}/students")
	@Timed
	public ResponseEntity<List<CourseStudent>> studentsInCourse(@PathVariable final Long id, @ApiParam final Pageable pageable) {
		log.debug("REST request to get students in course with id {}", id);
		final Page<CourseStudent> page = studentCourseService.findByCourseId(id, pageable);
		final HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/courses"+id+"/students");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * Get students not in course
	 *
	 * @param id the id of the course to search
	 * @return the students not in the course
	 */
	@GetMapping("/courses/{id}/studentsNot/")
	@Timed
	public ResponseEntity<List<CourseStudent>> studentsNotInCourse(@PathVariable final Long id, @ApiParam final Pageable pageable) {
		log.debug("REST request to get students not in course with id {}", id);
		final Page<CourseStudent> page = studentCourseService.findByCourseId(id, pageable);
		final HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/courses"+id+"/studentsNot");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * Add Student to course  /courses/{id}/students?student_id={studentId}
	 *
	 * @param id the id of the course to add the student to
	 * @param studentId the id of the student to add to the course
	 * @return the student added to the course
	 */
	@PostMapping("/courses/{id}/students")
	@Timed
	public ResponseEntity<CourseStudent> addStudentToCourse(@PathVariable final Long id, @RequestParam final Long studentId) {
		log.debug("REST request to add student with id {} to course with id {}", studentId, id);
		final CourseStudent studentCourse = studentCourseService.addStudentToCourse(id, studentId);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(studentCourse));
	}

	/**
	 * Remove Student from course  /courses/{id}/students?student_id={studentId}
	 *
	 * @param id the id of the course to add the student to
	 * @param studentId the id of the student to add to the course
	 * @return the student added to the course
	 */
	@DeleteMapping("/courses/{id}/students/{studentId}")
	@Timed
	public ResponseEntity<CourseStudent> removeStudentFromCourse(@PathVariable final Long id, @PathVariable final Long studentId) {
		log.debug("REST request to add student with id {} to course with id {}", studentId, id);
		final CourseStudent studentCourse = studentCourseService.removeStudentFromCourse(id, studentId);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(studentCourse));
	}

	/**
	 * Assign a grade to a course and student association /courses/{id}/students?student_id={studentId}&grade={grade}
	 *
	 * @param id the id of the course to add the student to
	 * @param studentId the id of the student to add to the course
	 * @return the student added to the course
	 */
	@PostMapping("/courses/{id}/grade")
	@Timed
	public ResponseEntity<CourseStudent> addGradeToCourseStudent(@PathVariable final Long id, @RequestParam final Long studentId, @RequestParam final String grade) {
		log.debug("REST request to set student with id {} in course with id {} to have the grade {}", studentId, id, grade);
		final CourseStudent studentCourse = studentCourseService.addGradeToStudentCourse(id, studentId, grade);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(studentCourse));
	}

	/**
	 * Get the resources associated with a course
	 *
	 * @param courseId the id of the course to add the item link to
	 * @return the item links associated with the course
	 */
	@GetMapping("/courses/{courseId}/resources")
	@Timed
	public ResponseEntity<Set<ItemLink>> getCourseResources(@PathVariable final Long courseId){
		log.debug("REST request to get resources associated with course id : {}", courseId);
		final Set<ItemLink> results = courseService.getItemLinksForCourse(courseId);
		return new ResponseEntity<Set<ItemLink>>(results, HttpStatus.OK);
	}

	/**
	 * Add a resource item link to a course
	 *
	 * @param courseId the id of the course to add the item link to
	 * @param itemLinkId the id of the item link to add to the course
	 * @return the item links that are associated with the course
	 */
	@PostMapping("/courses/{courseId}/resources")
	@Timed
	public ResponseEntity<Set<ItemLink>> addResourceToCourse(@PathVariable final Long courseId, @RequestParam Long itemLinkId){
		log.debug("REST request to add item link with id {} to course with id {}", itemLinkId, courseId);
		final Set<ItemLink> result = courseService.addItemLinkToCourse(courseId, itemLinkId);
		return new ResponseEntity<Set<ItemLink>>(result, HttpStatus.OK);
	}

	/**
	 * Add a resource item link to a course
	 *
	 * @param courseId the id of the course to add the item link to
	 * @param itemLinkId the id of the item link to add to the course
	 * @return the item links that are associated with the course
	 */
	@DeleteMapping("/courses/{courseId}/resources/{itemLinkId}")
	@Timed
	public ResponseEntity<Set<ItemLink>> removeItemLinkFromCourse(@PathVariable final Long courseId, @PathVariable final Long itemLinkId){
		log.debug("REST request to remove item link with id {} from course id {}", itemLinkId, courseId);
		final Set<ItemLink> result = courseService.removeItemLinkFromCourse(courseId, itemLinkId);
		return new ResponseEntity<Set<ItemLink>>(result, HttpStatus.OK);
	}
}
