package com.jobtrackingapp.software_service.repository;

import com.jobtrackingapp.software_service.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
