package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public <T> ResponseEntity<Object> create(/*ItemDto itemDto*/ T body, Long ownerId) {
        return post("", ownerId, null, body);
    }

    public ResponseEntity<Object> update(Long itemId, ItemDto itemDto) {
        return patch("/" + itemId, itemId, itemDto);
    }

    public ResponseEntity<Object> findById(Long itemId) {
        return get("/" + itemId, itemId);
    }

    public void delete(Long itemId) {
        delete("/" + itemId, itemId);
    }

    public ResponseEntity<Object> getAll(Long ownerId) {
        return get("", ownerId);
    }

    public ResponseEntity<Object> get(Long itemId, Long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> search(String text, Long userId) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get("/search", userId, parameters);
    }

    public ResponseEntity<Object> addComment(Long userId,
                                             Long itemId,
                                             String text,
                                             LocalDateTime tm) {
        Map<String, Object> parameters = Map.of(
                "itemId", itemId,
                "text", text
        );
        return post("/" + itemId + "/comment", userId, parameters);
    }
}
