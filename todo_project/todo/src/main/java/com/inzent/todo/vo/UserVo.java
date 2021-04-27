package com.inzent.todo.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// import java.io.Serializable;
// import java.util.List;

// public class UserVo implements Serializable {
@Getter
@Setter
@ToString
public class UserVo {
	private String id;
	private String password;
	private String name;
	private String birth;
	private String gender;
	private String phone;
	private String email;
	private String userType;
	private String rank;
	private String imgCode;
	private String deptCode;
}