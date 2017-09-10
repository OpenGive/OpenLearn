package org.openlearn.transformer;

import org.openlearn.domain.User;
import org.openlearn.dto.AdminDTO;
import org.springframework.stereotype.Service;

@Service
public class AdminTransformer {

	private final UserTransformer userTransformer;

	public AdminTransformer(final UserTransformer userTransformer) {
		this.userTransformer = userTransformer;
	}

	public AdminDTO transform(final User user) {
		AdminDTO adminDTO = new AdminDTO();
		userTransformer.transformUserToDTO(adminDTO, user);
		return adminDTO;
	}

	public User transform(final AdminDTO adminDTO) {
		User user = new User();
		userTransformer.transformDTOToUser(user, adminDTO);
		return user;
	}
}
