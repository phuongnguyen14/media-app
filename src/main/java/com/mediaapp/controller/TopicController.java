package com.mediaapp.controller;

import com.mediaapp.model.entity.Topic;
import com.mediaapp.repository.jpa.TopicRepository;
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

import java.util.List;

/**
 * Topic REST Controller
 * Handles topic management operations
 */
@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
@Tag(name = "Topics", description = "API for managing content topics")
public class TopicController {

    private final TopicRepository topicRepository;

    @Operation(summary = "Get all topics", description = "Retrieve a paginated list of all topics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<Topic>> getAllTopics(
            @Parameter(description = "Pagination information") Pageable pageable) {
        return ResponseEntity.ok(topicRepository.findAll(pageable));
    }

    @Operation(summary = "Get all topics (no pagination)", description = "Retrieve all topics without pagination")
    @GetMapping("/all")
    public ResponseEntity<List<Topic>> getAllTopicsNoPagination() {
        return ResponseEntity.ok(topicRepository.findAll());
    }

    @Operation(summary = "Get topic by ID", description = "Retrieve a specific topic by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved topic"),
        @ApiResponse(responseCode = "404", description = "Topic not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Topic> getTopicById(
            @Parameter(description = "ID of the topic") @PathVariable Long id) {
        return topicRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get topic by slug", description = "Retrieve a topic by its URL-friendly slug")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved topic"),
        @ApiResponse(responseCode = "404", description = "Topic not found")
    })
    @GetMapping("/slug/{slug}")
    public ResponseEntity<Topic> getTopicBySlug(
            @Parameter(description = "Slug of the topic") @PathVariable String slug) {
        return topicRepository.findBySlug(slug)
                .map(topic -> ResponseEntity.ok(topic))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get topics by category", description = "Retrieve topics belonging to a specific category")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Topic>> getTopicsByCategory(
            @Parameter(description = "Category ID") @PathVariable Long categoryId,
            @Parameter(description = "Pagination information") Pageable pageable) {
        return ResponseEntity.ok(topicRepository.findByCategoryId(categoryId));
    }
}
