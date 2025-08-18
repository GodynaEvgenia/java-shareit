package ru.practicum.shareit.item;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.NotAvailable;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResp;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    final ItemMapper itemMapper;
    final CommentMapper commentMapper;
    private ItemRepository repository;
    private UserRepository userRepository;
    private CommentRepository commentRepository;
    private BookingRepository bookingRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository repository, ItemMapper itemMapper,
                           UserRepository userRepository,
                           CommentRepository commentRepository,
                           CommentMapper commentMapper,
                           BookingRepository bookingRepository) {
        this.repository = repository;
        this.itemMapper = itemMapper;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.bookingRepository = bookingRepository;
    }

    @Override
    @Transactional
    public ItemDto createItem(ItemDto itemDto, Long ownerId) {
        Optional<User> owner = userRepository.findById(ownerId);
        if (!owner.isPresent()) {
            throw new NotFoundException("владелец не найден");
        }
        log.info("create item , ownerId={}, itemDto={}, owner={}", ownerId, itemDto, owner.toString());
        Item item = itemMapper.toModel(itemDto, null, owner.get());
        Item savedItem = repository.save(item);
        return itemMapper.toDto(savedItem, ownerId, new ArrayList<>());
    }

    @Override
    @Transactional
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId) {
        Optional<User> owner = userRepository.findById(ownerId);
        if (!owner.isPresent()) {
            throw new NotFoundException("владелец не найден");
        }
        Item exItem = repository.findById(itemId).get();
        Item item = itemMapper.toModelForUpdate(itemDto, exItem);
        Item savedItem = repository.save(item);
        List<Comment> comments = commentRepository.findAllByItem_id(item.getId());
        return itemMapper.toDto(savedItem, ownerId, comments);
    }

    public List<ItemDto> getAll(Long ownerId) {
        List<Item> items = repository.findByOwnerId(ownerId);
        List<ItemDto> itemsDto = items.stream().map(item -> itemMapper.toDto(item, ownerId, commentRepository.findAllByItem_id(item.getId()))).toList();
        return itemsDto;
    }

    public ItemDtoResp getItem(Long itemId, Long userId) {
        Item item = repository.findById(itemId).get();
        List<Comment> comments = commentRepository.findAllByItem_id(itemId);
        List<CommentDto> commentsDto = comments.stream().map(comment -> commentMapper.toDto(comment)).toList();
        return itemMapper.toDtoResp(item, userId, commentsDto);
    }

    public List<ItemDto> search(String text, Long userId) {
        if (text.isEmpty() || text == null) {
            return new ArrayList<>();
        } else {
            List<Item> items = repository.search(text);
            List<ItemDto> itemsDto = items.stream().map(item -> itemMapper.toDto(item, userId, commentRepository.findAllByItem_id(item.getId()))).toList();
            return itemsDto;
        }
    }
    @Transactional
    public CommentDto addComment(Long userId, Long itemId, String comment, LocalDateTime requestTime) {
        User user = userRepository.findById(userId).get();
        Item item = repository.findById(itemId).get();
        Comment cm = new Comment();
        cm.setText(comment);
        cm.setItem(item);
        cm.setAuthor(user);
        List<Booking> bookingList = bookingRepository.getByItemAndBooker(userId, itemId);
        Booking firstBooking = bookingList.stream().findFirst().get();
        if (firstBooking.getEndDate().isAfter(requestTime)) {
            throw new NotAvailable("");
        }
        Comment savedComment = commentRepository.save(cm);
        log.info("сохранение комментария " + savedComment.toString());
        return commentMapper.toDto(savedComment);
    }
}
