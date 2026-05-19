package com.bookmyshow.bms.Service;

import com.bookmyshow.bms.Dto.UserDto;
import com.bookmyshow.bms.Exception.resourceNotFoundException;
import com.bookmyshow.bms.Repository.UserRepository;
import com.bookmyshow.bms.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private UserDto createUser(UserDto userDto) {
        User user = mapTOEntity(userDto);
        User savedUser = userRepository.save(user);
        return mapToDto(savedUser);
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new resourceNotFoundException("User not found with id :"+id));
        return mapToDto(user);
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public UserDto updateUser(Long id,UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new resourceNotFoundException("User not found with id :"+id));
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhoneNumber());
        User savedUser = userRepository.save(user);
        return mapToDto(savedUser);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new resourceNotFoundException("User not found with id :"+id));
        userRepository.delete(user);
    }

    private User mapTOEntity(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhoneNumber());
        return user;
    }

    private UserDto mapToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhone());
        return userDto;
    }

}
