package com.inzent.todo.security;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inzent.todo.service.JwtService;
import com.inzent.todo.vo.UserVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class AuthInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private JwtService jwtService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 1. handler 여부 판단
		// System.out.println("Auth Interceptor-------------");
		if (handler instanceof HandlerMethod == false) {
			return true;
		}
		// 2. @Auth여부 판단
		Auth auth = ((HandlerMethod) handler).getMethodAnnotation(Auth.class);
		if (auth == null)
			return true;
		// 3. 접근제어 수항
		String jwtToken = request.getHeader("Authorization");
		jwtToken = jwtToken.replace("Bearer ", "");
		if (jwtToken == null) {
			// response.sendRedirect(request.getContextPath() + "/login");
			response.setStatus(401);
			throw new AuthenticationException("접근 권한이 없습니다.");
		}
		// System.out.println(jwtService);
		if (jwtService.isValidToken(jwtToken)) {
			UserVo authUser = jwtService.getUser(jwtToken);
			if (authUser == null) {
				// System.out.println("!! authUser == null !!");
				throw new AuthenticationException("접근 권한이 없습니다.");
			}
			// System.out.println("로그인성공---" + authUser.toString());
			request.setAttribute("user", authUser);
		} else {
			// response.sendRedirect(request.getContextPath() + "/login");
			response.setStatus(401);
			throw new AuthenticationException("접근 권한이 없습니다.");
		}
		// 4단계 : 모든 인증 완료(인증된 사용자)
		return true;
	}

}
