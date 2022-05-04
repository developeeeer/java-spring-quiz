package com.example.quiz.controller;

import com.example.quiz.entity.Quiz;
import com.example.quiz.form.QuizForm;
import com.example.quiz.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/quiz")
public class QuizController {
    @Autowired
    QuizService service;
    @ModelAttribute
    public QuizForm setUpForm(){
        QuizForm form = new QuizForm();
        form.setAnswer(true);
        return form;
    }

    @GetMapping
    public String showList(QuizForm quizForm, Model model) {
        quizForm.setNewQuiz(true);

        Iterable<Quiz> list = service.selectAll();
        model.addAttribute("list", list);
        model.addAttribute("title", "登録用フォーム");
        return "crud";
    }

    @PostMapping("/insert")
    public String insert(
            @Validated QuizForm quizForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        Quiz quiz = new Quiz();
        quiz.setQuestion(quizForm.getQuestion());
        quiz.setAnswer(quizForm.getAnswer());
        quiz.setAuthor(quizForm.getAuthor());

        if(!bindingResult.hasErrors()) {
            service.insertQuiz(quiz);
            redirectAttributes.addFlashAttribute("complete", "登録が完了しました");
            return "redirect:/quiz";
        } else {
            return showList(quizForm, model);
        }
    }

    @GetMapping("/{id}")
    public String showUpdate(QuizForm quizForm, @PathVariable Integer id, Model model) {
        Optional<Quiz> quizOptional = service.selectOneById(id);
        Optional<QuizForm> quizFormOptional = quizOptional.map(t -> makeQuizForm(t));
        if (quizFormOptional.isPresent()){
            quizForm = quizFormOptional.get();
        }
        makeUpdateModel(quizForm, model);
        return "crud";
    }

    private void makeUpdateModel(QuizForm quizForm, Model model) {
        model.addAttribute("id", quizForm.getId());
        quizForm.setNewQuiz(false);
        model.addAttribute("quizForm", quizForm);
        model.addAttribute("title", "更新用フォーム");
    }

    @PostMapping("/update")
    public String update(
            @Validated QuizForm quizForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        Quiz quiz = makeQuiz(quizForm);
        if(!bindingResult.hasErrors()){
            service.updateQuiz(quiz);
            redirectAttributes.addFlashAttribute("complete", "更新が完了しました");
            return "redirect:/quiz/" + quiz.getId();
        }else{
            makeUpdateModel(quizForm, model);
            return "crud";
        }
    }

    private Quiz makeQuiz(QuizForm quizForm) {
        Quiz quiz = new Quiz();
        quiz.setId(quizForm.getId());
        quiz.setQuestion(quizForm.getQuestion());
        quiz.setAnswer(quizForm.getAnswer());
        quiz.setAuthor(quizForm.getAuthor());
        return quiz;
    }

    private QuizForm makeQuizForm(Quiz quiz) {
        QuizForm quizForm = new QuizForm();
        quizForm.setId(quiz.getId());
        quizForm.setQuestion(quiz.getQuestion());
        quizForm.setAnswer(quiz.getAnswer());
        quizForm.setAuthor(quiz.getAuthor());
        quizForm.setNewQuiz(false);
        return quizForm;
    }

    @PostMapping("/delete")
    public String delete(
            @RequestParam("id") String id,
            Model model,
            RedirectAttributes redirectAttributes
    ){
        service.deleteQuizById(Integer.parseInt(id));
        redirectAttributes.addFlashAttribute("decomplete", "削除が完了しました");
        return "redirect:/quiz";
    }

    @GetMapping("/play")
    public String showQuiz(QuizForm quizForm, Model model) {
        Optional<Quiz> quizOptional = service.selectOneRandomQuiz();
        if(quizOptional.isPresent()) {
            Optional<QuizForm> quizFormOptional = quizOptional.map(t -> makeQuizForm(t));
            quizForm = quizFormOptional.get();
        } else {
            model.addAttribute("msg", "問題がありません・・・");
            return "play";
        }
        model.addAttribute("quizForm", quizForm);
        return "play";
    }

    @PostMapping("/check")
    public String checkQuiz(QuizForm quizForm, @RequestParam Boolean answer, Model model) {
        if(service.checkQuiz(quizForm.getId(), answer)) {
            model.addAttribute("msg", "正解です");
        } else {
            model.addAttribute("msg", "残念、不正解です・・・");
        }
        return "answer";
    }
}
