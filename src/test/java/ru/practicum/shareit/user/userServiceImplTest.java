package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.ContentAlreadyExistException;
import ru.practicum.shareit.exceptions.ContentNotFountException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class userServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    @Captor
    ArgumentCaptor<User> userArgumentCaptor;

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

        assertThat(userService.saveUser(userDto), equalTo(userDto));
    }

    @Test
    public void updateUser_whenUncreatedUser_ContentNotFountExceptionThrown() {
        UserDto userDto = createUser();
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        final ContentNotFountException exception = Assertions.assertThrows(
                ContentNotFountException.class,
                () -> userService.updateUser(userDto));

        verify(userRepository, never()).save(any());
        Assertions.assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    public void updateUser_whenIdIsNull_thenContentNotFountExceptionThrown() {
        UserDto userDto = createUser();
        userDto.setId(null);
        final ContentNotFountException exception = Assertions.assertThrows(
                ContentNotFountException.class,
                () -> userService.updateUser(userDto));

        verify(userRepository, never()).save(any());
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

        verify(userRepository, never()).save(any());
        Assertions.assertEquals("Пользователь с таким email уже существует", exception.getMessage());
    }

    @Test
    public void updateUser_whenUserFoundWithoutEmailDuplicate_thenReturnUpdatedUser() {
        UserDto oldUserDto = createUser();
        User oldUser = UserMapper.toUser(oldUserDto);
        UserDto newUserDto = createUser();
        newUserDto.setName("Anri");
        User newUser = UserMapper.toUser(newUserDto);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(oldUser));
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(new User());

        userService.updateUser(newUserDto);

        InOrder inOrder = inOrder(userRepository);
        inOrder.verify(userRepository, times(1)).findById(any());
        inOrder.verify(userRepository, times(1)).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertThat(savedUser.getEmail(), equalTo(oldUserDto.getEmail()));
        assertThat(savedUser.getName(), equalTo(newUser.getName()));
    }

    @Test
    public void updateUser_whenUserIsNotValid_thenConstraintViolationExceptionThrown() {
        UserDto userDto = createUser();
        userDto.setEmail("notValidEmail");
        User user = UserMapper.toUser(userDto);


        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());

        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> userService.updateUser(userDto));

        verify(userRepository, never()).save(any());
    }

    @Test
    public void getUserById_whenUserFound_thenReturnUser() {
        UserDto userDto = createUser();
        User user = UserMapper.toUser(userDto);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        UserDto returnedUserDto = userService.getUserById(userDto.getId());
        assertThat(returnedUserDto, equalTo(userDto));
    }

    @Test
    public void getUserById_whenUserNotFound_thenContentNotFountExceptionThrown() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThrows(
                ContentNotFountException.class,
                () -> userService.getUserById(any()));
    }

    @Test
    public void deleteUser_whenUserFound_thenInvokeDeleteUser() {
        UserDto userDto = createUser();
        User user = UserMapper.toUser(userDto);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        userService.deleteUser(any());
        verify(userRepository, times(1)).deleteById(any());
    }

    @Test
    public void deleteUser_whenUserNotFound_thenContentNotFoundExceptionThrown() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThrows(
                ContentNotFountException.class,
                () -> userService.deleteUser(any()));
        verify(userRepository, never()).deleteById(any());
    }
}
