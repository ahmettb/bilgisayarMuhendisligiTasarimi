package com.jobtrackingapp.software_service.controller;


import com.jobtrackingapp.software_service.model.Comment;
import com.jobtrackingapp.software_service.model.Task;
import com.jobtrackingapp.software_service.model.User;
import com.jobtrackingapp.software_service.model.request.CommentRequest;
import com.jobtrackingapp.software_service.model.response.ApiResponse;
import com.jobtrackingapp.software_service.repository.CommentRepository;
import com.jobtrackingapp.software_service.repository.TaskRepository;
import com.jobtrackingapp.software_service.repository.UserRepository;
import com.jobtrackingapp.software_service.service.CommentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("api/comment")
@AllArgsConstructor
@Log4j2
public class CommentController {

   private final CommentService commentService;


   @PostMapping("/add")
    public ResponseEntity<ApiResponse<Void>>createComment(@RequestBody CommentRequest request)
    {

        return new ResponseEntity<>(commentService.addCommentToTask(request), HttpStatus.OK);
    }

    @DeleteMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Void>>updateComment(@PathVariable("id")Long id,@RequestBody CommentRequest request)
    {

        return new ResponseEntity<>(commentService.updateComment(id,request), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>>deleteComment(@PathVariable("id")Long id)
    {

        return new ResponseEntity<>(commentService.deleteComment(id), HttpStatus.OK);
    }
}
