package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.InMemoryStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    final ItemMapper itemMapper;
    private InMemoryStorage inMemoryStorage;

    @Autowired
    public ItemServiceImpl(InMemoryStorage inMemoryStorage, ItemMapper itemMapper) {
        this.inMemoryStorage = inMemoryStorage;
        this.itemMapper = itemMapper;
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, Long ownerId) {
        User owner = inMemoryStorage.findUserById(ownerId);
        Item item = itemMapper.toModel(itemDto, null, owner);
        Item savedItem = inMemoryStorage.createItem(item);
        return itemMapper.toDto(savedItem);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId) {
        User owner = inMemoryStorage.findUserById(ownerId);
        Item itemInMemory = inMemoryStorage.getItem(itemId);
        Item item = itemMapper.toModelForUpdate(itemDto, itemInMemory);
        Item savedItem = inMemoryStorage.updateItem(item);
        return itemMapper.toDto(savedItem);
    }

    public List<ItemDto> getAll(Long ownerId) {
        List<Item> items = inMemoryStorage.getAllItems(ownerId);
        List<ItemDto> itemsDto = items.stream().map(item -> itemMapper.toDto(item)).toList();
        return itemsDto;
    }

    public ItemDto getItem(Long itemId) {
        Item item = inMemoryStorage.getItem(itemId);
        return itemMapper.toDto(item);
    }

    public List<ItemDto> search(String text) {
        if (text.isEmpty() || text == null) {
            return new ArrayList<>();
        } else {
            List<Item> items = inMemoryStorage.search(text);
            List<ItemDto> itemsDto = items.stream().map(item -> itemMapper.toDto(item)).toList();
            return itemsDto;
        }

    }
}
