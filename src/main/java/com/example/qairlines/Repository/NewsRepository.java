package com.example.qairlines.Repository;

import com.example.qairlines.Model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News,Long> {
    @Query("SELECT n FROM News n WHERE n.status = 'PUBLISHED'")
    List<News> getAllNewsByStatus();
}
