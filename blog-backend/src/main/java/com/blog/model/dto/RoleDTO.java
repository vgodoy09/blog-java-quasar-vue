package com.blog.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDTO {
	private String role;
	
	public Integer getPrivilegeByRole() {
        switch (this.role) {
            case "banned":
                return 0;
            case "regular":
                return 1;
            case "author":
                return 2;
            case "moderator":
                return 3;
            case "admin":
                return 4;
            default:
                return 0;
        }
    }
}
