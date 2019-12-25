/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.project.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.springframework.data.jpa.domain.AbstractPersistable;


@Entity
public class Message extends AbstractPersistable<Long> {
    
    private String content;
    
    @ManyToOne
    private Account owner;
    
    public Message(){
        this.content=null;
        this.owner = null;
    }
    
    public Message(String content, Account owner) {
        this.content = content;
        this.owner = owner;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }
    
    
    
}
