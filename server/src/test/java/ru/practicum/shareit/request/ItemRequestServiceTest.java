package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RequestRepository repository;
    @Mock
    private RequestMapper mapper;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemRequestService itemRequestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create() {
        Long requestorId = 1L;

        ItemRequestDto requestDto = new ItemRequestDto();

        User user = new User();
        user.setId(requestorId);

        Request requestModel = new Request();

        Request savedRequest = new Request();
        savedRequest.setId(10L);

        Item item = new Item();
        item.setId(100L);

        ItemDto itemDto = new ItemDto();
        itemDto.setId(100L);

        ItemRequestDto expectedDto = new ItemRequestDto();
        expectedDto.setId(10L);

        when(userRepository.findById(requestorId)).thenReturn(Optional.of(user));
        when(mapper.toModel(requestDto, null, user)).thenReturn(requestModel);
        when(repository.save(requestModel)).thenReturn(savedRequest);
        when(itemRepository.findByRequestId(user.getId())).thenReturn(List.of(item));
        when(itemMapper.toDto(item, user.getId(), null, null)).thenReturn(itemDto);
        when(mapper.toDto(savedRequest, requestorId, List.of(itemDto))).thenReturn(expectedDto);

        ItemRequestDto actualDto = itemRequestService.create(requestDto, requestorId);

        assertNotNull(actualDto);
        assertEquals(expectedDto.getId(), actualDto.getId());

        verify(userRepository).findById(requestorId);
        verify(mapper).toModel(requestDto, null, user);
        verify(repository).save(requestModel);
        verify(itemRepository).findByRequestId(user.getId());
        verify(itemMapper).toDto(item, user.getId(), null, null);
        verify(mapper).toDto(savedRequest, requestorId, List.of(itemDto));
    }

    @Test
    public void testCreate_UserNotFound_ThrowsException() {
        Long requestorId = 1L;
        ItemRequestDto requestDto = new ItemRequestDto();

        when(userRepository.findById(requestorId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            itemRequestService.create(requestDto, requestorId);
        });

        assertEquals("владелец не найден", exception.getMessage());

        verify(userRepository).findById(requestorId);
        verifyNoMoreInteractions(mapper, repository, itemRepository, itemMapper);
    }

    @Test
    public void testGetAllByOwner_Success() {
        Long ownerId = 1L;

        Request request1 = new Request();
        request1.setId(10L);
        Request request2 = new Request();
        request2.setId(20L);
        List<Request> requests = List.of(request1, request2);

        Item item1 = new Item();
        item1.setId(100L);
        Item item2 = new Item();
        item2.setId(200L);
        List<Item> items = List.of(item1, item2);

        ItemDto itemDto1 = new ItemDto();
        itemDto1.setId(100L);
        ItemDto itemDto2 = new ItemDto();
        itemDto2.setId(200L);
        List<ItemDto> itemsDto = List.of(itemDto1, itemDto2);

        ItemRequestDto dto1 = new ItemRequestDto();
        dto1.setId(10L);
        ItemRequestDto dto2 = new ItemRequestDto();
        dto2.setId(20L);

        when(repository.getAllByOwner(ownerId)).thenReturn(requests);
        when(itemRepository.findByOwnerId(ownerId)).thenReturn(items);

        when(itemMapper.toDto(item1, ownerId, null, null)).thenReturn(itemDto1);
        when(itemMapper.toDto(item2, ownerId, null, null)).thenReturn(itemDto2);

        when(mapper.toDto(request1, ownerId, itemsDto)).thenReturn(dto1);
        when(mapper.toDto(request2, ownerId, itemsDto)).thenReturn(dto2);

        List<ItemRequestDto> result = itemRequestService.getAllByOwner(ownerId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(dto1.getId(), result.get(0).getId());
        assertEquals(dto2.getId(), result.get(1).getId());

        verify(repository).getAllByOwner(ownerId);
        verify(itemRepository).findByOwnerId(ownerId);
        verify(itemMapper).toDto(item1, ownerId, null, null);
        verify(itemMapper).toDto(item2, ownerId, null, null);
        verify(mapper).toDto(request1, ownerId, itemsDto);
        verify(mapper).toDto(request2, ownerId, itemsDto);
    }

    @Test
    public void testGetAllByAnotherUsers_Success() {
        Long ownerId = 1L;

        Request request1 = new Request();
        request1.setId(10L);
        Request request2 = new Request();
        request2.setId(20L);
        List<Request> requests = List.of(request1, request2);

        ItemRequestDto dto1 = new ItemRequestDto();
        dto1.setId(10L);
        ItemRequestDto dto2 = new ItemRequestDto();
        dto2.setId(20L);

        when(repository.getAllByAnotherUsers(ownerId)).thenReturn(requests);
        when(mapper.toDto(request1, ownerId, null)).thenReturn(dto1);
        when(mapper.toDto(request2, ownerId, null)).thenReturn(dto2);

        List<ItemRequestDto> result = itemRequestService.getAllByAnotherUsers(ownerId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(dto1.getId(), result.get(0).getId());
        assertEquals(dto2.getId(), result.get(1).getId());

        verify(repository).getAllByAnotherUsers(ownerId);
        verify(mapper).toDto(request1, ownerId, null);
        verify(mapper).toDto(request2, ownerId, null);
    }

    @Test
    public void testFindById_Success() {
        Long requestId = 1L;

        Request request = new Request();
        request.setId(requestId);

        User requestor = new User();
        requestor.setId(100L);
        request.setRequestor(requestor);

        Item item1 = new Item();
        item1.setId(10L);
        User owner1 = new User();
        owner1.setId(200L);
        item1.setOwner(owner1);

        Item item2 = new Item();
        item2.setId(20L);
        User owner2 = new User();
        owner2.setId(300L);
        item2.setOwner(owner2);

        List<Item> items = List.of(item1, item2);

        ItemDto itemDto1 = new ItemDto();
        itemDto1.setId(10L);
        ItemDto itemDto2 = new ItemDto();
        itemDto2.setId(20L);

        List<ItemDto> itemsDto = List.of(itemDto1, itemDto2);

        ItemRequestDto expectedDto = new ItemRequestDto();
        expectedDto.setId(requestId);

        when(repository.findById(requestId)).thenReturn(Optional.of(request));
        when(itemRepository.findByRequestId(requestId)).thenReturn(items);

        when(itemMapper.toDto(item1, owner1.getId(), null, null)).thenReturn(itemDto1);
        when(itemMapper.toDto(item2, owner2.getId(), null, null)).thenReturn(itemDto2);

        when(mapper.toDto(request, requestor.getId(), itemsDto)).thenReturn(expectedDto);

        ItemRequestDto result = itemRequestService.findbyId(requestId);

        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());

        verify(repository).findById(requestId);
        verify(itemRepository).findByRequestId(requestId);
        verify(itemMapper).toDto(item1, owner1.getId(), null, null);
        verify(itemMapper).toDto(item2, owner2.getId(), null, null);
        verify(mapper).toDto(request, requestor.getId(), itemsDto);
    }

}