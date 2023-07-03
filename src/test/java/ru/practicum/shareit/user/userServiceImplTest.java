package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.ContentAlreadyExistException;
import ru.practicum.shareit.exceptions.ContentNotFountException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class userServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    private UserDto createUser() {
        UserDto userDto = new UserDto();
        userDto.setEmail("akhraa1@yandex.ru");
        userDto.setId(1L);
        userDto.setName("Akhra");
        return userDto;
    }

    @Test
    public void getAllUsers_whenInvoked_thenReturnUsersCollection() {
        when(userRepository.findAll())
                .thenReturn(List.of(UserMapper.toUser(createUser()), UserMapper.toUser(createUser())));

        Assertions.assertEquals(userService.getAllUsers().size(), 2);
        assertThat(userService.getAllUsers().size(), equalTo(2));
        assertThat(userService.getAllUsers().get(0).getId(), equalTo(1L));
        assertThat(userService.getAllUsers().get(0).getId(), equalTo(1L));
    }

    @Test
    public void saveUser_whenInvoked_thenReturnSavedUser() {
        UserDto userDto = createUser();

        when(userRepository.save(any())).thenReturn(UserMapper.toUser(userDto));

        assertThat(userService.saveUser(userDto).getId(), equalTo(userDto.getId()));
        assertThat(userService.saveUser(userDto).getName(), equalTo(userDto.getName()));
        assertThat(userService.saveUser(userDto).getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    public void updateUser_whenUncreatedUser_ContentNotFountExceptionThrown() {
        UserDto userDto = createUser();
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        final ContentNotFountException exception = Assertions.assertThrows(
                ContentNotFountException.class,
                () -> userService.updateUser(userDto));

        Assertions.assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    public void updateUser_whenIdIsNull_thenContentNotFountExceptionThrown() {
        UserDto userDto = createUser();
        userDto.setId(null);
        final ContentNotFountException exception = Assertions.assertThrows(
                ContentNotFountException.class,
                () -> userService.updateUser(userDto));

        Assertions.assertEquals("Необходимо указать id пользователя", exception.getMessage());
    }

    @Test
    public void updateUser_whenEmailDuplicate_thenContentAlreadyExistExceptionThrown() {
        UserDto userDto = createUser();
        UserDto userDtoSameEmail = createUser();
        userDtoSameEmail.setId(2L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(UserMapper.toUser(userDto)));
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(UserMapper.toUser(userDtoSameEmail)));

        final ContentAlreadyExistException exception = Assertions.assertThrows(
                ContentAlreadyExistException.class,
                () -> userService.updateUser(userDto));

        Assertions.assertEquals("Пользователь с таким email уже существует", exception.getMessage());
    }

    @Test
    public void updateUser_whenInvoked_thenReturnUpdatedUser() {
        User oldUser = UserMapper.toUser(createUser());
        UserDto newUserdto = createUser();
        newUserdto.setName("Avidzba");
        newUserdto.setEmail("Avidzba");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(oldUser));
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(oldUser);

        UserDto updatedUser = userService.updateUser(newUserdto);

    }

}
