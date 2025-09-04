package ru.practicum.shareit.request;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestService {
    private UserRepository userRepository;
    private RequestRepository repository;
    private RequestMapper mapper;
    private ItemRepository itemRepository;
    private ItemMapper itemMapper;

    @Autowired
    public ItemRequestService(RequestRepository repository,
                              UserRepository userRepository,
                              RequestMapper mapper,
                              ItemRepository itemRepository,
                              ItemMapper itemMapper) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    // @Override
    @Transactional
    public ItemRequestDto create(ItemRequestDto requestDto, Long requestorId) {
        log.info("create request, requestDto={}", requestDto.toString());
        Optional<User> owner = userRepository.findById(requestorId);
        if (!owner.isPresent()) {
            throw new NotFoundException("владелец не найден");
        }
        //log.info("create item , ownerId={}, itemDto={}, owner={}", ownerId, itemDto, owner.toString());
        Request request = mapper.toModel(requestDto, null, owner.get());
        Request savedRequest = repository.save(request);
        List<Item> items = itemRepository.findByRequestId(owner.get().getId());
        List<ItemDto> itemsDto = items.stream()
                .map(item -> itemMapper.toDto(item, owner.get().getId(), null, null)).toList();
        return mapper.toDto(savedRequest, requestorId, itemsDto);
    }

    public List<ItemRequestDto> getAllByOwner(Long ownerId) {
        log.info("получение запросов, ownerId={}", ownerId);
        List<Request> listRequests = repository.getAllByOwner(ownerId);
        List<Item> items = itemRepository.findByOwnerId(ownerId);
        List<ItemDto> itemsDto = items.stream()
                .map(item -> itemMapper.toDto(item, ownerId, null, null)).toList();
        return listRequests.stream().map(request -> mapper.toDto(request, ownerId, itemsDto)).toList();
    }

    public List<ItemRequestDto> getAllByAnotherUsers(Long ownerId) {
        log.info("получение запросов других пользователей, ownerId={}", ownerId);
        List<Request> listRequests = repository.getAllByAnotherUsers(ownerId);
        return listRequests.stream().map(request -> mapper.toDto(request, ownerId, null)).toList();
    }

    public ItemRequestDto findbyId(Long requestId) {
        log.info("получение запроса по id, requestId={}", requestId);
        Request req = repository.findById(requestId).get();
        List<Item> items = itemRepository.findByRequestId(requestId);
        List<ItemDto> itemsDto = items.stream()
                .map(item -> itemMapper.toDto(item, item.getOwner().getId(), null, null)).toList();

        return mapper.toDto(req, req.getRequestor().getId(), itemsDto);
    }
}
