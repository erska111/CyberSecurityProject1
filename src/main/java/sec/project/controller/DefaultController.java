/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.project.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sec.project.domain.Account;
import sec.project.domain.Message;
import sec.project.repository.AccountRepository;
import sec.project.repository.MessageRepository;

@Controller
public class DefaultController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountRepository AccountRepository;
    @Autowired
    private MessageRepository messageRepository;

    @RequestMapping("*")
    public String defaultMapping() {
        return "redirect:/";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Account a = AccountRepository.findByName(username);

        if (AccountRepository.findByName("Admin") == null) {
            Account admin = new Account("Admin", passwordEncoder.encode("admin"), "No secrets");
            admin.setUserType("Admin");
            AccountRepository.save(admin);
        }

        if (a == null) {
            return "home";
        }

        return "redirect:/funnyMessages";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Account a = AccountRepository.findByName(username);

        if (a != null) {
            model.addAttribute("user", a);
        }

        return "register";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm() {
        return "form";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String submitForm(@RequestParam String name, @RequestParam String password, @RequestParam String sensitiveData, @RequestParam(required = false) String userType) {
        System.out.println("POSTDATA: " + name + " : " + password + " : " + sensitiveData);
        if (AccountRepository.findByName(name) != null) {
            return "registerfail";
        }
        Account account = new Account(name, passwordEncoder.encode(password), sensitiveData);
        if (userType != null) {
            if (userType.equals("RegularUser") || userType.equals("Admin") || userType.equals("PlusUser")) {
                account.setUserType(userType);
            } else {
                return "registerfail";
            }
        }

        AccountRepository.save(account);

        return "redirect:/login";
    }

    @RequestMapping(value = "/funnyMessages", method = RequestMethod.GET)
    public String messages(Model model, @RequestParam(required = false) String queryString) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account a = AccountRepository.findByName(username);

        model.addAttribute("loggedInAs", a);
       
        model.addAttribute("messages", messageRepository.findAll());

        return "funnyMessages";
    }

    @RequestMapping(value = "/writeQuery", method = RequestMethod.POST)
    @ResponseBody
    public String writeQuery(@RequestParam String queryString) {
        if (queryString != null) {
            try {

                Connection connection = DriverManager.getConnection("jdbc:h2:mem:db", "sa", "");

                ResultSet resultSet = connection.createStatement().executeQuery("SELECT CONTENT FROM MESSAGE WHERE CONTENT LIKE '%" + queryString + "%'");
                int amountOfColumns = resultSet.getMetaData().getColumnCount();

                String r = "Messages that contain \"" +queryString + "\":<br/>";

                while (resultSet.next()) {
                    for (int i = 1; i <= amountOfColumns; i++) {
                        if (i == 1) {
                            r += "-";
                        }
                        r += resultSet.getString(i) + " ";

                    }
                    if (!resultSet.isLast()) {

                        r += "<br/> ";
                    }
                }

                System.out.println("Resultset: " + r);

                resultSet.close();
                connection.close();

                return r;

            } catch (Exception e) {
                System.out.println("Connection/Query failed. EM: " + e.getMessage());

            }

        }

        return "redirect:/funnyMessages";
    }

    @RequestMapping(value = "/writeMessage", method = RequestMethod.POST)
    public String writeMessage(@RequestParam String content) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account a = AccountRepository.findByName(username); 
        
        Message m = new Message(content, a);
        a.getMessages().add(m);
        messageRepository.save(m);

        return "redirect:/funnyMessages";
    }

}
