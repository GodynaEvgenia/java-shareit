package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestService itemRequestService;

    @MockBean
    private RequestMapper requestMapper;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void create() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("desc");

        ItemRequestDto expItemRequestDto = new ItemRequestDto();
        expItemRequestDto.setId(1L);
        expItemRequestDto.setDescription("desc");

        when(itemRequestService.create(any(ItemRequestDto.class), any(Long.class))).thenReturn(itemRequestDto);

        mockMvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

        verify(itemRequestService, times(1)).create(itemRequestDto, 1L);
    }

    @Test
    void getAllByOwner() throws Exception {
        /*List<ItemRequestDto> expectedItemRequestDto = List.of(new ItemRequestDto());
        when(itemRequestService.getAllByOwner(1L)).thenReturn(expectedItemRequestDto);

        mockMvc.perform(get("/{requestId}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(itemRequestService, times(1)).getAllByOwner(1L);*/
    }

    @Test
    void getAllByAnotherUsers() {
        List<ItemRequestDto> expectedItemRequestDto = List.of(new ItemRequestDto());
        when(itemRequestService.getAllByAnotherUsers(1L)).thenReturn(expectedItemRequestDto);
        List<ItemRequestDto> response = itemRequestService.getAllByAnotherUsers(1L);

        assertEquals(expectedItemRequestDto, response);
    }

    @Test
    void findById() {
    }
}