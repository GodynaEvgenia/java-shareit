package ru.practicum.shareit.requests;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

@Slf4j
@Service
public class RequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> create(ItemRequestDto requestDto, Long userId) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> update(Long userId, ItemRequestDto requestDto) {
        return patch("/" + userId, userId, requestDto);
    }

    public void delete(Long requestId) {
        delete("/" + requestId, requestId);
    }

    public ResponseEntity<Object> getAllByOwner(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getAllByAnotherUsers(Long userId) {
        log.info("gateway getAllByAnotherUsers");
        return get("/all", userId);
    }

    public ResponseEntity<Object> findById(Long requestId) {

        return get("/" + requestId, requestId);
    }
}