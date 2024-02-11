package kr.co.petmates.api.bussiness.oauth.controller;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import kr.co.petmates.api.bussiness.oauth.client.KakaoApiClient;
import kr.co.petmates.api.bussiness.oauth.service.AccessTokenStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kakao") // 로그아웃 컨트롤러에도 기본 경로 설정
public class LogoutController {
    private static final Logger logger = LoggerFactory.getLogger(KakaoOauthController.class);
    @Autowired
    private KakaoApiClient kakaoApiClient;
    @Autowired
    private AccessTokenStorage accessTokenStorage;

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {

        boolean isKakaoLogout = kakaoApiClient.kakaoLogout(session);
        logger.info("로그아웃 성공 여부: {}", isKakaoLogout);

        if (isKakaoLogout) {
            session.invalidate();

            Map<String, Object> response = new HashMap<>();
            response.put("result", "success");
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("result", "fail");
            response.put("data",isKakaoLogout);
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/logout/update")
    public ResponseEntity<?> logoutUpdate(HttpSession session) {
        String accessToken = accessTokenStorage.getAccessToken(session);
        logger.info("로그아웃 갱신 전 엑세스토큰: {}", accessToken);
        String refreshToken = accessTokenStorage.getRefreshToken(session);
        logger.info("로그아웃 갱신 전 리프레시토큰: {}", refreshToken);
        kakaoApiClient.updateAccessToken(session, refreshToken);

        kakaoApiClient.kakaoLogout(session);

        session.invalidate();

        Map<String, Object> response = new HashMap<>();
        response.put("result", "success");
        logger.info("로그아웃 카카오토큰 갱신 return: {}", ResponseEntity.ok(response));
        return ResponseEntity.ok(response);
    }
}







//        boolean isSuccessLogout = logoutService.logout(session);
//        return isSuccessLogout;
//            , @RequestHeader("Authorization") String jwtToken, @RequestHeader("Refresh-Token") String refreshToken) {
//        jwtToken = jwtToken.replace("Bearer ", "");
//        logger.info("로그아웃 jwtToken: {}", jwtToken);
//        // 카카오 로그아웃 API 호출
////        logoutService.logoutKakao(jwtToken);
//        String email = jwtTokenProvider.getEmail(refreshToken);
//        Optional<User> user = userRepository.findByEmail(email);
//        String getRefreshToken
//        // refreshToken 만료 처리
//        logoutService.kakaoLogout(refreshToken);
//
//        RedirectView redirectView = new RedirectView("http://localhost:3000/main");
//        redirectView.setStatusCode(HttpStatus.FOUND); // 여기에서 상태 코드를 설정합니다.
//        return redirectView;
        // 처리 완료 후 리다이렉트
//        return new RedirectView("http://localhost:3000/main", true, HttpStatus.FOUND.value(), false);
//    }
//}
