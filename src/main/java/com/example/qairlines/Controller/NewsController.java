package com.example.qairlines.Controller;

import com.example.qairlines.Model.News;
import com.example.qairlines.Services.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;

    /**
     * Route to get all new list
     *
     * @return list of news
     */
    @GetMapping("/all")
    public ResponseEntity<List<News>> getAllNews() {
        return ResponseEntity.ok(newsService.getAllNews());
    }

    @GetMapping("/allPublishNews")
    public ResponseEntity<List<News>> getAllNewsByStatus() {
        return ResponseEntity.ok(newsService.getNewsByStatus());
    }

    /**
     * Create news
     *
     * @param news object is body of post method
     * @return news just created
     */
    @PostMapping("/create")
    public ResponseEntity<News> createNews(@RequestBody News news) {
        return ResponseEntity.ok(newsService.createNews(news));
    }

    /**
     * Route for update existed new
     *
     * @return news just updated
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<News> updateNews(@PathVariable Long id, @RequestBody News updatedNews) {
        try {
            return ResponseEntity.ok(newsService.updateNews(id, updatedNews));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Accept news and change status from DRAFT to PUBLISHED
     *
     * @return the updated news
     */
    @PutMapping("/accept/{id}")
    public ResponseEntity<News> acceptNews(@PathVariable Long id) {
        try {
            News updatedNews = newsService.acceptNews(id);
            return ResponseEntity.ok(updatedNews);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Route to delete news
     *
     * @return nothing
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        try {
            newsService.deleteNews(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
