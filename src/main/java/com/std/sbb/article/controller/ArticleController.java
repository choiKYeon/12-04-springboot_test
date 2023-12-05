package com.std.sbb.article.controller;

import com.std.sbb.article.entity.Article;
import com.std.sbb.article.form.ArticleForm;
import com.std.sbb.article.service.ArticleService;
import com.std.sbb.user.entity.SiteUser;
import com.std.sbb.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;
    private final UserService userService;
    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0")int page,  @RequestParam(value = "kw", defaultValue = "") String kw){
        Page<Article> paging = this.articleService.getList(page, kw);
        model.addAttribute("paging", paging);
//        model.addAttribute("kw", kw);
        return "article_list";
    }
    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String articleCreate(ArticleForm articleForm){
        return "article_form";
    }
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String articleCreate(@Valid ArticleForm articleForm, BindingResult bindingResult, Principal principal){
        if (bindingResult.hasErrors()){
            return "article_form";
        }
        SiteUser user = this.userService.getUser(principal.getName());
        this.articleService.create(articleForm.getSubject(), articleForm.getContent(), user);
        return "redirect:/";
    }
    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id){
        Article article = this.articleService.getArticle(id);
        model.addAttribute("article", article);
        return "article_detail";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(ArticleForm articleForm,  @PathVariable("id") Integer id, Principal principal){
        Article article = this.articleService.getArticle(id);
        if (!article.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        articleForm.setContent(article.getContent());
        articleForm.setSubject(article.getSubject());
        return "article_form";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@Valid ArticleForm articleForm, BindingResult bindingResult, @PathVariable("id") Integer id, Principal principal){
        if (bindingResult.hasErrors()){
            return "article_detail";
        }
        Article article = this.articleService.getArticle(id);
        if (!article.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.articleService.modify(article, articleForm.getSubject(), articleForm.getContent());
        return String.format("redirect:/article/detail/%s", id);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, Principal principal){
        Article article = this.articleService.getArticle(id);
        if (!article.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.articleService.delete(article);
        return "redirect:/";
    }
}
