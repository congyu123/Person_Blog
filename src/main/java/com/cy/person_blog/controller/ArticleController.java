// ArticleController.java
package com.cy.person_blog.controller;

import com.cy.person_blog.entity.*;
import com.cy.person_blog.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private UserService userService;
    @Autowired
    private ArticleViewStatService viewStatService;
    @Autowired
    private FollowService followService;
    @GetMapping
    public String redirectToNew() {
        return "redirect:/articles/new";
    }

    @GetMapping({"/new", "/edit/{id}"})
    public String form(@PathVariable(required = false) Integer id,
                       Model model, HttpSession session) {
        Article article = (id == null)
                ? new Article()
                : articleService.findById(id);
        List<Category> categories = categoryService.listAllCategories();
        model.addAttribute("article", article);
        model.addAttribute("categories", categories);
        return "publish_article";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Article article,
                       @RequestParam String action,
                       HttpSession session) {

        Integer userId = ((com.cy.person_blog.entity.User)
                session.getAttribute("currentUser")).getId();
        article.setAuthorId(userId);
        article.setStatus(Article.Status.valueOf("draft".equals(action) ? "DRAFT" : "PENDING"));
        articleService.saveOrUpdate(article);
        return "redirect:/profile";
    }
    @GetMapping("/{id}")
    public String viewArticleDetail(
            @PathVariable("id") Integer id,
            Model model,
            HttpSession session
    ) {
        viewStatService.recordView(id);
        Article article = articleService.getPublishedArticleById(id);
        articleService.addViewCount(id);
        List<Comment> comments = commentService.listCommentsByArticleId(id);
        List<Category> categories = categoryService.listAllCategories();
        List<Article> related = articleService.listRelatedArticles(id, 5);

        model.addAttribute("article", article);
        model.addAttribute("comments", comments);
        model.addAttribute("categories", categories);
        model.addAttribute("relatedArticles", related);
        model.addAttribute("newComment", new Comment());

        Object currentUserObj = session.getAttribute("currentUser");
        model.addAttribute("currentUser", currentUserObj);
        User author = userService.findById(article.getAuthorId());
        model.addAttribute("author", author);

        Object cu = session.getAttribute("currentUser");
        boolean isFollowing = false;
        if (cu != null) {
            Integer currentUserId = ((User) cu).getId();
            isFollowing = followService.isFollowing(currentUserId, author.getId());
        }
        model.addAttribute("isFollowing", isFollowing);

        long followerCount = followService.getFollowerCount(author.getId());
        model.addAttribute("followerCount", followerCount);
        if(currentUserObj != null) {
            Integer userId = ((com.cy.person_blog.entity.User) currentUserObj).getId();
            boolean liked = favoriteService.hasFavorite(userId, id, Favorite.FavoriteType.LIKE);
            boolean favorited = favoriteService.hasFavorite(userId, id, Favorite.FavoriteType.FAVORITE);

            long likeCount = favoriteService.countFavorites(id, Favorite.FavoriteType.LIKE);
            long favoriteCount = favoriteService.countFavorites(id, Favorite.FavoriteType.FAVORITE);

            model.addAttribute("liked", liked);
            model.addAttribute("favorited", favorited);
            model.addAttribute("likeCount", likeCount);
            model.addAttribute("favoriteCount", favoriteCount);
        } else {
            model.addAttribute("liked", false);
            model.addAttribute("favorited", false);
            model.addAttribute("likeCount", favoriteService.countFavorites(id, Favorite.FavoriteType.LIKE));
            model.addAttribute("favoriteCount", favoriteService.countFavorites(id, Favorite.FavoriteType.FAVORITE));
        }

        return "article_detail";
    }

    @PostMapping("/comment")
    public String postComment(@ModelAttribute("newComment") Comment comment) {
        commentService.addComment(comment);
        return "redirect:/articles/" + comment.getArticleId();
    }
    @PostMapping("/{id}/like")
    @ResponseBody
    public String toggleLike(@PathVariable Integer id, HttpSession session) {
        Object currentUserObj = session.getAttribute("currentUser");
        if (currentUserObj == null) {
            return "error:not_logged_in";
        }
        Integer userId = ((com.cy.person_blog.entity.User) currentUserObj).getId();

        boolean hasLiked = favoriteService.hasFavorite(userId, id, Favorite.FavoriteType.LIKE);
        if (hasLiked) {
            favoriteService.removeFavorite(userId, id, Favorite.FavoriteType.LIKE);
        } else {
            favoriteService.addFavorite(userId, id, Favorite.FavoriteType.LIKE);
        }
        long likeCount = favoriteService.countFavorites(id, Favorite.FavoriteType.LIKE);
        return String.valueOf(likeCount);
    }

    @PostMapping("/{id}/favorite")
    @ResponseBody
    public String toggleFavorite(@PathVariable Integer id, HttpSession session) {
        Object currentUserObj = session.getAttribute("currentUser");
        if (currentUserObj == null) {
            return "error:not_logged_in";
        }
        Integer userId = ((com.cy.person_blog.entity.User) currentUserObj).getId();

        boolean hasFav = favoriteService.hasFavorite(userId, id, Favorite.FavoriteType.FAVORITE);
        if (hasFav) {
            favoriteService.removeFavorite(userId, id, Favorite.FavoriteType.FAVORITE);
        } else {
            favoriteService.addFavorite(userId, id, Favorite.FavoriteType.FAVORITE);
        }
        long favCount = favoriteService.countFavorites(id, Favorite.FavoriteType.FAVORITE);
        return String.valueOf(favCount);
    }

}
