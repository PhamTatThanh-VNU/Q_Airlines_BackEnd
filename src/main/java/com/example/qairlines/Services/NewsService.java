package com.example.qairlines.Services;

import com.example.qairlines.Model.News;
import com.example.qairlines.Repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {
    @Autowired
    private NewsRepository newsRepository;

    /**
     * Get all list of news
     *
     * @return list of new
     */
    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    /**
     * Create news
     *
     * @return news just create
     */
    public News createNews(News news) {
        news.setStatus(News.Status.DRAFT);
        return newsRepository.save(news);
    }

    /**
     * Update news
     *
     * @param id          of news
     * @param updatedNews body of post method
     * @return news Update
     */
    public News updateNews(Long id, News updatedNews) {
        return newsRepository.findById(id)
                .map(existingNews -> {
                    existingNews.setTitle(updatedNews.getTitle());
                    existingNews.setContent(updatedNews.getContent());
                    existingNews.setStatus(News.Status.DRAFT);
                    return newsRepository.save(existingNews);
                })
                .orElseThrow(() -> new IllegalArgumentException("News with id " + id + " not found"));
    }

    /**
     * Delete news
     *
     * @param id of news that you want to delete
     */
    public void deleteNews(Long id) {
        if (newsRepository.existsById(id)) {
            newsRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("News with id " + id + " not found");
        }
    }

    public List<News> getNewsByStatus() {
        return newsRepository.getAllNewsByStatus();
    }
}
