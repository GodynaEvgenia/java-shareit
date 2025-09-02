package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private RequestMapper requestMapper;//= new RequestMapper(); // real object

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
        itemRequestService = new ItemRequestService(
                requestRepository,
                userRepository,
                mapper, itemRepository, itemMapper);
    }

    @Test
    void create() {
     /*   ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("desc");

        ItemRequestDto expItemRequestDto = new ItemRequestDto();
        expItemRequestDto.setId(1L);
        expItemRequestDto.setDescription("desc");

        when(itemRequestService.create(any(ItemRequestDto.class),any(Long.class))).thenReturn(itemRequestDto);

        verify(itemRequestService, times(1)).create(itemRequestDto, 1L);
    */
    }

    @Test
    void getAllByOwner() {
    }

    @Test
    void getAllByAnotherUsers() {
    }

    @Test
    void findbyId() {
    }
}