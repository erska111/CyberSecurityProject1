/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.project.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Account extends AbstractPersistable<Long> {
    
    private String userType; //"Admin", "PlusUser" "RegularUser"
    
    private String name;
    
    private String password;
    
    private String sensitiveData;
    
    @OneToMany(mappedBy = "owner")
    private List<Message> messages = new ArrayList();
    
    public Account() {
        name = null;
        password = null;
        sensitiveData = null;
    }
    
    public Account(String name, String password, String sd) {
        this.name = name;
        this.password = password;
        this.sensitiveData = sd;
        this.userType = "RegularUser";
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getSensitiveData() {
        return sensitiveData;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSensitiveData(String sensitiveData) {
        this.sensitiveData = sensitiveData;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }
    
    
    
    
    
    
}
