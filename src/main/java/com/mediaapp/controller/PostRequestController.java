package com.mediaapp.controller;

import com.mediaapp.dto.request.CreatePostRequestDto;
import com.mediaapp.dto.request.UpdatePostRequestDto;
import com.mediaapp.model.entity.Category;
import com.mediaapp.model.entity.PostRequest;
import com.mediaapp.model.entity.User;
import com.mediaapp.repository.jpa.CategoryRepository;
import com.mediaapp.repository.jpa.PostRequestRepository;
import com.mediaapp.repository.jpa.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * PostRequest REST Controller
 * Handles CRUD operations for content creation requests
 */
@RestController
@RequestMapping("/post-requests")
@RequiredArgsConstructor
@Tag(name = "Post Requests", description = "API for managing content creation requests")
public class PostRequestController {

    private final PostRequestRepository postRequestRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Operation(summary = "Get all post requests", description = "Retrieve a paginated list of all post requests")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<PostRequest>> getAllPostRequests(
            @Parameter(description = "Pagination information") Pageable pageable) {
        Page<PostRequest> postRequests = postRequestRepository.findAll(pageable);
        return ResponseEntity.ok(postRequests);
    }

    @Operation(summary = "Get post request by ID", description = "Retrieve a specific post request by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved post request"),
        @ApiResponse(responseCode = "404", description = "Post request not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PostRequest> getPostRequestById(
            @Parameter(description = "ID of the post request") @PathVariable Long id) {
        return postRequestRepository.findById(id)
                .map(postRequest -> ResponseEntity.ok(postRequest))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create new post request", description = "Create a new content creation request")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Post request created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<PostRequest> createPostRequest(
            @Parameter(description = "Post request data") @Valid @RequestBody CreatePostRequestDto dto) {
        
        // For demo purposes, using a default user (ID: 1)
        // In production, get from authentication context
        User requester = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Default user not found"));

        PostRequest postRequest = PostRequest.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .priority(dto.getPriority() != null ? dto.getPriority() : "MEDIUM")
                .status("OPEN")
                .requester(requester)
                .dueDate(dto.getDueDate())
                .build();

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            postRequest.setCategory(category);
        }

        PostRequest saved = postRequestRepository.save(postRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Update post request", description = "Update an existing post request")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Post request updated successfully"),
        @ApiResponse(responseCode = "404", description = "Post request not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PostRequest> updatePostRequest(
            @Parameter(description = "ID of the post request") @PathVariable Long id,
            @Parameter(description = "Updated post request data") @Valid @RequestBody UpdatePostRequestDto dto) {
        
        return postRequestRepository.findById(id)
                .map(postRequest -> {
                    if (dto.getTitle() != null) {
                        postRequest.setTitle(dto.getTitle());
                    }
                    if (dto.getDescription() != null) {
                        postRequest.setDescription(dto.getDescription());
                    }
                    if (dto.getStatus() != null) {
                        postRequest.setStatus(dto.getStatus());
                    }
                    if (dto.getPriority() != null) {
                        postRequest.setPriority(dto.getPriority());
                    }
                    if (dto.getCategoryId() != null) {
                        Category category = categoryRepository.findById(dto.getCategoryId())
                                .orElseThrow(() -> new RuntimeException("Category not found"));
                        postRequest.setCategory(category);
                    }
                    if (dto.getAssignedToId() != null) {
                        User assignee = userRepository.findById(dto.getAssignedToId())
                                .orElseThrow(() -> new RuntimeException("User not found"));
                        postRequest.setAssignedTo(assignee);
                    }
                    
                    PostRequest updated = postRequestRepository.save(postRequest);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete post request", description = "Delete a post request by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Post request deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Post request not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostRequest(
            @Parameter(description = "ID of the post request") @PathVariable Long id) {
        
        if (!postRequestRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        postRequestRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get post requests by status", description = "Retrieve post requests filtered by status")
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<PostRequest>> getPostRequestsByStatus(
            @Parameter(description = "Status to filter by") @PathVariable String status,
            @Parameter(description = "Pagination information") Pageable pageable) {
        
        Page<PostRequest> postRequests = postRequestRepository.findAll(pageable);
        // Note: You should add a custom query method in repository for actual filtering
        return ResponseEntity.ok(postRequests);
    }
}
