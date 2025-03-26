package vn.titv.spring.ManageStudent.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import vn.titv.spring.ManageStudent.entity.RoleEntity;
import vn.titv.spring.ManageStudent.entity.UserEntity;
import vn.titv.spring.ManageStudent.repository.RoleRepository;
import vn.titv.spring.ManageStudent.repository.UserRepository;
import vn.titv.spring.ManageStudent.service.IUserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = this.userRepository.findByUserName(username);
        List<RoleEntity> roles = this.roleRepository.findAll() ;
        userEntity.setRoles(roles);
        if (userEntity == null) {
            throw new UsernameNotFoundException("Not found User with user name");
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(userEntity.getUserName())
                .password(userEntity.getPassword())
                .authorities(authorities(userEntity.getRoles()))
                .build();    }
    public List<SimpleGrantedAuthority> authorities (List<RoleEntity> roles) {
        return roles.stream().map(roleEntity -> new SimpleGrantedAuthority(roleEntity.getName())).collect(Collectors.toList());
    }

    public void addNewUser(UserEntity user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(this.roleRepository.findAll());
        this.userRepository.save(user);
    }
}
