package com.futuro.proyecto.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futuro.proyecto.dto.AdminDto;
import com.futuro.proyecto.models.Admin;
import com.futuro.proyecto.repository.AdminRepository;

@Service
public class AdminServiceImp implements AdminService{

	@Autowired
    private AdminRepository adminRepository;
	
	@Override
    public AdminDto findByUsername(String username) {
		Admin admin = adminRepository.findByUsername(username).orElse(new Admin());
        if (admin == null || admin.getId() == null) {
            return new AdminDto(); // O puedes devolver un AdminDto vacío, según tu necesidad
        }
        return AdminDto.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .password(admin.getPassword())
                .build();
    }

}
