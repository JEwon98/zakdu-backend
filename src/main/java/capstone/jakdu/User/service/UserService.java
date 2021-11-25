package capstone.jakdu.User.service;

import capstone.jakdu.User.dto.UserSaveRequestDto;
import capstone.jakdu.User.domain.User;
import capstone.jakdu.User.domain.AuthRequest;
import capstone.jakdu.User.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

//@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserInfoRepository userInfoRepository;

    /* 유저 등록 */
    @Transactional(rollbackOn = {Exception.class})
    public void userRegister(UserSaveRequestDto userSaveRequestDto) throws IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
        User user = User.of(
                userSaveRequestDto.getUsername(),
                userSaveRequestDto.getEmail(),
                userSaveRequestDto.getPassword(),
                userSaveRequestDto.getUserType(),
                userSaveRequestDto.getPoint()
        );
        userInfoRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userInfoRepository.findByUsername(username);
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

//    @Transactional
//    public UserResponseDto getUserInfo(String email) {
//        return userInfoRepository.findByEmail(email)
//                .map(UserResponseDto::of)
//                .orElseThrow(()-> new RuntimeException("유저 정보가 없습니다."));
//    }
    
    

    /* 비밀번호 검사 */

//
//    @Transactional
//    public Long save(UserSaveRequestDto requestDto) {
//        return userInfoRepository.save(requestDto.toEntity()).getId();
//    }
//
//
//    public Long update(Long id, UserUpdateRequestDto requestDto) {
//        User user = userInfoRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("f"));
//        user.update(requestDto.getUsername(), requestDto.getEmail(), requestDto.getPassword(), requestDto.getUserType(), requestDto.getPoint());
//        return id;
//    }
//    public UserResponseDto findById(Long id) {
//        User entity = userInfoRepository.findById(id)
//                .orElseThrow(()-> new IllegalArgumentException("f"));
//
//        return new UserResponseDto(entity);
//    }
}