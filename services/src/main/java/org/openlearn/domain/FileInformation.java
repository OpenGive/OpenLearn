package org.openlearn.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "file_information")
public class FileInformation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "userId")
	private User user;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "courseId")
	private Course course;

	@Column(name = "fileUrl", nullable = false)
	private String fileUrl;

	@Column(name = "type", nullable = false)
	private String fileType; //TODO cbernal make this an enum, restrict DB values

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	//TODO cbernal write tostring, equals etc...from address
}
