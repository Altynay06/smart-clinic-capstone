package com.project.back_end.mvc;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
public class DashboardController {
 @GetMapping("/portal/{role:admin|doctor|patient}") public String portal(@PathVariable String role,Model model){model.addAttribute("role",role);return "portal";}
}
