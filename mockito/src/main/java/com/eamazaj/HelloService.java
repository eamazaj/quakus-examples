package com.eamazaj;


import javax.enterprise.context.RequestScoped;

@RequestScoped
public class HelloService {

    public String sayHello() {
        return "Hello";
    }


    public String sayBye() {
        return "Bye";
    }
}
