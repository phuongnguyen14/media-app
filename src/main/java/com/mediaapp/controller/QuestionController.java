package com.mediaapp.controller;

import com.mediaapp.model.entity.Question;
import com.mediaapp.repository.jpa.QuestionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Question REST Controller
 * Handles question management operations
 */
@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
@Tag(name = "Questions", description = "API for managing questions")
public class QuestionController {

    private final QuestionRepository questionRepository;

    @Operation(summary = "Get all questions", description = "Retrieve a paginated list of all questions")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<Question>> getAllQuestions(
            @Parameter(description = "Pagination information") Pageable pageable) {
        return ResponseEntity.ok(questionRepository.findAll(pageable));
    }

    @Operation(summary = "Get question by ID", description = "Retrieve a specific question by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved question"),
        @ApiResponse(responseCode = "404", description = "Question not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(
            @Parameter(description = "ID of the question") @PathVariable Long id) {
        return questionRepository.findById(id)
                .map(question -> ResponseEntity.ok(question))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Search questions", description = "Search questions by title or content")
    @GetMapping("/search")
    public ResponseEntity<Page<Question>> searchQuestions(
            @Parameter(description = "Search query") @RequestParam String query,
            @Parameter(description = "Pagination information") Pageable pageable) {
        // This would need a custom repository method for actual search
        return ResponseEntity.ok(questionRepository.findAll(pageable));
    }
}
