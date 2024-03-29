/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import entity.Customer;
import error.NoResultException;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import session.CustomerSessionLocal;

/**
 *
 * @author Congx2
 */
@Named(value = "forgetPasswordManagedBean")
@RequestScoped
public class ForgetPasswordManagedBean {

    @EJB
    CustomerSessionLocal customerSessionLocal;
    /**
     * Creates a new instance of ForgetPasswordManagedBean
     */
    private String email;

    public String submit() {
        try {
            Customer c = customerSessionLocal.getCustomerByEmail(email);
            if (c != null) {
                customerSessionLocal.forgetPassword(c);
                
                return "emailExist.xhtml";
            } else {
                return "emailDoesNotExist.xhtml";
            }
        } catch (NoResultException e) {
            return "emailDoesNotExist.xhtml";
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ForgetPasswordManagedBean() {
    }

}
