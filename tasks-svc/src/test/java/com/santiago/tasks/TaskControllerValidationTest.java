package com.santiago.tasks;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;
    @Test
    void createTask_titleTooLong_returns400() throws Exception {
        String body = """
  {
    "title": "This title is definitely too long",
    "description": "A valid description",
    "tags": ["test"],
    "priority": "HIGH",
    "dueAt": "2025-12-31T10:00:00Z",
    "estimatedMinutes": 60
  }
  """;

        mockMvc.perform(post("/tasks")
                        .header("X-USER-ID", "Santi")
                        .contentType("application/json")
                        .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTask_dueAtPast_returns400() throws Exception {
        String body = """
        {
          "title": "Past Date",
          "description": "Trying to set a past due date",
          "tags": ["test"],
          "priority": "HIGH",
          "dueAt": "2020-01-01T10:00:00Z",
          "estimatedMinutes": 60
        }
        """;

        mockMvc.perform(post("/tasks")
                        .header("X-USER-ID", "Santi")
                        .contentType("application/json")
                        .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTask_estimatedMinutesOutOfRange_returns400() throws Exception {
        String body = """
                {
                  "title": "Bad Minutes",
                  "description": "Minutes should be between 1 and 1440",
                  "tags": ["test"],
                  "priority": "MEDIUM",
                  "dueAt": "2025-12-31T10:00:00Z",
                  "estimatedMinutes": 0
                }
        """;

        mockMvc.perform(post("/tasks")
                        .header("X-USER-ID", "Santi")
                        .contentType("application/json")
                        .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


}
