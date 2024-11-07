package hsf301.fe.project.controller;

import hsf301.fe.project.pojo.Users;
import hsf301.fe.project.service.defines.IEmailService;
import hsf301.fe.project.service.defines.IUsersService;
import hsf301.fe.project.service.defines.IVerificationService;
import hsf301.fe.project.utils.CodeGenerator;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@SessionAttributes("user")
public class UserController {

    @Autowired
    private IUsersService usersService;
    @Autowired
    private IEmailService emailService;
    @Autowired
    private IVerificationService verificationService;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/register")
    public String showRegistrationForm(@RequestParam(name = "role", required = false) String role, Model model) {
        if (role == null) {
            return "user/roleSelection";
        }
        Users user = new Users();
        user.setRole(role);
        model.addAttribute("user", user);
        return "user/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") Users user,
            @RequestParam("confirmPassword") String confirmPassword, Model model) {
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("registrationError", "Passwords do not match.");
            return "user/register";
        }

        Users existingUser = usersService.findByEmail(user.getEmail());
        if (existingUser != null) {
            model.addAttribute("registrationError", "Email already exists.");
            return "user/register";
        }

        String code = CodeGenerator.generateCode();
        verificationService.saveCode(user.getEmail(), code);
        emailService.sendVerificationCode(user.getEmail(), code);

        System.out.println("Email to be verified: " + user.getEmail());

        model.addAttribute("email", user.getEmail());
        return "user/verify";
    }

    @PostMapping("/verify")
    public String verifyUser(@RequestParam("email") String email,
            @RequestParam("code") String code,
            @ModelAttribute("user") Users user,
            Model model) {
        boolean verified = verificationService.verifyCode(email, code);
        model.addAttribute("email", email); // Ensure email is added to model

        if (verified) {
            user.setActive(true);
            usersService.createNewUsers(user);
            model.addAttribute("verificationSuccess", "Account successfully verified! Please log in.");
            return "user/verificationSuccess"; // Redirect to a success page
        } else {
            model.addAttribute("verificationError", "Invalid or expired verification code.");
            return "user/verify";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "user/login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session, Model model) {
        Users user = usersService.authenticateUser(email, password);
        if (user != null) {
            if (user.isActive()) {
                session.setAttribute("loggedInUser", user);
                String role = user.getRole();

                // Navigate based on role
                if ("ADMIN".equals(role)) {
                    return "redirect:/admin/dashboard";
                } else if ("CUSTOMER".equals(role)) {
                    return "redirect:/customer/dashboard";
                } else if ("SELLER".equals(role)) {
                    return "redirect:/seller/dashboard";
                } else {
                    model.addAttribute("loginError", "Role not recognized.");
                    return "user/login";
                }
            } else {
                model.addAttribute("loginError", "Your account is not active.");
                return "user/login";
            }
        } else {
            model.addAttribute("loginError", "Invalid email or password.");
            return "user/login";
        }
    }

    @GetMapping("/logout")
    public String logoutUser(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login";
    }
    
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "user/forgotPassword";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) throws Exception {
        Users user = usersService.findByEmail(email);
        if (user != null) {
            String code = CodeGenerator.generateCode();
            verificationService.saveCode(email, code);
            emailService.sendVerificationCode(email, code);
            model.addAttribute("email", email);
            return "user/verifyForgotPassword";
        } else {
            model.addAttribute("error", "Email not found.");
            return "user/forgotPassword";
        }
    }

    @PostMapping("/verify-forgot-password")
    public String verifyForgotPassword(@RequestParam("email") String email, @RequestParam("code") String code, Model model) {
        boolean verified = verificationService.verifyCode(email, code);
        if (verified) {
            model.addAttribute("email", email);
            return "user/resetPassword";
        } else {
            model.addAttribute("error", "Invalid or expired verification code.");
            return "user/verifyForgotPassword";
        }
    }

    @PostMapping("/reset-password")
public String resetPassword(@RequestParam("email") String email,
                            @RequestParam("password") String password,
                            @RequestParam("confirmPassword") String confirmPassword,
                            Model model) {
    if (!password.equals(confirmPassword)) {
        model.addAttribute("error", "Passwords do not match.");
        model.addAttribute("email", email);
        return "user/resetPassword";
    }

    Users user = usersService.findByEmail(email);
    if (user != null) {
        user.setPassword(passwordEncoder.encode(password)); // Mã hóa mật khẩu
        usersService.updateUser(user);
        model.addAttribute("success", "Password has been reset.");
        return "user/resetPasswordSuccess";
    } else {
        model.addAttribute("error", "Email not found.");
        return "user/resetPassword";
    }
}

}   




            