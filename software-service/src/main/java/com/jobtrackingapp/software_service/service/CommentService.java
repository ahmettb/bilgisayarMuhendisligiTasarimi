package com.jobtrackingapp.software_service.service;

import com.jobtrackingapp.software_service.model.Comment;
import com.jobtrackingapp.software_service.model.Task;
import com.jobtrackingapp.software_service.model.User;
import com.jobtrackingapp.software_service.model.request.CommentRequest;
import com.jobtrackingapp.software_service.model.response.ApiResponse;
import com.jobtrackingapp.software_service.repository.CommentRepository;
import com.jobtrackingapp.software_service.repository.TaskRepository;
import com.jobtrackingapp.software_service.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public ApiResponse<Void> addCommentToTask(CommentRequest request) {
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task bulunamadı."));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı."));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setTask(task);
        comment.setUser(user);

        commentRepository.save(comment);

        return ApiResponse.success("Comment Added",null);
    }

    public ApiResponse<Void> deleteComment(Long commentId)
    {
        Comment comment=commentRepository.findById(commentId).orElseThrow(()->new EntityNotFoundException());

        comment.setDeleted(true);
        commentRepository.save(comment);
        return ApiResponse.success("Comment Deleted",null);


    }
    public ApiResponse<Void> updateComment(Long commentId,CommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Yorum bulunamadı."));

        comment.setContent(request.getContent());
        comment.setCreatedAt(new Date());

        commentRepository.save(comment);

        return ApiResponse.success("Comment Updated",null);
    }


}
