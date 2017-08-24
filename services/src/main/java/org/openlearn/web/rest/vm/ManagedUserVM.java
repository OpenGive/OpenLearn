package org.openlearn.web.rest.vm;

import java.time.ZonedDateTime;
import java.util.Set;

import javax.validation.constraints.Size;

import org.openlearn.domain.Address;
import org.openlearn.service.dto.UserDTO;

/**
 * View Model extending the UserDTO, which is meant to be used in the user management UI.
 */
public class ManagedUserVM extends UserDTO {

	public static final int PASSWORD_MIN_LENGTH = 6;

	public static final int PASSWORD_MAX_LENGTH = 50;

	@Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
	private String password;

	public ManagedUserVM() {
		// Empty constructor needed for Jackson.
	}

	public ManagedUserVM(final Long id, final String login, final String password, final String firstName, final String lastName,
                       final String email, final String phoneNumber, final Address address, final boolean activated,
                       final String imageUrl, final String createdBy, final ZonedDateTime createdDate, final String lastModifiedBy,
                       final ZonedDateTime lastModifiedDate, final Set<String> authorities, final boolean is14Plus, final String biography, final Set<Long> organizationIds){

		super(id, login, firstName, lastName, email, phoneNumber, address, activated, imageUrl,
				createdBy, createdDate, lastModifiedBy, lastModifiedDate, authorities, is14Plus, biography, organizationIds);

		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return "ManagedUserVM{" +
				"} " + super.toString();
	}
}
