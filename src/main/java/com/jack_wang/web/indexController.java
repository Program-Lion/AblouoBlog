package com.jack_wang.web;

import com.jack_wang.service.BlogService;
import com.jack_wang.service.TagService;
import com.jack_wang.service.TypeService;
import com.jack_wang.util.CaptchaCodeUtil;
import com.jack_wang.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class indexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;


    @GetMapping("/")
    public String homeIndex(){
        return "home_index";
    }

    @GetMapping("/index")
    public String index(@PageableDefault(size = 6,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){
        //分页数据
        model.addAttribute("page",blogService.listBlog(pageable));
        model.addAttribute("types",typeService.listTypeTop(6));
        model.addAttribute("tags",tagService.listTagTop(10));
        model.addAttribute("recommendBlogs",blogService.listRecommendBlogTop(8));
        return "index";
    }

    @PostMapping("/search")
    public String search(@PageableDefault(size = 6,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam String query, Model model){
        model.addAttribute("page",blogService.listBlog("%"+query+"%",pageable));
        model.addAttribute("query",query);
        return "search";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id,Model model){
        model.addAttribute("blog",blogService.getAndConvert(id));
        return "blog";
    }

    @GetMapping("/footer/newblog")
    public String newblogs(Model model){
        model.addAttribute("newblogs",blogService.listRecommendBlogTop(3));
        return "_fragments :: newbloglist";
    }

}
