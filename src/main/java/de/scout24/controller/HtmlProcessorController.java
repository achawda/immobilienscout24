package de.scout24.controller;


import de.scout24.model.ErrorMessage;
import de.scout24.model.WebDocument;
import de.scout24.service.HtmlProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HtmlProcessorController {

    @Autowired
    HtmlProcessorService htmlProcessorService;

    @RequestMapping("/htmlinfo")
    public String home(Model model) {
        model.addAttribute("webDocument", new WebDocument());
        model.addAttribute("errorMsg", new ErrorMessage(""));
        return "main";
    }

    @PostMapping(value = "/htmlinfo")
    public String getHtmlInformation(@ModelAttribute WebDocument webDocument, Model model) {
        ErrorMessage error = new ErrorMessage("");
        try {
            htmlProcessorService.processHtml(webDocument);
        }catch (Exception e){
            error.setDetail(e.getMessage());
        }
        model.addAttribute(webDocument);
        model.addAttribute("errorMsg", error);
        return "main";
    }

}

