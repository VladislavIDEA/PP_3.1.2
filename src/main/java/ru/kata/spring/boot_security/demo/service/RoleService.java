package ru.kata.spring.boot_security.demo.service;



import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;

public interface RoleService {

    List<Role> findAll();

    void add(Role role);

    Role findByName(String name);

    Role findById(int id);
}
