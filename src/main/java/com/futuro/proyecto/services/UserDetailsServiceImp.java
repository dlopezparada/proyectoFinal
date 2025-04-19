package com.futuro.proyecto.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.futuro.proyecto.models.Admin;
import com.futuro.proyecto.repository.AdminRepository;

@Service
public class UserDetailsServiceImp implements UserDetailsService{

	@Autowired
    private AdminRepository adminRepository;
	
	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("El administrador con nombre de usuario %s no existe en la base de datos", username)));

        List<SimpleGrantedAuthority> authoritiesList = new ArrayList<>();
        admin.getRoles().forEach(role -> {
            authoritiesList.add(new SimpleGrantedAuthority(role.getRoleEnum().name()));
            role.getPermisos().forEach(permiso -> authoritiesList.add(new SimpleGrantedAuthority(permiso.getPermiso())));
        });
//        authoritiesList.add(new SimpleGrantedAuthority("ADMIN"));

        User user = new User(
                admin.getUsername(),
                admin.getPassword(),
                authoritiesList
        );
        return user;
    }
}
